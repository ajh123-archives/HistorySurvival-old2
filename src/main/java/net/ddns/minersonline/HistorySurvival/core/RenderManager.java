package net.ddns.minersonline.HistorySurvival.core;

import net.ddns.minersonline.HistorySurvival.Launch;
import org.lwjgl.opengl.GL11;

public class RenderManager {
    private final WindowManager window;

    public RenderManager() {
        this.window = Launch.getWindow();
    }

    public void init() throws Exception {

    }

    public void render() {

    }

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanUp(){

    }
}
