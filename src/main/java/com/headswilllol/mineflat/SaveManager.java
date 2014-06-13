package com.headswilllol.mineflat;

import com.headswilllol.mineflat.util.FileUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class SaveManager {

	/**
	 * Saves all loaded chunks in the current world
	 */
	public static void saveWorld(){
		System.out.println("Saving chunks...");
		try {
			int headerSize = 8 * 1024;
			// save level information
			File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator +
					".mineflat", "saves");
			saveFolder = new File(saveFolder, Main.world.getName());
			if (!saveFolder.exists())
				saveFolder.mkdirs();
			File levelDat = new File(saveFolder, "level.bin");
			if (levelDat.exists())
				levelDat.delete();
			levelDat.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(levelDat));
			
			ByteBuffer header = ByteBuffer.allocate(headerSize);
			
			{
				// file metadata (0x00XX)

				{
					// header size
					header.putShort((short)2);
					header.put(new byte[]{(byte)0x00, (byte)0x00});
					header.putShort((short)headerSize);
				}

				{
					// revision
					header.putShort((short)2);
					header.put(new byte[]{(byte)0x00, (byte)0x01});
					header.putShort((short)1);
				}

				{
					// last compatible revision
					header.putShort((short)2);
					header.put(new byte[]{(byte)0x00, (byte)0x02});
					header.putShort((short)1);
				}

				{
					// creation time
					header.putShort((short)8);
					header.put(new byte[]{(byte)0x00, (byte)0x03});
					long time = System.currentTimeMillis();
					header.putLong(time);
				}

				{
					// edit time
					header.putShort((short)4);
					header.put(new byte[]{(byte)0x00, (byte)0x04});
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
					header.put(new byte[]{(byte)0x01, (byte)0x02});
					header.putShort((short)Main.world.getChunkHeight());
				}
			}
			
			List<Byte> body = new ArrayList<Byte>();
			{
				
			}

			// end of header
			header.putShort((short)0);
			header.put(new byte[]{(byte)0x00, (byte)0xFF});

			bos.write(header.array());
			bos.flush();
			bos.close();
		}
		catch (Exception ex){
			ex.printStackTrace();
			System.err.println("Failed to save world to disk");
		}
	}

}
