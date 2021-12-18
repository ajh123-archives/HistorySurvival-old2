package net.ddns.minersonline.engine.core;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class Camera {
    private Vector3f pos, rot;

    public Camera(){
        pos = new Vector3f(0, 0, 0);
        rot = new Vector3f(0, 0 ,0);
    }

    public Camera(Vector3f pos, Vector3f rot) {
        this.pos = pos;
        this.rot = rot;
    }

    public void movePos(@NotNull Vector3f pos){
        if(pos.z != 0){
            this.pos.x += (float) Math.sin(Math.toRadians(rot.y)) * -1.0f * pos.z;
            this.pos.z += (float) Math.cos(Math.toRadians(rot.y)) * pos.z;
        }
        if(pos.x != 0){
            this.pos.x += (float) Math.sin(Math.toRadians(rot.y - 90)) * -1.0f * pos.x;
            this.pos.z += (float) Math.cos(Math.toRadians(rot.y - 90)) * pos.x;
        }
        this.pos.y += pos.y;
    }

    public void setPos(@NotNull Vector3f pos){
        this.pos = pos;
    }

    public void setRot(@NotNull Vector3f rot){
        this.rot = rot;
    }

    public void moveRot(@NotNull Vector3f rot){
        this.rot.add(rot);
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRot() {
        return rot;
    }
}
