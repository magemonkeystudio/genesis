package studio.magemonkey.genesis.bungee;

import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

public class GenesisBungeeListener implements Listener {

    private final Plugin plugin;

    public GenesisBungeeListener(Plugin plugin) {
        this.plugin = plugin;
    }


    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {

        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
            DataInputStream dis = new DataInputStream(new ByteArrayInputStream(e.getData()));


            try {
                String subchannel = dis.readUTF();


                if (subchannel.equalsIgnoreCase("Genesis")) {

                    String type = dis.readUTF();

                    if (type.equalsIgnoreCase("Command")) {

                        String command = dis.readUTF();
                        plugin.getProxy().getPluginManager().dispatchCommand(plugin.getProxy().getConsole(), command);

                    }

                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

    }
}