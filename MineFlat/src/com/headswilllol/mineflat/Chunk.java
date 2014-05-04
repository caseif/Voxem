package com.headswilllol.mineflat;

public class Chunk {

	protected int num;

	private Block[][] blocks;

	public Chunk(int num){
		this.num = num;
		blocks = new Block[Main.world.getChunkLength()][Main.world.getChunkHeight()];
		Main.world.chunks.add(this);
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
		for (int x = 0; x < Main.world.getChunkLength(); x++)
			for (int y = 0; y < Main.world.getChunkHeight(); y++)
				if (Block.isSolid(x, y)) this.getBlock(x, y).setLightLevel(0);
		for (int i = 0; i < 2; i++){
			for (int xx = 0; xx < Main.world.getChunkLength(); xx++)
				for (int yy = 0; yy < Main.world.getChunkHeight(); yy++){
					int x = i == 0 ? xx : 15 - xx;
					int y = i == 0 ? yy : Main.world.getChunkHeight() - 1 - yy;
					Block b = this.getBlock(x, y);
					if (b != null){
						if (Block.getTop(b.getX()) == b.getY())
							this.getBlock(x, y).setLightLevel(16);
						else {
							Block up = null, down = null, left = null, right = null;
							if (b.getY() > 0)
								up = Block.getBlock((int)b.getX(), b.getY() - 1);
							if (b.getY() < Main.world.getChunkHeight() - 1)
								down = Block.getBlock((int)b.getX(), b.getY() +
										1);
							left = Block.getBlock((int)b.getX() - 1, b.getY());
							right = Block.getBlock((int)b.getX() + 1, b.getY());
							Block[] adjacent = new Block[]{up, down, left, right};
							int brightest = 0;
							for (Block bl : adjacent){
								if (bl != null && bl.getLightLevel() > brightest)
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

	public static int getBlockXFromChunk(int chunk, int block){
		return chunk > 0 ? (chunk - 1) * Main.world.getChunkLength() + block :
			chunk * Main.world.getChunkLength() + block;
	}

	public static int getChunkNum(int x){
		int add = 1;
		if (x < 0)
			add = -1;
		return x / Main.world.getChunkLength() + add;
	}
}
