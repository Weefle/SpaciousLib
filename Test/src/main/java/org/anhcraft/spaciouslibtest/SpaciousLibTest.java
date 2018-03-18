package org.anhcraft.spaciouslibtest;

import org.anhcraft.spaciouslib.command.CommandArgument;
import org.anhcraft.spaciouslib.command.CommandRunnable;
import org.anhcraft.spaciouslib.command.SCommand;
import org.anhcraft.spaciouslib.command.SubCommand;
import org.anhcraft.spaciouslib.protocol.Particle;
import org.anhcraft.spaciouslib.protocol.PlayerList;
import org.anhcraft.spaciouslib.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SpaciousLibTest extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        try {
            new SCommand("sld",
                    new CommandRunnable() {
                        @Override
                        public void run(SCommand sCommand, SubCommand subCommand, CommandSender commandSender, String[] strings, String s) {
                            for(String str : sCommand.getCommandsAsString(true)){
                                commandSender.sendMessage(str);
                            }
                        }
                    })

                    .addSubCommand(new SubCommand("particle spawn", "Spawn a specific type of particle at your location", new CommandRunnable() {
                        @Override
                        public void run(SCommand sCommand, SubCommand subCommand, CommandSender commandSender, String[] strings, String s) {
                            commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                        }
                    }).setArgument(
                            new CommandArgument("type", new CommandRunnable() {
                                @Override
                                public void run(SCommand sCommand, SubCommand subCommand, CommandSender commandSender, String[] strings, String s) {
                                    if(commandSender instanceof Player) {
                                        Particle.Type type = StringUtils.get(strings[0].toUpperCase(), Particle.Type.values());
                                        if(type == null) {
                                            commandSender.sendMessage("Invalid particle type!");
                                        } else {
                                            Location location = ((Player) commandSender).getLocation();
                                            for (int degree = 0; degree < 360; degree++) {
                                                double radians = Math.toRadians(degree);
                                                double x = Math.cos(radians) * 3;
                                                double z = Math.sin(radians) * 3;
                                                location.add(x,0,z);
                                                Particle.create(type, location, 10).send(((Player) commandSender).getWorld());
                                                location.subtract(x,0,z);
                                            }
                                        }
                                    }
                                }
                            }, false),
                            CommandArgument.Type.CUSTOM
                    ).setArgument(
                            new CommandArgument("count", new CommandRunnable() {
                                @Override
                                public void run(SCommand sCommand, SubCommand subCommand, CommandSender commandSender, String[] strings, String s) {
                                    if(commandSender instanceof Player) {
                                        Particle.Type type = StringUtils.get(strings[0], Particle.Type.values());
                                        if(type == null) {
                                            commandSender.sendMessage("Invalid particle type!");
                                        } else {
                                            Location location = ((Player) commandSender).getLocation();
                                            for (int degree = 0; degree < 360; degree++) {
                                                double radians = Math.toRadians(degree);
                                                double x = Math.cos(radians) * 3;
                                                double z = Math.sin(radians) * 3;
                                                location.add(x,0,z);
                                                Particle.create(type, location, StringUtils.toIntegerNumber(strings[1])).send(((Player) commandSender).getWorld());
                                                location.subtract(x,0,z);
                                            }
                                        }
                                    }
                                }
                            }, true),
                            CommandArgument.Type.INTEGER_NUMBER
                    ).hideTypeCommandString())

                    .addSubCommand(new SubCommand("particle list", "show all types of particle", new CommandRunnable() {
                        @Override
                        public void run(SCommand sCommand, SubCommand subCommand, CommandSender commandSender, String[] strings, String s) {
                            StringBuilder pls = new StringBuilder();
                            for(Particle.Type p : Particle.Type.values()){
                                pls.append(p.toString()).append(" ");
                            }
                            commandSender.sendMessage(pls.toString());
                        }
                    }))

                    .addSubCommand(
                            new SubCommand("playerlist set", null, new CommandRunnable() {
                                @Override
                                public void run(SCommand sCommand, SubCommand subCommand, CommandSender commandSender, String[] strings, String s) {
                                    commandSender.sendMessage(sCommand.getCommandAsString(subCommand, true));
                                }
                            })
                                    .setArgument("text", new CommandRunnable() {
                                        @Override
                                        public void run(SCommand sCommand, SubCommand subCommand, CommandSender commandSender, String[] strings, String s) {
                                            PlayerList.create(s, s).sendAll();
                                            commandSender.sendMessage("ok");
                                        }
                                    }, CommandArgument.Type.CUSTOM, false)
                    )

                    .addSubCommand(new SubCommand("playerlist remove", null, new CommandRunnable() {
                        @Override
                        public void run(SCommand sCommand, SubCommand subCommand, CommandSender commandSender, String[] strings, String s) {
                            PlayerList.remove();
                        }
                    }))
                    .buildExecutor(this);
        } catch(Exception e) {
            e.printStackTrace();
        }
        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
    }
}