package org.sobadfish.fishclear.listener;

import cn.nukkit.Player;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.entity.ItemSpawnEvent;
import cn.nukkit.event.inventory.InventoryTransactionEvent;
import cn.nukkit.event.player.PlayerDropItemEvent;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.inventory.transaction.InventoryTransaction;
import cn.nukkit.inventory.transaction.action.InventoryAction;
import cn.nukkit.item.Item;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.entity.FakeEntityItem;
import org.sobadfish.fishclear.windows.items.BasePlayPanelItemInstance;
import org.sobadfish.fishclear.windows.lib.ChestInventoryPanel;

import java.lang.reflect.Field;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class ClearListener implements Listener {

    @EventHandler
    public void onDropEvent(ItemSpawnEvent event){
        if(ClearMainClass.otherSettingControl.itemTiming.enable) {
            EntityItem entityItem = event.getEntity();
            if (entityItem instanceof FakeEntityItem) {
                return;
            }
            entityItem.close();
            FakeEntityItem fakeEntityItem = new FakeEntityItem(entityItem.chunk, entityItem.namedTag);
            fakeEntityItem.deleteTime = ClearMainClass.otherSettingControl.itemTiming.time;
            fakeEntityItem.setNameTagAlwaysVisible(true);
            fakeEntityItem.setNameTagVisible(true);
            fakeEntityItem.spawnToAll();
        }

    }

    @EventHandler
    public void onPlayerDrop(PlayerDropItemEvent event){

    }

    @EventHandler
    public void onItemChange(InventoryTransactionEvent event) {
        ChestInventoryPanel chest = null;
        InventoryTransaction transaction = event.getTransaction();
        for (InventoryAction action : transaction.getActions()) {
            for (Inventory inventory : transaction.getInventories()) {
                if (inventory instanceof ChestInventoryPanel) {
                    chest = (ChestInventoryPanel) inventory;
                    Player player = ((ChestInventoryPanel) inventory).getPlayer();
                    Item i = action.getSourceItem();
                    if(i.hasCompoundTag()){
                        if(i.getNamedTag().contains("index") && i.getNamedTag().contains("button")){
                            event.setCancelled();
                            int index = i.getNamedTag().getInt("index");
                            BasePlayPanelItemInstance item = ((ChestInventoryPanel) inventory).getPanel().getOrDefault(index,null);

                            if(item != null){
                                ((ChestInventoryPanel) inventory).clickSolt = index;
                                item.onClick((ChestInventoryPanel) inventory,player);
//                                ((ChestInventoryPanel) inventory).update();
                            }
                        }

                    }

                }
//                if(inventory instanceof PlayerInventory){
//                    EntityHuman player =((PlayerInventory) inventory).getHolder();
//                    if(chest != null){
//                        if(player instanceof Player) {
//                            chest.onUpdate((Player) player);
//                        }
//                    }
//
//                }
            }
        }
    }

}
