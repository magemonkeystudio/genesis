package com.promcteam.genesis.managers.external;


import com.google.common.collect.Iterables;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.promcteam.genesis.Genesis;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.serverpinging.ServerInfo;
import com.promcteam.genesis.managers.serverpinging.ServerPingingManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;


public class BungeeCordManager implements PluginMessageListener {


    public final static String PLUGIN_CHANNEL                = "Genesis";
    public final static String PLUGIN_SUBCHANNEL_PLAYERINPUT = "PlayerInput";


    public void sendToServer(String server, PlayerMoveEvent event, Genesis plugin) {
        event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
        event.setCancelled(true);
        sendToServer(server, event.getPlayer(), plugin);
    }

    public void sendToServer(String server, Player p, Genesis plugin) {
        sendPluginMessage(p, plugin, "Connect", server);
    }

    public boolean sendPluginMessage(Player p, Genesis plugin, String... args) {
        if (p == null) {
            p = Iterables.getFirst(Bukkit.getOnlinePlayers(), null);
        }

        if (p != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            for (String arg : args) {
                if (arg != null) {
                    out.writeUTF(arg);
                }
            }
            p.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
            return true;
        }

        return false;
    }

    public void sendShopPluginMessage(Player p,
                                      String subchannel,
                                      String argumentA,
                                      String argumentB,
                                      String argumentC) {
        sendPluginMessage(p,
                ClassManager.manager.getPlugin(),
                PLUGIN_CHANNEL,
                subchannel,
                argumentA,
                argumentB,
                argumentC);
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        try {
            if (!channel.equals("BungeeCord")) {
                return;
            }
            ByteArrayDataInput in = ByteStreams.newDataInput(message);

            String subchannel = in.readUTF();


            if (subchannel.equals("PlayerCount")) {
                String server      = in.readUTF(); //
                int    playercount = in.readInt();

                ServerPingingManager m = ClassManager.manager.getServerPingingManager();
                if (m == null) {
                    throw new RuntimeException(
                            "Received PlayerCount plugin message but server pinging is not even loaded?!");
                }
                for (ServerInfo c : ClassManager.manager.getServerPingingManager().getList().getInfos().values()) {
                    if (c.getHost().equalsIgnoreCase(server)) {
                        c.setPlayers(playercount);
                        c.setOnline(true);
                    }
                }
            }
        } catch (Exception e) {
            //EOF/IllegalStateException?
        }
    }

    public void executeCommand(Player p, String command) {
        sendPluginMessage(p, ClassManager.manager.getPlugin(), PLUGIN_CHANNEL, "Command", command);
    }


    public void playerInputNotification(Player p, String type, String additionalInfo) {
        sendShopPluginMessage(p, PLUGIN_SUBCHANNEL_PLAYERINPUT, p.getUniqueId().toString(), type, additionalInfo);
    }

}
