package mineflat.event.block;

import mineflat.Block;
import mineflat.Location;
import mineflat.util.VboUtil;

public class BlockPlaceEvent extends BlockEvent {
	
	public BlockPlaceEvent(Location l, Block b){
		this.location = l;
		this.oldBlock = null;
		this.newBlock = b;
		b.updateLight();
		VboUtil.updateArray();
	}
	
}
