package com.headswilllol.mineflat.event.block;

import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.world.Block;
import com.headswilllol.mineflat.event.Event;

public class BlockEvent extends Event {

	protected Location location;
	protected Block oldBlock;
	protected Block newBlock;
	
	public Block getBlock(){
		return location.getBlock();
	}
	
	public Location getLocation(){
		return location;
	}
	
	public Block getOldBlock(){
		return oldBlock;
	}

}
