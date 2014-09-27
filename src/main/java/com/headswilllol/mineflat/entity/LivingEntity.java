package com.headswilllol.mineflat.entity;

import com.headswilllol.mineflat.Direction;
import com.headswilllol.mineflat.location.WorldLocation;

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

	public LivingEntity(EntityType type, WorldLocation location, float width, float height){
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
			setXVelocity(-getSpeed());
		if(direction == Direction.RIGHT)
			setXVelocity(getSpeed());
		if(direction == Direction.STATIONARY)
			setXVelocity(0);

		if(jump && isOnGround())
			setYVelocity(-jumpPower);

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
