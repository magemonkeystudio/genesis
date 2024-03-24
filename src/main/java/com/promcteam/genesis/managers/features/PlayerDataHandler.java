package com.promcteam.genesis.managers.features;

import com.promcteam.genesis.core.GenesisShop;
import com.promcteam.genesis.misc.userinput.GenesisChatUserInput;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerDataHandler {

    private final HashMap<Player, GenesisShop>          lastShop     = new HashMap<>();
    private final HashMap<Player, String>               input        = new HashMap<>();
    private final HashMap<Player, GenesisChatUserInput> inputWaiting = new HashMap<>();

    public void openedShop(Player p, GenesisShop shop) {
        this.lastShop.put(p, shop);
    }

    /*
     * TODO:
     * - Add new input placeholder to the string manager
     * - Add an optional "input: <type>" property to shopitems which will cause input to be requested and the item being bought after the input is defined
     */

    public void requestInput(Player p, GenesisChatUserInput input) {
        this.inputWaiting.put(p, input);
    }

    public void removeInputRequest(Player p) {
        this.inputWaiting.remove(p);
    }

    public void enteredInput(Player p, String input) {
        this.input.put(p, input);
    }


    public void leftServer(Player p) {
        lastShop.remove(p);
        input.remove(p);
        inputWaiting.remove(p);
    }


    public String getInput(Player p) {
        if (input.containsKey(p)) {
            return input.get(p);
        }
        return "Player did not have the option to enter input! Shop seems to not be set up correctly.";
    }

    public GenesisChatUserInput getInputRequest(Player p) {
        return inputWaiting.get(p);
    }

    public GenesisShop getLastShop(Player p) {
        return lastShop.get(p);
    }

}
