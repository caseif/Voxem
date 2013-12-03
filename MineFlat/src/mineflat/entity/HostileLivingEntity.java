package mineflat.entity;

public class HostileLivingEntity {

	protected LivingEntity target;
	
	public Entity getTarget(){
		return target;
	}
	
	public void setTarget(LivingEntity target){
		this.target = target;
	}
	
}
