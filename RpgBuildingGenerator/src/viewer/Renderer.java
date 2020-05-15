package viewer;

import building.Building;
import building.BuildingItem;
import building.Room;
import building.Wall;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_Q;
import static java.awt.event.KeyEvent.VK_RIGHT;
import java.awt.image.BufferedImage;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Timestamp;
import java.util.Date;
import javax.imageio.ImageIO;
import main.Controller;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector3f;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener, ComponentListener {

    public Controller controller;
    
    private Timestamp viewerTimestamp = null;
    
    private final Vector3f cameraInc;
    private final Camera camera;
    private ArrayList<ViewerItem> viewerItems;

    private static final float CAMERA_POS_STEP = 0.05f;
    
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Transformation transformation;
    private ShaderProgram shaderProgram;
    
    Vector3f sceneRotation = new Vector3f(0,0,0);
    Vector2d mouseDown = null;
    
    int canvasWidth, canvasHeight;
    
    public Renderer() {
        transformation = new Transformation();
        camera = new Camera();
        cameraInc = new Vector3f();
        viewerTimestamp = new Timestamp(new Date().getTime());
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
            
    @Override
    public void init(GLAutoDrawable drawable) {
        try {

            final GL4 gl = drawable.getGL().getGL4();

            // Output OpenGL version
            System.out.println(" GL_VERSION: "+ gl.glGetString(GL4.GL_VERSION) );
            
            viewerItems = new ArrayList<>();
            
            // Create shader
            shaderProgram = new ShaderProgram(gl);
            shaderProgram.createVertexShader(gl, Utils.loadResource("/shaders/vertex_shader.glsl"));
            shaderProgram.createFragmentShader(gl, Utils.loadResource("/shaders/fragment_shader.glsl"));
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
        for (ViewerItem viewerItem : viewerItems) {
            viewerItem.getMesh().cleanUp(gl);
        }
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();
        
        this.clear(gl);
        
        if (viewerTimestamp.before(controller.getFloorPlanner().timestamp)) {
            System.out.println("Change detected");
            // Clean up/reset
            for (ViewerItem viewerItem : viewerItems) {
               System.out.println("Delete Mesh");
               viewerItem.getMesh().cleanUp(gl);
            }
            viewerItems.clear();
            viewerTimestamp = (Timestamp) controller.getFloorPlanner().timestamp.clone();
            
            // load new objects
            if (controller.getFloorPlanner().hasfloorPlanAvailable()) {
                System.out.println("Loading objects");
                loadObjects(gl);
                camera.setPosition(0, 0, 1);
                sceneRotation.set(-35.0f, 0, 0);
            }
        }

        
        shaderProgram.bind(gl);
        
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, canvasWidth, canvasHeight, Z_NEAR, Z_FAR);
        shaderProgram.setUniform(gl, "projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        
        shaderProgram.setUniform(gl, "texture_sampler", 0);
        // Render each viewerItem
        for (ViewerItem viewerItem : viewerItems) {
            Mesh mesh = viewerItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(viewerItem, viewMatrix, sceneRotation);
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
        this.canvasWidth = w;
        this.canvasHeight = h;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
         mouseDown = new Vector2d(e.getX(), e.getY());
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
        // Update camera based on mouse 
        double dY = (double) e.getX() - mouseDown.x;
        double dX = ((double) e.getY() - mouseDown.y);

        if (sceneRotation.x+dX <= -90 || sceneRotation.x+dX >= 0) {dX=0;}      
        
        sceneRotation.set(sceneRotation.x+=dX, sceneRotation.y+=dY, sceneRotation.z);
        mouseDown = new Vector2d(e.getX(), e.getY());
        update();    
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
        
        key = e.getKeyCode();
        switch (key) {
            case VK_Q: 
                cameraInc.z = -1;
                break;
            case VK_A: 
                cameraInc.z = 1;
                break; 
            case VK_LEFT:
                sceneRotation.set(sceneRotation.x, sceneRotation.y, sceneRotation.y += 5);
                break;
            case VK_RIGHT:     
                sceneRotation.set(sceneRotation.x, sceneRotation.y, sceneRotation.y -= 5);
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
        cameraInc.set(0, 0, 0);
    }
    
    private void loadObjects(GL4 gl) {
                    
        try {
            initBuilding(gl);
            
            Mesh mesh = OBJLoader.loadMesh(gl, "/models/cube.obj");
            Texture texture = new Texture(gl, "textures/Mossy_driveway.png");
            mesh.setTexture(texture);
            
            ViewerItem originCube = new ViewerItem(mesh);
            originCube.setScale(0.05f);
            originCube.setPosition(0.0f, 0.0f, 0.0f);            
            viewerItems.add(originCube);
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    private void initBuilding(GL4 gl) {
    
        if (this.controller == null) return;
        if (this.controller.getFloorPlanner().hasfloorPlanAvailable() == false) return;
        
        Building building = this.controller.getFloorPlanner().get3DBuilding();
        
        // Create dynamic objects, starting with external walls
        String roomTextureFile = chooseExternalWallTexture(building);
        for (Wall wall : building.getExternalWalls()) {
            Mesh mesh = buildWallMesh(gl, building, wall, roomTextureFile);          
            ViewerItem viewerItem = new ViewerItem(mesh);
            viewerItem.setPosition(toNX(wall.getLocation().x), 0.0f, toNX(wall.getLocation().z));
            viewerItems.add(viewerItem);  
        }
        
        // Now room interior
        for (Room room : building.getRooms()) {
            roomTextureFile = chooseRoomWallTexture(building, room);
            // Internal walls
            for (Wall wall : room.getInternalWalls()) {
                Mesh mesh = buildWallMesh(gl, building, wall, roomTextureFile);          
                ViewerItem viewerItem = new ViewerItem(mesh);
                viewerItem.setPosition(toNX(wall.getLocation().x), 0.0f, toNX(wall.getLocation().z));
                viewerItems.add(viewerItem);                  
            }
            //Furniture
            for (BuildingItem item : room.getFurniture()) {
                Mesh mesh = buildFurnitureMesh(gl, item);          
                ViewerItem viewerItem = new ViewerItem(mesh);
                viewerItem.setPosition(toNX(item.getLocation().x), 0.0f, toNY(item.getLocation().z));
                viewerItem.setScale(item.scaleFactor);
                viewerItems.add(viewerItem);                   
            }
        }
    }
    
    private String chooseRoomWallTexture(Building building, Room room) {
        String textureFile;

        switch (building.getWealthIndicator()) {
            case POOR:
                if (Math.random() < 0.5) {
                    textureFile = "textures/Red_stone_wall.png";                        
                } else {
                    textureFile = "textures/Grunge_wall.png";     
                }
                break;
            case WEATHLY:
                double r;
                r = Math.random();
                if (r < 0.25) {
                    textureFile = "textures/blue_tiles.png";                        
                } else if (r < 0.5) {
                    textureFile = "textures/pattern.png";     
                } else if (r < 0.75) {
                    textureFile = "textures/Bronze.png";     
                } else {
                    textureFile = "textures/Marble_tiles.png";     
                }
                break;
            default:
                textureFile = "textures/old_wooden_wall.png";
                break;        
        }
        return textureFile;
    }
    
    private String chooseExternalWallTexture(Building building) {
        String textureFile;

        switch (building.getWealthIndicator()) {
            case POOR:
                if (Math.random() < 0.5) {
                    textureFile = "textures/stone_wall2.png";                        
                } else {
                    textureFile = "textures/stone_wall.png";     
                }

                break;
            case WEATHLY:
                double r;
                r = Math.random();
                if (Math.random() < 0.5) {
                    textureFile = "textures/brick_wall.png";                        
                } else {
                    textureFile = "textures/brick_wall2.png";     
                }
                break;
            default:
                textureFile = "textures/stone_wall2.png";
                break;        
        }
        return textureFile;
    }

    private Mesh buildFurnitureMesh(GL4 gl, BuildingItem furniture) {
        Mesh mesh = null;
        
        try {
            mesh = OBJLoader.loadMesh(gl, "/models/" + furniture.obj);
            Texture texture = new Texture(gl, "textures/" + furniture.texture);
            mesh.setTexture(texture);
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mesh;
    }

    
    private Mesh buildWallMesh(GL4 gl, Building building, Wall wall, String textureFile) {
        Mesh mesh = null;
        Texture texture;
        
        try {
            texture = new Texture(gl, textureFile);
            texture.enableWrap(gl);
            
            // normalise positions
            float[] positions = new float[wall.positions.length];
            for (int i=0; i < wall.positions.length; i+=3) {
                positions[i] = toNX(wall.positions[i]);
                positions[i+1] = toNX(wall.positions[i+1]);
                positions[i+2] = toNY(wall.positions[i+2]);
            }

            mesh = new Mesh(gl, positions, wall.textCoords, wall.indices, texture);
            
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mesh;
    }
   
    private float[] calcTextureCoords(float[] positions) {
        float minX = 2.0f;
        float minY = 2.0f;
        float maxX = -2.0f;
        float maxY = -2.0f;
        
        for (int i=0; i < positions.length; i+=3) {
            minX = Math.min(minX, positions[i]);
            maxX = Math.max(minX, positions[i]);
            
            minY = Math.min(minY, positions[i+1]);        
            maxY = Math.max(maxY, positions[i+1]);
        }
        
        float[] textures = new float[positions.length * 2];
        int t = 0;
        for (int i=0; i < positions.length; i+=3) {
            float offsetX = 0 - minX;
            float offsetY = 0 - minY;
            float rangeX = maxX - minX;
            float rangeY = maxY - minY;
            
            float vX = positions[i];
            float vY = positions[i+1];

            // Convert world coord to texture coord
            textures[t++] = (vX + offsetX) / rangeX;
            textures[t++] = (vY + offsetY) / rangeY;
            
        }
                       
        return textures;
    }
    
    private float toNX(float deviceCoordX) {
        return (2.0f / (float) canvasWidth)  * deviceCoordX;
    }
    
    private float toNY(float deviceCoordY) { 
        return -1 * (2.0f / (float) canvasHeight)  * deviceCoordY;
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        canvasHeight = e.getComponent().getHeight();
        canvasWidth = e.getComponent().getWidth();
    }

    @Override
    public void componentMoved(ComponentEvent e) {
    }

    @Override
    public void componentShown(ComponentEvent e) {
    }

    @Override
    public void componentHidden(ComponentEvent e) {
    }
    
    protected void saveImage(GL4 gl) {

        try {
            BufferedImage screenshot = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = screenshot.getGraphics();

            ByteBuffer buffer = Buffers.newDirectByteBuffer(canvasWidth * canvasHeight * 4);
            
            // be sure you are reading from the right fbo (here is supposed to be the default one)
            // bind the right buffer to read from
            gl.glReadBuffer(GL4.GL_BACK);
            // if the width is not multiple of 4, set unpackPixel = 1
            gl.glReadPixels(0, 0, canvasWidth, canvasHeight, GL4.GL_RGBA, GL4.GL_UNSIGNED_BYTE, buffer);

            for (int h = 0; h < canvasHeight; h++) {
                for (int w = 0; w < canvasWidth; w++) {
                    // The color are the three consecutive bytes, it's like referencing
                    // to the next consecutive array elements, so we got red, green, blue..
                    // red, green, blue, and so on..+ ", "
                    graphics.setColor(new Color((buffer.get() & 0xff), (buffer.get() & 0xff),
                            (buffer.get() & 0xff)));
                    buffer.get();   // consume alpha
                    graphics.drawRect(w, canvasHeight - h, 1, 1); // height - h is for flipping the image
                }
            }
            File outputfile = new File("D:\\Downloads\\texture.png");
            ImageIO.write(screenshot, "png", outputfile);
        } catch (IOException ex) {
        }
    }


}
