package mineflat.util;

import static org.lwjgl.opengl.GL11.*;

public class ProgressUtil {

	public static int intPercent, exactPercent, completed, total;
	
	public static int progressHandle;
	
	public static void initialize(){
		
		progressHandle = glGenLists(0);
		glNewList(GL_COMPILE, progressHandle);
		{
			glBegin(GL_QUADS);
			
			glEnd();
		}
		glEndList();
		
	}
	
	public static void draw(){
		
		
		
	}
	
}
