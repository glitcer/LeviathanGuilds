package me.khalit.projectleviathan.utils.reflection.packet;

import io.netty.channel.*;
import me.khalit.projectleviathan.api.events.PacketReceiveEvent;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class NettyManager {

    private static final String PIPELINE_PARENT = "LeviathanProjectPacketHandler";
    private static final String PIPELINE_NAME = "LeviathanGuilds";

    private static final Field channel =
            Reflection.getField(Reflection.getCraftClass("NetworkManager"), null, Channel.class, 0);

    private static final Field playerConnection = Reflection.getField(Reflection.getCraftClass("EntityPlayer"), "playerConnection");
    private static final Field networkManager = Reflection.getField(Reflection.getCraftClass("PlayerConnection"), "networkManager");
    private static final Method handle = Reflection.getMethod(Reflection.getBukkitClass("entity.CraftEntity"), "getHandle");

    private static Channel getChannel(Player player) {
        try {
            Object entityHandle = handle.invoke(player);
            return (Channel) channel.get(networkManager.get(playerConnection.get(entityHandle)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void register(Player player) throws IllegalAccessException {
        Channel c = getChannel(player);
        ChannelHandler channelHandler = new ChannelDuplexHandler() {
            @Override
            public void write(ChannelHandlerContext channelHandlerContext, Object packet, ChannelPromise channelPromise) throws Exception {
                if (packet == null) {
                    return;
                }
                super.write(channelHandlerContext, packet, channelPromise);
            }

            @Override
            public void channelRead(ChannelHandlerContext channelHandlerContext, Object packet) throws Exception {
                try {
                    if (packet == null) {
                        return;
                    }
                    PacketReceiveEvent event = new PacketReceiveEvent(packet, player);
                    Bukkit.getPluginManager().callEvent(event);

                    if (event.isCancelled() || event.getPacket() == null) {
                        return;
                    }
                    super.channelRead(channelHandlerContext, event.getPacket());
                } catch (Exception e) {
                    super.channelRead(channelHandlerContext, packet);
                }
            }
        };
        ChannelPipeline channelPipeline = c.pipeline();
        if (channelPipeline.names().contains(PIPELINE_PARENT)) {
            if (channelPipeline.names().contains(PIPELINE_NAME))
                channelPipeline.replace(PIPELINE_NAME, PIPELINE_NAME, channelHandler);
            else {
                channelPipeline.addBefore(PIPELINE_PARENT, PIPELINE_NAME, channelHandler);
            }
        }
    }

}
