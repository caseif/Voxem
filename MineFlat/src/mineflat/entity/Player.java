package mineflat.entity;

import java.io.InputStream;

import javax.imageio.ImageIO;

import mineflat.Block;
import mineflat.Direction;
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
	 * The height to which the player will jump
	 */
	public static float jumpHeight = 3;

	protected static Texture sprite = null;
	protected static Texture sprite_flip = null;
	protected static char lastSprite;
	
	protected static int playerHandle = 0;
	
	

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
	

	public void draw(){
		glPushMatrix();
		glEnable(GL_BLEND);
		if(MineFlat.player.dir == Direction.LEFT){
			glBindTexture(GL_TEXTURE_2D, sprite.getTextureID());
			lastSprite = 'l';
		}else if(MineFlat.player.dir == Direction.RIGHT){
			glBindTexture(GL_TEXTURE_2D, sprite_flip.getTextureID());
			lastSprite = 'r';
		}else{
			if(lastSprite == 'r'){
				glBindTexture(GL_TEXTURE_2D, sprite_flip.getTextureID());
			}else{
				glBindTexture(GL_TEXTURE_2D, sprite.getTextureID());
			}
		}
		
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
			InputStream is2 = BlockUtil.class.getClassLoader().getResourceAsStream(
					"textures/char_flip.png");
			InputStream new2 = ImageUtil.asInputStream(ImageUtil.scaleImage(
					ImageIO.read(is2), 64, 64));
			sprite_flip = TextureLoader.getTexture("PNG", new2);
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
