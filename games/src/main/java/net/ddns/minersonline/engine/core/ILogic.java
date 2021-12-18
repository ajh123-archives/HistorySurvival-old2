package net.ddns.minersonline.engine.core;

import net.ddns.minersonline.engine.core.managers.MouseManager;
import net.ddns.minersonline.engine.plugins.PluginPolicy;

import java.security.Policy;

public abstract class ILogic {
    public void init() throws Exception {
        Policy.setPolicy(new PluginPolicy());
        System.setSecurityManager(new SecurityManager());
    }

    public void input(MouseManager mouse) throws Exception {}

    public void update() throws Exception{}

    public void render() throws Exception {}

    public void cleanUp() throws Exception {}
}
