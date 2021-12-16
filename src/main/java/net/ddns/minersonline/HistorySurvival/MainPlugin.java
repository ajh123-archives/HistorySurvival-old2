package net.ddns.minersonline.HistorySurvival;

import net.ddns.minersonline.engine.plugins.Plugin;

public class MainPlugin implements Plugin {

    public void run() {
        System.out.println(getClass().getName() + ": user.home: " + System.getProperty("user.home"));
    }
}
