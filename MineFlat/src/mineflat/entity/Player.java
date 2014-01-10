package mineflat.entity;

import java.io.InputStream;

import javax.imageio.ImageIO;

import mineflat.Block;
import mineflat.Location;
import mineflat.MineFlat;
import mineflat.event.Event;
import mineflat.event.player.PlayerMoveEvent;
import mineflat.util.BlockUtil;
import mineflat.util.ImageUtil;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Player extends LivingEntity {



	/**
	 * The speed at which the player will fall
	 */
	public static float gravity = .5f;
	public static float jumpPower = .2f;
	public static float terminalVelocity = 1.5f;
	/**
	 * The speed at which the player will move
	 */
	public static float playerSpeed = 5;

	/**
	 * The height to which the player will jump
	 */
	public static float jumpHeight = 3;

	protected static Texture sprite = null;

	protected static int playerHandle = 0;

	public static float velocityY = 0;
	
	

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
	
	public void setVelocityY(float v){
		velocityY = v;
	}
	
	public float getVelocityY(){
		return velocityY;
	}
	

	public void draw(){
		glPushMatrix();
		glEnable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, sprite.getTextureID());
		glColor3f(1f, 1f, 1f);
		glTranslatef(getX() * Block.length + MineFlat.xOffset - (4f / 16) * Block.length, getY() * Block.length + MineFlat.yOffset, 0);
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

	public static void initialize(){
		try {
			InputStream is = BlockUtil.class.getClassLoader().getResourceAsStream(
					"textures/char_prim.png");
			InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
					ImageIO.read(is), 64, 64));
			sprite = TextureLoader.getTexture("PNG", newIs);
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

	public boolean isOnGround() {
		if (Math.floor(MineFlat.player.getY() + 2) < 128){
			   float x = (Math.abs(MineFlat.player.getX()) % 1 >= 0.5 && MineFlat.player.getX() > 0) || (Math.abs(MineFlat.player.getX()) % 1 <= 0.5 && MineFlat.player.getX() < 0) ? MineFlat.player.getX() - 4f / 16 : MineFlat.player.getX() + 4f / 16;
			       if (x < 0) x -= 1;
			       Block below = null;
			       if (MineFlat.player.getY() >= -2) below = new Location((float)x, (float)Math.floor(MineFlat.player.getY() + 2)).getBlock();
			       if (below != null) return true;
			       else return false;
		}
			       
		else return true;
	}

}
