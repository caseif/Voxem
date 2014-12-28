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
package com.headswilllol.mineflat.entity;

import com.headswilllol.mineflat.Direction;
import com.headswilllol.mineflat.world.Location;

public class LivingEntity extends Entity {

	/**
	 * The speed at which entities will jump
	 */
	public static final float jumpPower = 13f;

	/**
	 * The speed at which this particular entity will move
	 */
	protected float speed = 5f;
	protected Direction facing = Direction.LEFT;
	protected Direction direction = Direction.STATIONARY;
	protected boolean jump = false;

	public LivingEntity(EntityType type, Location location, float width, float height){
		super(type, location, width, height);
	}

	public Direction getFacingDirection(){
		return facing;
	}

	public void setFacingDirection(Direction facing){
		this.facing = facing;
	}

	public Direction getMovementDirection(){
		return direction;
	}

	public void setMovementDirection(Direction direction){
		this.direction = direction;
	}

	@Override
	public void manageMovement(){

		if(direction == Direction.LEFT)
			getVelocity().setX(-getSpeed());
		if(direction == Direction.RIGHT)
			getVelocity().setX(getSpeed());
		if(direction == Direction.STATIONARY)
			getVelocity().setX(0f);

		if(jump && isOnGround())
			getVelocity().setY(-jumpPower);

		super.manageMovement();

	}

	public boolean isJumping(){
		return jump;
	}

	public void setJumping(boolean jump){
		this.jump = jump;
	}

	public float getSpeed(){
		return speed;
	}

	public void setSpeed(float speed){
		this.speed = speed;
	}
}
