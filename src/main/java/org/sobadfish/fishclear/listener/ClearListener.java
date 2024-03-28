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
import org.sobadfish.fishclear.config.OtherSettingControl;
import org.sobadfish.fishclear.entity.FakeEntityItem;
import org.sobadfish.fishclear.manager.TrashManager;
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
        //掉落物倒计时
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
        Player player = event.getPlayer();
        OtherSettingControl.DropType dropType = ClearMainClass.otherSettingControl.getDropTypeByWorldName(player.level.getFolderName());
        switch (dropType){
            case CMD:
                boolean drop = false;
                if(ClearMainClass.otherSettingControl.playerDropSetting.containsKey(player.getName())){
                    drop = ClearMainClass.otherSettingControl.playerDropSetting.get(player.getName());
                }
                if(!drop){
                    event.setCancelled();
                    player.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&c此世界禁止丢弃物品"));
                    player.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&e执行 &a/"+ClearMainClass.PLUGIN_CMD+" drop &e开启"));
                }
                break;
            case FORCE:
                event.setCancelled();
                player.sendTip(ClearMainClass.TITLE+ClearMainClass.formatString("&c此世界强制禁止丢弃物品"));
                break;
            default:
                break;

        }
        if(!event.isCancelled()){
            if(ClearMainClass.otherSettingControl.addTrash){
                event.setCancelled();
                player.getInventory().removeItem(event.getItem());
                ClearMainClass.trashManager.addItem(event.getItem());

            }
        }


    }

    @EventHandler
    public void onItemChange(InventoryTransactionEvent event) {
        //预计做翻页
        InventoryTransaction transaction = event.getTransaction();
        for (InventoryAction action : transaction.getActions()) {
            for (Inventory inventory : transaction.getInventories()) {
                if (inventory instanceof ChestInventoryPanel) {
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
                                ((ChestInventoryPanel) inventory).update();
                            }
                        }

                    }

                }
            }
        }
    }

}
