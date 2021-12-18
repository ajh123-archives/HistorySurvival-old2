package net.ddns.minersonline.engine.core.entity;

import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

public class Entity {
    private final Model model;
    private final Vector3f pos, rot;
    private final float scale;

    public Entity(@NotNull Model model, @NotNull Vector3f pos, @NotNull Vector3f rot, float scale) {
        this.model = model;
        this.pos = pos;
        this.rot = rot;
        this.scale = scale;
    }

    public void incPos(@NotNull Vector3f pos){
        this.pos.add(pos);
    }

    public void setPos(@NotNull Vector3f pos){
        this.pos.set(pos.x, pos.y, pos.z);
    }

    public void incRot(@NotNull Vector3f rot){
        this.rot.add(rot);
    }

    public void setRot(@NotNull Vector3f rot){
        this.rot.set(rot.x, rot.y, rot.z);
    }

    public Model getModel() {
        return model;
    }

    public Vector3f getPos() {
        return pos;
    }

    public Vector3f getRot() {
        return rot;
    }

    public float getScale() {
        return scale;
    }
}
