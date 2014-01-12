package mineflat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaveFactory {

	public static Random r = new Random(Terrain.seed);
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
		if (y > 0 && Block.getBlock(x, y - 1) != null &&
				Block.getBlock(x, y - 1).getType() != Material.AIR){
			surrounding.add(Block.getBlock(x, y - 1));
		}
		if (y < MineFlat.world.getChunkHeight() - 2 && Block.getBlock(x, y + 1) != null &&
				Block.getBlock(x, y + 1).getType() != Material.AIR){
			surrounding.add(Block.getBlock(x, y + 1));
			surrounding.add(Block.getBlock(x, y + 1));
			surrounding.add(Block.getBlock(x, y + 1));
		}
		if (x > MineFlat.world.getChunkCount() / 2 * -MineFlat.world.getChunkLength() &&
				Block.getBlock(x - 1, y) != null &&
				Block.getBlock(x - 1, y).getType() != Material.AIR){
			surrounding.add(Block.getBlock(x - 1, y));
			surrounding.add(Block.getBlock(x - 1, y));
		}
		if (x < (MineFlat.world.getChunkCount() / 2 + 1) * MineFlat.world.getChunkLength() - 1 &&
				Block.getBlock(x + 1, y) != null &&
				Block.getBlock(x + 1, y).getType() != Material.AIR){
			surrounding.add(Block.getBlock(x + 1, y));
			surrounding.add(Block.getBlock(x + 1, y));
		}
		if (surrounding.size() == 0)
			this.deactivate();
		else {
			Block destroy = surrounding.get(r.nextInt(surrounding.size()));
			this.x = destroy.getX();
			this.y = destroy.getY();
			destroy.destroy(); // destroy *overdrive intensifies*
			int chance = (170 - y) / 2;
			if (r.nextInt(chance) == 0)
				new CaveFactory(x, y);
		}

	}

	public void deactivate(){
		deactivate.add(this);
	}

}
