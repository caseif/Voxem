package mineflat.entity;

import mineflat.Direction;

public class LivingEntity extends Entity {
	
	/**
	 * The speed at which entities will jump
	 */
	public static final float jumpPower = .2f;

	/**
	 * The speed at which this particular entity will move
	 */
	protected float speed = 5f;
	protected Direction facing;
	protected Direction direction;
	protected boolean jump = false;
	protected int walkFor;
	protected int walkTime;
	protected int lastUpdate;
	
	public Direction getFacing(){
		return facing;
	}
	
	public int getPlannedWalkTime(){
		return walkFor;
	}
	
	public int getActualWalkTime(){
		return walkTime;
	}
	
	public int getLastUpdate(){
		return lastUpdate;
	}
	
	public void setFacing(Direction facing){
		this.facing = facing;
	}
	
	public void setDirection(Direction direction){
		this.direction = direction;
	}
	
	public Direction getFacing(Direction facing){
		return direction;
	}
	
	public void setPlannedWalkTime(int time){
		this.walkFor = time;
	}
	
	public void setActualWalkTime(int time){
		this.walkTime = time;
	}
	
	public void setLastUpdate(int time){ // probably won't need this, but whatever
		this.lastUpdate = time;
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
