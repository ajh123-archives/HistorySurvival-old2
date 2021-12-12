package net.ddns.minersonline.engine.core.loading;

import net.ddns.minersonline.engine.core.entity.Model;
import net.ddns.minersonline.engine.core.utils.Utils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class ObjectLoader {
    private final List<Integer> VAOs = new ArrayList<>();
    private final List<Integer> VBOs = new ArrayList<>();

    public Model loadModel(float[] vertices){
        int id = createVAO();
        storeDataInVBOs(0,3, vertices);
        unbind();
        return new Model(id, vertices.length / 3);
    }

    private int createVAO(){
        int id = GL30.glGenVertexArrays();
        VAOs.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeDataInVBOs(int attrNo, int vertexCount, float[] data){
        int vbo = GL15.glGenBuffers();
        VBOs.add(vbo);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
        FloatBuffer buffer = Utils.storeDataInFloatBuffer(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attrNo, vertexCount, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void unbind(){
        GL30.glBindVertexArray(0);
    }

    public void cleanUp(){
        for(int vao : VAOs){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo : VBOs){
            GL30.glDeleteBuffers(vbo);
        }
    }
}
