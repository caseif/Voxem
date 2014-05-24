package com.headswilllol.mineflat.event.block;

import com.headswilllol.mineflat.Block;
import com.headswilllol.mineflat.Location;
import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.util.VboUtil;

public class BlockBreakEvent extends BlockEvent {

	public BlockBreakEvent(Location l, Block block){
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
		VboUtil.updateChunkArray(l.getChunk());
		if (l.getChunk() == 1){
			if (Main.world.isChunkGenerated(l.getChunk() - 2)){
				//Main.world.getChunk(l.getChunk() - 2).updateLight();
				VboUtil.updateChunkArray(l.getChunk() - 2);
			}
		}
		else {
			if (Main.world.isChunkGenerated(l.getChunk() - 1)){
				//Main.world.getChunk(l.getChunk() - 1).updateLight();
				VboUtil.updateChunkArray(l.getChunk() - 1);
			}
		}
		if (l.getChunk() == -1){
			if (Main.world.isChunkGenerated(l.getChunk() + 2)){
				//Main.world.getChunk(l.getChunk() + 2).updateLight();
				VboUtil.updateChunkArray(l.getChunk() + 2);
			}
		}
		else {
			if (Main.world.isChunkGenerated(l.getChunk() + 1)){
				//Main.world.getChunk(l.getChunk() + 1).updateLight();
				VboUtil.updateChunkArray(l.getChunk() + 1);
			}
		}
		VboUtil.prepareBindArray();
	}

	//TODO: Implement Cancellable

}
