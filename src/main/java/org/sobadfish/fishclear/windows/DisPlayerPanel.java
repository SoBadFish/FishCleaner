package org.sobadfish.fishclear.windows;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import org.sobadfish.fishclear.windows.lib.AbstractFakeInventory;
import org.sobadfish.fishclear.windows.lib.ChestInventoryPanel;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;

/**
 * 发送窗口
 * @author SoBadFish
 * 2022/1/2
 */
public class DisPlayerPanel implements InventoryHolder {

    private AbstractFakeInventory inventory;

    public ChestInventoryPanel panel;

    public static LinkedHashMap<Player,DisPlayerPanel> panelLib = new LinkedHashMap<>();

    private DisPlayerPanel(){
    }



    public static DisPlayerPanel getDisPlayPanel(Player player,String name,Class<? extends ChestInventoryPanel> tClass){
        try {
            DisPlayerPanel panel;
            if(!panelLib.containsKey(player)){
                panelLib.put(player,new DisPlayerPanel());
            }
            panel = panelLib.get(player);

            Constructor<?> tConstructor = tClass.getConstructor(Player.class,InventoryHolder.class,String.class);
            panel.panel = (ChestInventoryPanel) tConstructor.newInstance(player,panel,name);
            return panel;
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }



    public void displayPlayer(){
        panel.id = ++Entity.entityCount;
        inventory = panel;
        panel.getPlayer().addWindow(panel);

    }



    @Override
    public Inventory getInventory() {
        return inventory;
    }


}
