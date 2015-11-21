package top.navyblue.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.newdawn.slick.opengl.ImageIOImageData;

public class ResourcesManager {

	private static final File assetsDir = new File(Constants.TEXTURE_DIR);

	public static InputStream getStream(String name) throws Exception {
		InputStream stream = ResourcesManager.class.getResourceAsStream(name);
		if (stream == null)
			stream = new FileInputStream(new File(assetsDir, name));
		return stream;
	}

	public static BufferedImage getImage(String name) throws Exception {
		return ImageIO.read(getStream(name));
	}

	public static ByteBuffer getImageBuf(String name) throws Exception {
		return new ImageIOImageData().imageToByteBuffer(getImage(name), false,
				false, null);
	}

}
