package com.headswilllol.mineflat.entity;

import com.headswilllol.mineflat.Location;

public class Zombie extends Mob {

	public Zombie(Location location){
		super(EntityType.ZOMBIE, location, 0.875f, 2f);
	}
	
}
