package mineflat;

import java.io.InputStream;

import javax.imageio.ImageIO;

import mineflat.util.BlockUtil;
import mineflat.util.ImageUtil;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Player {

	/**
	 * The speed at which the player will move
	 */
	public static int playerSpeed = 5;
	
	/**
	 * The speed at which the player will fall
	 */
	public static int gravity = 2;
	
	/**
	 * The speed at which the player will jump
	 */
	public static int jumpSpeed = 2;
	
	/**
	 * The height to which the player will jump
	 */
	public static int jumpHeight = 2;
	
	protected Location location;
	
	protected static Texture sprite = null;
	
	protected static int playerHandle = 0;
	
	public Player(Location l){
		this.location = l;
	}
	
	public Location getLocation(){
		return location;
	}
	
	public void setLocation(Location location){
		this.location = location;
	}
	
	public float getX(){
		return location.getX();
	}
	
	public float getY(){
		return location.getY();
	}
	
	public void setX(float x){
		this.location.setX(x);
	}
	
	public void setY(float y){
		this.location.setY(y);
	}
	
	public void draw(){
		glPushMatrix();
		glEnable(GL_BLEND);
		glBindTexture(GL_TEXTURE_2D, sprite.getTextureID());
		glColor3f(1f, 1f, 1f);
		glTranslatef(getX() * Block.length + MineFlat.xOffset, getY() * Block.length + MineFlat.yOffset, 0);
		glBegin(GL_QUADS);
		int hWidth = Block.length / 2;
		int hHeight = Block.length * 2;
		glTexCoord2f(0f, 0f);
		glVertex2f(0.25f, 0);
		glTexCoord2f(1f, 0f);
		glVertex2f(hWidth, 0);
		glTexCoord2f(1f, 1f);
		glVertex2f(hWidth, hHeight);
		glTexCoord2f(0f, 1f);
		glVertex2f(0.25f, hHeight);
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
	
}
