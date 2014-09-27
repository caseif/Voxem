package com.headswilllol.mineflat.event.block;

import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.world.Block;
import com.headswilllol.mineflat.util.VboUtil;

public class BlockPlaceEvent extends BlockEvent {

	public BlockPlaceEvent(Location l, Block b){
		this.location = l;
		this.oldBlock = null;
		this.newBlock = b;
		b.updateLight();
		/*Main.world.getChunk(l.getChunk()).updateLight();
		if (Main.world.getChunk(l.getChunk() - 1) != null)
			Main.world.getChunk(l.getChunk() - 1).updateLight();
		if (Main.world.getChunk(l.getChunk() + 1) != null)
			Main.world.getChunk(l.getChunk() + 1).updateLight();*/
		VboUtil.updateChunkArray(l.getLevel(), l.getChunk());
		if (l.getChunk() == 1 && l.getLevel().isChunkGenerated(l.getChunk() - 2)){
			//Main.world.getChunk(l.getChunk() - 2).updateLight();
			VboUtil.updateChunkArray(l.getLevel(), l.getChunk() - 2);
		}
		else if (l.getLevel().isChunkGenerated(l.getChunk() - 1)){
			//Main.world.getChunk(l.getChunk() - 1).updateLight();
			VboUtil.updateChunkArray(l.getLevel(), l.getChunk() - 1);
		}
		if (l.getChunk() == -1 && l.getLevel().isChunkGenerated(l.getChunk() + 2)){
			//Main.world.getChunk(l.getChunk() + 2).updateLight();
			VboUtil.updateChunkArray(l.getLevel(), l.getChunk() + 2);
		}
		else if (l.getLevel().isChunkGenerated(l.getChunk() + 1)){
			//Main.world.getChunk(l.getChunk() + 1).updateLight();
			VboUtil.updateChunkArray(l.getLevel(), l.getChunk() + 1);
		}
		VboUtil.prepareBindArray();
	}

}
