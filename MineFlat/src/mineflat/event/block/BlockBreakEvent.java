package mineflat.event.block;

import mineflat.Block;
import mineflat.Location;
import mineflat.util.VboUtil;

public class BlockBreakEvent extends BlockEvent {
	
	public BlockBreakEvent(Location l, Block block){
		this.location = l;
		this.oldBlock = block;
		this.newBlock = null;
		block.destroy();
		block = null;
		VboUtil.updateChunkArray(l.getChunk());
	}
	
	//TODO: Implement Cancellable
	
}
