package org.sobadfish.fishclear.windows;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import org.sobadfish.fishclear.manager.TrashManager;


import java.util.LinkedHashMap;

/**
 * 发送窗口
 * @author SoBadFish
 * 2022/1/2
 */
public class DisPlayerPanel implements InventoryHolder {

    public Player player;

    public TrashManager.TrashInventory  panel;

    public static LinkedHashMap<String,DisPlayerPanel> panelLib = new LinkedHashMap<>();

    private DisPlayerPanel(){
    }





    public static DisPlayerPanel getDisPlayPanel(Player player, TrashManager.TrashInventory inventory){
        DisPlayerPanel panel;
        if(!panelLib.containsKey(player.getName())){
            panelLib.put(player.getName(),new DisPlayerPanel());
        }
        panel = panelLib.get(player.getName());
        panel.player = player;

        panel.panel = inventory;
//        panel.panel.setPage(index);
        return panel;
    }



    public void displayPlayer(){
//        panel.id = ++Entity.entityCount;
//        inventory = panel;
//        panel.inventory.getViewers().g.addWindow(panel);
        player.addWindow(panel.inventory);

    }



    @Override
    public Inventory getInventory() {
        return panel.inventory;
    }


}
