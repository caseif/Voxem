package mineflat;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import mineflat.util.FileUtil;

public class SaveManager {

	/**
	 * Saves all loaded chunks in the current world
	 */
	public static void saveWorld(){
			System.out.println("Saving chunks...");
			try {
				// save level information
				File saveFolder = new File(FileUtil.getAppDataFolder() + File.separator +
						".mineflat", "saves");
				saveFolder = new File(saveFolder, "world"); //TODO: Use dynamic world name
				if (!saveFolder.exists())
					saveFolder.mkdirs();
				File levelDat = new File(saveFolder, "level.dat");
				if (levelDat.exists())
					levelDat.delete();
				levelDat.createNewFile();
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(levelDat));
				List<Byte> bytes = new ArrayList<Byte>();
				// revision
				bytes.add((byte)0xFF);
				bytes.add((byte)1);
				// byte allocation definition
				bytes.add((byte)0xFF);
				bytes.add((byte)4);
				// chunk count
				bytes.add((byte)0xFF);
				bytes.add((byte)MineFlat.world.getChunkCount());
				// chunk length
				bytes.add((byte)0xFF);
				bytes.add((byte)MineFlat.world.getChunkLength());
				// chunk height
				bytes.add((byte)0xFF);
				bytes.add((byte)MineFlat.world.getChunkHeight());
				// world name
				bytes.add((byte)0xFF);
				byte[] name = new String("world").getBytes("UTF-8"); //TODO: Lots of dynamifying to do here
				for (byte b : name)
					bytes.add(b);
				// last modification time
				bytes.add((byte)0xFF);
				byte[] timestampBytes =
						(byte[])new String(Long.toString(System.currentTimeMillis() / 1000)).getBytes();
				for (byte b : timestampBytes)
					bytes.add(b);
				byte[] primByteArray = new byte[bytes.size()];
				for (int i = 0; i < bytes.size(); i++)
					primByteArray[i] = (byte)bytes.get(i);
				bos.write(primByteArray);
				bos.flush();
				bos.close();
			}
			catch (Exception ex){
				ex.printStackTrace();
				System.err.println("Failed to save world to disk");
			}
	}
	
}
