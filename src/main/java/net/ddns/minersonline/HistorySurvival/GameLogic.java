package net.ddns.minersonline.HistorySurvival;

import net.ddns.minersonline.engine.core.Camera;
import net.ddns.minersonline.engine.core.ILogic;
import net.ddns.minersonline.engine.core.entity.Entity;
import net.ddns.minersonline.engine.core.entity.Texture;
import net.ddns.minersonline.engine.core.managers.RenderManager;
import net.ddns.minersonline.engine.core.managers.WindowManager;
import net.ddns.minersonline.engine.core.entity.Model;
import net.ddns.minersonline.engine.core.loading.ObjectLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

public class GameLogic implements ILogic {
    private static final Logger LOGGER = LogManager.getLogger(GameLogic.class);

    private final RenderManager renderer;
    private final WindowManager window;
    private final ObjectLoader loader;

    private Entity entity;
    private final Camera camera;
    private final Vector3f camPos = new Vector3f(0, 0, 0);

    public GameLogic(){
        renderer = new RenderManager();
        window = Launch.getWindow();
        loader = new ObjectLoader();
        camera = new Camera();
    }

    @Override
    public void init() throws Exception {
        LOGGER.info("Reloading ResourceManager: Default");
        renderer.init();

        float[] vertices = new float[] {
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, 0.5f, -0.5f,
                0.5f, 0.5f, -0.5f,
                -0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, 0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
                -0.5f, 0.5f, 0.5f,
                -0.5f, -0.5f, 0.5f,
                -0.5f, -0.5f, -0.5f,
                0.5f, -0.5f, -0.5f,
                -0.5f, -0.5f, 0.5f,
                0.5f, -0.5f, 0.5f,
        };
        float[] TCCs = new float[]{
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                0.0f, 0.0f,
                0.5f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 0.5f,
                0.5f, 0.5f,
                0.0f, 1.0f,
                0.5f, 1.0f,
                0.0f, 0.0f,
                0.0f, 0.5f,
                0.5f, 0.0f,
                0.5f, 0.5f,
                0.5f, 0.0f,
                1.0f, 0.0f,
                0.5f, 0.5f,
                1.0f, 0.5f,
        };
        int[] uvIndices = new int[]{
                0, 1, 3, 3, 1, 2,
                8, 10, 11, 9, 8, 11,
                12, 13, 7, 5, 12, 7,
                14, 15, 6, 4, 14, 6,
                16, 18, 19, 17, 16, 19,
                4, 6, 7, 5, 4, 7,
        };

        Model model = loader.loadModel(vertices, TCCs, uvIndices);
        model.setTexture(new Texture(loader.loadTextureResource("/textures/grass_block.png")));
        entity = new Entity(model,
                new Vector3f(.5f,0,0),
                new Vector3f(0,0,0),
                1f);
    }

    @Override
    public void input() {
        camPos.set(0,0,0);
        if (window.isKeyPressed(GLFW.GLFW_KEY_W)){
            camPos.z = -0.01f;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_S)){
            camPos.z = 0.01f;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_A)){
            camPos.x = -0.01f;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_D)){
            camPos.x = 0.01f;
        }

        if (window.isKeyPressed(GLFW.GLFW_KEY_Z)){
            camPos.y = -0.01f;
        }
        if (window.isKeyPressed(GLFW.GLFW_KEY_X)){
            camPos.y = 0.01f;
        }
    }

    @Override
    public void update() {
        camera.movePos(camPos);
        entity.incRot(new Vector3f(0,0.031f,0));
    }

    @Override
    public void render() throws Exception {
        renderer.clear();
        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }
        renderer.render(entity, camera);
    }

    @Override
    public void cleanUp() {
        renderer.cleanUp();
        window.cleanUp();
        loader.cleanUp();
    }
}
