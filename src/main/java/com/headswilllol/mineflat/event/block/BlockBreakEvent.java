/*
 * MineFlat
 * Copyright (c) 2014, Maxim Roncacé <mproncace@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.headswilllol.mineflat.event.block;

import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.world.Block;
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
