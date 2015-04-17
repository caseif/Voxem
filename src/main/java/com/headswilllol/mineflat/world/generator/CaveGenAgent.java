/*
 * MineFlat
 * Copyright (c) 2014-2015, Maxim Roncacé <caseif@caseif.net>
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
package com.headswilllol.mineflat.world.generator;

import com.headswilllol.mineflat.Main;
import com.headswilllol.mineflat.world.Location;
import com.headswilllol.mineflat.world.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaveGenAgent {

	public static final Random r = new Random(Main.world.seed);
	public static List<CaveGenAgent> caveFactories = new ArrayList<>();
	public static final List<CaveGenAgent> deactivate = new ArrayList<>(); // dem CMEs :P
	public static final int
			UP_BIAS = 3,
			DOWN_BIAS = 2,
			LEFT_BIAS = 2,
			RIGHT_BIAS = 2;
	public static final int SPAWN_CHANCE = 130;

	private Location l;

	public CaveGenAgent(Location location){
		this.l = location;
		CaveGenAgent.caveFactories.add(this);
	}

	public void dig(){
		List<Block> surrounding = new ArrayList<>();
		if (l.getY() > 0 &&
				Block.isSolid(l.getLevel(), l.getX(), l.getY())){
			for (int i = 0; i < UP_BIAS; i++)
				surrounding.add(Block.getBlock(l.getLevel(), l.getX(), l.getY() - 1));
		}
		if (l.getY() < Main.world.getChunkHeight() - 2 &&
				Block.isSolid(l.getLevel(), l.getX(), l.getY() + 1)){
			for (int i = 0; i < DOWN_BIAS; i++)
				surrounding.add(Block.getBlock(l.getLevel(), l.getX(), l.getY() + 1));
		}
		if (l.getX() > Main.world.getChunkCount() / 2 * -Main.world.getChunkLength() &&
				Block.isSolid(l.getLevel(), l.getX() - 1, l.getY())){
			for (int i = 0; i < LEFT_BIAS; i++)
				surrounding.add(Block.getBlock(l.getLevel(), l.getX() - 1, l.getY()));
		}
		if (l.getX() < (Main.world.getChunkCount() / 2 + 1) * Main.world.getChunkLength() - 1 &&
				Block.isSolid(l.getLevel(), l.getX() + 1, l.getY())){
			for (int i = 0; i < RIGHT_BIAS; i++)
				surrounding.add(Block.getBlock(l.getLevel(), l.getX() + 1, l.getY()));
		}
		if (surrounding.size() == 0)
			this.deactivate();
		else {
			Block destroy = surrounding.get(r.nextInt(surrounding.size()));
			l.setX(destroy.getX());
			l.setY(destroy.getY());
			destroy.destroy(); // destroy *overdrive intensifies*
			int chance = (int)((SPAWN_CHANCE - l.getY()) / 2);
			if (r.nextInt(chance) == 0)
				new CaveGenAgent(l);
		}

	}

	public void deactivate(){
		deactivate.add(this);
	}

}
