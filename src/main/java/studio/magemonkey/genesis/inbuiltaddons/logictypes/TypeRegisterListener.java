package studio.magemonkey.genesis.inbuiltaddons.logictypes;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import studio.magemonkey.genesis.events.GenesisRegisterTypesEvent;

public class TypeRegisterListener implements Listener {


    @EventHandler
    public void onCreate(GenesisRegisterTypesEvent event) {
        new GenesisPriceTypeAnd().register();
        new GenesisPriceTypeOr().register();
        new GenesisRewardTypeAnd().register();
    }

}
