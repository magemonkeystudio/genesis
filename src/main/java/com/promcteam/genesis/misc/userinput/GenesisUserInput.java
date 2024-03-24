package com.promcteam.genesis.misc.userinput;

import com.promcteam.genesis.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class GenesisUserInput {


    /**
     * Get user input from anvil
     *
     * @param p         the player to check
     * @param anvilText the text from anvil
     * @param anvilItem the item from anvil
     * @param chatText  the chat text
     */
    public void getUserInput(Player p,
                             String anvilText,
                             ItemStack anvilItem,
                             String chatText) { // Might not receive input for sure
        if (supportsAnvils()) {
            AnvilTools.openAnvilGui(anvilText,
                    anvilItem,
                    new GenesisAnvilHolderUserInput(this),
                    p); // Does not work atm
            return;
        }
        ClassManager.manager.getPlayerDataHandler()
                .requestInput(p,
                        new GenesisChatUserInput(p,
                                this,
                                ClassManager.manager.getSettings().getInputTimeout() * 1000L));
        ClassManager.manager.getMessageHandler()
                .sendMessageDirect(ClassManager.manager.getStringManager().transform(chatText, p), p);
        p.closeInventory();
    }

    public abstract void receivedInput(Player p, String text);


    public boolean supportsAnvils() {
        return false; // Anvils are currently not working & when they are check for server version here
    }

}
