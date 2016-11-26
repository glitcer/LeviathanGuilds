package me.khalit.projectleviathan.utils.reflection;

import me.khalit.projectleviathan.api.ActionBar;
import me.khalit.projectleviathan.api.Hologram;
import me.khalit.projectleviathan.api.TabPacket;
import me.khalit.projectleviathan.utils.reflection.v1_11.ActionBarPacket1_11;
import me.khalit.projectleviathan.utils.reflection.v1_11.HologramPacket1_11;
import me.khalit.projectleviathan.utils.reflection.v1_11.TabPacket1_11;
import me.khalit.projectleviathan.utils.reflection.v1_8.ActionBarPacket1_8;
import me.khalit.projectleviathan.utils.reflection.v1_9.TabPacket1_9;

public class ProtocolManager {

    private static TabPacket tabPacket;
    private static ActionBar actionBar;

    public static TabPacket getTabPacket() {
        if (tabPacket == null) {
            String version = Reflection.getVersion();
            if (version.contains("10") || version.contains("11")) {
                tabPacket = new TabPacket1_11();
            }
            else if (version.contains("9") || version.contains("8")) {
                tabPacket = new TabPacket1_9();
            }
            else {
                tabPacket = new TabPacket1_11();
            }
        }
        return tabPacket;
    }

    public static ActionBar getActionBar() {
        if (actionBar == null) {
            String version = Reflection.getVersion();
            if (version.contains("10") || version.contains("11") || version.contains("9")) {
                actionBar = new ActionBarPacket1_11();
            }
            else if (version.contains("8")) {
                actionBar = new ActionBarPacket1_8();
            }
        }
        return actionBar;
    }

    public static Hologram getHologram() {
        return new HologramPacket1_11(); // not sure what errors are on each version, tested on 1.11
    }
}
