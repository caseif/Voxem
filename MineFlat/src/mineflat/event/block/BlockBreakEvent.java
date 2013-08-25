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

	//TODO: Implement Cancellable

}
