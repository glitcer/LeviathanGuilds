package me.khalit.projectleviathan.utils.reflection;

import me.khalit.projectleviathan.api.Hologram;
import me.khalit.projectleviathan.api.TabPacket;
import me.khalit.projectleviathan.utils.reflection.v1_11.Hologram1_11;
import me.khalit.projectleviathan.utils.reflection.v1_11.TabPacket1_11;
import me.khalit.projectleviathan.utils.reflection.v1_9.TabPacket1_9;

import java.lang.ref.Reference;

public class ProtocolManager {

    private static TabPacket tabPacket;

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

    public static Hologram getHologram() {
        return new Hologram1_11(); // not sure what errors are on each version, tested on 1.11
    }
}
