package studio.magemonkey.genesis.managers.item;

import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;
import de.tr7zw.changeme.nbtapi.NBTItem;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public class ItemDataPartNBTTag extends ItemDataPart {
    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        NBTItem  i     = new NBTItem(item);
        String[] parts = argument.split("#");
        switch (parts[0].toUpperCase()) {
            case "INT":
                i.setInteger(parts[1], Integer.valueOf(parts[2]));
                break;
            case "FLOAT":
                i.setFloat(parts[1], Float.valueOf(parts[2]));
                break;
            case "DOUBLE":
                i.setDouble(parts[1], Double.valueOf(parts[2]));
                break;
            case "STRING":
                i.setString(parts[1], parts[2]);
                break;
            case "BOOLEAN":
                i.setBoolean(parts[1], Boolean.valueOf(parts[2]));
                break;
            case "BYTE":
                i.setByte(parts[1], Byte.valueOf(parts[2]));
                break;
            case "LONG":
                i.setLong(parts[1], Long.valueOf(parts[2]));
                break;
            case "SHORT":
                i.setShort(parts[1], Short.valueOf(parts[2]));
                break;
            case "UUID":
                i.setUUID(parts[1], UUID.fromString(parts[2]));
                break;
            case "OBJ":
            case "OBJECT":
                i.setObject(parts[1], parts[2]);
                break;
            default:
                ClassManager.manager.getBugFinder().severe("Mistake in Config: '" + parts[0] +
                        "': Wrong data type. The currently supported data types are INT, LONG, STRING, FLOAT, BOOLEAN, DOUBLE, BYTE, SHORT, UUID and OBJECT(OBJ)");
                return item;
        }
        return i.getItem();
    }

    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        return true;
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        NBTItem i2 = new NBTItem(i);
        if (i2.hasNBTData()) {
            for (String key : i2.getKeys()) {
                Object value = i2.getObject(key, Object.class);
                output.add("nbttag:OBJ#" + key + "#" + value.toString());
            }
        }
        return output;
    }

    @Override
    public int getPriority() {
        return PRIORITY_EARLY;
    }

    @Override
    public boolean removeSpaces() {
        return false;
    }

    @Override
    public String[] createNames() {
        return new String[]{"nbttag"};
    }
}
