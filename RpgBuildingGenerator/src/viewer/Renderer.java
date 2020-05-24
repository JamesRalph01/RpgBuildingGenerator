package viewer;

import building.Building;
import building.BuildingItem;
import building.Floor;
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
import static java.awt.event.KeyEvent.VK_D;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_M;
import static java.awt.event.KeyEvent.VK_N;
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
import org.joml.Vector4f;

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
    private float specularPower;
    private Vector3f ambientLight;
    private PointLight pointLight;
    private float reflectance = 1f;    
    
    private enum DisplayMode {WIREFRAME, SOLID};
    private DisplayMode displayMode = DisplayMode.SOLID;
    
    Vector3f sceneRotation = new Vector3f(0,0,0);
    Vector3f sceneTranslation = new Vector3f(0,0,0);
    Vector2d mouseDown = null;
    
    int canvasWidth, canvasHeight;
    
    public Renderer() {
        transformation = new Transformation();
        specularPower = 10f;
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
            
            // Lighting         
            ambientLight = new Vector3f(0.3f, 0.3f, 0.3f);
            Vector3f lightColour = new Vector3f(1, 1, 1);
            Vector3f lightPosition = new Vector3f(0, 0, 1);
            float lightIntensity = 1.0f;
            pointLight = new PointLight(lightColour, lightPosition, lightIntensity);
            PointLight.Attenuation att = new PointLight.Attenuation(0.0f, 0.0f, 1.0f);
            pointLight.setAttenuation(att);
            
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
            // Create uniform for material
            shaderProgram.createMaterialUniform(gl, "material");
            // Create lighting related uniforms
            shaderProgram.createUniform(gl, "specularPower");
            shaderProgram.createUniform(gl, "ambientLight");
            shaderProgram.createPointLightUniform(gl, "pointLight");
            
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
                loadObjects(gl);
                camera.setPosition(0, 0, 1);
                sceneRotation.set(-35.0f, 0, 0);
                sceneTranslation.x = toNX(controller.getFloorPlanner().get3DBuilding().getLocation().x);
                sceneTranslation.y = 0;
                sceneTranslation.z = toNY(controller.getFloorPlanner().get3DBuilding().getLocation().z);
                
            }
        }

        //Display mode
        if (displayMode == DisplayMode.SOLID) {
            gl.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_FILL);
        } else {
            gl.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_LINE);
        }
        
        shaderProgram.bind(gl);
        
        // Update projection Matrix
        Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, canvasWidth, canvasHeight, Z_NEAR, Z_FAR);
        shaderProgram.setUniform(gl, "projectionMatrix", projectionMatrix);

        // Update view Matrix
        Matrix4f viewMatrix = transformation.getViewMatrix(camera);
        
                // Update Light Uniforms
        shaderProgram.setUniform(gl, "ambientLight", ambientLight);
        shaderProgram.setUniform(gl, "specularPower", specularPower);
        // Get a copy of the light object and transform its position to view coordinates
        PointLight currPointLight = new PointLight(pointLight);
        Vector3f lightPos = currPointLight.getPosition();
        Vector4f aux = new Vector4f(lightPos, 1);
        aux.mul(viewMatrix);
        lightPos.x = aux.x;
        lightPos.y = aux.y;
        lightPos.z = aux.z;
        shaderProgram.setUniform(gl, "pointLight", currPointLight);        
        
        shaderProgram.setUniform(gl, "texture_sampler", 0);
        // Render each viewerItem
        for (ViewerItem viewerItem : viewerItems) {
            Mesh mesh = viewerItem.getMesh();
            // Set model view matrix for this item
            Matrix4f modelViewMatrix = transformation.getModelViewMatrix(viewerItem, viewMatrix, sceneRotation, sceneTranslation);
            shaderProgram.setUniform(gl, "modelViewMatrix", modelViewMatrix);
            // Render the mesh for this game item
            shaderProgram.setUniform(gl, "material", mesh.getMaterial());
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
        
        float lightPos = pointLight.getPosition().z;
        key = e.getKeyCode();
        switch (key) {
            case VK_Q: 
                cameraInc.z = -1;
                break;
            case VK_A: 
                cameraInc.z = 1;
                break; 
            case VK_N:
                this.pointLight.getPosition().z = lightPos + 0.1f;
                break;
            case VK_M:
                this.pointLight.getPosition().z = lightPos - 0.1f;
                break;
            case VK_D:
                if (this.displayMode == DisplayMode.SOLID) {
                    this.displayMode = DisplayMode.WIREFRAME;    
                } else {
                    this.displayMode = DisplayMode.SOLID;
                }
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
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
            
    }
    
    private void initBuilding(GL4 gl) {
    
        if (this.controller == null) return;
        if (this.controller.getFloorPlanner().hasfloorPlanAvailable() == false) return;
        
        System.out.println("loading objects...");
        
        Building building = this.controller.getFloorPlanner().get3DBuilding();
        
        // Create dynamic objects, starting with external walls
        for (Wall wall : building.getExternalWalls()) {
            Mesh mesh = buildWallMesh(gl, building, wall);          
            ViewerItem viewerItem = new ViewerItem(mesh);
            viewerItems.add(viewerItem);  
        }
        
        //Floor
        try {
            Floor floor = building.getFloor();
            Mesh mesh = buildFloorMesh(gl, floor);         
            ViewerItem viewerItem = new ViewerItem(mesh);
            viewerItems.add(viewerItem);      
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }
         
        
        // Now room interior
        for (Room room : building.getRooms()) {
            // Internal walls
            for (Wall wall : room.getInternalWalls()) {
                Mesh mesh = buildWallMesh(gl, building, wall);          
                ViewerItem viewerItem = new ViewerItem(mesh);
                viewerItems.add(viewerItem);                  
            }
            //Furniture
            for (BuildingItem item : room.getFurniture()) {
                buildFurnitureMesh(gl, item);                            
            }
        }
    }
    

    private void buildFurnitureMesh(GL4 gl, BuildingItem furniture) {
        ViewerItem viewerItem;
        FurnitureLoader loader = new FurnitureLoader();
        
        try {
            ArrayList<Mesh> meshes;
            meshes = loader.loadFurniture(gl, furniture);
            for (Mesh mesh : meshes) {
                viewerItem = new ViewerItem(mesh);
                viewerItem.setPosition(toNX(furniture.getLocation().x), 0.0f, toNY(furniture.getLocation().z));
                viewerItem.setScale(furniture.scaleFactor);
                viewerItem.setRotation(furniture.getRotation().x, furniture.getRotation().y, furniture.getRotation().z);
                viewerItems.add(viewerItem);  
            }

        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
   
    private Mesh buildWallMesh(GL4 gl, Building building, Wall wall) {
        Mesh mesh = null;
        Texture texture;
        
        try {
            
            texture = new Texture(gl, "textures/" + wall.rootPath + wall.textures[0]);
            texture.enableWrap(gl);
            
            Material material = new Material(texture, reflectance);
            
            // normalise positions
            float[] positions = new float[wall.positions.length];
            for (int i=0; i < wall.positions.length; i+=3) {
                positions[i] = toNX(wall.positions[i]);
                positions[i+1] = toNX(wall.positions[i+1]);
                positions[i+2] = toNY(wall.positions[i+2]);
            }

            mesh = new Mesh(gl, positions, wall.textCoords, wall.normals, wall.indices);
            mesh.setMaterial(material);
            
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mesh;
    }
    
     private Mesh buildFloorMesh(GL4 gl, Floor floor) {
        Mesh mesh = null;
        Texture texture;
        
        try {
            
            texture = new Texture(gl, "textures/" + floor.rootPath + floor.textures[0]);
            texture.enableWrap(gl);
            
            Material material = new Material(texture, reflectance);
            
            // normalise positions
            float[] positions = new float[floor.positions.length];
            for (int i=0; i < floor.positions.length; i+=3) {
                positions[i] = toNX(floor.positions[i]);
                positions[i+1] = toNX(floor.positions[i+1]);
                positions[i+2] = toNY(floor.positions[i+2]);
            }

            mesh = new Mesh(gl, positions, floor.textCoords, floor.normals, floor.indices);
            mesh.setMaterial(material);
            
        } catch (Exception ex) {
            Logger.getLogger(Renderer.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mesh;
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
