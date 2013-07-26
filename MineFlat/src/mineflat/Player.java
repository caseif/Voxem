package mineflat;

import java.io.InputStream;
import java.util.Random;

import javax.imageio.ImageIO;

import mineflat.util.BlockUtil;
import mineflat.util.ImageUtil;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Player {
	
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
		glBindTexture(GL_TEXTURE_2D, sprite.getTextureID());
		glTranslatef(getX() * Block.length + MineFlat.xOffset, getY() * Block.length + MineFlat.yOffset, 0);
		//glCallList(playerHandle);
		glBindTexture(GL_TEXTURE_2D, 0);
		glPopMatrix();
		glBegin(GL_QUADS);
		Random r = new Random();
		glColor3f(r.nextFloat(), r.nextFloat(), r.nextFloat());
		glVertex2f(15, 15);
		glVertex2f(50, 15);
		glVertex2f(50, 50);
		glVertex2f(15, 50);
		glEnd();
	}
	
	public static void initialize(){
		try {
			InputStream is = BlockUtil.class.getClassLoader().getResourceAsStream(
					"textures/char_prim.png");
			InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
					ImageIO.read(is), Block.length, Block.length));
			sprite = TextureLoader.getTexture("PNG", newIs);
		}
		catch (Exception ex){
			System.err.println("Exception occurred while preparing texture for player sprite");
			ex.printStackTrace();
		}
		
		playerHandle = glGenLists(1);
		
		glNewList(GL_COMPILE, playerHandle);
		{
			glBegin(GL_QUADS);
			int hWidth = Block.length;
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
		}
		glEndList();
	}
	
}
