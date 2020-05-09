package viewer;

import com.jogamp.opengl.GL4;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.stb.STBImage.*;

public class Texture {

    private final int id;

    public Texture(GL4 gl, String fileName) throws Exception {
        this(loadTexture(gl, fileName));
    }

    public Texture(int id) {
        this.id = id;
    }

    public void bind(GL4 gl) {
        gl.glBindTexture(GL4.GL_TEXTURE_2D, id);
    }

    public int getId() {
        return id;
    }

    private static int loadTexture(GL4 gl, String fileName) throws Exception {
        int width;
        int height;
        
        
        ByteBuffer buf;
        // Load Texture file
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer channels = stack.mallocInt(1);

            buf = stbi_load(fileName, w, h, channels, 4);
            if (buf == null) {
                throw new Exception("Image file [" + fileName  + "] not loaded: " + stbi_failure_reason());
            }

            width = w.get();
            height = h.get();
        }

        // Create a new OpenGL texture
        int[] textureIds = new int[1];
        gl.glGenTextures(1, IntBuffer.wrap(textureIds));
        int textureId = textureIds[0];

        // Bind the texture
        gl.glBindTexture(GL4.GL_TEXTURE_2D, textureId);
    
        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        gl.glPixelStorei(GL4.GL_UNPACK_ALIGNMENT, 1);
        
       // Upload the texture data
        gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_RGBA, width, height, 0,
                GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, buf);
        
        // Generate Mip Map
        gl.glGenerateMipmap(GL4.GL_TEXTURE_2D);

        stbi_image_free(buf);
        
        return textureId;
    }

    public void cleanup(GL4 gl) {
        int[] textIds = new int[1];
        textIds[0] = id;
        gl.glDeleteTextures(1, IntBuffer.wrap(textIds));
    }
    
    public void enableWrap(GL4 gl) {
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
        gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);
    }
}
