package mineflat.entity;

public class Entity {

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
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public void setType(EntityType type){
		this.type = type;
	}
	
}
