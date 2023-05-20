package dev.efnilite.vilib.serialization;

import java.util.Map;
import java.util.stream.Collectors;

public class InventorySerializer {

    public static PlayerInventory deserialize64(Map<Integer, String> map) {
        PlayerInventory inventory = new PlayerInventory();
        map.keySet().forEach(slot -> inventory.add(slot, ObjectSerializer.deserialize64(map.get(slot))));
        return inventory;
    }

    public static Map<Integer, String> serialize64(PlayerInventory inventory) {
        return inventory.getItems().keySet().stream()
                .mapToInt(slot -> slot)
                .boxed()
                .collect(Collectors.toMap(slot -> slot, slot -> ObjectSerializer.serialize64(inventory.get(slot)), (a, b) -> b));
    }
}
