package org.sobadfish.fishclear.listener;

import cn.nukkit.Player;
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
import cn.nukkit.inventory.transaction.action.SlotChangeAction;
import cn.nukkit.item.Item;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.config.OtherSettingControl;
import org.sobadfish.fishclear.entity.FakeEntityItem;
import org.sobadfish.fishclear.windows.items.BasePlayPanelItemInstance;
import org.sobadfish.fishclear.windows.lib.ChestInventoryPanel;

import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

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

    /**
     * 用于缓存玩家从箱子中获取的物品
     */
    private static final Cache<String, ObjectList<Item>> PLAYER_GET_ITEM_CACHE = Caffeine.newBuilder()
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build();

    @EventHandler
    public void onItemChange(InventoryTransactionEvent event) {
        //预计做翻页
        InventoryTransaction transaction = event.getTransaction();
        Player player = transaction.getSource();
        Item getFromChest = null;
        Item putToChest = null;
        Item putToPlayer = null;
        ChestInventoryPanel chestInventoryPanel = null;
        for (InventoryAction action : transaction.getActions()) {
            for (Inventory inventory : transaction.getInventories()) {
                if (inventory instanceof ChestInventoryPanel panel) {
                    chestInventoryPanel = panel;
                    Item i = action.getSourceItem();
                    if (i.hasCompoundTag()) {
                        if (i.getNamedTag().contains("index") && i.getNamedTag().contains("button")) {
                            event.setCancelled();
                            int index = i.getNamedTag().getInt("index");
                            BasePlayPanelItemInstance item = panel.getPanel().getOrDefault(index, null);
                            if (item != null) {
                                panel.clickSolt = index;
                                item.onClick(panel, player);
                                panel.update();
                            }
                        }

                    } else if (action instanceof SlotChangeAction slotChangeAction) {
                        if (slotChangeAction.getInventory() instanceof ChestInventoryPanel) {
                            if (!i.isNull() && action.getTargetItem().isNull()) {
                                getFromChest = i.clone();
                            } else if (i.isNull() && !action.getTargetItem().isNull()) {
                                putToChest = action.getTargetItem().clone();
                            }
                        }
                    }

                } else if (action instanceof SlotChangeAction slotChangeAction) {
                    if (slotChangeAction.getInventory() instanceof PlayerInventory
                            && action.getSourceItem().isNull()
                            && !action.getTargetItem().isNull()) {
                        putToPlayer = action.getTargetItem().clone();
                    }
                }
            }
        }
        ObjectList<Item> getItems = PLAYER_GET_ITEM_CACHE.get(player.getName(), (k) -> new ObjectArrayList<>());
        if (getFromChest != null && !getFromChest.isNull()) {
            getItems.add(getFromChest);
        }
        if (putToChest != null && !putToChest.isNull()) {
            getItems.remove(putToChest);
        }
        Optional<Inventory> topWindow = transaction.getSource().getTopWindow();
        if (getItems.contains(putToPlayer) && topWindow.isPresent() && topWindow.get() instanceof ChestInventoryPanel) {
            getItems.remove(putToPlayer);
            if (ThreadLocalRandom.current().nextInt(100) < 5) {
                player.awardAchievement("FishClear_WIS");
            }
            player.awardAchievement("FishClear_TFF");
        }
    }

}
