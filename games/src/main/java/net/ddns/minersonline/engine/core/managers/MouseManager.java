package net.ddns.minersonline.engine.core.managers;

import net.ddns.minersonline.HistorySurvival.Launch;
import org.joml.Vector2d;
import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class MouseManager {
    private final Vector2d lastPos, pos;
    private final Vector2f disPos;
    private boolean inWindow = false, lbPressed = false, rbPressed = false;

    public MouseManager(){
        lastPos = new Vector2d(-1, -1);
        pos = new Vector2d(0, 0);
        disPos = new Vector2f();
    }

    public void init() throws Exception{
        GLFW.glfwSetCursorPosCallback(Launch.getWindow().getWindow(), (window, xpos, ypos) ->{
            pos.set(xpos, ypos);
        });

        GLFW.glfwSetCursorEnterCallback(Launch.getWindow().getWindow(), (window, entered) -> {
            inWindow = entered;
        });

        GLFW.glfwSetMouseButtonCallback(Launch.getWindow().getWindow(), (window, button, action, mode) -> {
            lbPressed = button == GLFW.GLFW_MOUSE_BUTTON_1 && action == GLFW.GLFW_PRESS;
            rbPressed = button == GLFW.GLFW_MOUSE_BUTTON_2 && action == GLFW.GLFW_PRESS;
        });
    }

    public void input() throws Exception {
        if (lastPos.x > 0 && lastPos.y > 0 && inWindow){
            double x = pos.x - lastPos.x;
            double y = pos.y - lastPos.y;
            boolean rX = x != 0;
            boolean rY = y != 0;
            if(rX){
                disPos.y = (float) x;
            }
            if(rY){
                disPos.x = (float) y;
            }
        }
        lastPos.set(pos.x, pos.y);
    }

    public Vector2f getDisPos() {
        return disPos;
    }

    public boolean isLbPressed() {
        return lbPressed;
    }

    public boolean isRbPressed() {
        return rbPressed;
    }
}
