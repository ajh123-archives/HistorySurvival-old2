package net.ddns.minersonline.engine.core.managers;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.system.MemoryStack;

import java.util.HashMap;
import java.util.Map;

public class ShaderManager {
    private final int programID;
    private int vertexShaderID, fragmentShaderID;

    private final Map<String, Integer> uniforms;

    public ShaderManager() throws Exception{
        programID = GL20.glCreateProgram();
        if (programID == 0){
            throw new Exception("Unable to create shader");
        }
        uniforms = new HashMap<>();
    }

    public void createUniform(String uniformName) throws Exception{
        int ufLoc = GL20.glGetUniformLocation(programID, uniformName);
        if (ufLoc < 0){
            throw new Exception("Unable to locate uniform ("+uniformName+")");
        }
        uniforms.put(uniformName, ufLoc);
    }

    public void setUniform(String uniformName, Matrix4f matrix) throws Exception{
        try(MemoryStack stack = MemoryStack.stackPush()){
            GL20.glUniformMatrix4fv(uniforms.get(uniformName), false, matrix.get(stack.mallocFloat(16)));
        } catch (Exception e){
            throw new Exception("Unable to locate uniform ("+uniformName+")");
        }
    }

    public void setUniform(String uniformName, int number) throws Exception{
        try {
            GL20.glUniform1i(uniforms.get(uniformName), number);
        }catch (Exception e){
            throw new Exception("Unable to locate uniform ("+uniformName+")");
        }
    }

    public void createVertexShader(String shaderCode) throws Exception{
        vertexShaderID = createShader(shaderCode, GL20.GL_VERTEX_SHADER);
    }

    public void createFragmentShader(String shaderCode) throws Exception{
        fragmentShaderID = createShader(shaderCode, GL20.GL_FRAGMENT_SHADER);
    }

    private int createShader(String shaderCode, int type) throws Exception{
        int shaderID = GL20.glCreateShader(type);
        if (shaderID == 0){
            throw new Exception("Unable to create shader ("+type+")");
        }

        GL20.glShaderSource(shaderID, shaderCode);
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == 0){
            throw new Exception("Unable to compile shader ("+type+") "+GL20.glGetShaderInfoLog(shaderID));
        }

        GL20.glAttachShader(programID, shaderID);
        return shaderID;
    }

    public void link() throws Exception{
        GL20.glLinkProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_LINK_STATUS) == 0){
            throw new Exception("Unable to link shader program "+GL20.glGetProgramInfoLog(programID));
        }

        if(vertexShaderID != 0){
            GL20.glDetachShader(programID, vertexShaderID);
        }
        if(fragmentShaderID != 0){
            GL20.glDetachShader(programID, fragmentShaderID);
        }

        GL20.glValidateProgram(programID);
        if (GL20.glGetProgrami(programID, GL20.GL_VALIDATE_STATUS) == 0){
            throw new Exception("Unable to validate shader program\n"+GL20.glGetProgramInfoLog(programID));
        }
    }

    public void bind(){
        GL20.glUseProgram(programID);
    }

    public void unBind(){
        GL20.glUseProgram(0);
    }

    public void cleanUp(){
        unBind();
        if(programID != 0){
            GL20.glDeleteProgram(programID);
        }
    }
}
