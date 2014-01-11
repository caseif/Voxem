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
						(gravity * Timing.delta / Timing.timeResolution);
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
