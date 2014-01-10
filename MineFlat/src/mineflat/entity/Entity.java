package mineflat.entity;

import mineflat.Block;
import mineflat.Location;
import mineflat.MineFlat;
import mineflat.util.MiscUtil;

public class Entity {

	/**
	 * The speed at which entities will fall
	 */
	public static float gravity = .5f;

	/**
	 * The terminal downwards velocity of entities
	 */
	public static float terminalVelocity = 1f;

	public static float yVelocity = 0;

	protected float x;
	protected float y;
	protected EntityType type;

	public float getX(){
		return x;
	}

	public float getY(){
		return y;
	}

	public EntityType getType(){
		return type;
	}

	public void setX(float x){
		this.x = x;
	}

	public void setY(float y){
		this.y = y;
	}

	public void setType(EntityType type){
		this.type = type;
	}

	public float getYVelocity(){
		return yVelocity;
	}

	public void setYVelocity(float v){
		yVelocity = v;
	}

	public void manageMovement(){

		if(isOnGround())
			MineFlat.player.setYVelocity(0);	
		else {
			if (getYVelocity() < terminalVelocity){
				float newFallSpeed = getYVelocity() +
						(gravity * MineFlat.delta / MiscUtil.timeResolution);
				if (newFallSpeed > terminalVelocity)
					newFallSpeed = terminalVelocity;
				setYVelocity(newFallSpeed);

			}
		}
	}

	public boolean isOnGround() {
		if (Math.floor(getY() + 2) < 128){
			float x = (Math.abs(getX()) % 1 >= 0.5 && getX() > 0) || (Math.abs(getX()) % 1 <= 0.5 &&
					getX() < 0) ? getX() - 4f / 16 : getX() + 4f / 16;
					if (x < 0)
						x -= 1;
					Block below = null;
					if (getY() >= -2)
						below = new Location((float)x, (float)Math.floor(getY() + 2)).getBlock();
					if (below != null)
						return true;
					else
						return false;
		}

		else return true;
	}

}
