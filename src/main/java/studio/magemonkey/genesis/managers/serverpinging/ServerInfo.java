package studio.magemonkey.genesis.managers.serverpinging;

import lombok.Getter;
import lombok.Setter;
import studio.magemonkey.genesis.managers.ClassManager;

import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServerInfo {
    public final static int TYPE_NORMAL     = 0;
    public final static int TYPE_BUNGEECORD = 1;

    private final int type;

    @Getter
    private final String            host;
    @Getter
    private final int               port;
    @Getter
    private final int               timeout;
    @Getter
    private final InetSocketAddress address;

    @Getter
    @Setter
    private int     players;
    @Getter
    @Setter
    private int     maxPlayers;
    @Getter
    @Setter
    private boolean online;
    @Getter
    @Setter
    private String  motd;

    private long    wait;
    @Setter
    private boolean beingPinged;

    private final Set<ConnectedBuyItem> buyitems = new LinkedHashSet<ConnectedBuyItem>();

    public ServerInfo(String host, int port, int timeout) {
        type = TYPE_NORMAL;
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.address = new InetSocketAddress(host, port);
    }

    public ServerInfo(String bungeeCordServerName, int timeout) {
        type = TYPE_BUNGEECORD;
        ClassManager.manager.getSettings().setBungeeCordServerEnabled(true);
        this.host = bungeeCordServerName;
        this.port = -1;
        this.address = null;
        this.motd = "unknown";
        this.timeout = timeout;
    }

    public void update(ServerConnector currentConnector) {
        switch (type) {
            case TYPE_NORMAL:
                currentConnector.update(this);
                break;
            case TYPE_BUNGEECORD:
                ClassManager.manager.getBungeeCordManager()
                        .sendPluginMessage(null, ClassManager.manager.getPlugin(), "PlayerCount", host);
                break;
        }
    }

    public void setNoConnection() {
        online = false;
        players = 0;
        maxPlayers = 0;
        motd = "Offline";
    }

    public void hadNoSuccess() {
        int delay = ClassManager.manager.getSettings().getServerPingingWaitTime();
        if (delay > 0) {
            wait = System.currentTimeMillis() + delay;
        }
    }

    public boolean isWaiting() {
        return wait > System.currentTimeMillis() || beingPinged;
    }

    public void addShopItem(ConnectedBuyItem shopItem) {
        buyitems.add(shopItem);
    }

    public Set<ConnectedBuyItem> getConnectedBuyItems() {
        return buyitems;
    }
}
