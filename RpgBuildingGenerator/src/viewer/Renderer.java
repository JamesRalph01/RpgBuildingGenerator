package viewer;

import viewer.Camera;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_S;
import static java.awt.event.KeyEvent.VK_W;
import static java.awt.event.KeyEvent.VK_X;
import static java.awt.event.KeyEvent.VK_Z;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener {

    private final Vector3f cameraInc;
    private final Camera camera;
    private GameItem[] gameItems;

    private static final float CAMERA_POS_STEP = 0.05f;
    
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Transformation transformation;
    private ShaderProgram shaderProgram;
    
    
    int w, h;
    
    public Renderer() {
        transformation = new Transformation();
        camera = new Camera();
        cameraInc = new Vector3f();
    }
            
    @Override
    public void init(GLAutoDrawable drawable) {
        try {

            final GL4 gl = drawable.getGL().getGL4();

            // Output OpenGL version
            System.out.println(" GL_VERSION: "+ gl.glGetString(GL4.GL_VERSION) );
            
            Mesh mesh = OBJLoader.loadMesh(gl, "/models/cube.obj");
            Texture texture = new Texture(gl, "textures/grassblock.png");
            mesh.setTexture(texture);
            GameItem gameItem = new GameItem(mesh);
            gameItem.setScale(0.5f);
            gameItem.setPosition(0, 0, -2);
            gameItems = new GameItem[]{gameItem};
            
            // Create shader
            shaderProgram = new ShaderProgram(gl);
            shaderProgram.createVertexShader(gl, Utils.loadResource("/shaders/vertex.vs"));
            shaderProgram.createFragmentShader(gl, Utils.loadResource("/shaders/fragment.fs"));
            shaderProgram.link(gl);
            
            // Create uniforms for modelView and projection matrices and texture
            shaderProgram.createUniform(gl, "projectionMatrix");
            shaderProgram.createUniform(gl, "modelViewMatrix");
            shaderProgram.createUniform(gl, "texture_sampler");
            // Create uniform for default colour and the flag that controls it
            shaderProgram.createUniform(gl, "colour");    
            shaderProgram.createUniform(gl, "useColour");
            
            gl.glEnable(GL4.GL_DEPTH_TEST);
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void clear(GL4 gl) {
        gl.glClear(GL4.GL_COLOR_BUFFER_BIT | GL4.GL_DEPTH_BUFFER_BIT);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();
        if (shaderProgram != null) {
            shaderProgram.cleanup(gl);
        }
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp(gl);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();
        
        this.clear(gl);
        
        shaderProgram.bind(gl);
        
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, w, h, Z_NEAR, Z_FAR);
        shaderProgram.setUniform(gl, "projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        
        shaderProgram.setUniform(gl, "texture_sampler", 0);
        // Render each gameItem
        for (GameItem gameItem : gameItems) {
            Mesh mesh = gameItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
            shaderProgram.setUniform(gl, "modelViewMatrix", modelViewMatrix);
            // Render the mesh for this game item
            shaderProgram.setUniform(gl, "colour", mesh.getColour());
            shaderProgram.setUniform(gl, "useColour", mesh.isTextured() ? 0 : 1);
            mesh.render(gl);
        }

        shaderProgram.unbind(gl);    
         
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) {
        final GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(x, y, w, h);
        this.w = w;
        this.h = h;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {   
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key;
        Vector3f rotation = gameItems[0].getRotation();
        
        cameraInc.set(0, 0, 0);
        
        key = e.getKeyCode();
        switch (key) {
            case VK_W: 
                cameraInc.z = -1;
                break;
            case VK_S: 
                cameraInc.z = 1;
                break;  
            case VK_A: 
                cameraInc.x = -1;
                break;
            case VK_D: 
                cameraInc.x = 1;
                break;
            case VK_Z: 
                cameraInc.y = -1;
                break;
            case VK_X: 
                cameraInc.y = 1;
                break;
            case VK_UP: 
                gameItems[0].setRotation(rotation.x+=5, rotation.y, rotation.z);
                break;    
            case VK_DOWN: 
                gameItems[0].setRotation(rotation.x-=5, rotation.y, rotation.z);
                break;
            case VK_LEFT: 
                gameItems[0].setRotation(rotation.x, rotation.y+5, rotation.z);
                break;                   
            case VK_RIGHT: 
                gameItems[0].setRotation(rotation.x, rotation.y-5, rotation.z);
                break;    
        
        }
        e.consume();
        update();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    public void update() {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse            
//        if (mouseInput.isRightButtonPressed()) {
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//        }
    }


}
