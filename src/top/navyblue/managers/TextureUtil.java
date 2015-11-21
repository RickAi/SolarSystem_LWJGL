package top.navyblue.managers;

import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.PNGDecoder;


public class TextureUtil {
	
	public int id, width, height;
	public final String name;
	
	public TextureUtil(String name) {
		this.name = name;
	}
	
	private int createID() {
		ByteBuffer buf = ByteBuffer.allocateDirect(4);
		buf.order(ByteOrder.nativeOrder());
		IntBuffer tmp = buf.asIntBuffer();
		GL11.glGenTextures(tmp);
		return tmp.get(0);
	}
	
	public void prepare() throws Exception{
		InputStream in = ResourcesManager.getStream(name);
		PNGDecoder decoder = new PNGDecoder(in);
		width = decoder.getWidth();
		height = decoder.getHeight();
		ByteBuffer buf = ByteBuffer.allocateDirect(4*width*height);
		decoder.decode(buf, decoder.getWidth()*4, PNGDecoder.RGBA);
		buf.flip();
		
		id = createID();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, decoder.getWidth(), decoder.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buf);
	}
	
}
