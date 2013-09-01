package mineflat.event.block;

import mineflat.Block;
import mineflat.Location;
import mineflat.util.ChunkUtil;
import mineflat.util.VboUtil;

public class BlockBreakEvent extends BlockEvent {

	public BlockBreakEvent(Location l, Block block){
		this.location = l;
		this.oldBlock = block;
		this.newBlock = null;
		block.getLocation().getChunk();
		block.destroy();
		block = null;
		ChunkUtil.getChunk(l.getChunk()).updateLight();
		VboUtil.updateChunkArray(l.getChunk());
		if (l.getChunk() == 1){
			ChunkUtil.getChunk(l.getChunk() - 2).updateLight();
			VboUtil.updateChunkArray(l.getChunk() - 2);
		}
		else {
			ChunkUtil.getChunk(l.getChunk() - 1).updateLight();
			VboUtil.updateChunkArray(l.getChunk() - 1);
		}
		if (l.getChunk() == -1){
			ChunkUtil.getChunk(l.getChunk() + 2).updateLight();
			VboUtil.updateChunkArray(l.getChunk() + 2);
		}
		else {
			ChunkUtil.getChunk(l.getChunk() + 1).updateLight();
			VboUtil.updateChunkArray(l.getChunk() + 1);
		}
	}

	//TODO: Implement Cancellable

}
