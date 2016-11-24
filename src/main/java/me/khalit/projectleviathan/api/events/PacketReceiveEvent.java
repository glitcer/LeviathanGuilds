package me.khalit.projectleviathan.api.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PacketReceiveEvent extends Event implements Cancellable {

    private final Player player;
    private static final HandlerList handlers = new HandlerList();
    private Object packet;
    private boolean cancelled;

    public PacketReceiveEvent(Object packet, Player player) {
        this.cancelled = false;
        this.packet = packet;
        this.player = player;
    }

    public String getName() {
        return this.packet.getClass().getSimpleName();
    }

    public Object getPacket() {
        return packet;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
