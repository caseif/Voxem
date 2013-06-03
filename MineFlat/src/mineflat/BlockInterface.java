package mineflat;

public interface BlockInterface {
	
	public int getX();
	
	public int getY();
	
	public void setX(int x);
	
	public void setY(int y);
	
	public void setLocation(int x, int y);
	
	public void create();
	
	public void destroy();
	
}
