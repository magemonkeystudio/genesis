package org.black_ixx.bossshop.managers.serverpinging;

import lombok.Getter;
import org.black_ixx.bossshop.core.BSBuy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class ServerPingingList {
    private Map<String, ServerInfo> toAdd  = new LinkedHashMap<>();
    @Getter
    private Map<String, ServerInfo> infos  = new LinkedHashMap<>();
    private List<ServerInfo>        toPing = new Vector<>();


    public void update(ServerConnector currentConnector, boolean complete) {
        synchronized (toAdd) {
            for (String name : toAdd.keySet()) {
                ServerInfo info = toAdd.get(name);
                infos.put(name, info);
                toPing.add(info);
            }
            toAdd.clear();
        }

        if (complete) {
            synchronized (toPing) {
                for (ServerInfo info : toPing) {
                    info.update(currentConnector);
                }
            }
        }
    }


    public void addServerInfo(String name, ServerInfo info) {
        toAdd.put(name, info);
    }

    public ServerInfo getInfo(String name) {
        for (String s : infos.keySet()) {
            if (s.equalsIgnoreCase(name)) {
                return infos.get(s);
            }
        }
        return null;
    }

    public ServerInfo getFirstInfo(BSBuy buy) {
        for (ServerInfo info : infos.values()) {
            for (ConnectedBuyItem item : info.getConnectedBuyItems()) {
                if (item.getShopItem() == buy) {
                    return info;
                }
            }
        }
        return null;
    }


}
