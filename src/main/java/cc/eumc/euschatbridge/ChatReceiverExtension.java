package cc.eumc.euschatbridge;

import cc.eumc.euschatbridge.handler.ChatReceiveHandler;
import cc.eumc.uniban.controller.UniBanController;
import cc.eumc.uniban.extension.HttpService;
import cc.eumc.uniban.extension.UniBanExtension;

public class ChatReceiverExtension extends UniBanExtension {
    UniBanController controller;
    EusChatBridge plugin;
    HttpService httpService;

    public ChatReceiverExtension(EusChatBridge instance) {
        this.plugin = instance;
    }

    @Override
    public void onExtensionLoad() {
        this.controller = getUniBanController();
        this.httpService = new HttpService(PluginConfig.Receiving_Path, new ChatReceiveHandler(plugin, controller));
    }

    @Override
    public void onExtensionUnload() {
        if (httpService.context != null)
            controller.removeHttpContent(httpService.context);
    }

    @Override
    public HttpService getHttpService() {
        return this.httpService;
    }

    @Override
    public String getName() {
        return plugin.getDescription().getName();
    }

    @Override
    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    @Override
    public String getVersion() {
        return plugin.getDescription().getVersion();
    }
}
