package net.ddns.minersonline.HistorySurvival;

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
import org.lwjgl.opengl.GL11;

public class GameLogic implements ILogic {
    private static final Logger LOGGER = LogManager.getLogger(GameLogic.class);

    private final RenderManager renderer;
    private final WindowManager window;
    private final ObjectLoader loader;
    private Entity entity;

    public GameLogic(){
        renderer = new RenderManager();
        window = Launch.getWindow();
        loader = new ObjectLoader();
    }

    @Override
    public void init() throws Exception {
        LOGGER.info("Reloading ResourceManager: Default");
        renderer.init();

        float[] vertices = {
                -0.5f, 0.5f, 0f,
                -0.5f, -0.5f, 0f,
                0.5f, -0.5f, 0f,
                0.5f, 0.5f, 0f
        };

        int[] uvIndices = {
                0, 1, 3,
                3, 1, 2
        };

        float[] TCCs = {
                0, 0,
                0, 1,
                1, 1,
                1, 0
        };
        Model model = loader.loadModel(vertices, TCCs, uvIndices);
        model.setTexture(new Texture(loader.loadTextureResource("/textures/grass_block.png")));
        entity = new Entity(model,
                new Vector3f(.5f, 0, 0),
                new Vector3f(0, 0, 0),
                1f);
    }

    @Override
    public void input() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render() throws Exception {
        renderer.clear();
        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }
        renderer.render(entity);
    }

    @Override
    public void cleanUp() {
        renderer.cleanUp();
        loader.cleanUp();
    }
}
