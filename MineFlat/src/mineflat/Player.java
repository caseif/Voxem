package mineflat;

import java.io.InputStream;

import javax.imageio.ImageIO;

import mineflat.event.Event;
import mineflat.event.PlayerMoveEvent;
import mineflat.util.BlockUtil;
import mineflat.util.ImageUtil;
import mineflat.util.MiscUtil;

import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class Player {

	/**
	 * The speed at which the player will move
	 */
	public static float playerSpeed = 5;

	/**
	 * The speed at which the player will fall
	 */
	public static float gravity = 1;

	/**
	 * The speed at which the player will jump
	 */
	public static float jumpSpeed = 1;

	/**
	 * The height to which the player will jump
	 */
	public static float jumpHeight = 3;

	protected Location location;

	protected static Texture sprite = null;

	protected static int playerHandle = 0;

	public static boolean falling = false;

	public static float jumpFrame = 0;
	public static float fallFrame = 0;

	public static boolean isFalling(){
		return falling;
	}

	public static void setFalling(boolean falling){
		Player.falling = falling;
	}

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
		Location old = location;
		this.location.setX(x);
		Event.fireEvent(new PlayerMoveEvent(this, getLocation(), old));
	}

	public void setY(float y){
		Location old = location;
		this.location.setY(y);
		Event.fireEvent(new PlayerMoveEvent(this, getLocation(), old));
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

	public static void handleVerticalMovement(){
		
		float oldY = MineFlat.player.getY();

		if (isFalling()){
			float newY = MineFlat.player.getY() +
					gravity * 10 * (MineFlat.delta / MiscUtil.getTimeResolution());
			fallFrame +=  10 * MineFlat.delta / MiscUtil.getTimeResolution();
			MineFlat.player.setY(newY);
		}
		else if (jumpFrame > 0){
			float newY = MineFlat.player.getY() -
					jumpSpeed * 10 * (MineFlat.delta / MiscUtil.getTimeResolution());
			boolean block = false;
			if (newY >= 1){
				Block b = new Location(MineFlat.player.getX(), (float)Math.floor(newY - 1)).getBlock();
				if (b != null)
					if (b.getType() != Material.AIR)
						block = true;
			}
			if (jumpFrame < jumpHeight && !block){
				jumpFrame += jumpSpeed * 10 * (MineFlat.delta / MiscUtil.getTimeResolution());
				MineFlat.player.setY(newY);
			}
			else {
				jumpFrame = 0;
				setFalling(true);
			}
		}
		

		// check if the player should be fallling
		if (Math.floor(MineFlat.player.getY() + 2.5) < 128){
			Block below1 = new Location((float)Math.floor(MineFlat.player.getX()),
					(float)Math.floor(MineFlat.player.getY() + 2)).getBlock();
			Block below2 = new Location((float)Math.nextUp(MineFlat.player.getX()),
					(float)Math.floor(MineFlat.player.getY() + 2)).getBlock();
			boolean fall = false;
			if (below1 == null)
				fall = true;
			else if (below1.getType() == Material.AIR)
				fall = true;
			if (below2 == null)
				fall = true;
			else if (below2.getType() == Material.AIR)
				fall = true;

			if (!fall && isFalling()){
				setFalling(false);
				MineFlat.player.setY(oldY);
				fallFrame = 0;
			}
			else if (fall && !isFalling() && jumpFrame == 0){
				setFalling(true);
			}
		}
		else
			Player.setFalling(false);
		
	}

}
