package studio.magemonkey.genesis.api;

import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class InventoryUtil {

    /**
     * In API versions 1.20.6 and earlier, InventoryView is a class.
     * In versions 1.21 and later, it is an interface.
     * This method uses reflection to get the top Inventory object from the
     * InventoryView associated with an InventoryEvent, to avoid runtime errors.
     *
     * @param event The generic InventoryEvent with an InventoryView to inspect.
     * @return The top Inventory object from the event's InventoryView.
     */
    public static Inventory getTopInventory(InventoryEvent event) {
        Object view = event.getView();
        return getTopInventory(view);
    }

    public static Inventory getTopInventory(Object view) {
        try {
            Method getTopInventory = view.getClass().getMethod("getTopInventory");
            getTopInventory.setAccessible(true);
            return (Inventory) getTopInventory.invoke(view);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In API versions 1.20.6 and earlier, InventoryView is a class.
     * In versions 1.21 and later, it is an interface.
     * This method uses reflection to get the bottom Inventory object from the
     * InventoryView associated with an InventoryEvent, to avoid runtime errors.
     *
     * @param event The generic InventoryEvent with an InventoryView to inspect.
     * @return The bottom Inventory object from the event's InventoryView.
     */
    public static Inventory getBottomInventory(InventoryEvent event) {
        try {
            Object view               = event.getView();
            Method getBottomInventory = view.getClass().getMethod("getBottomInventory");
            getBottomInventory.setAccessible(true);
            return (Inventory) getBottomInventory.invoke(view);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In API versions 1.20.6 and earlier, InventoryView is a class.
     * In versions 1.21 and later, it is an interface.
     * This method uses reflection to set the cursor ItemStack in the
     * InventoryView associated with an InventoryEvent, to avoid runtime errors.
     *
     * @param event The generic InventoryEvent with an InventoryView to modify.
     * @param item The ItemStack to set as the cursor in the event's InventoryView.
     */
    public static void setCursor(InventoryEvent event, ItemStack item) {
        try {
            Object view      = event.getView();
            Method setCursor = view.getClass().getMethod("setCursor", ItemStack.class);
            setCursor.setAccessible(true);
            setCursor.invoke(view, item);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setItem(Object view, int slot, ItemStack item) {
        try {
            Method setItem = view.getClass().getMethod("setItem", int.class, ItemStack.class);
            setItem.setAccessible(true);
            setItem.invoke(view, slot, item);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In API versions 1.20.6 and earlier, InventoryView is a class.
     * In versions 1.21 and later, it is an interface.
     * This method uses reflection to get the Inventory object from the
     * InventoryView associated with an InventoryEvent, to avoid runtime errors.
     * The slot parameter is used to determine which Inventory object to return.
     * If the slot is in the top Inventory, the top Inventory is returned.
     * If the slot is in the bottom Inventory, the bottom Inventory is returned.
     * If the slot is not in either Inventory, null is returned.
     *
     * @param event The generic InventoryEvent with an InventoryView to inspect.
     * @param slot The slot index to check in the InventoryView.
     * @return The Inventory object from the event's InventoryView at the specified slot.
     */
    public static Inventory getInventory(InventoryEvent event, int slot) {
        try {
            Object view         = event.getView();
            Method getInventory = view.getClass().getMethod("getInventory", int.class);
            getInventory.setAccessible(true);
            return (Inventory) getInventory.invoke(view, slot);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In API versions 1.20.6 and earlier, InventoryView is a class.
     * In versions 1.21 and later, it is an interface.
     * This method uses reflection to get the ItemStack at the specified slot
     * in the InventoryView associated with an InventoryEvent, to avoid runtime errors.
     * The slot parameter is used to determine which ItemStack to return.
     *
     * @param event The generic InventoryEvent with an InventoryView to inspect.
     * @param slot The slot index to check in the InventoryView.
     * @return The ItemStack from the event's InventoryView at the specified slot.
     */
    public static ItemStack getItem(InventoryEvent event, int slot) {
        try {
            Object view    = event.getView();
            Method getItem = view.getClass().getMethod("getItem", int.class);
            getItem.setAccessible(true);
            return (ItemStack) getItem.invoke(view, slot);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * In API versions 1.20.6 and earlier, InventoryView is a class.
     * In versions 1.21 and later, it is an interface.
     * This method uses reflection to convert the slot index from the
     * InventoryView associated with an InventoryEvent, to avoid runtime errors.
     * The slot parameter is used to determine which slot index to convert.
     *
     * @param event The generic InventoryEvent with an InventoryView to inspect.
     * @param slot The slot index to convert in the InventoryView.
     * @return The converted slot index from the event's InventoryView.
     */
    public static int convertSlot(InventoryEvent event, int slot) {
        try {
            Object view        = event.getView();
            Method convertSlot = view.getClass().getMethod("convertSlot", int.class);
            convertSlot.setAccessible(true);
            return (int) convertSlot.invoke(view, slot);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
