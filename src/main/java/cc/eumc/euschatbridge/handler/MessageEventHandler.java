package cc.eumc.euschatbridge.handler;

import cc.eumc.euschatbridge.EusChatBridge;
import cc.eumc.euschatbridge.PluginConfig;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class MessageEventHandler implements Listener {
    EusChatBridge plugin;

    public MessageEventHandler(EusChatBridge instance) {
        this.plugin = instance;
    }

    @EventHandler
    public void onPlayerMessage(PlayerChatEvent e) {
        if (!PluginConfig.Sending_Enabled) return;
        if (PluginConfig.Sending_Webhooks.size() == 0) return;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            for (String webhookURL : PluginConfig.Sending_Webhooks) {
                sendMessageToWebhook(webhookURL, e.getPlayer().getName(), e.getMessage());
            }
        });
    }

    void sendMessageToWebhook(String webhookURL, String playerName, String message) {
        try {
            String params = "";
            params = "password=" + URLEncoder.encode(PluginConfig.Sending_ConnectionPassword, StandardCharsets.UTF_8.toString());
            params += "&playername=" + URLEncoder.encode(playerName, StandardCharsets.UTF_8.toString());
            params += "&message=" + URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            httpPost(webhookURL, params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String httpPost(String url, String urlParameters) throws Exception {
        try {
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            //add reuqest header
            con.setRequestMethod("POST");

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            //print result
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /*
    static String httpPost(String url, List<NameValuePair> params) throws Exception {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);

        httpPost.setEntity(new UrlEncodedFormEntity(params, Consts.UTF_8));

        CloseableHttpResponse response = client.execute(httpPost);
        client.close();

        return response.toString();
    }*/
}
