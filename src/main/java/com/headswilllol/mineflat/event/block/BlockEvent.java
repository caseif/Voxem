package com.headswilllol.mineflat.event.block;

import com.headswilllol.mineflat.world.Block;
import com.headswilllol.mineflat.location.WorldLocation;
import com.headswilllol.mineflat.event.Event;

public class BlockEvent extends Event {

	protected WorldLocation location;
	protected Block oldBlock;
	protected Block newBlock;
	
	public Block getBlock(){
		return location.getBlock();
	}
	
	public WorldLocation getLocation(){
		return location;
	}
	
	public Block getOldBlock(){
		return oldBlock;
	}

}
