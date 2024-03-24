package com.promcteam.genesis.managers.serverpinging;

import com.promcteam.genesis.Genesis;
import com.promcteam.genesis.core.GenesisBuy;
import com.promcteam.genesis.core.GenesisShop;
import com.promcteam.genesis.managers.ClassManager;
import com.promcteam.genesis.managers.misc.StringManipulationLib;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class ServerPingingManager {

    private final String[] placeholderNames = new String[]{"players", "motd"};


    @Getter
    private final ServerPingingList    list = new ServerPingingList();
    private       ServerConnectorSmart connector;
    @Setter
    private       boolean              readyToTransform;

    private final ServerPingingRunnableHandler runnableHandler = new ServerPingingRunnableHandler();


    public ServerPingingManager(Genesis plugin) {
        Genesis.log("[ServerPinging] Loading ServerPinging Package!");

        int connectorType = plugin.getClassManager().getStorageManager().getConfig().getInt("serverpinging.connector");
        setup(plugin.getConfig().getStringList("ServerPinging.List"), connectorType);
    }

    public ServerPingingRunnableHandler getServerPingingRunnableHandler() {
        return runnableHandler;
    }

    public void setup(List<String> pingingList, int connectorType) {
        List<ServerConnector> connectors = new ArrayList<>();
        try {
            connectors.add(new ServerConnector1());
        } catch (NoClassDefFoundError e) {
        }
        try {
            connectors.add(new ServerConnector2());
        } catch (NoClassDefFoundError e) {
        }
        try {
            connectors.add(new ServerConnector3());
        } catch (NoClassDefFoundError e) {
        }
        try {
            connectors.add(new ServerConnector4());
        } catch (NoClassDefFoundError e) {
        }
        connector = new ServerConnectorSmart(connectors);

        if (pingingList != null) {
            for (String connection : pingingList) {
                String[] parts = connection.split(":", 3);

                if (parts.length != 3 && parts.length != 2 && parts.length != 1) {
                    ClassManager.manager.getBugFinder()
                            .warn("Unable to read ServerPinging list entry '" + connection
                                    + "'. It should look like following: '<name>:<server ip>:<server port>' or if you are using BungeeCord then simply enter the name of the connected BungeeCord server: 'server name'.");
                    continue;
                }

                String name = parts[0].trim();

                if (parts.length == 1) {
                    ServerInfo info =
                            new ServerInfo(name, ClassManager.manager.getSettings().getServerPingingTimeout());
                    list.addServerInfo(name, info);
                    continue;
                }


                String host = parts[1].trim();
                int    port = 25565;

                if (parts.length == 3) {
                    try {
                        port = Integer.parseInt(parts[2].trim());
                    } catch (NumberFormatException e) {
                        //keep default port
                    }
                }

                ServerInfo info =
                        new ServerInfo(host, port, ClassManager.manager.getSettings().getServerPingingTimeout());
                list.addServerInfo(name, info);

            }
        }
        list.update(connector, false);
    }

    public void update() {
        list.update(connector, true);
        if (ClassManager.manager.getShops() != null) {
            ClassManager.manager.getShops().refreshShops(true);
        }
    }


    public String transform(String s) {
        if (!readyToTransform) {
            return s;
        }

        if (StringManipulationLib.figureOutVariable(s, 0, placeholderNames) != null) {

            for (String placeholderName : placeholderNames) {
                int fromIndex = 0;

                String variable = StringManipulationLib.figureOutVariable(s, placeholderName, fromIndex);
                while (variable != null) {
                    s = transform(variable.split(":"), s, fromIndex);
                    fromIndex = StringManipulationLib.getIndexOfVariableEnd(s, placeholderName, fromIndex);
                    variable = StringManipulationLib.figureOutVariable(s, placeholderName, fromIndex);
                }

            }
        }

        return s;
    }

    public String transform(String[] servers, String current, int fromIndex) {
        if (!readyToTransform) {
            return current;
        }

        String motd    = null;
        int    players = 0;

        for (String serverName : servers) {
            ServerInfo c = getInfo(serverName);
            if (c != null) {
                if (c.isOnline()) {
                    if (motd == null) {
                        motd = c.getMotd();
                    }
                    players += c.getPlayers();
                }
            }
        }
        return transform(current, current, motd == null ? "unknown" : motd, String.valueOf(players), fromIndex);
    }

    public String transform(String serverName, String current, int fromIndex) {
        if (!readyToTransform) {
            return current;
        }

        ServerInfo c = getInfo(serverName);
        if (c != null) {
            if (c.isOnline()) {
                String motd    = c.getMotd();
                String players = String.valueOf(c.getPlayers());
                return transform(current, current, motd, players, fromIndex);
            }
        }
        return current;
    }

    private String transform(String original, String current, String motd, String players, int fromIndex) {
        if (!readyToTransform) {
            return current;
        }

        boolean b = false;
        if (original.contains("%motd_") && motd != null) {
            original = StringManipulationLib.replacePlaceholder(original, "motd", motd, fromIndex);
            b = true;
        }
        if (original.contains("%players_") && players != null) {
            original = StringManipulationLib.replacePlaceholder(original, "players", players, fromIndex);
            b = true;
        }
        if (!b) {
            return current;
        }
        return original;
    }


    public void registerShopItem(String serverName, ConnectedBuyItem connection) {
        ServerInfo info = getInfo(serverName);
        if (info != null) {
            info.addShopItem(connection);
        }
    }

    public void clear() {
        //not doing that yet
    }


    public ServerInfo getInfo(String serverName) {
        return list.getInfo(serverName);
    }


    public boolean containsServerpinging(GenesisShop shop) {
        for (GenesisBuy b : shop.getItems()) {
            if (b != null) {
                if (isConnected(b)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isConnected(GenesisBuy buy) {
        return getFirstInfo(buy) != null;
    }

    public ServerInfo getFirstInfo(GenesisBuy buy) {
        return list.getFirstInfo(buy);
    }


}
