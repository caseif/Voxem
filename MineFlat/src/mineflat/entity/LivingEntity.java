package mineflat.entity;

import mineflat.Block;
import mineflat.Direction;
import mineflat.Location;
import mineflat.MineFlat;
import mineflat.Timing;

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
		
		super.manageMovement();

		if(jump && isOnGround())
			setYVelocity(-jumpPower);

		float newY = getY() + getYVelocity() + 0.06f;
		if (newY >= 0 && newY <= MineFlat.world.getChunkHeight() - 1){
			float pX = getX() >= 0 ? getX() :
				getX() - 1;
			if (new Location(pX, (float)Math.floor(newY)).getBlock() != null)
				setYVelocity(
						gravity * Timing.delta / Timing.timeResolution);
		}

		setY(getY() + getYVelocity());

		if (Math.floor(getY() + 2) < MineFlat.world.getChunkHeight()){
			float x = (Math.abs(getX()) % 1 >= 0.5 && getX() > 0) ||
					(Math.abs(getX()) % 1 <= 0.5 && getX() < 0) ?
							getX() - 4f / Block.length : getX() + 4f / Block.length;
			if (x < 0) x -= 1;
			Block below = null;
			if (getY() >= -2) below = new Location((float)x,
					(float)Math.floor(getY() + 2)).getBlock();
			if (below != null){
				if((float)below.getY() - getY() < 2){
					setY(below.getY() - 2);
				}  	   
			}
		}
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
