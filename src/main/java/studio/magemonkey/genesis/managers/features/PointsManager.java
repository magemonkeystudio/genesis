package studio.magemonkey.genesis.managers.features;

import studio.magemonkey.genesis.Genesis;
import studio.magemonkey.genesis.managers.ClassManager;
import studio.magemonkey.genesis.pointsystem.*;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import studio.magemonkey.genesis.pointsystem.*;

public class PointsManager {

    private GenesisPointsPlugin pa;

    public PointsManager() {
        this(ClassManager.manager.getSettings().getPointsPlugin());
    }


    public PointsManager(PointsPlugin p) {
        if (p == null) {
            this.pa = new GenesisPointsPluginFailed();
            return;
        }

        if (p != PointsPlugin.NONE) {
            if (Bukkit.getPluginManager().getPlugin(p.getPluginName()) == null) {
                ClassManager.manager.getBugFinder()
                        .severe("You defined " + p.getPluginName()
                                + " as Points Plugin... BUT IT WAS NOT FOUND?! Please install it or use an alternative like PlayerPoints (https://www.spigotmc.org/resources/playerpoints.80745/). If you want "
                                + Genesis.NAME
                                + " to auto-detect your Points plugin simply set 'PointsPlugin: auto-detect'.");
                this.pa = new GenesisPointsPluginFailed();
                return;
            }
        }

        switch (p) {
            case PLAYERPOINTS:
                this.pa = new GenesisPointsPluginPlayerPoints();
                break;

            case TOKENENCHANT:
                this.pa = new GenesisPointsPluginTokenEnchant();
                break;

            case Jobs:
                this.pa = new GenesisPointsPluginJobs();
                break;

            case KINGDOMS:
                this.pa = new GenesisPointsPluginKingdoms();
                break;

            case VOTINGPLUGIN:
                this.pa = new GenesisPointsPluginVotingPlugin();
                break;

            case GadetsMenu:
                this.pa = new GenesisPointsPluginGadgetsMenu();
                break;

            case Gringotts:
                this.pa = new GenesisPointsPluginGringotts();
                break;

            case NONE:
                this.pa = new GenesisPointsPluginNone();
                break;

            case CUSTOM:
                GenesisPointsPlugin customPoints = GenesisPointsAPI.get(p.getCustom());
                if (customPoints != null) {
                    this.pa = customPoints;
                }
                break;
        }

        if (this.pa == null) {
            ClassManager.manager.getBugFinder()
                    .warn("No PointsPlugin was found... You need one if you want Genesis to work with Points! Get PlayerPoints here: http://dev.bukkit.org/server-mods/playerpoints/");
            this.pa = new GenesisPointsPluginFailed();
        } else {
            Genesis.log("Successfully hooked into Points plugin " + this.pa.getName() + ".");
        }
    }

    public double getPoints(OfflinePlayer player) {
        return pa.getPoints(player);
    }

    public double setPoints(OfflinePlayer player, double points) {
        return pa.setPoints(player, points);
    }

    public double givePoints(OfflinePlayer player, double points) {
        return pa.givePoints(player, points);
    }

    public double takePoints(OfflinePlayer player, double points) {
        return pa.takePoints(player, points);
    }

    public boolean usesDoubleValues() {
        return pa.usesDoubleValues();
    }

    public enum PointsPlugin {
        NONE(new String[]{"none", "nothing"}),
        PLAYERPOINTS(new String[]{"PlayerPoints", "PlayerPoint", "PP"}),
        ENJIN_MINECRAFT_PLUGIN(new String[]{"EnjinMinecraftPlugin", "Enjin", "EMP"}),
        TOKENENCHANT(new String[]{"TokenEnchant", "TE", "TokenEnchants"}),
        Jobs(new String[]{"Jobs", "JobsReborn"}),
        KINGDOMS(new String[]{"Kingdoms", "Kingdom"}),
        VOTINGPLUGIN(new String[]{"VotingPlugin", "VP"}),
        GadetsMenu(new String[]{"GadgetsMenu"}),
        Gringotts(new String[]{"Gringotts"}),
        CUSTOM(new String[0]);

        @Getter
        private final String[] nicknames;
        private       String   customName;

        PointsPlugin(String[] nicknames) {
            this.nicknames = nicknames;
        }

        public String getCustom() {
            return this.customName;
        }

        public void setCustom(String customName) {
            this.customName = customName;
        }

        public String getPluginName() {
            if (getNicknames() == null) {
                return customName;
            }
            if (getNicknames().length == 0) {
                return customName;
            }
            return getNicknames()[0];
        }
    }

}
