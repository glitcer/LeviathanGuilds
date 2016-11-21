package me.khalit.projectleviathan.api.gui;

import lombok.Getter;
import me.khalit.projectleviathan.utils.Util;

import java.util.ArrayList;
import java.util.List;

public class GUIHandler {

    @Getter
    private static final List<ActionInventory> inventories = new ArrayList<>();

    public ActionInventory getActionInventory(String title) {
        for (ActionInventory actionInventory : inventories) {
            if (actionInventory.getInventory().getTitle().startsWith(Util.fixColors(title))) {
                return actionInventory;
            }
        }
        return null;
    }
}
