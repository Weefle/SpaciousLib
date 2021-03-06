package org.anhcraft.spaciouslib.utils;

import org.bukkit.Bukkit;

public enum GameVersion {
    v1_8_R1(0),
    v1_8_R2(1),
    v1_8_R3(2),
    v1_9_R1(3),
    v1_9_R2(4),
    v1_10_R1(5),
    v1_11_R1(6),
    v1_12_R1(7);

    private int id;

    GameVersion(int id){
        this.id = id;
    }

    public int getID(){
        return this.id;
    }

    /**
     * Gets Minecraft version of this server
     * @return current Minecraft version
     */
    public static GameVersion getVersion(){
        return GameVersion.valueOf(Bukkit.getServer().getClass().getPackage()
                .getName().replace(".",  ",").split(",")[3]);
    }

    public static boolean is1_8Above(){
        return GameVersion.v1_8_R1.getID() <= getVersion().getID();
    }

    public static boolean is1_9Above(){
        return GameVersion.v1_9_R1.getID() <= getVersion().getID();
    }

    public static boolean is1_10Above(){
        return GameVersion.v1_10_R1.getID() <= getVersion().getID();
    }

    public static boolean is1_11Above(){
        return GameVersion.v1_11_R1.getID() <= getVersion().getID();
    }

    public static boolean is1_12Above(){
        return GameVersion.v1_12_R1.getID() <= getVersion().getID();
    }
}
