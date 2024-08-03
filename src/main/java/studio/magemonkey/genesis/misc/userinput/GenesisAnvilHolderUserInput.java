package studio.magemonkey.genesis.misc.userinput;

import org.bukkit.entity.Player;

public class GenesisAnvilHolderUserInput extends GenesisAnvilHolder {


    private final GenesisUserInput input;

    public GenesisAnvilHolderUserInput(GenesisUserInput input) {
        this.input = input;
    }

    @Override
    public void userClickedResult(Player p) {
        input.receivedInput(p, getOutputText());
    }

}
