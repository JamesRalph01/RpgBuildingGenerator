package viewer;

import building.Building;
import building.Room;
import building.Wall;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import static java.awt.event.KeyEvent.VK_A;
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_Q;
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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import main.Controller;
import org.joml.Matrix4f;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.joml.Vector3f;
import util.Point;

public class Renderer implements GLEventListener, MouseListener, MouseMotionListener, KeyListener, ComponentListener {

    public Controller controller;
    
    private final Vector3f cameraInc;
    private final Camera camera;
    private ArrayList<ViewerItem> buildingItems;

    private static final float CAMERA_POS_STEP = 0.05f;
    
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.f;
    private final Transformation transformation;
    private ShaderProgram shaderProgram;
    
    Vector3f sceneRotation = new Vector3f(0,0,0);
    Vector2d mouseDown = null;
    
    int w, h;
    
    public Renderer() {
        transformation = new Transformation();
        camera = new Camera();
        cameraInc = new Vector3f();
    }
    
    public void setController(Controller controller) {
        this.controller = controller;
    }
            
    @Override
    public void init(GLAutoDrawable drawable) {
        try {

            final GL4 gl = drawable.getGL().getGL4();

            ViewerItem buildingItem;

            // Output OpenGL version
            System.out.println(" GL_VERSION: "+ gl.glGetString(GL4.GL_VERSION) );
            
            buildingItems = new ArrayList<>();
            
            initBuilding(gl);
            
            Mesh mesh = OBJLoader.loadMesh(gl, "/models/cube.obj");
            Texture texture = new Texture(gl, "textures/Mossy_driveway.png");
            mesh.setTexture(texture);
          
            buildingItem = new ViewerItem(mesh);
            buildingItem.setScale(0.01f);
            buildingItem.setPosition(0.0f, 0.0f, 0.0f);
            buildingItems.add(buildingItem);            
            
            camera.setPosition(0, 0, 1);
            
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
        for (ViewerItem buildingItem : buildingItems) {
            buildingItem.getMesh().cleanUp(gl);
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
        // Render each buildingItem
        for (ViewerItem buildingItem : buildingItems) {
            Mesh mesh = buildingItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(buildingItem, viewMatrix, sceneRotation);
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
        //mouseDown = new Vector2d(e.getX(), e.getY());
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
        double dX = (double) e.getX() - mouseDown.x;
        double dY = (double) e.getY() - mouseDown.y;

        sceneRotation.set(sceneRotation.x+=dY, sceneRotation.y, sceneRotation.z+=dX);
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
                sceneRotation.set(sceneRotation.x, sceneRotation.y, sceneRotation.z += 5);
                break;
            case VK_RIGHT:     
                sceneRotation.set(sceneRotation.x, sceneRotation.y, sceneRotation.z -= 5);
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
        
        // Update camera based on mouse            
//        if (mouseInput.isRightButtonPressed()) {
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//        }
    }
    
    private void initBuilding(GL4 gl) {
    
        if (this.controller == null) return;
        if (this.controller.getFloorPlanner().hasActiveFloorplan() == false) return;
        
        Building building = this.controller.getFloorPlanner().get3DBuilding();
        
        // Create dynamic objects, starting with external walls
        String roomTextureFile = chooseExternalWallTexture(building);
        for (Wall wall : building.getExternalWalls()) {
            Mesh mesh = buildWallMesh(gl, building, wall, roomTextureFile);          
            ViewerItem buildingItem = new ViewerItem(mesh);
            buildingItem.setPosition(toNX(wall.getLocation().x), toNY(wall.getLocation().y), 0);
            buildingItems.add(buildingItem);  
        }
        
        // Now internal walls in each room
        for (Room room : building.getRooms()) {
            roomTextureFile = chooseRoomWallTexture(building, room);
            for (Wall wall : room.getInternalWalls()) {
                
                Mesh mesh = buildWallMesh(gl, building, wall, roomTextureFile);          
                ViewerItem buildingItem = new ViewerItem(mesh);
                buildingItem.setPosition(toNX(wall.getLocation().x), toNY(wall.getLocation().y), 0);
                buildingItems.add(buildingItem);                  
            }

        }
        
    }
    
    private String chooseRoomWallTexture(Building building, Room room) {
        String textureFile;

        switch (building.getWealthIndicator()) {
            case POOR:
                if (Math.random() < 0.5) {
                    textureFile = "textures/brown_rock.png";                        
                } else {
                    textureFile = "textures/Sandy_gravel.png";     
                }
                break;
            case WEATHLY:
                double r;
                r = Math.random();
                if (r < 0.25) {
                    textureFile = "textures/blue_tiles.png";                        
                } else if (r < 0.5) {
                    textureFile = "textures/Round_metal_tiles.png";     
                } else if (r < 0.75) {
                    textureFile = "textures/Bronze.png";     
                } else {
                    textureFile = "textures/Pattern.png";     
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
                    textureFile = "textures/old_wooden_wall.png";                        
                } else {
                    textureFile = "textures/Stone_wall.png";     
                }

                break;
            case WEATHLY:
                double r;
                r = Math.random();
                if (Math.random() < 0.5) {
                    textureFile = "textures/Brown_rock.png";                        
                } else {
                    textureFile = "textures/Stone_wall.png";     
                }
                break;
            default:
                textureFile = "textures/old_wooden_wall.png";
                break;        
        }
        return textureFile;
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
                positions[i+1] = toNY(wall.positions[i+1]);
                positions[i+2] = toNX(wall.positions[i+2]);
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
    
    private float toNX(float screenX) {
        return (2.0f / (float) w)  * screenX;
    }
    
    private float toNY(float screenY) { 
        return -1 * (2.0f / (float) h)  * screenY;
    }
    
    @Override
    public void componentResized(ComponentEvent e) {
        h = e.getComponent().getHeight();
        w = e.getComponent().getWidth();
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


}
