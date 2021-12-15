package net.ddns.minersonline.engine.core.loading;

import net.ddns.minersonline.engine.core.entity.Model;
import net.ddns.minersonline.engine.core.utils.Utils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static net.ddns.minersonline.engine.core.utils.Utils.ioResourceToByteBuffer;
import static org.lwjgl.stb.STBImage.stbi_load_from_memory;

public class ObjectLoader {
    private final List<Integer> VAOs = new ArrayList<>();
    private final List<Integer> VBOs = new ArrayList<>();
    private final List<Integer> TCCs = new ArrayList<>();

    public Model loadModel(float[] vertices, float[] textCoOrds, int[] indices){
        int id = createVAO();
        storeIndicesBuffer(indices);
        storeDataInVBOs(0,3, vertices);
        storeDataInVBOs(1,2, textCoOrds);
        unbind();
        return new Model(id, indices.length);
    }

    public int loadTexture(String fileName) throws Exception{
        int width, height;
        ByteBuffer buffer;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);

            buffer = STBImage.stbi_load(fileName, w, h, c, 4);
            if(buffer == null){
                throw new Exception("Unable to load texture ("+fileName+") | "+STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();

        }
        int errorCode = GL11.glGetError();
        if (errorCode != GL11.GL_NO_ERROR) {
            throw new RuntimeException("OpenGL error " + errorCode+ " before loading texture: " + fileName);
        }
        int id = GL11.glGenTextures();
        TCCs.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(buffer);
        return id;
    }

    public int loadTextureResource(String fileName) throws Exception{
        int width, height;
        ByteBuffer data;
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer c = stack.mallocInt(1);
            data = stbi_load_from_memory(ioResourceToByteBuffer(fileName), w, h, c, 4);
            if (data == null) {
                throw new Exception("Unable to load texture (" + fileName + ") | " + STBImage.stbi_failure_reason());
            }
            width = w.get();
            height = h.get();
        }
        int errorCode = GL11.glGetError();
        if (errorCode != GL11.GL_NO_ERROR) {
            throw new RuntimeException("OpenGL error " + errorCode+ " before loading texture: " + fileName);
        }
        int id = GL11.glGenTextures();
        TCCs.add(id);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data);
        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
        STBImage.stbi_image_free(data);
        return id;
    }


    private int createVAO(){
        int id = GL30.glGenVertexArrays();
        VAOs.add(id);
        GL30.glBindVertexArray(id);
        return id;
    }

    private void storeIndicesBuffer(int[] indices){
        int vbo = GL15.glGenBuffers();
        VBOs.add(vbo);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vbo);
        IntBuffer buffer = Utils.storeDataInIntBuffer(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
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
        for(int tcc : TCCs){
            GL11.glDeleteTextures(tcc);
        }
    }
}
