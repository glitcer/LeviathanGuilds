package me.khalit.projectleviathan.utils.reflection.packet;

import me.khalit.projectleviathan.data.Guild;
import me.khalit.projectleviathan.data.managers.GuildManager;
import me.khalit.projectleviathan.utils.KeyPair;
import me.khalit.projectleviathan.utils.reflection.Reflection;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GuildEntity {

    private final Map<Integer, Object> entityIds = new HashMap<>();
    private final Map<Guild, KeyPair<Integer, Object>> entityMap = new HashMap<>();
    private final Class<?> entityClass = Reflection.getCraftClass("Entity");
    private final Class<?> packetPlayOutSpawnEntityClass = Reflection.getCraftClass("PacketPlayOutSpawnEntity");
    private final Class<?> packetPlayOutEntityDestroyClass = Reflection.getCraftClass("PacketPlayOutEntityDestroy");
    private final Class<?> worldClass = Reflection.getCraftClass("World");

    private Method setLocationMethod;
    private Method getIdMethod;
    private Class<?> entityClassPath;

    public GuildEntity(String classPathEntity) {
        if (!classPathEntity.contains("Entity")) {
            classPathEntity = "Entity" + classPathEntity;
        }
        this.entityClassPath = Reflection.getCraftClass(classPathEntity);
        this.setLocationMethod = Reflection.getMethod(entityClassPath, "setLocation");
        this.getIdMethod = Reflection.getMethod(entityClassPath, "getId");
    }

    public Object getDestroyPacket(int id) {
        try {
            return packetPlayOutEntityDestroyClass.getConstructor(int[].class)
                    .newInstance(id);
        } catch (InstantiationException
                | IllegalAccessException
                | InvocationTargetException
                | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public KeyPair<Integer, Object> getSpawnPacketData(Location location) {
        try {
            Object world = Reflection.getHandle(location.getWorld());
            Object crystal = entityClassPath.getConstructor(worldClass).newInstance(world);
            Constructor packetPlayOutSpawn = packetPlayOutSpawnEntityClass.getConstructor(entityClass, int.class);

            setLocationMethod.invoke(crystal,
                    location.getBlockX() + 0.50,
                    location.getBlockY(),
                    location.getBlockZ() + 0.50,
                    0, 0);
            Object packet = packetPlayOutSpawn.newInstance(crystal, 51);

            int id = (int) getIdMethod.invoke(crystal);

            entityIds.put(id, packet);
            return new KeyPair<>(id, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void spawn(Player... players) {
        GuildManager.getGuilds().values().forEach(guild ->
                PacketInjector.sendPacket(players, getPacket(guild)));
    }

    public void spawn(Guild guild) {
        PacketInjector.sendPacket(getPacket(guild));
    }

    public void destroy(Guild guild) {
        int id = getId(guild);
        entityIds.remove(id);
        entityMap.remove(guild);
        PacketInjector.sendPacket(getDestroyPacket(id));
    }

    private int getId(Guild guild) {
        return entityMap.get(guild).getValueFirst();
    }

    private Object getPacket(Guild guild) {
        Object packet;
        if (!entityMap.containsKey(guild)) {
            KeyPair<Integer, Object> packetData =
                    getSpawnPacketData(
                            guild.getRegion().getCenter());

            entityMap.put(guild, packetData);
            return packetData.getValueSecond();
        }
        return entityMap.get(guild).getValueSecond();
    }

}
