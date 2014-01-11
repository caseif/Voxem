package mineflat.util;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

import mineflat.Block;
import mineflat.Chunk;
import mineflat.MineFlat;

public class FileUtil {
	
	/**
	 * Saves the world to a given file.
	 * @param f The file to save to.
	 * @return Whether the world was saved successfully.
	 */
	public boolean saveWorld(File f){
		if (f != null){
			try {
				f.createNewFile();
				FileWriter fw = new FileWriter(f.getAbsoluteFile());
				StringBuilder content = new StringBuilder();
				BufferedWriter bw = new BufferedWriter(fw);

				for (Chunk c : Chunk.chunks){
					for (int x = 0; x < 16; x++)
						for (int y = 0; y < 128; y++)
							content.append("b " + x + " " + y + " " + " " + c.getBlocks()[x][y].toString() + "\n");
				}

				bw.write(content.toString());
				bw.close();
				return true;
			}
			catch (Exception ex){
				ex.printStackTrace();
			}
		}
		f = null;
		return false;
	}

	public static String getAppDataFolder(){
		String OS = System.getProperty("os.name").toUpperCase();
		if (OS.contains("WIN"))
			return System.getenv("APPDATA");
		else if (OS.contains("MAC"))
			return System.getProperty("user.home") + "/Library/Application Support";
		try {return new File(MineFlat.class.getProtectionDomain()
				.getCodeSource().getLocation().toURI().getPath()).getParent();}
		catch (Exception ex){ex.printStackTrace();}
		return System.getProperty("user.dir");
	}
	
	/**
	 * Saves a world to disk in the MineFlat Level format. The first two bytes of the file
	 * represent the revision of the format (currently 1). Each chunk is stored to a separate
	 * section of the file. The first two bytes (0xFF 0xFF) of each section signify the start
	 * of a new chunk. The next byte represents whether or not the chunk number is negative. The
	 * next two bytes store the number of the chunk. Each block is then saved. 6 bytes are reserved
	 * per block: two for the type, two for a data value (NYI), and two for additional data (NYI).
	 * In this way, a total of 12293 bytes, or 12 kilobytes plus 5 bytes, are used per chunk.
	 * @param name The name of the world to be saved (reserved for future use).
	 */
	//TODO: Fix this steaming pile of sh*t
	public static void saveWorld(String name){
		System.out.println("Saving chunks...");
		try {
			File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator +
					"MineFlat", "saves");
			if (!saveFolder.exists())
				saveFolder.mkdir();
			saveFolder = new File(saveFolder, name);
			if (!saveFolder.exists())
				saveFolder.mkdir();
			File save = new File(saveFolder, "level.mfl");
			if (save.exists())
				save.delete();
			save.createNewFile();
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(save));
			byte[] bytes = new byte[32 * (128 * 16 * 6 + 5) + 5];
			// revision
			bytes[0] = 0x0;
			bytes[1] = 0x1;
			int i = 2;
			for (Chunk c : Chunk.chunks){
				// chunk definition
				bytes[i] = (byte)0xFF;
				bytes[i + 1] = (byte)0xFF;
				i += 2;
				bytes[i] = c.getNum() >= 0 ? (byte)0x0 : (byte)0x1;
				i += 1;
				for (byte cN : NumUtil.hexToByte(Integer.toHexString(c.getNum()))){
					bytes[i] = cN;
					i += 1;
				}
				for (int x = 0; x < 16; x++){
					for (int y = 0; y < 128; y++){
						Block b = c.getBlock(x, y);
						// block type
						byte[] type = b == null ? new byte[]{0x0, 0x0} :
							NumUtil.hexToByte(Integer.toHexString(b.getType().ordinal()));
						if (bytes.length == 1){
							bytes[i] = 0x0;
							i += 1;
						}
						for (int o = 0; i < type.length; o++)
							bytes[i + o] = type[o];
						i += type.length;
						// data value (NYI)
						bytes[i] = 0x0;
						bytes[i + 1] = 0x0;
						// additional data (NYI)
						bytes[i + 2] = 0x0;
						bytes[i + 3] = 0x0;
						i += 4;
					}
				}
			}
			bos.write(bytes);
			bos.flush();
			bos.close();
		}
		catch (Exception ex){
			ex.printStackTrace();
			System.err.println("Failed to save world to disk");
		}
	}
	
}
