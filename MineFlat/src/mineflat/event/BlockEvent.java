package mineflat.event;

import mineflat.Block;
import mineflat.Location;

public class BlockEvent extends Event {

	protected Location location;
	protected Block oldBlock;
	protected Block newBlock;
	
	public Block getBlock(){
		return location.getBlock();
	}

}
