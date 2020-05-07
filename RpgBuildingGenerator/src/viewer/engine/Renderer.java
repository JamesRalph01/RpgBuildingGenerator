package viewer.engine;

import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import viewer.engine.graph.ShaderProgram;
import viewer.engine.graph.Transformation;
import viewer.engine.graph.Mesh;
import viewer.engine.graph.Camera;
import viewer.engine.graph.Texture;
import viewer.engine.graph.OBJLoader;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener {

    private final Vector3f cameraInc;
    private final Camera camera;
    private GameItem[] gameItems;

    private static final float CAMERA_POS_STEP = 0.05f;
    
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Transformation transformation;
    private ShaderProgram shaderProgram;
    
    //remove later
    private int[] triangleVaoHandle = new int[1];
    private int[] squareVaoHandle = new int[1];
    private int[] circleVaoHandle = new int[1];
    
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
           
            // Create Shader Objects
//            int vertexShader = ShaderHandler.createShader("src/shaders/vertex_shader.glsl", GL4.GL_VERTEX_SHADER, gl);
//            int fragmentShader = ShaderHandler.createShader("src/shaders/fragment_shader.glsl", GL4.GL_FRAGMENT_SHADER, gl);
//
//            int shaderList[] = {vertexShader,fragmentShader};
//
//            int programHandle = ShaderHandler.createProgram(shaderList, gl);
//
//            final int VERTEX_POSITION_INDEX = 0;
//            final int VERTEX_COLOUR_INDEX = 1;
//
//            Triangle triangle = new Triangle();
//            Circle circle = new Circle(-0.5f,0.5f,0.02f,40);
//            BufferHandler.setupBuffers(triangleVaoHandle, triangle.getPositionData(), 
//                    triangle.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
//            BufferHandler.setupBuffers(circleVaoHandle, circle.getPositionData(), 
//                    circle.getColourData(), VERTEX_POSITION_INDEX, VERTEX_COLOUR_INDEX, gl);
//            ShaderHandler.linkProgram(programHandle, gl);
//            gl.glUseProgram(programHandle);
            
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
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
//    
//    public void clear() {
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//    }

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
        
//        gl.glClear(GL4.GL_COLOR_BUFFER_BIT);
//        
//        gl.glBindVertexArray(triangleVaoHandle[0]);
//        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, 3);
//        
//        gl.glBindVertexArray(circleVaoHandle[0]);
//        gl.glDrawArrays(GL4.GL_TRIANGLE_FAN, 0, 40);
//        
//        gl.glFlush();
//
//        clear();
//
//        shaderProgram.bind();
//        
//        // Update projection Matrix
//        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, w, h, Z_NEAR, Z_FAR);
//        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
//
//        // Update view Matrix
//        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
//        
//        shaderProgram.setUniform("texture_sampler", 0);
//        // Render each gameItem
//        for (GameItem gameItem : gameItems) {
//            Mesh mesh = gameItem.getMesh();
//            // Set model view matrix for this item
//            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
//            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
//            // Render the mesh for this game item
//            shaderProgram.setUniform("colour", mesh.getColour());
//            shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
//            mesh.render();
//        }
//
//        shaderProgram.unbind();     
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
}


 /**
     * Field of View in Radians
     */
//    private static final float FOV = (float) Math.toRadians(60.0f);
//
//    private static final float Z_NEAR = 0.01f;
//
//    private static final float Z_FAR = 1000.f;
//
//    private final Transformation transformation;
//
//    private ShaderProgram shaderProgram;
//
//    public Renderer() {
//        transformation = new Transformation();
//    }
//
//    public void init(Window window) throws Exception {
//        // Create shader
//        shaderProgram = new ShaderProgram();
//        shaderProgram.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
//        shaderProgram.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
//        shaderProgram.link();
//        
//        // Create uniforms for modelView and projection matrices and texture
//        shaderProgram.createUniform("projectionMatrix");
//        shaderProgram.createUniform("modelViewMatrix");
//        shaderProgram.createUniform("texture_sampler");
//        // Create uniform for default colour and the flag that controls it
//        shaderProgram.createUniform("colour");
//        shaderProgram.createUniform("useColour");
//    }
//
//    public void clear() {
//        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
//    }
//
//    public void render(Window window, Camera camera, GameItem[] gameItems) {
//        clear();
//
//        if (window.isResized()) {
//            glViewport(0, 0, window.getWidth(), window.getHeight());
//            window.setResized(false);
//        }
//
//        shaderProgram.bind();
//        
//        // Update projection Matrix
//        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
//        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
//
//        // Update view Matrix
//        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
//        
//        shaderProgram.setUniform("texture_sampler", 0);
//        // Render each gameItem
//        for (GameItem gameItem : gameItems) {
//            Mesh mesh = gameItem.getMesh();
//            // Set model view matrix for this item
//            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(gameItem, viewMatrix);
//            shaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
//            // Render the mesh for this game item
//            shaderProgram.setUniform("colour", mesh.getColour());
//            shaderProgram.setUniform("useColour", mesh.isTextured() ? 0 : 1);
//            mesh.render();
//        }
//
//        shaderProgram.unbind();
//    }
//
//    public void cleanup() {
//        if (shaderProgram != null) {
//            shaderProgram.cleanup();
//        }
//    }
//}
