package mineflat.event.block;

import mineflat.Block;
import mineflat.Location;
import mineflat.util.ChunkUtil;
import mineflat.util.VboUtil;

public class BlockPlaceEvent extends BlockEvent {

	public BlockPlaceEvent(Location l, Block b){
		this.location = l;
		this.oldBlock = null;
		this.newBlock = b;
		ChunkUtil.getChunk(l.getChunk()).updateLight();
		VboUtil.updateChunkArray(l.getChunk());
		if (l.getChunk() == 1){
			if (ChunkUtil.isChunkGenerated(l.getChunk() - 2)){
				ChunkUtil.getChunk(l.getChunk() - 2).updateLight();
				VboUtil.updateChunkArray(l.getChunk() - 2);
			}
		}
		else {
			if (ChunkUtil.isChunkGenerated(l.getChunk() - 1)){
				ChunkUtil.getChunk(l.getChunk() - 1).updateLight();
				VboUtil.updateChunkArray(l.getChunk() - 1);
			}
		}
		if (l.getChunk() == -1){
			if (ChunkUtil.isChunkGenerated(l.getChunk() + 2)){
				ChunkUtil.getChunk(l.getChunk() + 2).updateLight();
				VboUtil.updateChunkArray(l.getChunk() + 2);
			}
		}
		else {
			if (ChunkUtil.isChunkGenerated(l.getChunk() + 1)){
				ChunkUtil.getChunk(l.getChunk() + 1).updateLight();
				VboUtil.updateChunkArray(l.getChunk() + 1);
			}
		}
		VboUtil.prepareBindArray();
	}

}
