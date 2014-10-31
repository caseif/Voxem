package com.headswilllol.mineflat;

public enum Material {

	AIR("missing_texture"), // shouldn't be rendered anyway. if it is, something's wrong.
	DIRT,
	GRASS,
	GRASS_TOP,
	LOG("log_oak"),
	LOG_TOP("log_oak_top"),
	LEAVES("leaves_oak"),
	WOOD("wood_oak"),
	STONE,
	BEDROCK,
	COAL_ORE,
	IRON_ORE,
	GOLD_ORE,
	DIAMOND_ORE,
	PUMPKIN,
	PUMPKIN_TOP;

	private String[] textures;

	//TODO: add durability argument
	Material(String[] textures){
		this.textures = textures;
	}

	Material(String texture){
		this.textures = new String[]{texture};
	}

	Material(){
		this.textures = new String[]{this.toString().toLowerCase()};
	}

	public String[] getTextures(){
		return textures;
	}

	public String getTexture(int dataValue){
		return textures.length >= dataValue - 1 ? textures[dataValue] : textures[0];
	}
}
