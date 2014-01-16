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
	 * The width of the entity relative to a block's width
	 */
	public float width;

	/**
	 * The height of the entity relative to a block's height
	 */
	public float height;

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

	public Entity(EntityType type, float x, float y, float width, float height){
		this.type = type;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

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

		if (!isOnGround()){
			if (getYVelocity() < terminalVelocity){
				float newFallSpeed = getYVelocity() +
						(gravity * Timing.delta / Timing.timeResolution);
				if (newFallSpeed > terminalVelocity)
					newFallSpeed = terminalVelocity;
				setYVelocity(newFallSpeed);

			}
		}

		if (!isXMovementBlocked())
			setX(x + xVelocity * (Timing.delta / Timing.timeResolution));

		float newY = getY() + getYVelocity() + 0.06f;
		if (newY >= 0 && newY <= MineFlat.world.getChunkHeight() - 1){
			float pX = getX() >= 0 ? getX() :
				getX() - 1;
			if (new Location(pX, (float)Math.floor(newY)).getBlock() != null)
				setYVelocity(
						gravity * Timing.delta / Timing.timeResolution);
		}

		setY(getY() + getYVelocity());
		if (Math.floor(getY() + height) < MineFlat.world.getChunkHeight()){
			float x = (Math.abs(getX()) % 1 >= 0.5 && getX() > 0) ||
					(Math.abs(getX()) % 1 <= 0.5 && getX() < 0) ?
							getX() - 4f / Block.length : getX() + 4f / Block.length;
							if (x < 0) x -= 1;
							Block below = null;
							if (getY() >= -height) below = new Location((float)x,
									(float)Math.floor(getY() + height)).getBlock();
							if (below != null){
								if((float)below.getY() - getY() < height){
									setY(below.getY() - height);
								}  	   
							}
		}
	}

	public boolean isOnGround(){
		if (Math.floor(getY() + height) < MineFlat.world.getChunkHeight()){
			float x = (Math.abs(getX()) % 1 >= width / 2 && getX() > 0) || (Math.abs(getX()) % 1 <= width / 2 &&
					getX() < 0) ? getX() - width / 4 : getX() + width / 4;
					if (x < 0)
						x -= 1;
					Block below = null;
					if (getY() >= -height)
						below = new Location((float)x, (float)Math.floor(getY() + height)).getBlock();
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
		int minY = (int)Math.floor(y);
		int maxY = (int)Math.floor(y + height - 1);
		for (int y = minY; y <= maxY; y++)
			if (y >= 0 && y < MineFlat.world.getChunkHeight() && Block.getBlock(newX, y) != null)
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
			glTranslatef(Block.length * width, 0f, 0f);
			glScalef(-1f, 1f, 1f);
		}
		glBegin(GL_QUADS);
		int hWidth = (int)(Block.length * width);
		int hHeight = (int)(Block.length * height);
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
