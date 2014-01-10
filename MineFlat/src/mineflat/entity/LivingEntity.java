package mineflat.entity;

import mineflat.Block;
import mineflat.Direction;
import mineflat.Location;
import mineflat.MineFlat;
import mineflat.util.MiscUtil;

public class LivingEntity extends Entity {

	/**
	 * The speed at which entities will move
	 */
	public static float speed = 5;
	
	/**
	 * The speed at which entities will jump
	 */
	public static float jumpPower = .2f;

	protected Direction dir;
	protected boolean jump = false;
	protected int walkFor;
	protected int walkTime;
	protected int lastUpdate;
	
	public Direction getDirection(){
		return dir;
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
	
	public void setDirection(Direction dir){
		this.dir = dir;
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
		
		if (dir == Direction.LEFT){
			float newX = getX() -
					(speed * (MineFlat.delta / MiscUtil.timeResolution));
			float y = getY();
			if ((int)y == y)
				y -= 1;
			Block b1 = null, b2 = null, b3 = null;
			if (y >= 0 && y < 128 && y % 1 != 0)
				b1 = new Location((float)Math.floor(newX),
						(float)Math.floor(y)).getBlock();
			if (y >= -1 && y < 127)
				b2 = new Location((float)Math.floor(newX),
						(float)Math.floor(y) + 1).getBlock();
			if (y >= -2 && y < 126)
				b3 = new Location((float)Math.floor(newX),
						(float)Math.floor(y) + 2).getBlock();
			boolean blocked = false;
			if (b1 != null)
				blocked = true;
			else if (b2 != null)
				blocked = true;
			else if (b3 != null)
				blocked = true;
			if (!blocked)
				setX(newX);
		}
		if (dir == Direction.RIGHT){
			float newX = x +
					(speed * (MineFlat.delta / MiscUtil.timeResolution));
			float y = getY();
			if ((int)y == y)
				y -= 1;
			Block b1 = null, b2 = null, b3 = null;
			if (y >= 0 && y < 128 && y % 1 != 0)
				b1 = new Location((float)Math.floor(newX),
						(float)Math.floor(y)).getBlock();
			if (y >= -1 && y < 127)
				b2 = new Location((float)Math.floor(newX),
						(float)Math.floor(y) + 1).getBlock();
			if (y >= -2 && y < 126)
				b3 = new Location((float)Math.floor(newX),
						(float)Math.floor(y) + 2).getBlock();
			boolean blocked = false;
			if (b1 != null)
				blocked = true;
			else if (b2 != null)
				blocked = true;
			else if (b3 != null)
				blocked = true;
			if (!blocked)
				setX(newX);
		}

		if(jump && isOnGround())
			setYVelocity(-jumpPower);

		float newY = getY() + getYVelocity() + 0.06f;
		if (newY >= 0 && newY <= 127){
			float pX = getX() >= 0 ? getX() :
				getX() - 1;
			if (new Location(pX, (float)Math.floor(newY)).getBlock() != null)
				setYVelocity(
						gravity * MineFlat.delta / MiscUtil.timeResolution);
		}

		setY(getY() + getYVelocity());

		if (Math.floor(getY() + 2) < 128){
			float x = (Math.abs(getX()) % 1 >= 0.5 && getX() > 0) ||
					(Math.abs(getX()) % 1 <= 0.5 && getX() < 0) ?
							getX() - 4f / 16 : getX() + 4f / 16;
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
	
	public boolean getJumping(){
		return jump;
	}
	
	public void setJumping(boolean jump){
		this.jump = jump;
	}
}
