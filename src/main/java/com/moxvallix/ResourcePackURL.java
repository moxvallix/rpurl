package com.moxvallix;

import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.lang.Math;

public class ResourcePackURL extends JavaPlugin implements Listener {
    static Server server;
    static ConsoleCommandSender console;
    static Plugin plugin;

    public static void getResourcePack(Player player) {
        try {
            File path = new File("plugins//ResourcePackURL");
            File fileYourFile = new File("plugins//ResourcePackURL//Config.yml");
            YamlConfiguration ymlYourFile = YamlConfiguration.loadConfiguration(fileYourFile);
            if (!path.exists()) path.mkdir();
            if (!fileYourFile.exists()) plugin.saveResource("Config.yml", true);
            String resourcePackURL = ymlYourFile.getString("ResourcePackURL");
            if (resourcePackURL == null || resourcePackURL.isEmpty()) {
                return;
            } else {
                final URL url = new URL(ymlYourFile.getString("ResourcePackURL"));
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                int responseCode = huc.getResponseCode();
                int min = 1;
                int max = 100000000;
                int randnum = (int)(Math.random() * (max - min + 1) + min);
                String randstring = Integer.toString(randnum);
                String totalurl = String.join("", url.toString(),"#",randstring);
                console.sendMessage(totalurl);
                if (responseCode == 200) player.setResourcePack(totalurl);
            }
        } catch (UnknownHostException uhe) {
            console.sendMessage(ChatColor.RED + "Resource Pack URL UnknownHostException : " + uhe.getMessage());
        } catch (FileNotFoundException fnfe) {
            console.sendMessage(ChatColor.RED + "Resource Pack URL FileNotFoundException : " + fnfe.getMessage());
        } catch (Exception e) {
            console.sendMessage(ChatColor.RED + "Resource Pack URL Exception : " + e.getMessage());
        }
    }

    public void onEnable() {
        plugin = this;
        server = this.getServer();
        this.getCommand("rp").setExecutor(new CommandRP());
        console = server.getConsoleSender();
        server.getPluginManager().registerEvents(this, this);
        console.sendMessage(ChatColor.GREEN + "" + ChatColor.ITALIC + "Resource Pack URL Loaded");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        getResourcePack(event.getPlayer());
    }

    @Override
    public void onDisable() {
        console.sendMessage(ChatColor.RED + "Resource Pack URL Disabled");
    }
}