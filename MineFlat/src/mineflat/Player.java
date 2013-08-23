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
			Block b = null;
			if (newY >= 1){
				b = new Location(MineFlat.player.getX(), (float)Math.floor(newY)).getBlock();
			}
			if (jumpFrame < jumpHeight && b == null){
				jumpFrame += jumpSpeed * 10 * (MineFlat.delta / MiscUtil.getTimeResolution());
				MineFlat.player.setY(newY);
			}
			else {
				jumpFrame = 0;
				setFalling(true);
			}
		}


		// check if the player should be fallling
		if (Math.floor(MineFlat.player.getY() + 2) < 128){
			//System.out.println(MineFlat.player.getX());
			//System.out.println(Math.floor(MineFlat.player.getX()));
			float x = (Math.abs(MineFlat.player.getX()) % 1 >= 0.5 && MineFlat.player.getX() > 0) ||
					(Math.abs(MineFlat.player.getX()) % 1 <= 0.5 && MineFlat.player.getX() < 0) ?
							MineFlat.player.getX() - 4f / 16 : MineFlat.player.getX() + 4f / 16;
							if (x < 0)
								x -= 1;
							Block below = null;
							if (MineFlat.player.getY() >= -2)
								below = new Location((float)x,
										(float)Math.floor(MineFlat.player.getY() + 2)).getBlock();
							//System.out.println(below);

							if (below != null && isFalling()){
								setFalling(false);
								MineFlat.player.setY(oldY);
								fallFrame = 0;
							}
							else if (below == null && !isFalling() && jumpFrame == 0){
								setFalling(true);
							}
		}
		else
			setFalling(false);

	}

}
