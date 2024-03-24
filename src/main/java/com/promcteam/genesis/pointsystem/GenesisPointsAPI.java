package com.promcteam.genesis.pointsystem;

import java.util.LinkedHashMap;

public class GenesisPointsAPI {
    private static final LinkedHashMap<String, GenesisPointsPlugin> interfaces = new LinkedHashMap<>();

    /**
     * Register a points plugin
     *
     * @param points the points to register
     */
    public static void register(GenesisPointsPlugin points) {
        interfaces.put(points.getName(), points);
    }

    /**
     * Get a points plugin object
     *
     * @param name name of points plugin
     * @return points plugin
     */
    public static GenesisPointsPlugin get(String name) {
        if (name != null) {
            GenesisPointsPlugin p = interfaces.get(name);
            if (p != null) {
                return p;
            }
            for (GenesisPointsPlugin api : interfaces.values()) {
                if (api.getName().equalsIgnoreCase(name)) {
                    return api;
                }
            }
            for (GenesisPointsPlugin api : interfaces.values()) {
                for (String nickname : api.getNicknames()) {
                    if (nickname.equalsIgnoreCase(name)) {
                        return api;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get the first available points plugin
     *
     * @return points plugin
     */
    public static GenesisPointsPlugin getFirstAvailable() {
        for (GenesisPointsPlugin api : interfaces.values()) {
            if (api.isAvailable()) {
                return api;
            }
        }
        return null;
    }
}
