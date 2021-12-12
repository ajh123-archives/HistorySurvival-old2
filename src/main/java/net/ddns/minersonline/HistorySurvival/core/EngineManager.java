package net.ddns.minersonline.HistorySurvival.core;

import net.ddns.minersonline.HistorySurvival.Launch;
import net.ddns.minersonline.HistorySurvival.LogOutputStream;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFWErrorCallback;


import java.io.PrintStream;
import static org.lwjgl.glfw.GLFW.*;

public class EngineManager {
    private static final Logger LOGGER = LogManager.getLogger(EngineManager.class);
    private final LogOutputStream errorLog = new LogOutputStream(LOGGER, Level.FATAL);

    public static final long NANOSECOND = 1000000000L;
    public static final float FRAMERATE = 1000;

    private static int fps;
    private static final float frameTime = 1.0f / FRAMERATE;

    private boolean isRunning;
    private WindowManager window;
    private GLFWErrorCallback errorCallback;
    private ILogic gameLogic;

    private void init() throws Exception{
        errorCallback = GLFWErrorCallback.createPrint(new PrintStream(errorLog));
        glfwSetErrorCallback(errorCallback);
        window = Launch.getWindow();
        gameLogic = Launch.getGame();
        window.init();
        gameLogic.init();
    }

    public void start() throws Exception{
        init();
        if(isRunning){
            return;
        }
        run();
    }

    private void run(){
        isRunning = true;
        int frames = 0;
        long frameCounter = 0;
        long lastTime = System.nanoTime();
        double unprocessedTime = 0;

        while (isRunning){
            boolean render = false;
            long startTime = System.nanoTime();
            long passedTime = startTime - lastTime;
            lastTime = startTime;

            unprocessedTime += passedTime / (double) NANOSECOND;
            frameCounter += passedTime;

            input();

            while(unprocessedTime > frameTime){
                render = true;
                unprocessedTime -= frameTime;

                if(window.windowShouldClose()){
                    stop();
                    return;
                }
                if(frameCounter >= NANOSECOND){
                    setFps(frames);
                    frames = 0;
                    frameCounter = 0;
                }
            }

            if(render){
                update();
                render();
                frames++;
            }
        }
        cleanUp();
    }

    private void stop(){
        if(!isRunning){
            return;
        }
        isRunning = false;
    }

    private void input(){
        gameLogic.input();
    }

    private void render(){
        gameLogic.render();
        window.update();
    }

    private void update(){
        gameLogic.update();
    }

    private void cleanUp(){
        window.cleanUp();
        gameLogic.cleanUp();
        errorCallback.free();
        glfwTerminate();
    }

    public static int getFps() {
        return fps;
    }

    public static void setFps(int fps) {
        EngineManager.fps = fps;
    }
}
