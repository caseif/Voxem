package mineflat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mineflat.util.BlockUtil;
import mineflat.util.ChunkUtil;

public class CaveFactory {

	public static Random r = new Random(MineFlat.seed);
	public static List<CaveFactory> caveFactories = new ArrayList<CaveFactory>();
	public static List<CaveFactory> deactivate = new ArrayList<CaveFactory>(); // dem CMEs :P

	private int x;
	private int y;

	public CaveFactory(int x, int y){
		this.x = x;
		this.y = y;
		CaveFactory.caveFactories.add(this);
	}

	public void dig(){
		List<Block> surrounding = new ArrayList<Block>();
		if (y > 0 && BlockUtil.getBlock(x, y - 1) != null &&
				BlockUtil.getBlock(x, y - 1).getType() != Material.AIR){
			surrounding.add(BlockUtil.getBlock(x, y - 1));
		}
		if (y < 126 && BlockUtil.getBlock(x, y + 1) != null &&
				BlockUtil.getBlock(x, y + 1).getType() != Material.AIR){
			surrounding.add(BlockUtil.getBlock(x, y + 1));
			surrounding.add(BlockUtil.getBlock(x, y + 1));
			surrounding.add(BlockUtil.getBlock(x, y + 1));
		}
		if (x > ChunkUtil.totalChunks / 2 * -16 && BlockUtil.getBlock(x - 1, y) != null &&
				BlockUtil.getBlock(x - 1, y).getType() != Material.AIR){
			surrounding.add(BlockUtil.getBlock(x - 1, y));
			surrounding.add(BlockUtil.getBlock(x - 1, y));
		}
		if (x < ChunkUtil.totalChunks / 2 * 16 + 15 && BlockUtil.getBlock(x + 1, y) != null &&
				BlockUtil.getBlock(x + 1, y).getType() != Material.AIR){
			surrounding.add(BlockUtil.getBlock(x + 1, y));
			surrounding.add(BlockUtil.getBlock(x + 1, y));
		}
		if (surrounding.size() == 8)
			System.out.println(surrounding.size());
		if (surrounding.size() == 0)
			this.deactivate();
		else {
			Block destroy = surrounding.get(r.nextInt(surrounding.size()));
			this.x = destroy.getX();
			this.y = destroy.getY();
			destroy.destroy(); // destroy *overdrive intensifies*
			int chance = (150 - y) / 2;
			if (r.nextInt(chance) == 0)
				new CaveFactory(x, y);
		}

	}

	public void deactivate(){
		deactivate.add(this);
	}

}
