package cc.eumc.euschatbridge.handler;

import cc.eumc.euschatbridge.EusChatBridge;
import cc.eumc.euschatbridge.PluginConfig;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class MessageEventHandler implements Listener {
    EusChatBridge plugin;

    public MessageEventHandler(EusChatBridge instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerMessage(AsyncPlayerChatEvent e) {
        if (!PluginConfig.Sending_Enabled) return;
        if (PluginConfig.Sending_Webhooks.size() == 0) return;
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> {
            for (String webhookURL : PluginConfig.Sending_Webhooks) {
                sendMessageToWebhook(webhookURL, e.getPlayer().getName(), e.getMessage());
            }
        }, 1);
    }

    void sendMessageToWebhook(String webhookURL, String playerName, String message) {
        try {
            List<NameValuePair> params = new ArrayList<>();
            params.add(new BasicNameValuePair("password", PluginConfig.Sending_ConnectionPassword));
            params.add(new BasicNameValuePair("playername", playerName));
            params.add(new BasicNameValuePair("message", message));
            httpPost(webhookURL, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String httpPost(String url, List<NameValuePair> params) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));

        CloseableHttpResponse response = client.execute(httpPost);
        client.close();

        return response.toString();
    }
}
