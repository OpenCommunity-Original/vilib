package dev.efnilite.vilib.util;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

/**
 * An utilities class for SuperItems
 */
public class PersistentUtil {

    /**
     * Avoid new instances
     */
    private PersistentUtil() { }

    /**
     * Returns the value of a specific key.
     *
     * @param plugin The plugin.
     * @param itemStack The item.
     * @param key The key.
     * @param type The type.
     * @param <T> The return type.
     * @return The data.
     */
    @Nullable
    public static <T> T getPersistentData(Plugin plugin, ItemStack itemStack, String key, PersistentDataType<T, T> type) {
        if (itemStack.getType() == Material.AIR) {
            return null;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        return container.get(namespacedKey, type);
    }

    /**
     * Checks if an itemstack has persistentdata
     *
     * @param itemStack The itemstack
     * @param key       The key
     * @param type      The PersistentData type
     * @param <T>       The type of value
     * @return true if it has the data
     */
    public static <T> boolean hasPersistentData(Plugin plugin, ItemStack itemStack, String key, PersistentDataType<T, T> type) {
        if (itemStack.getType() == Material.AIR) {
            return false;
        }
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        return container.has(namespacedKey, type);
    }

    /**
     * Sets the persistent data of an itemstack
     *
     * @param itemStack The itemstack
     * @param key       The key
     * @param type      The PersistentData type
     * @param t         The value
     * @param <T>       The type of value
     */
    public static <T> void setPersistentData(Plugin plugin, ItemStack itemStack, String key, PersistentDataType<T, T> type, T t) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        container.set(namespacedKey, type, t);
        itemStack.setItemMeta(meta);
    }
}
