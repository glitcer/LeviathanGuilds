package me.khalit.projectleviathan.utils.reflection;

import me.khalit.projectleviathan.api.TabPacket;
import me.khalit.projectleviathan.utils.reflection.v1_11.TabPacket1_11;
import me.khalit.projectleviathan.utils.reflection.v1_9.TabPacket1_9;

public class ProtocolManager {

    public static TabPacket getTabPacket() {
        String version = Reflection.getVersion();
        if (version.contains("10") || version.contains("11")) {
            return new TabPacket1_11();
        }
        else if (version.contains("9") || version.contains("8")) {
            return new TabPacket1_9();
        }
        return new TabPacket1_11();
    }
}
