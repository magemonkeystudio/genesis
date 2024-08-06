package studio.magemonkey.genesis.core;

import org.bukkit.entity.Player;


public class GenesisCustomLink {

    private final GenesisCustomActions actions;
    private final int                  id;

    public GenesisCustomLink(int id, GenesisCustomActions actions) {
        this.actions = actions;
        this.id = id;
    }

    public void doAction(Player p) {
        actions.customAction(p, id);
    }


}
