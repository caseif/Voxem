package mineflat;

import java.util.ArrayList;
import java.util.List;

public class Chunk {

	public static List<Chunk> chunks = new ArrayList<Chunk>();

	protected int num;

	private Block[][] blocks;

	public Chunk(int num){
		this.num = num;
		blocks = new Block[MineFlat.world.getChunkLength()][MineFlat.world.getChunkHeight()];
		chunks.add(this);
	}

	public int getNum(){
		return num;
	}

	public Block getBlock(int x, int y){
		return blocks[x][y];
	}

	public Block[][] getBlocks(){
		return blocks;
	}

	public void setNum(int num){
		this.num = num;
	}

	public void setBlock(int x, int y, Block b){
		blocks[x][y] = b;
	}



	public void updateLight(){
		for (int x = 0; x < MineFlat.world.getChunkLength(); x++)
			for (int y = 0; y < MineFlat.world.getChunkHeight(); y++)
				if (Block.isSolid(x, y)) this.getBlock(x, y).setLightLevel(0);
		for (int i = 0; i < 2; i++){
			for (int xx = 0; xx < MineFlat.world.getChunkLength(); xx++)
				for (int yy = 0; yy < MineFlat.world.getChunkHeight(); yy++){
					int x = i == 0 ? xx : 15 - xx;
					int y = i == 0 ? yy : MineFlat.world.getChunkHeight() - 1 - yy;
					Block b = this.getBlock(x, y);
					if (Block.isSolid(b)){
						if (Block.getTop(b.getX()) == b.getY())
							this.getBlock(x, y).setLightLevel(16);
						else {
							Block up = null, down = null, left = null, right = null;
							if (b.getY() > 0)
								up = Block.getBlock((int)b.getX(), b.getY() - 1);
							if (b.getY() < MineFlat.world.getChunkHeight() - 1)
								down = Block.getBlock((int)b.getX(), b.getY() +
										1);
							left = Block.getBlock((int)b.getX() - 1, b.getY());
							right = Block.getBlock((int)b.getX() + 1, b.getY());
							Block[] adjacent = new Block[]{up, down, left, right};
							int brightest = 0;
							for (Block bl : adjacent){
								if (Block.isSolid(bl) && bl.getLightLevel() > brightest)
									brightest = bl.getLightLevel();
							}
							if (brightest > Block.minLight)
								b.setLightLevel(brightest - Block.lightDistance);
							else
								b.setLightLevel(Block.minLight);
						}
					}
				}
		}
	}

	public static Chunk getChunk(int i){
		for (Chunk c : Chunk.chunks){
			if (c.getNum() == i)
				return c;
		}
		return null;
	}

	public static boolean isChunkGenerated(int i){
		return getChunk(i) != null;
	}

	public static int getBlockXFromChunk(int chunk, int block){
		return chunk > 0 ? (chunk - 1) * MineFlat.world.getChunkLength() + block :
			chunk * MineFlat.world.getChunkLength() + block;
	}

	public static int getChunkNum(int x){
		int add = 1;
		if (x < 0)
			add = -1;
		return x / MineFlat.world.getChunkLength() + add;
	}
}
