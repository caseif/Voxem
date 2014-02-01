package mineflat.entity;

import mineflat.GraphicsHandler;
import mineflat.Location;
import mineflat.MineFlat;
import mineflat.event.Event;
import mineflat.event.player.PlayerMoveEvent;

import org.lwjgl.opengl.Display;

public class Player extends LivingEntity {

	/**
	 * Distance from center of screen at which the screen will begin scrolling when the player reaches it
	 */
	public static final float scrollTolerance = 0.25f;

	public Player(float x, float y){
		super(EntityType.PLAYER, x, y, 0.5f, 2f);
	}

	public Location getLocation(){
		return new Location(x, y);
	}

	@Override
	public void setX(float x){
		Location old = getLocation();
		super.setX(x);
		Event.fireEvent(new PlayerMoveEvent(this, getLocation(), old));
	}

	@Override
	public void setY(float y){
		Location old = getLocation();
		super.setY(y);
		Event.fireEvent(new PlayerMoveEvent(this, getLocation(), old));
	}

	public void setYVelocity(float v){
		yVelocity = v;
	}

	public float getYVelocity(){
		return yVelocity;
	}

	public static void centerPlayer(){
		if (MineFlat.player.getLocation().getPixelX() <
				Display.getWidth() / 2 - GraphicsHandler.xOffset -
				(int)(Display.getWidth() / 2 * scrollTolerance))
			GraphicsHandler.xOffset =
			Display.getWidth() / 2 - MineFlat.player.getLocation().getPixelX() -
			(int)(Display.getWidth() / 2 * scrollTolerance);
		else if (MineFlat.player.getLocation().getPixelX() >
		Display.getWidth() / 2 - GraphicsHandler.xOffset + (int)(Display.getWidth() / 2 * scrollTolerance))
			GraphicsHandler.xOffset =
			Display.getWidth() / 2 - MineFlat.player.getLocation().getPixelX() +
			(int)(Display.getWidth() / 2 * scrollTolerance);

		if (MineFlat.player.getLocation().getPixelY() <
				Display.getHeight() / 2 - GraphicsHandler.yOffset -
				(int)(Display.getHeight() / 2 * scrollTolerance))
			GraphicsHandler.yOffset =
			Display.getHeight() / 2 - MineFlat.player.getLocation().getPixelY() -
			(int)(Display.getHeight() / 2 * scrollTolerance);
		else if (MineFlat.player.getLocation().getPixelY() >
		Display.getHeight() / 2 - GraphicsHandler.yOffset + (int)(Display.getHeight() / 2 * scrollTolerance))
			GraphicsHandler.yOffset =
			Display.getHeight() / 2 - MineFlat.player.getLocation().getPixelY() +
			(int)(Display.getHeight() / 2 * scrollTolerance);
		//GraphicsHandler.yOffset = Display.getHeight() / 2 - MineFlat.player.getLocation().getPixelY();

	}

}
