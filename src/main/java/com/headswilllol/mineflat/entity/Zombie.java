package com.headswilllol.mineflat.entity;

import com.headswilllol.mineflat.location.WorldLocation;

public class Zombie extends Mob {

	public Zombie(WorldLocation location){
		super(EntityType.ZOMBIE, location, 0.875f, 2f);
	}
	
}
