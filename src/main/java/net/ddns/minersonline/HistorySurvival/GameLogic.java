package net.ddns.minersonline.HistorySurvival;

import net.ddns.minersonline.HistorySurvival.core.ILogic;
import net.ddns.minersonline.HistorySurvival.core.RenderManager;
import net.ddns.minersonline.HistorySurvival.core.WindowManager;
import org.lwjgl.opengl.GL11;

public class GameLogic implements ILogic {
    private final RenderManager renderer;
    private final WindowManager window;

    public GameLogic(){
        renderer = new RenderManager();
        window = Launch.getWindow();
    }

    @Override
    public void init() throws Exception {
        renderer.init();
    }

    @Override
    public void input() {

    }

    @Override
    public void update() {

    }

    @Override
    public void render() {
        if(window.isResize()){
            GL11.glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResize(true);
        }
        renderer.clear();
    }

    @Override
    public void cleanUp() {
        renderer.cleanUp();
    }
}
