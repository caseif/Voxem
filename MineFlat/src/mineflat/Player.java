package mineflat;

public class Player {
	
	protected Location location;
	
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
	
}
