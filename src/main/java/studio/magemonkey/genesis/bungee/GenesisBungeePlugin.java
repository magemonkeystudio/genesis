package studio.magemonkey.genesis.bungee;


import net.md_5.bungee.api.plugin.Plugin;

public class GenesisBungeePlugin extends Plugin {


    @Override
    public void onEnable() {
        getProxy().getPluginManager().registerListener(this, new GenesisBungeeListener(this));
    }
}
