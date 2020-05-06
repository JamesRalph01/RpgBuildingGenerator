package viewer.engine;

//package viewer.game;
//
//import org.joml.Vector2f;
//import org.joml.Vector3f;
//import static org.lwjgl.glfw.GLFW.*;
//import viewer.engine.GameItem;
//import viewer.engine.IGameLogic;
//import viewer.engine.MouseInput;
//import viewer.engine.Window;
//import viewer.engine.graph.Camera;
//import viewer.engine.graph.Mesh;
//import viewer.engine.graph.OBJLoader;
//import viewer.engine.graph.Texture;
//
//public class DummyGame implements IGameLogic {
//
//    private static final float MOUSE_SENSITIVITY = 0.2f;
//
//    private final Vector3f cameraInc;
//
//    private final Renderer renderer;
//
//    private final Camera camera;
//
//    private GameItem[] gameItems;
//
//    private static final float CAMERA_POS_STEP = 0.05f;
//
//    public DummyGame() {
//        renderer = new Renderer();
//        camera = new Camera();
//        cameraInc = new Vector3f();
//    }
//
//    @Override
//    public void init(Window window) throws Exception {
//        renderer.init(window);
//
//        //Mesh mesh = OBJLoader.loadMesh("/models/bunny.obj");
//        Mesh mesh = OBJLoader.loadMesh("/models/cube.obj");
//        Texture texture = new Texture("textures/grassblock.png");
//        mesh.setTexture(texture);
//        GameItem gameItem = new GameItem(mesh);
//        gameItem.setScale(0.5f);
//        gameItem.setPosition(0, 0, -2);
//        gameItems = new GameItem[]{gameItem};
//    }
//
//    @Override
//    public void input(Window window, MouseInput mouseInput) {
//        cameraInc.set(0, 0, 0);
//        if (window.isKeyPressed(GLFW_KEY_W)) {
//            cameraInc.z = -1;
//        } else if (window.isKeyPressed(GLFW_KEY_S)) {
//            cameraInc.z = 1;
//        }
//        if (window.isKeyPressed(GLFW_KEY_A)) {
//            cameraInc.x = -1;
//        } else if (window.isKeyPressed(GLFW_KEY_D)) {
//            cameraInc.x = 1;
//        }
//        if (window.isKeyPressed(GLFW_KEY_Z)) {
//            cameraInc.y = -1;
//        } else if (window.isKeyPressed(GLFW_KEY_X)) {
//            cameraInc.y = 1;
//        }
//        // Add object rotation
//        if (window.isKeyPressed(GLFW_KEY_UP)) {
//            Vector3f rotation = gameItems[0].getRotation();
//            gameItems[0].setRotation(rotation.x+=5, rotation.y, rotation.z);
//        } else if (window.isKeyPressed(GLFW_KEY_DOWN)) {
//            Vector3f rotation = gameItems[0].getRotation();
//            gameItems[0].setRotation(rotation.x-=5, rotation.y, rotation.z);
//        } else if (window.isKeyPressed(GLFW_KEY_LEFT)) {
//            Vector3f rotation = gameItems[0].getRotation();
//            gameItems[0].setRotation(rotation.x, rotation.y+5, rotation.z);
//        } else if (window.isKeyPressed(GLFW_KEY_RIGHT)) {
//            Vector3f rotation = gameItems[0].getRotation();
//            gameItems[0].setRotation(rotation.x, rotation.y-5, rotation.z);
//        }
//    }
//
//    @Override
//    public void update(float interval, MouseInput mouseInput) {
//        // Update camera position
//        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);
//
//        // Update camera based on mouse            
//        if (mouseInput.isRightButtonPressed()) {
//            Vector2f rotVec = mouseInput.getDisplVec();
//            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
//        }
//    }
//
//    @Override
//    public void render(Window window) {
//        renderer.render(window, camera, gameItems);
//    }
//
//    @Override
//    public void cleanup() {
//        renderer.cleanup();
//        for (GameItem gameItem : gameItems) {
//            gameItem.getMesh().cleanUp();
//        }
//    }
//
//}
