package com.headswilllol.mineflat;

import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaveFactory {

	public static final Random r = new Random(Main.world.seed);
	public static List<CaveFactory> caveFactories = new ArrayList<CaveFactory>();
	public static final List<CaveFactory> deactivate = new ArrayList<CaveFactory>(); // dem CMEs :P
	public static final int UP_BIAS = 3, DOWN_BIAS = 2, LEFT_BIAS = 2, RIGHT_BIAS = 2;
	public static final int SPAWN_CHANCE = 130;

	private Location l;

	public CaveFactory(Location location){
		this.l = location;
		CaveFactory.caveFactories.add(this);
	}

	public void dig(){
		List<Block> surrounding = new ArrayList<Block>();
		if (l.getY() > 0 &&
				Block.isSolid(l.getLevel(), l.getX(), l.getY())){
			for (int i = 0; i < UP_BIAS; i++)
				surrounding.add(Block.getBlock(l.getLevel(), l.getX(), l.getY() - 1));
		}
		if (l.getY() < Main.world.getChunkHeight() - 2 &&
				Block.isSolid(l.getLevel(), l.getX(), l.getY() + 1)){
			for (int i = 0; i < DOWN_BIAS; i++)
				surrounding.add(Block.getBlock(l.getLevel(), l.getX(), l.getY() + 1));
		}
		if (l.getX() > Main.world.getChunkCount() / 2 * -Main.world.getChunkLength() &&
				Block.isSolid(l.getLevel(), l.getX() - 1, l.getY())){
			for (int i = 0; i < LEFT_BIAS; i++)
				surrounding.add(Block.getBlock(l.getLevel(), l.getX() - 1, l.getY()));
		}
		if (l.getX() < (Main.world.getChunkCount() / 2 + 1) * Main.world.getChunkLength() - 1 &&
				Block.isSolid(l.getLevel(), l.getX() + 1, l.getY())){
			for (int i = 0; i < RIGHT_BIAS; i++)
				surrounding.add(Block.getBlock(l.getLevel(), l.getX() + 1, l.getY()));
		}
		if (surrounding.size() == 0)
			this.deactivate();
		else {
			Block destroy = surrounding.get(r.nextInt(surrounding.size()));
			l.setX(destroy.getX());
			l.setY(destroy.getY());
			destroy.destroy(); // destroy *overdrive intensifies*
			int chance = (int)((SPAWN_CHANCE - l.getY()) / 2);
			if (r.nextInt(chance) == 0)
				new CaveFactory(l);
		}

	}

	public void deactivate(){
		deactivate.add(this);
	}

}
