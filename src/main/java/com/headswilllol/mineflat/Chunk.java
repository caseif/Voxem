package com.headswilllol.mineflat;

import com.headswilllol.mineflat.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Chunk {

	protected final Level level;
	protected final int num;

	private Block[][] blocks;

	public Chunk(Level level, int num){
		this.level = level;
		this.num = num;
		blocks = new Block[Main.world.getChunkLength()][Main.world.getChunkHeight()];
		level.chunks.put(num, this);
	}
	
	public Level getLevel(){
		return level;
	}

	public int getNum(){
		return num;
	}

	public Block getBlock(int x, int y){
		if (x >= 0 && x < Main.world.getChunkLength() && y >= 0 && y < Main.world.getChunkHeight())
			return blocks[x][y];
		return null;
	}

	public Block[][] getBlocks(){
		return blocks;
	}

	public void setBlock(int x, int y, Block b){
		blocks[x][y] = b;
	}

	public void updateLight(){
		for (int x = 0; x < Main.world.getChunkLength(); x++)
			for (int y = 0; y < Main.world.getChunkHeight(); y++)
				if (this.getBlock(x, y) != null && Block.isSolid(level, x, y)) this.getBlock(x, y).setLightLevel(0);
		for (int i = 0; i < 2; i++){
			for (int xx = 0; xx < Main.world.getChunkLength(); xx++)
				for (int yy = 0; yy < Main.world.getChunkHeight(); yy++){
					int x = i == 0 ? xx : 15 - xx;
					int y = i == 0 ? yy : Main.world.getChunkHeight() - 1 - yy;
					Block b = this.getBlock(x, y);
					if (b != null){
						if (b.getY() <= Block.getTop(new Location(level, b.getX(), 0)))
							b.setLightLevel(Block.maxLight);
						else {
							Block up = null, down = null, left, right;
							if (b.getY() > 0)
								up = Block.getBlock(level, b.getX(), b.getY() - 1);
							if (b.getY() < Main.world.getChunkHeight() - 1)
								down = Block.getBlock(level, b.getX(), b.getY() +
										1);
							left = Block.getBlock(level, b.getX() - 1, b.getY());
							right = Block.getBlock(level, b.getX() + 1, b.getY());
							Block[] adjacent = new Block[]{up, down, left, right};
							float average = 0;
							int total = 0;
							for (Block bl : adjacent){
								if (bl != null){
									average += bl.getLightLevel();
									total += 1;
								}
							}
							average /= total;
							if (!Block.isSolid(b)){
								b.setLightLevel((int)Math.floor(average));
							}
							else if ((int)Math.floor(average) - Block.lightDistance >= Block.minLight)
								b.setLightLevel((int)Math.floor(average) - Block.lightDistance);
							else
								b.setLightLevel(Block.minLight);
						}
					}
				}
		}
	}

	public static float getWorldXFromChunkIndex(int chunk, float block){
		return chunk > 0 ? (chunk - 1) * Main.world.getChunkLength() + block :
			chunk * Main.world.getChunkLength() + block;
	}

	public static int getChunkNum(int x){
		int add = 1;
		if (x < 0)
			add = -1;
		return x / Main.world.getChunkLength() + add;
	}
	
	public static int getIndexInChunk(int x){
		return Math.abs(x < 0 ? (x % Main.world.getChunkLength() == 0 ? 0 :
			Main.world.getChunkLength() - Math.abs(x % Main.world.getChunkLength())) :
			x % Main.world.getChunkLength());
		//return Math.abs(x % Main.world.getChunkLength());
	}

	public Collection<Entity> getEntities() {
		List<Entity> entities = new ArrayList<Entity>();
		for (Entity e : level.getEntities())
			if (e.getLocation().getChunk() == this.getNum())
				entities.add(e);
		return entities;
	}

}
