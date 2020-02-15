package cc.eumc.euschatbridge;

import cc.eumc.euschatbridge.handler.MessageEventHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class EusChatBridge extends JavaPlugin {
    public boolean UniBan;

    @Override
    public void onEnable() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            saveDefaultConfig();
            saveResource("webhook_example.php", false);
        }
        reloadConfig();

        new PluginConfig(this);

        if (PluginConfig.Receiving_Enabled) {
            if (Bukkit.getPluginManager().getPlugin("UniBan") != null) {
                UniBan = true;
                new ChatReceiverExtension(this).register();
                getLogger().info("Message Receiving API enabled.");
            } else {
                UniBan = false;
                getLogger().severe("UniBan not found, Message Receiving API will not be enabled.");
            }
        }

        if (PluginConfig.Sending_Enabled) {
            Bukkit.getPluginManager().registerEvents(new MessageEventHandler(this), this);
            getLogger().info("Message Sending enabled with " +PluginConfig.Sending_Webhooks.size() + " webhook(s)");
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
