package studio.magemonkey.genesis.managers.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import studio.magemonkey.genesis.core.GenesisBuy;
import studio.magemonkey.genesis.managers.ClassManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class ItemDataPartCustomSkull extends ItemDataPart {

    public static ItemStack transformSkull(ItemStack i, String input) {
        if (input == null || input.isEmpty()) {
            return i;
        }

        SkullMeta skullMeta = (SkullMeta) i.getItemMeta();
        if (skullMeta == null) return i;
        if (input.contains("http://textures.minecraft.net/texture")) {
            try {
                PlayerProfile pprofile = Bukkit.createPlayerProfile(UUID.randomUUID());
                pprofile.getTextures().setSkin(new URL(input));
                skullMeta.setOwnerProfile(pprofile);
                i.setItemMeta(skullMeta);
                return i;
            } catch (MalformedURLException e) {
                throw new RuntimeException("Could not convert url to texture: " + input, e);
            }
        }

        UUID id = UUID.randomUUID();
        try {
            PlayerProfile playerProfile = Bukkit.createPlayerProfile(id);
            String        decoded       = new String(Base64.getDecoder().decode(input));
            // Construct the json object
            JsonObject json = new Gson().fromJson(decoded, JsonObject.class);
            // Get the textures object
            JsonObject texturesJson = json.getAsJsonObject("textures");
            // Get the skin object
            JsonObject skin = texturesJson.getAsJsonObject("SKIN");
            // Get the url
            String url = skin.get("url").getAsString();
            playerProfile.getTextures().setSkin(new URL(url));
            skullMeta.setOwnerProfile(playerProfile);
        } catch (MalformedURLException | NoClassDefFoundError | NoSuchMethodError | IllegalArgumentException e) {
            try {
                String      cleaned = id.toString().replace("-", "");
                GameProfile profile = new GameProfile(id, cleaned.substring(0, Math.min(cleaned.length(), 16)));
                profile.getProperties().put("textures", getProperty(input));
                Field profileField = skullMeta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                profileField.set(skullMeta, profile);
            } catch (NoSuchFieldException | SecurityException | IllegalAccessException e1) {
                ClassManager.manager.getBugFinder().warn("Could not set profile texture.");
                e1.printStackTrace();
            }
        }
        i.setItemMeta(skullMeta);
        return i;
    }

    private static Property getProperty(String texture) {
        return new Property("textures", texture);
    }

    public static String readSkullTexture(ItemStack i) {
        if (i.getType() == Material.PLAYER_HEAD) {
            SkullMeta meta = (SkullMeta) i.getItemMeta();
            if (meta == null) return null;

            try {
                PlayerProfile profile = meta.getOwnerProfile();
                if(profile == null) return null;
                if (profile.getTextures().getSkin() == null) return null;

               return profile.getTextures().getSkin().toString();
            } catch (NoSuchMethodError | Exception ex) {
                try {
                Field profileField = meta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);

                    GameProfile profile = (GameProfile) profileField.get(meta);
                    if (profile != null) {
                        if (profile.getProperties() != null) {
                            Collection<Property> properties = profile.getProperties().get("textures");
                            if (properties != null) {
                                Iterator<Property> iterator = properties.iterator();
                                if (iterator.hasNext()) {
                                    Property property = iterator.next();
                                    try {
                                        // We'll try to call getValue just in case the property is not a record
                                        Method getValueMethod = property.getClass().getDeclaredMethod("getValue");
                                        getValueMethod.setAccessible(true);
                                        return (String) getValueMethod.invoke(property);
                                    } catch (Exception e) {
                                        // If the property is a record...
                                        Method valueMethod = property.getClass().getDeclaredMethod("value");
                                        valueMethod.setAccessible(true);
                                        return (String) valueMethod.invoke(property);
                                    }
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack transform(ItemStack item, String usedName, String argument) {
        if (!(item.getItemMeta() instanceof SkullMeta)) {
            ClassManager.manager.getBugFinder()
                    .warn("Mistake in Config: Itemdata of type '" + usedName + "' with value '" + argument
                            + "' can not be added to an item with material '" + item.getType().name()
                            + "'. Don't worry I'll automatically transform the material into '" + Material.PLAYER_HEAD
                            + ".");
            item.setType(Material.PLAYER_HEAD);
        }
        item = transformSkull(item, argument);
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
        return new String[]{"customskull", "skull"};
    }

    @Override
    public List<String> read(ItemStack i, List<String> output) {
        String skulltexture = readSkullTexture(i);
        if (skulltexture != null) {
            output.add("customskull:" + skulltexture);
        }
        return output;
    }


    @Override
    public boolean isSimilar(ItemStack shopItem, ItemStack playerItem, GenesisBuy buy, Player p) {
        return true; //Custom skull textures do not matter
    }


}
