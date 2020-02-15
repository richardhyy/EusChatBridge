package cc.eumc.euschatbridge.handler;

import cc.eumc.euschatbridge.EusChatBridge;
import cc.eumc.euschatbridge.PluginConfig;
import cc.eumc.uniban.controller.AccessController;
import cc.eumc.uniban.controller.UniBanController;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatReceiveHandler implements HttpHandler {
    EusChatBridge plugin;
    UniBanController controller;
    AccessController accessController = new AccessController(PluginConfig.Receiving_MinRequestInterval);

    public ChatReceiveHandler(EusChatBridge instance, UniBanController unibanController) {
        this.plugin = instance;
        this.controller = unibanController;
    }

    @Override
    public void handle(HttpExchange t) throws IOException {
        if (!accessController.canAccess(t.getRemoteAddress().getHostName())) {
            // if host was blocked
            t.close();
            return;
        }
        plugin.getLogger().info("Request from: " + t.getRemoteAddress().getHostName());

        Map<String, String> args = new HashMap<>();
        if (t.getRequestMethod().equals("POST")) {
            List<String> postData = new ArrayList<>();
            postData = new BufferedReader(new InputStreamReader(t.getRequestBody()))
                    .lines().collect(Collectors.toList());
            args = parseParams(postData);
        }


        String response = "";
        if (args.containsKey("password") && args.containsKey("name") && args.containsKey("message")) {
            if (args.get("password").equals(PluginConfig.Receiving_ConnectionPassword)) {
                Bukkit.broadcastMessage(PluginConfig.Receiving_MessageTemplate
                        .replace("{name}", args.get("name"))
                        .replace("{message}", args.get("message")));
                t.sendResponseHeaders(200, response.length());
            }
            else {
                t.sendResponseHeaders(401, response.length());
            }
        }
        else {
            t.sendResponseHeaders(400, response.length());
        }

        byte[] responseByte;
        responseByte = response.getBytes();

        OutputStream os = t.getResponseBody();
        os.write(responseByte);
        os.close();
    }


    private static Map<String, String> parseParams(List<String> postData) {
        if (postData.size() > 0) {
            Map<String, String> argMap= new HashMap<>();
            for (String str : postData) {
                String[] split = str.split("=", 2);
                if (split.length == 2)
                    argMap.put(split[0], split[1]);
            }
            return argMap;
        }
        return new HashMap<>();
    }

}