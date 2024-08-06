package studio.magemonkey.genesis.events;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

public abstract class GenesisEvent extends Event {

    public GenesisEvent() {
        super(!Bukkit.getServer().isPrimaryThread());
    }
}
