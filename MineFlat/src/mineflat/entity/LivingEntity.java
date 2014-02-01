package mineflat.entity;

import java.io.InputStream;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.TextureLoader;

import mineflat.Direction;
import mineflat.util.ImageUtil;

public class LivingEntity extends Entity {

	/**
	 * The speed at which entities will jump
	 */
	public static final float jumpPower = .2f;

	/**
	 * The speed at which this particular entity will move
	 */
	protected float speed = 5f;
	protected Direction facing = Direction.LEFT;
	protected Direction direction = Direction.STATIONARY;
	protected boolean jump = false;

	public LivingEntity(EntityType type, float x, float y, float width, float height){
		super(type, x, y, width, height);
	}

	public Direction getFacing(){
		return facing;
	}

	public void setFacing(Direction facing){
		this.facing = facing;
	}

	public void setDirection(Direction direction){
		this.direction = direction;
	}

	public Direction getFacing(Direction facing){
		return direction;
	}

	@Override
	public void manageMovement(){

		if(direction == Direction.LEFT)
			setXVelocity(-getSpeed());
		if(direction == Direction.RIGHT)
			setXVelocity(getSpeed());
		if(direction == Direction.STATIONARY)
			setXVelocity(0);

		if(jump && isOnGround())
			setYVelocity(-jumpPower);

		super.manageMovement();

	}

	public boolean isJumping(){
		return jump;
	}

	public void setJumping(boolean jump){
		this.jump = jump;
	}

	public float getSpeed(){
		return speed;
	}

	public void setSpeed(float speed){
		this.speed = speed;
	}

	public static void initialize(){
		for (EntityType et : EntityType.values()){
			if (et != EntityType.ITEM_DROP){
				try {
					InputStream is = LivingEntity.class.getClassLoader().getResourceAsStream(
							"textures/" + et.toString().toLowerCase() + ".png");
					InputStream newIs = ImageUtil.asInputStream(ImageUtil.scaleImage(
							ImageIO.read(is), 64, 64));
					Entity.sprites.put(et, TextureLoader.getTexture("PNG", newIs));
				}
				catch (Exception ex){
					System.err.println("Exception occurred while preparing texture for player sprite");
					ex.printStackTrace();
				}
			}
		}
	}
}
