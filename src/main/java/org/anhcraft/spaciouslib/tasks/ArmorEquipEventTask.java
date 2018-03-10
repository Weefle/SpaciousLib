package org.anhcraft.spaciouslib.tasks;

import org.anhcraft.spaciouslib.events.ArmorEquipEvent;
import org.anhcraft.spaciouslib.inventory.EquipSlot;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedHashMap;

public class ArmorEquipEventTask extends BukkitRunnable implements Listener {
    private static LinkedHashMap<Player,
            LinkedHashMap<EquipSlot, ItemStack>> data = new LinkedHashMap<>();

    @EventHandler
    public void quit(PlayerQuitEvent e){
        data.remove(e.getPlayer());
    }

    @Override
    public void run() {
        for(Player p : Bukkit.getServer().getOnlinePlayers()){
            ItemStack h = p.getInventory().getHelmet();
            ItemStack c = p.getInventory().getChestplate();
            ItemStack l = p.getInventory().getLeggings();
            ItemStack b = p.getInventory().getBoots();
            LinkedHashMap<EquipSlot, ItemStack> x = new LinkedHashMap<>();
            if(data.containsKey(p)){
                x = data.get(p);
                ItemStack oh = x.get(EquipSlot.HEAD);
                ItemStack oc = x.get(EquipSlot.CHEST);
                ItemStack ol = x.get(EquipSlot.LEGS);
                ItemStack ob = x.get(EquipSlot.FEET);
                if(h != oh){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, oh, h);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    x.put(EquipSlot.HEAD, e.getNewArmor());
                }
                if(c != oc){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, oc, c);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    x.put(EquipSlot.CHEST, e.getNewArmor());
                }
                if(l != ol){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, ol, l);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    x.put(EquipSlot.LEGS, e.getNewArmor());
                }
                if(b != ob){
                    ArmorEquipEvent e = new ArmorEquipEvent(p, ob, b);
                    Bukkit.getServer().getPluginManager().callEvent(e);
                    x.put(EquipSlot.FEET, e.getNewArmor());
                }
            } else {
                x.put(EquipSlot.HEAD, h);
                x.put(EquipSlot.CHEST, c);
                x.put(EquipSlot.LEGS, l);
                x.put(EquipSlot.FEET, b);
                data.put(p, x);
            }
        }
    }
}