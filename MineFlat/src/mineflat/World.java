package mineflat;

public class World {

	private String name;
	private int chunkCount;
	private int chunkLength;
	private int chunkHeight;
	
	public String getName() {
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public int getChunkCount(){
		return chunkCount;
	}

	public void setChunkCount(int chunkCount){
		this.chunkCount = chunkCount;
	}

	public int getChunkLength(){
		return chunkLength;
	}

	public void setChunkLength(int chunkLength){
		this.chunkLength = chunkLength;
	}

	public int getChunkHeight(){
		return chunkHeight;
	}

	public void setChunkHeight(int chunkHeight){
		this.chunkHeight = chunkHeight;
	}
	
	public World(String name, int chunkCount, int chunkLength, int chunkHeight){
		this.name = name;
		this.chunkCount = chunkCount;
		this.chunkLength = chunkLength;
		this.chunkHeight = chunkHeight;
	}
	
}
