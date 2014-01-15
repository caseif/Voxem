package mineflat.entity;

import static org.lwjgl.opengl.GL11.*;

import java.util.HashMap;

import org.newdawn.slick.opengl.Texture;

import mineflat.Block;
import mineflat.Direction;
import mineflat.GraphicsHandler;
import mineflat.Location;
import mineflat.MineFlat;
import mineflat.Timing;

public class Entity {

	/**
	 * The speed at which entities will fall
	 */
	public static final float gravity = .5f;

	/**
	 * The terminal downwards velocity of entities
	 */
	public static final float terminalVelocity = 1f;

	/**
	 * The current velocity on the x axis (e.g. from moving, throwing)
	 */
	public static float xVelocity = 0;
	
	/**
	 * The current velocity on the y axis (e.g. from falling, jumping)
	 */
	public static float yVelocity = 0;
	
	public static HashMap<EntityType, Texture> sprites = new HashMap<EntityType, Texture>();

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

	public float getXVelocity(){
		return xVelocity;
	}

	public void setXVelocity(float v){
		xVelocity = v;
	}

	public float getYVelocity(){
		return yVelocity;
	}

	public void setYVelocity(float v){
		yVelocity = v;
	}

	public void manageMovement(){
		
		
		setY(getY() + getYVelocity());
		
		if (!isXMovementBlocked())
			setX(x + xVelocity * (Timing.delta / Timing.timeResolution));
		
		if(isOnGround())
			MineFlat.player.setYVelocity(0);	
		else {
			if (getYVelocity() < terminalVelocity){
				float newFallSpeed = getYVelocity() +
						(gravity * Timing.delta / Timing.timeResolution);
				if (newFallSpeed > terminalVelocity)
					newFallSpeed = terminalVelocity;
				setYVelocity(newFallSpeed);

			}
		}
		
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

	public boolean isOnGround(){
		if (Math.floor(getY() + 2) < MineFlat.world.getChunkHeight()){
			float x = (Math.abs(getX()) % 1 >= 0.5 && getX() > 0) || (Math.abs(getX()) % 1 <= 0.5 &&
					getX() < 0) ? getX() - 4f / Block.length : getX() + 4f / Block.length;
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
	
	public boolean isXMovementBlocked(){
		float newX = getX() +
				(xVelocity * (Timing.delta / Timing.timeResolution));
		float y = getY();
		if ((int)y == y)
			y -= 1;
		Block b1 = null, b2 = null, b3 = null;
		if (y >= 0 && y < MineFlat.world.getChunkHeight() && y % 1 != 0)
			b1 = new Location((float)Math.floor(newX),
					(float)Math.floor(y)).getBlock();
		if (y >= -1 && y < MineFlat.world.getChunkHeight() - 1)
			b2 = new Location((float)Math.floor(newX),
					(float)Math.floor(y) + 1).getBlock();
		if (y >= -2 && y < 126)
			b3 = new Location((float)Math.floor(newX),
					(float)Math.floor(y) + 2).getBlock();
		if (b1 != null || b2 != null || b3 != null)
			return true;
		return false;
	}
	
	public void draw(){
		glPushMatrix();
		glEnable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, sprites.get(type).getTextureID());
		glColor3f(1f, 1f, 1f);
		glTranslatef(getX() * Block.length + GraphicsHandler.xOffset - (1f / 4f) * Block.length,
				getY() * Block.length + GraphicsHandler.yOffset, 0);
		if (this instanceof LivingEntity && ((LivingEntity)this).getFacing() == Direction.RIGHT){
			glTranslatef(Block.length / 2, 0f, 0f);
			glScalef(-1f, 1f, 1f);
		}
		glBegin(GL_QUADS);
		int hWidth = Block.length / 2;
		int hHeight = Block.length * 2;
		glTexCoord2f(0f, 0f);
		glVertex2f(0, 0);
		glTexCoord2f(1f, 0f);
		glVertex2f(hWidth, 0);
		glTexCoord2f(1f, 1f);
		glVertex2f(hWidth, hHeight);
		glTexCoord2f(0f, 1f);
		glVertex2f(0, hHeight);
		glEnd();
		glDisable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
	}

}
