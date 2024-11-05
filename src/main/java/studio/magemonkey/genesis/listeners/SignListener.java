package studio.magemonkey.genesis.listeners;

import lombok.Setter;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.core.GenesisShop;

import java.util.Map;

public class SignListener implements Listener {

    @Setter
    private       boolean signsEnabled;
    private final Genesis plugin;

    public SignListener(boolean signsEnabled, Genesis plugin) {
        this.signsEnabled = signsEnabled;
        this.plugin = plugin;
    }

    private GenesisShop getGenesisSign(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }
        line = line.toLowerCase();
        Map<Integer, GenesisShop> set = plugin.getClassManager().getShops().getShops();

        for (Integer s : set.keySet()) {

            GenesisShop shop     = set.get(s);
            String      signText = shop.getSignText();

            if (signText != null) {
                if (line.endsWith(signText.toLowerCase())) {
                    return shop;
                }
            }


        }

        return null;
    }

    @EventHandler
    public void createSign(SignChangeEvent e) {
        if (!signsEnabled) {
            return;
        }

        final GenesisShop shop = getGenesisSign(e.getLine(0));
        if (shop != null) {

            if (shop.needPermToCreateSign()) {
                if (!e.getPlayer().hasPermission("Genesis.createSign")) {

                    plugin.getClassManager().getMessageHandler().sendMessage("Main.NoPermission", e.getPlayer());
                    e.setCancelled(true);
                    return;
                }
            }

            if (e.getLine(0) != "") {
                e.setLine(0, plugin.getClassManager().getStringManager().transform(e.getLine(0)));
            }
            if (e.getLine(1) != "") {
                e.setLine(1, plugin.getClassManager().getStringManager().transform(e.getLine(1)));
            }
            if (e.getLine(2) != "") {
                e.setLine(2, plugin.getClassManager().getStringManager().transform(e.getLine(2)));
            }
            if (e.getLine(3) != "") {
                e.setLine(3, plugin.getClassManager().getStringManager().transform(e.getLine(3)));
            }
        }
    }


    @EventHandler
    public void interactSign(PlayerInteractEvent e) {
        if (!signsEnabled) {
            return;
        }

        if (e.getClickedBlock() != null) {
            if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {

                Block b = e.getClickedBlock();
                if (b.getType().name().contains("SIGN")) {

                    if (b.getState() instanceof Sign) {
                        Sign s = (Sign) b.getState();

                        GenesisShop shop = getGenesisSign(s.getLine(0));
                        if (shop != null) {
                            e.setCancelled(true);
                            if (e.getPlayer().hasPermission("Genesis.open") || e.getPlayer()
                                    .hasPermission("Genesis.open.sign") || e.getPlayer()
                                    .hasPermission("Genesis.open.sign." + shop.getShopName())) {
                                plugin.getClassManager().getShops().openShop(e.getPlayer(), shop);
                                return;
                            }

                            plugin.getClassManager()
                                    .getMessageHandler()
                                    .sendMessage("Main.NoPermission", e.getPlayer());
                        }

                    }
                }
            }
        }
    }
}
