package net.ddns.minersonline.engine.core;

import net.ddns.minersonline.engine.LogOutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joml.Matrix4f;
import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;


import java.io.PrintStream;

import static org.lwjgl.glfw.GLFW.*;

public class WindowManager {
    private static final Logger LOGGER = LogManager.getLogger(WindowManager.class);
    private final LogOutputStream errorLog = new LogOutputStream(LOGGER, Level.FATAL);
    public static final float FOV = (float) Math.toRadians(60);
    public static final float Z_NEAR = 0.01f;
    public static final float Z_FAR = 1000;

    private String title;
    private int width, height;
    private long window;

    private boolean resize, vSync;

    private final Matrix4f projectionMatrix;

    public WindowManager(String title, int width, int height, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        projectionMatrix = new Matrix4f();
    }

    public void init() throws Exception{
        LOGGER.info("Backend library: LWJGL version "+ Version.getVersion());
        glfwSetErrorCallback(GLFWErrorCallback.createPrint(new PrintStream(errorLog)));
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialise GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL11.GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GL11.GL_TRUE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);

        boolean maximised = false;
        if(width == 0 || height == 0){
            width = 100;
            height = 100;
            maximised = true;
            glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        }

        window = glfwCreateWindow(width, height, title, MemoryUtil.NULL, MemoryUtil.NULL);
        if (window == MemoryUtil.NULL){
            throw new RuntimeException("Failed to create GLFW window");
        }

        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
           this.width = width;
           this.height = height;
           this.setResize(true);
        });

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            //Maybe needed for the future
        });

        if(maximised){
            glfwMaximizeWindow(window);
        } else {
            GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidMode != null;
            glfwSetWindowPos(window,  (vidMode.width() - width) / 2,  (vidMode.height() - height) / 2);
        }
        glfwMakeContextCurrent(window);

        if(isVSync()){
            glfwSwapInterval(1);
        }

        glfwShowWindow(window);
        GL.createCapabilities();
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_STENCIL_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public void update(){
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    public void cleanUp(){
        glfwDestroyWindow(window);
    }

    public void setClearColour(float r, float b, float g, float a){
        GL11.glClearColor(r, g, b, a);
    }

    public boolean isKeyPressed(int glfwKeyCode){
        return  glfwGetKey(window, glfwKeyCode) == GLFW_PRESS;
    }

    public boolean windowShouldClose(){
        return glfwWindowShouldClose(window);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        glfwSetWindowTitle(window, title);
    }

    public boolean isResize() {
        return resize;
    }

    public void setResize(boolean resize) {
        this.resize = resize;
    }

    public boolean isVSync() {
        return vSync;
    }

    public void setVSync(boolean vSync) {
        this.vSync = vSync;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getWindow() {
        return window;
    }

    public void setWindow(long window) {
        this.window = window;
    }

    public Matrix4f getProjectionMatrix() {
        return projectionMatrix;
    }

    public Matrix4f updateProjectionMatrix(){
        float aspectRatio = (float) width / height;
        return projectionMatrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }

    public Matrix4f updateProjectionMatrix(Matrix4f matrix, int width, int height){
        float aspectRatio = (float) width / height;
        return matrix.setPerspective(FOV, aspectRatio, Z_NEAR, Z_FAR);
    }
}
