package studio.magemonkey.genesis.managers.item;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.managers.misc.InputReader;

import java.util.List;

public class ItemDataPartAmount extends ItemDataPart {

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        int amount = InputReader.getInt(argument, -1);
        if (amount == -1) {
            ClassManager.manager.getBugFinder()
                    .severe("Mistake in Config: '" + argument + "' is not a valid '" + usedName
                            + "'. It needs to be a number like '1', '12' or '64'.");
            return item;
        }
        item.setAmount(amount);
        return item;
    }

    @Override
    public int getPriority() {
        return PRIORITY_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return true;
    }

    @Override
    public String[] createNames() {
        return new String[]{"amount", "number"};
    }


    @Override
    public List<String> read(ItemStack i, List<String> output) {
        output.add("amount:" + i.getAmount());
        return output;
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        return shopItem.getAmount() == playerItem.getAmount();
    }


}
