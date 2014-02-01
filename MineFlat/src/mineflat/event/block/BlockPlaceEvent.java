package mineflat.event.block;

import mineflat.Block;
import mineflat.Location;
import mineflat.MineFlat;
import mineflat.util.VboUtil;

public class BlockPlaceEvent extends BlockEvent {

	public BlockPlaceEvent(Location l, Block b){
		this.location = l;
		this.oldBlock = null;
		this.newBlock = b;
		MineFlat.world.getChunk(l.getChunk()).updateLight();
		if (MineFlat.world.getChunk(l.getChunk() - 1) != null)
			MineFlat.world.getChunk(l.getChunk() - 1).updateLight();
		if (MineFlat.world.getChunk(l.getChunk() + 1) != null)
			MineFlat.world.getChunk(l.getChunk() + 1).updateLight();
		VboUtil.updateChunkArray(l.getChunk());
		if (l.getChunk() == 1){
			if (MineFlat.world.isChunkGenerated(l.getChunk() - 2)){
				MineFlat.world.getChunk(l.getChunk() - 2).updateLight();
				VboUtil.updateChunkArray(l.getChunk() - 2);
			}
		}
		else {
			if (MineFlat.world.isChunkGenerated(l.getChunk() - 1)){
				MineFlat.world.getChunk(l.getChunk() - 1).updateLight();
				VboUtil.updateChunkArray(l.getChunk() - 1);
			}
		}
		if (l.getChunk() == -1){
			if (MineFlat.world.isChunkGenerated(l.getChunk() + 2)){
				MineFlat.world.getChunk(l.getChunk() + 2).updateLight();
				VboUtil.updateChunkArray(l.getChunk() + 2);
			}
		}
		else {
			if (MineFlat.world.isChunkGenerated(l.getChunk() + 1)){
				MineFlat.world.getChunk(l.getChunk() + 1).updateLight();
				VboUtil.updateChunkArray(l.getChunk() + 1);
			}
		}
		VboUtil.prepareBindArray();
	}

}
