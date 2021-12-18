package net.ddns.minersonline.engine.core.managers;

import net.ddns.minersonline.HistorySurvival.Launch;
import net.ddns.minersonline.engine.core.Camera;
import net.ddns.minersonline.engine.core.entity.Entity;
import net.ddns.minersonline.engine.core.utils.Transformation;
import net.ddns.minersonline.engine.core.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RenderManager {
    private final WindowManager window;
    private ShaderManager shader;

    public RenderManager() {
        this.window = Launch.getWindow();
    }

    public void init() throws Exception {
        shader = new ShaderManager();
        shader.createVertexShader(Utils.loadResource("/shaders/vertex.vs"));
        shader.createFragmentShader(Utils.loadResource("/shaders/fragment.fs"));
        shader.link();
        shader.createUniform("textureSampler");
        shader.createUniform("transMatrix");
        shader.createUniform("projMatrix");
        shader.createUniform("viewMatrix");
    }

    public void render(@NotNull Entity entity, Camera cam) throws Exception{
        shader.bind();
        shader.setUniform("textureSampler", 0);
        shader.setUniform("transMatrix", Transformation.createTransformMatrix(entity));
        shader.setUniform("projMatrix", window.updateProjectionMatrix());
        shader.setUniform("viewMatrix", Transformation.createViewMatrix(cam));
        GL30.glBindVertexArray(entity.getModel().getId());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, entity.getModel().getTexture().getId());
        GL11.glDrawElements(GL11.GL_TRIANGLES, entity.getModel().getVertexCount(), GL11.GL_UNSIGNED_INT,  0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL30.glBindVertexArray(0);
        shader.unBind();
    }

    public void clear(){
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void cleanUp(){
        shader.cleanUp();
    }
}
