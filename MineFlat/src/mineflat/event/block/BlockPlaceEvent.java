package mineflat.event.block;

import mineflat.Block;
import mineflat.Location;
import mineflat.Chunk;
import mineflat.util.VboUtil;

public class BlockPlaceEvent extends BlockEvent {

	public BlockPlaceEvent(Location l, Block b){
		this.location = l;
		this.oldBlock = null;
		this.newBlock = b;
		Chunk.getChunk(l.getChunk()).updateLight();
		if (Chunk.getChunk(l.getChunk() - 1) != null)
			Chunk.getChunk(l.getChunk() - 1).updateLight();
		if (Chunk.getChunk(l.getChunk() + 1) != null)
			Chunk.getChunk(l.getChunk() + 1).updateLight();
		VboUtil.updateChunkArray(l.getChunk());
		if (l.getChunk() == 1){
			if (Chunk.isChunkGenerated(l.getChunk() - 2)){
				Chunk.getChunk(l.getChunk() - 2).updateLight();
				VboUtil.updateChunkArray(l.getChunk() - 2);
			}
		}
		else {
			if (Chunk.isChunkGenerated(l.getChunk() - 1)){
				Chunk.getChunk(l.getChunk() - 1).updateLight();
				VboUtil.updateChunkArray(l.getChunk() - 1);
			}
		}
		if (l.getChunk() == -1){
			if (Chunk.isChunkGenerated(l.getChunk() + 2)){
				Chunk.getChunk(l.getChunk() + 2).updateLight();
				VboUtil.updateChunkArray(l.getChunk() + 2);
			}
		}
		else {
			if (Chunk.isChunkGenerated(l.getChunk() + 1)){
				Chunk.getChunk(l.getChunk() + 1).updateLight();
				VboUtil.updateChunkArray(l.getChunk() + 1);
			}
		}
		VboUtil.prepareBindArray();
	}

}
