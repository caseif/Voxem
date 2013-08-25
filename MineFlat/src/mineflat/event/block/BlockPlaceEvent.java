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
		VboUtil.updateChunkArray(l.getChunk());
		if (l.getPosInChunk() == 0)
			if (l.getChunk() == 1)
				VboUtil.updateChunkArray(l.getChunk() - 2);
			else
				VboUtil.updateChunkArray(l.getChunk() - 1);
		else if (l.getPosInChunk() == 15)
			if (l.getChunk() == -1)
				VboUtil.updateChunkArray(l.getChunk() + 2);
			else
				VboUtil.updateChunkArray(l.getChunk() + 1);
	}
	
}
