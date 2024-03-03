package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.misc.userinput.BSChatUserInput;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerDataHandler {

    private HashMap<Player, BSShop>          lastShop     = new HashMap<>();
    private HashMap<Player, String>          input        = new HashMap<>();
    private HashMap<Player, BSChatUserInput> inputWaiting = new HashMap<>();

    public void openedShop(Player p, BSShop shop) {
        this.lastShop.put(p, shop);
    }

    /*
     * TODO:
     * - Add new input placeholder to the string manager
     * - Add an optional "input: <type>" property to shopitems which will cause input to be requested and the item being bought after the input is defined
     */

    public void requestInput(Player p, BSChatUserInput input) {
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

    public BSChatUserInput getInputRequest(Player p) {
        return inputWaiting.get(p);
    }

    public BSShop getLastShop(Player p) {
        return lastShop.get(p);
    }

}
