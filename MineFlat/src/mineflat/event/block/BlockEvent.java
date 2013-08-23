package mineflat.event.block;

import mineflat.Block;
import mineflat.Location;
import mineflat.event.Event;

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
