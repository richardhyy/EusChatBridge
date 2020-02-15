package cc.eumc.euschatbridge;

import java.util.List;

public class PluginConfig {
    public static boolean Receiving_Enabled;
    public static String Receiving_ConnectionPassword;
    public static String Receiving_Path;
    public static double Receiving_MinRequestInterval;
    public static String Receiving_MessageTemplate;

    public static boolean Sending_Enabled;
    public static String Sending_ConnectionPassword;
    public static List<String> Sending_Webhooks;

    public PluginConfig(EusChatBridge instance) {
        Receiving_Enabled = instance.getConfig().getBoolean("Settings.Receiving.Enabled", false);
        if (Receiving_Enabled) {
            Receiving_ConnectionPassword = instance.getConfig().getString("Settings.Receiving.ConnectionPassword", "");
            Receiving_Path = instance.getConfig().getString("Settings.Receiving.Path", "/ChatBridge");
            Receiving_MinRequestInterval = instance.getConfig().getDouble("Settings.Receiving.MinRequestInterval", 0.02);
            Receiving_MessageTemplate = instance.getConfig().getString("Settings.Receiving.MessageTemplate", "[{name}] {message}").replace("&", "§");
        }

        Sending_Enabled = instance.getConfig().getBoolean("Settings.Sending.Enabled", false);
        if (Sending_Enabled) {
            Sending_ConnectionPassword = instance.getConfig().getString("Settings.Sending.ConnectionPassword", "");
            Sending_Webhooks = instance.getConfig().getStringList("Settings.Sending.Webhooks");
        }
    }
}
