package studio.magemonkey.genesis.managers;


import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.core.GenesisShop;
import studio.magemonkey.genesis.managers.features.ShopCreator;
import studio.magemonkey.genesis.managers.item.ItemDataPart;
import studio.magemonkey.genesis.misc.Misc;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String arg, String[] args) {

        if (cmd.getName().equalsIgnoreCase("genesis") || cmd.getName().equalsIgnoreCase("shop") || cmd.getName()
                .equalsIgnoreCase("gen")) {

            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    if (sender.hasPermission("Genesis.reload")) {
                        if (args.length == 2) {
                            if (!ClassManager.manager.getShops().getShopIds().containsKey(args[1].toLowerCase())) {
                                ClassManager.manager.getMessageHandler().sendMessage("Main.ShopNotExisting", sender);
                                return true;
                            }
                            GenesisShop shop = ClassManager.manager.getShops().getShop(args[1].toLowerCase());
                            sender.sendMessage(
                                    ChatColor.YELLOW + String.format("Starting reload of '%s'...", shop.getShopName()));
                            ClassManager.manager.getShops().reloadShop(shop);
                            sender.sendMessage(ChatColor.YELLOW + "Done!");
                        } else {
                            sender.sendMessage(ChatColor.YELLOW + "Starting Genesis reload...");
                            ClassManager.manager.getPlugin().reloadPlugin(sender);
                            sender.sendMessage(ChatColor.YELLOW + "Done!");
                        }

                    } else {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                        return false;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("read")) {
                    if (sender instanceof Player) {
                        if (sender.hasPermission("Genesis.read")) {
                            Player    p    = (Player) sender;
                            ItemStack item = Misc.getItemInMainHand(p);
                            if (item == null || item.getType() == Material.AIR) {
                                ClassManager.manager.getMessageHandler().sendMessage("Main.NeedItemInHand", sender);
                                return false;
                            }
                            List<String> itemdata = ItemDataPart.readItem(item);
                            ClassManager.manager.getItemDataStorage().addItemData(p.getName(), itemdata);
                            ClassManager.manager.getMessageHandler().sendMessage("Main.PrintedItemInfo", sender);
                            for (String line : itemdata) {
                                sender.sendMessage("- " + line);
                            }
                            return true;
                        } else {
                            ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                            return false;
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("simulate")) {
                    if (sender.hasPermission("Genesis.simulate")) {
                        if (args.length == 4) {
                            Player p = Bukkit.getPlayer(args[1]);
                            if (p == null) {
                                ClassManager.manager.getMessageHandler()
                                        .sendMessage("Main.PlayerNotFound", sender, args[1]);
                                return false;
                            }

                            GenesisShop shop = ClassManager.manager.getShops().getShop(args[2]);
                            if (shop == null) {
                                ClassManager.manager.getMessageHandler()
                                        .sendMessage("Main.ShopNotExisting", sender, null, p, null, null, null);
                                return false;
                            }

                            GenesisBuy buy = shop.getItem(args[3]);
                            if (buy == null) {
                                ClassManager.manager.getMessageHandler()
                                        .sendMessage("Main.ShopItemNotExisting", sender, null, p, shop, null, null);
                                return false;
                            }

                            buy.click(p, shop, null, null, null, ClassManager.manager.getPlugin());
                            return true;
                        }
                        sendCommandList(sender);
                        return false;
                    }
                }

                if (args[0].equalsIgnoreCase("close")) {
                    if (sender.hasPermission("Genesis.close")) {
                        Player p    = null;
                        String name = sender instanceof Player ? sender.getName() : "CONSOLE";

                        if (sender instanceof Player) {
                            p = (Player) sender;
                        }
                        if (args.length >= 2) {
                            name = args[1];
                            p = Bukkit.getPlayer(name);
                        }

                        if (p == null) {
                            ClassManager.manager.getMessageHandler().sendMessage("Main.PlayerNotFound", sender, name);
                            return false;
                        }

                        p.closeInventory();
                        if (p != sender) {
                            ClassManager.manager.getMessageHandler()
                                    .sendMessage("Main.CloseShopOtherPlayer", sender, p);
                        }

                    } else {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                        return false;
                    }
                    return true;
                }

                if (args[0].equalsIgnoreCase("create") & args.length == 3) {
                    if (sender.hasPermission("Genesis.create")) {
                        Player p;
                        if (sender instanceof Player) {
                            p = (Player) sender;
                        } else {
                            return false;
                        }
                        ShopCreator sc = new ShopCreator(ClassManager.manager.getPlugin(),
                                ClassManager.manager.getMessageHandler());
                        //replace !sp! as space
                        String shopTitle = args[2].replaceAll("!sp!", " ");
                        sc.startCreate(p, args[1], shopTitle);
                        return true;
                    } else {
                        ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                    }
                    return false;
                }

                if (args.length >= 3 && args[0].equalsIgnoreCase("open")) {
                    String      shopName = args[1].toLowerCase();
                    GenesisShop shop     = ClassManager.manager.getShops().getShop(shopName);
                    String      name     = args[2];
                    Player      p        = Bukkit.getPlayerExact(name);
                    String      argument = args.length > 3 ? args[3] : null;

                    if (p == null) {
                        p = Bukkit.getPlayer(name);
                    }

                    if (p == null) {
                        ClassManager.manager.getMessageHandler()
                                .sendMessage("Main.PlayerNotFound", sender, name, null, shop, null, null);
                        return false;
                    }

                    if (shop == null) {
                        ClassManager.manager.getMessageHandler()
                                .sendMessage("Main.ShopNotExisting", sender, null, p, null, null, null);
                        return false;
                    }

                    playerCommandOpenShop(sender, p, shopName, argument);
                    if (p != sender) {
                        ClassManager.manager.getMessageHandler()
                                .sendMessage("Main.OpenShopOtherPlayer", sender, null, p, shop, null, null);
                    }

                    return true;
                }

            }

            if (sender instanceof Player) {
                Player p = (Player) sender;

                String shop = ClassManager.manager.getSettings().getMainShop();
                if (args.length != 0) {
                    shop = args[0].toLowerCase();
                }
                String argument = args.length > 1 ? args[1] : null;
                playerCommandOpenShop(sender, p, shop, argument);
                return true;
            }
            sendCommandList(sender);
            return false;
        }

        return false;
    }


    private void playerCommandOpenShop(CommandSender sender, Player target, String shop, String argument) {
        if (sender == target) {
            if (!(sender.hasPermission("Genesis.open") || sender.hasPermission("Genesis.open.command")
                    || sender.hasPermission("Genesis.open.command." + shop))) {
                ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                return;
            }
        } else {
            if (!sender.hasPermission("Genesis.open.other")) {
                ClassManager.manager.getMessageHandler().sendMessage("Main.NoPermission", sender);
                return;
            }
        }
        if (argument != null) {
            ClassManager.manager.getPlayerDataHandler().enteredInput(target, argument);
        }
        if (ClassManager.manager == null) {
            return;
        }
        if (ClassManager.manager.getShops() == null) {
            return;
        }
        ClassManager.manager.getShops().openShop(target, shop);
    }

    private void sendCommandList(CommandSender s) {
        MessageHandler mh = ClassManager.manager.getMessageHandler();
        mh.sendMessage("Command.Help1", s);
        mh.sendMessage("Command.Help2", s);
        mh.sendMessage("Command.Help3", s);
        mh.sendMessage("Command.Help4", s);
        mh.sendMessage("Command.Help5", s);
        mh.sendMessage("Command.Help6", s);
        if (s instanceof Player) {
            mh.sendMessage("Command.Help7", s);
            mh.sendMessage("Command.Help8", s);
        }
        mh.sendMessage("Command.Help", s);
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender,
                                      @NotNull Command command,
                                      @NotNull String label,
                                      @NotNull String[] args) {
        List<String> arglist = new ArrayList<>();
        if (args.length == 1) {
            if (sender.hasPermission("Genesis.open.command") || sender.hasPermission("Genesis.open")) {
                arglist.add("open");
                if (sender instanceof Player) {
                    arglist.add("<shop>");
                }
            }
            arglist.add("help");
            if (sender.hasPermission("Genesis.close")) {
                arglist.add("close");
            }
            if (sender instanceof Player) {
                if (sender.hasPermission("Genesis.read")) {
                    arglist.add("read");
                }
                if (sender.hasPermission("Genesis.create")) {
                    arglist.add("create");
                }
            }
            if (sender.hasPermission("Genesis.reload")) {
                arglist.add("reload");
            }
            if (sender.hasPermission("BossShop.simulate")) {
                arglist.add("simulate");
            }
        } else if (args.length == 2) {
            if ((args[0].equalsIgnoreCase("open") && (sender.hasPermission("BossShop.open.command")
                    || sender.hasPermission("BossShop.open"))) ||
                    (args[0].equalsIgnoreCase("reload") && sender.hasPermission("BossShop.reload"))) {
                for (GenesisShop shop : ClassManager.manager.getShops().getShops().values()) {
                    if (shop.getShopName().toLowerCase().startsWith(args[1].toLowerCase()))
                        arglist.add(shop.getShopName());
                }
            } else if (args[0].equalsIgnoreCase("close") && sender.hasPermission("BossShop.close")) {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getName().toLowerCase().startsWith(args[1].toLowerCase())) arglist.add(p.getName());
                }
            }
        }
        return arglist;
    }
}
