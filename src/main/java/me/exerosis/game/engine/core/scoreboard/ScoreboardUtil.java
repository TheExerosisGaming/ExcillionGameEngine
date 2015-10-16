package me.exerosis.game.engine.core.scoreboard;

import me.exerosis.reflection.Reflect;
import me.exerosis.reflection.ReflectClass;
import net.minecraft.server.v1_8_R1.EnumScoreboardHealthDisplay;

public class ScoreboardUtil {
    private ScoreboardUtil() {
    }

    public static Object getCreatePacket() {
        ReflectClass<Object> createPacket = Reflect.Class("{nms}.PacketPlayOutScoreboardDisplayObjective");
        createPacket.newInstance();
        createPacket.getField(int.class, "a").setValue(0);
        createPacket.getField(String.class, "b").setValue("test");

        return createPacket.getInstance();
    }

    public static Object getCreateObjectivePacket() {
        ReflectClass<Object> createObjective = Reflect.Class("{nms}.PacketPlayOutScoreboardObjective");
        createObjective.newInstance();
        createObjective.getField(int.class, "d").setValue(0);
        createObjective.getField(String.class, "a").setValue("dummy");
        createObjective.getField(String.class, "b").setValue("dummy");

        createObjective.getField(Object.class, "c").setValue(EnumScoreboardHealthDisplay.INTEGER);

        return createObjective.getInstance();
    }

    public static Object getUpdateScorePacket(String text, int score) {
        ReflectClass<Object> updatePacket = Reflect.Class("{nms}.PacketPlayOutScoreboardDisplayObjective");
        updatePacket.newInstance();
        updatePacket.getField(String.class, 1).setValue(text);
        updatePacket.getField(String.class, 2).setValue("Excillion Engine");
        updatePacket.getField(int.class, 1).setValue(score);

        ReflectClass<Object> action = Reflect.Class("{nms}.EnumScoreboardAction");

        updatePacket.getField(Object.class, 3).setValue(action.getMethod(String.class).call("CHANGE"));
        return updatePacket.getInstance();
    }
}