package mineflat;

import java.util.ArrayList;
import java.util.List;

import mineflat.util.BlockUtil;

public class Chunk {

	public static List<Chunk> chunks = new ArrayList<Chunk>();

	protected int num;

	private Block[][] blocks;

	public Chunk(int num){
		this.num = num;
		blocks = new Block[16][128];
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
		/*if (BlockUtil.getTop(this.getX()) == this.getY())
			this.setLightLevel(16);
		Block up = null, down = null, left = null, right = null;
		if (this.getY() > 0)
			up = BlockUtil.getBlock((int)this.getX(), this.getY() - 1);
		if (this.getY() < 127)
			down = BlockUtil.getBlock((int)this.getX(), this.getY() + 1);
		left = BlockUtil.getBlock((int)this.getX() - 1, this.getY());
		right = BlockUtil.getBlock((int)this.getX() + 1, this.getY());
		Block[] adjacent = new Block[]{up, down, left, right};
		boolean adj = false;
		for (Block b : adjacent){
			if (b != null){
				if (b.getLightLevel() < this.getLightLevel() - 1){
					if (getLightLevel() <= minLight + 1)
						b.setLightLevel(minLight);
					else
						b.setLightLevel(this.getLightLevel() - 1);
					if (b == left || b == up)
						b.updateLight();
				}
				adj = true;
			}
		}
		if (!adj)
			for (int y = getY(); y >= 0; y--)
				if (BlockUtil.getBlock(getX(), y) != null)
					light -= 1;*/
		for (int x = 0; x < 16; x++)
			for (int y = 0; y < 128; y++)
				if (this.getBlock(x, y) != null)
					this.getBlock(x, y).setLightLevel(0);
		for (int i = 0; i < 2; i++){
		for (int xx = 0; xx < 16; xx++)
			for (int yy = 0; yy < 128; yy++){
				int x = i == 0 ? xx : 15 - xx;
				int y = i == 0 ? yy : 127 - yy;
				Block b = this.getBlock(x, y);
				if (b != null){
					if (BlockUtil.getTop(b.getX()) == b.getY())
						this.getBlock(x, y).setLightLevel(16);
					else {
						Block up = null, down = null, left = null, right = null;
						if (b.getY() > 0)
							up = BlockUtil.getBlock((int)b.getX(), b.getY() - 1);
						if (b.getY() < 127)
							down = BlockUtil.getBlock((int)b.getX(), b.getY() +
									1);
						left = BlockUtil.getBlock((int)b.getX() - 1, b.getY());
						right = BlockUtil.getBlock((int)b.getX() + 1, b.getY());
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
}
