package com.headswilllol.mineflat.world;

public enum Biome {

	HILLS("hills", "Hills"),
	SNOWY_HILLS("snow_hills", "Snowy Hills");

	private String id;
	private String name;

	Biome(String id, String friendlyName){
		this.id = id;
		this.name = friendlyName;
	}

	public String getId(){
		return id;
	}

	public static Biome getById(String id){
		for (Biome b : Biome.values()){
			if (b.getId().equals(id))
				return b;
		}
		return Biome.HILLS;
	}

}
