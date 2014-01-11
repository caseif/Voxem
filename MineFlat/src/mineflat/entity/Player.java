package mineflat.entity;

import java.io.InputStream;

import javax.imageio.ImageIO;

import mineflat.Location;
import mineflat.MineFlat;
import mineflat.event.Event;
import mineflat.event.player.PlayerMoveEvent;
import mineflat.util.BlockUtil;
import mineflat.util.ImageUtil;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.TextureLoader;

public class Player extends LivingEntity {

	/**
	 * The height to which the player will jump
	 */
	public static float jumpHeight = 3;

	public Player(float x, float y){
		this.x = x;
		this.y = y;
		this.type = EntityType.PLAYER;
	}

	public Location getLocation(){
		return new Location(x, y);
	}

	public void setX(float x){
		Location old = getLocation();
		this.x = x;
		Event.fireEvent(new PlayerMoveEvent(this, getLocation(), old));
	}

	public void setY(float y){
		Location old = getLocation();
		this.y = y;
		Event.fireEvent(new PlayerMoveEvent(this, getLocation(), old));
	}
	
	public void setYVelocity(float v){
		yVelocity = v;
	}
	
	public float getYVelocity(){
		return yVelocity;
	}

	public static void initialize(){
		try {
			InputStream is = BlockUtil.class.getClassLoader().getResourceAsStream(
					"textures/char_prim.png");
			InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
					ImageIO.read(is), 64, 64));
			Entity.sprites.put(EntityType.PLAYER, TextureLoader.getTexture("PNG", newIs));
		}
		catch (Exception ex){
			System.err.println("Exception occurred while preparing texture for player sprite");
			ex.printStackTrace();
		}
	}


	
	public static void centerPlayer(){
		MineFlat.xOffset = Display.getWidth() / 2 - MineFlat.player.getLocation().getPixelX();
		MineFlat.yOffset = Display.getHeight() / 2 - MineFlat.player.getLocation().getPixelY();
	}

}
