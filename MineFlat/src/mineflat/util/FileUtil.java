package mineflat.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import mineflat.Chunk;

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
	
}
