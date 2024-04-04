package studio.magemonkey.genesis.inbuiltaddons.logictypes;

import studio.magemonkey.genesis.events.GenesisRegisterTypesEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TypeRegisterListener implements Listener {


    @EventHandler
    public void onCreate(GenesisRegisterTypesEvent event) {
        new GenesisPriceTypeAnd().register();
        new GenesisPriceTypeOr().register();
        new GenesisRewardTypeAnd().register();
    }

}
