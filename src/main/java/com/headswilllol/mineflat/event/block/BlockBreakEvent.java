package com.headswilllol.mineflat.event.block;

import com.headswilllol.mineflat.world.Block;
import com.headswilllol.mineflat.location.WorldLocation;
import com.headswilllol.mineflat.util.VboUtil;

public class BlockBreakEvent extends BlockEvent {

	public BlockBreakEvent(WorldLocation l, Block block){
		this.location = l;
		this.oldBlock = block;
		this.newBlock = null;
		block.getLocation().getChunk();
		block.destroy();
		/*Main.world.getChunk(l.getChunk()).updateLight();
		if (Main.world.getChunk(l.getChunk() - 1) != null)
			Main.world.getChunk(l.getChunk() - 1).updateLight();
		if (Main.world.getChunk(l.getChunk() + 1) != null)
			Main.world.getChunk(l.getChunk() + 1).updateLight();*/
		block.updateLight();
		VboUtil.updateChunkArray(l.getLevel(), l.getChunk());
		if (l.getChunk() == 1){
			if (l.getLevel().isChunkGenerated(l.getChunk() - 2)){
				//Main.world.getChunk(l.getChunk() - 2).updateLight();
				VboUtil.updateChunkArray(l.getLevel(), l.getChunk() - 2);
			}
		}
		else {
			if (l.getLevel().isChunkGenerated(l.getChunk() - 1)){
				//Main.world.getChunk(l.getChunk() - 1).updateLight();
				VboUtil.updateChunkArray(l.getLevel(), l.getChunk() - 1);
			}
		}
		if (l.getChunk() == -1){
			if (l.getLevel().isChunkGenerated(l.getChunk() + 2)){
				//Main.world.getChunk(l.getChunk() + 2).updateLight();
				VboUtil.updateChunkArray(l.getLevel(), l.getChunk() + 2);
			}
		}
		else {
			if (l.getLevel().isChunkGenerated(l.getChunk() + 1)){
				//Main.world.getChunk(l.getChunk() + 1).updateLight();
				VboUtil.updateChunkArray(l.getLevel(), l.getChunk() + 1);
			}
		}
		VboUtil.prepareBindArray();
	}

	//TODO: Implement Cancellable

}
