package com.headswilllol.mineflat;

import com.headswilllol.mineflat.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.ListSelectionEvent;

public class SaveManager {

	/**
	 * Saves all loaded chunks in the current world.
	 */
	public static void saveWorld(){
		System.out.println("Saving chunks...");
		BufferedOutputStream bos = null;
		try {
			int headerSize = 8 * 1024;
			// save level information
			File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator +
					".mineflat", "saves");
			saveFolder = new File(saveFolder, Main.world.getName());
			if (!saveFolder.exists())
				saveFolder.mkdirs();
			File levelDat = new File(saveFolder, "level.dat");
			if (levelDat.exists())
				levelDat.delete();
			levelDat.createNewFile();
			bos = new BufferedOutputStream(new FileOutputStream(levelDat));

			ByteBuffer header = ByteBuffer.allocate(headerSize);

			{
				// file metadata (0x00XX)

				{
					// header size
					header.putShort((short)headerSize);
				}

				{
					// revision
					header.putShort((short)2);
					header.put(new byte[]{(byte)0x00, (byte)0x00});
					header.putShort((short)1);
				}

				{
					// last compatible revision
					header.putShort((short)2);
					header.put(new byte[]{(byte)0x00, (byte)0x01});
					header.putShort((short)1);
				}

				{
					// creation time
					header.putShort((short)8);
					header.put(new byte[]{(byte)0x00, (byte)0x02});
					long time = System.currentTimeMillis();
					header.putLong(time);
				}

				{
					// edit time
					header.putShort((short)8);
					header.put(new byte[]{(byte)0x00, (byte)0x03});
					long editTime = System.currentTimeMillis();
					header.putLong(editTime);
				}
			}

			{
				// world metadata (0x01XX)

				{
					// world name
					byte[] worldNameBytes = Main.world.getName().getBytes("UTF-8");
					header.putShort((short)worldNameBytes.length);
					header.put(new byte[]{(byte)0x01, (byte)0x00});
					header.put(worldNameBytes);
				}

				{
					// ticks
					header.putShort((short)2);
					header.put(new byte[]{(byte)0x01, (byte)0x01});
					header.putShort((short)TickManager.getTicks());
				}

				{
					// chunk length
					header.putShort((short)2);
					header.put(new byte[]{(byte)0x01, (byte)0x02});
					header.putShort((short)Main.world.getChunkLength());
				}

				{
					// chunk height
					header.putShort((short)2);
					header.put(new byte[]{(byte)0x01, (byte)0x03});
					header.putShort((short)Main.world.getChunkHeight());
				}
			}

			List<Byte> body = new ArrayList<Byte>();
			{
				for (Level l : Main.world.getLevels()){
					for (Chunk c : l.chunks.values()){
						List<Byte> chunk = new ArrayList<Byte>();
						int index = chunk.size();

						int lastMat = -1;
						int skip = 0;
						for (int x = 0; x < Main.world.getChunkLength(); x++){
							for (int y = 0; y < Main.world.getChunkHeight(); y++){
								Block block = Block.getBlock(l, x, y);
								int mat = block != null ? block.getType().ordinal() : 0;
								if (mat != lastMat || lastMat == -1){
									lastMat = mat;
									for (byte b : ByteBuffer.allocate(2).putShort((short)lastMat).array())
										chunk.add(b);
									if (skip > 0){
										chunk.add((byte)0x00); chunk.add((byte)0x02);
										chunk.add((byte)0x01); chunk.add((byte)0x00);
										for (byte b : ByteBuffer.allocate(2).putShort((short)skip).array())
											chunk.add(b);
									}
									chunk.add((byte)0x00); chunk.add((byte)0x02);
									chunk.add((byte)0x00); chunk.add((byte)0x00);
									for (byte b : ByteBuffer.allocate(2).putShort((short)x).array())
										body.add(b);
									chunk.add((byte)0x00); chunk.add((byte)0x02);
									chunk.add((byte)0x00); chunk.add((byte)0x01);
									for (byte b : ByteBuffer.allocate(2).putShort((short)y).array())
										body.add(b);
								}
								else {
									skip += 1;
								}
								chunk.add((byte)0x00); chunk.add((byte)0x02);
								chunk.add((byte)0x00); chunk.add((byte)0x02);
								for (byte b : ByteBuffer.allocate(2).putShort((short)mat).array())
									chunk.add(b);
							}
						}
						for (byte b : ByteBuffer.allocate(2).putShort((short)lastMat).array())
							chunk.add(b);
						if (skip > 0){
							chunk.add((byte)0x00); body.add((byte)0x02);
							chunk.add((byte)0x01); body.add((byte)0x00);
							for (byte b : ByteBuffer.allocate(2).putShort((short)skip).array())
								chunk.add(b);
							skip = 0;
						}

						for (byte b : ByteBuffer.allocate(2).putShort((short)chunk.size()).array())
							body.add(b);
						for (byte b : ByteBuffer.allocate(2).putShort((short)l.getIndex()).array())
							chunk.add(b);
						body.add(c.getNum() < 0 ? (byte)0x01 : (byte)0x00);
						for (byte b : ByteBuffer.allocate(2).putShort((short)c.getNum()).array())
							body.add(b);
						body.addAll(chunk);

						header.put(new byte[]{(byte)0x00, (byte)0x04});
						header.put(new byte[]{(byte)0x02, (byte)0x00});
						header.putInt((body.size() - index));
						header.putInt(index + headerSize);
					}
				}
			}

			// end of header
			header.putShort((short)0);
			header.put(new byte[]{(byte)0x00, (byte)0xFF});

			bos.write(header.array());
			byte[] bodyBytes = new byte[body.size()];
			for (int i = 0; i < body.size(); i++)
				bodyBytes[i] = body.get(i);
			bos.write(bodyBytes);
		}
		catch (Exception ex){
			ex.printStackTrace();
			System.err.println("Failed to save world to disk");
		}
		finally {
			if (bos != null){
				try {
					bos.flush();
					bos.close();
				}
				catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
	}

	public static void loadWorld(String world){
		BufferedInputStream bis = null;
		try {
			File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator +
					".mineflat", "saves");
			saveFolder = new File(saveFolder, Main.world.getName());
			if (!saveFolder.exists())
				saveFolder.mkdirs();
			File levelDat = new File(saveFolder, "level.dat");
			bis = new BufferedInputStream(new FileInputStream(levelDat));
			byte[] bytes = new byte[(int)levelDat.length()];
			bis.read(bytes);
			short revision = -1;
			short lastCompatible = -1;
			long creationTime = -1;
			ByteBuffer sizeBuffer = ByteBuffer.allocate(2).put(new byte[]{bytes[0], bytes[1]});
			sizeBuffer.rewind();
			short headerSize = sizeBuffer.getShort();
			boolean ogre = false;
			for (int i = 2; i < headerSize; i += 0){
				if (ogre)
					break;
				System.out.println("index: " + i);
				ByteBuffer buffer = ByteBuffer.allocate(2).put(new byte[]{bytes[i], bytes[i + 1]});
				buffer.rewind();
				short size = buffer.getShort();
				System.out.println("size: " + size);
				ByteBuffer value = ByteBuffer.allocate(size);
				System.out.println("key: " + bytes[i + 2] + " " + bytes[i + 3]);
				for (int in = i + 4; in < i + size + 4; in++)
					value.put(bytes[in]);
				String valueString = "";
				for (byte b : value.array())
					valueString += b + " ";
				System.out.println("value: " + valueString);
				value.rewind();
				switch (bytes[i + 2]){
				case 0x00: // storage metadata
					switch (bytes[i + 3]){
					case 0x00: // format revision
						revision = value.getShort();
						System.out.println("revision");
						System.out.println(bytes[i + 3]);
						break;
					case 0x01: // last compatible format revision
						lastCompatible = value.getShort();
						System.out.println(bytes[i + 3]);
						System.out.println("last");
						break;
					case 0x02: // creation time
						creationTime = value.getLong();
						System.out.println("creation");
						System.out.println(bytes[i + 3]);
						break;
					case (byte)0xFF: // end of header
						ogre = true;
					System.out.println("ogre");
					System.out.println(bytes[i + 3]);
					break;
					}
					break;
				case 0x02:
					switch (bytes[i + 3]){
					case 0x00:
						ByteBuffer cBuffer = ByteBuffer.allocate(4);
						for (int k = 4; k < 8; k++)
							cBuffer.put(bytes[k]);
						int address = cBuffer.getInt(0);

						ByteBuffer csBuffer = ByteBuffer.allocate(4);
						for (int k = 0; k < 4; k++)
							csBuffer.put(bytes[address + k]);
						int length = csBuffer.getInt(0); // size of chunk section

						for (int l = address; l < address + length; l += 0){ // entire chunk
							short bSize = ByteBuffer.allocate(2).put(bytes[l]).put(bytes[l + 1]).getShort(0); // size of block section
							for (int m = l + 2; m < l + 2 + bSize; m += 0){
								short sSize = ByteBuffer.allocate(2).put(bytes[m]).put(bytes[m + 1]).getShort(0); // size of block subsection
								short bKey = ByteBuffer.allocate(2).put(bytes[m + 2]).put(bytes[m + 3]).getShort(0);
								ByteBuffer bValue = ByteBuffer.allocate(sSize); // value of section
								for (int n = m + 4; n < m + 4 + sSize; n += 0)
									value.put(bytes[n]);
								
								int x = 0;
								int y = 0;
								Material type = null;
								switch (bKey){
								case 0x00:
									x = value.getShort();
								case 0x01:
									y = value.getShort();
								}
								
								m += sSize;
							}
							l += 4 + bSize;
						}
					}
				}
				i += 4 + size;
			}
			System.out.println(headerSize + ", " + revision + ", " + lastCompatible + ", " + creationTime);
		}
		catch (IOException ex){
			ex.printStackTrace();
		}
		finally {
			if (bis != null){
				try {
					bis.close();
				}
				catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
	}

}
