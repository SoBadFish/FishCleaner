package org.sobadfish.fishclear.manager;


import cn.nukkit.Player;
import cn.nukkit.item.Item;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.windows.DisPlayerPanel;
import org.sobadfish.fishclear.windows.lib.ChestInventoryPanel;
import org.sobadfish.fishclear.windows.lib.DoubleChestFakeInventory;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class TrashManager {

    public ArrayList<TrashInventory> trashInventories = new ArrayList<>();

    public static void clear(int index){
        ClearMainClass.trashManager.trashInventories.get(index).inventory.clearAll();
        for(Map.Entry<Player, DisPlayerPanel> pe: DisPlayerPanel.panelLib.entrySet()){
            ChestInventoryPanel pc = pe.getValue().panel;
            pc.slots.putAll(ClearMainClass.trashManager.trashInventories.get(index).getSlot());
            pc.sendContents(pe.getKey());

        }
    }

    public static void clearAll(){
        for (int i = 0;i < ClearMainClass.trashManager.trashInventories.size();i++){
            clear(i);
        }
    }

    public TrashInventory get(int index){
        if(trashInventories.size() > index){
            return trashInventories.get(index);
        }
        return null;
    }

    public TrashManager(int size){
        for(int i = 0;i < size;i++){
            trashInventories.add(new TrashInventory());
        }
    }

    public void addItem(Item item){
        for(TrashInventory trashInventory: trashInventories){
            if(trashInventory.canAdd(item)){
                trashInventory.addItem(item);
                return;
            }
        }

    }

    public static class TrashInventory{
        public DoubleChestFakeInventory inventory = new DoubleChestFakeInventory(null);


        public Map<Integer,Item> getSlot(){
            return inventory.getContents();
        }

        public void update(DoubleChestFakeInventory dfc){
            inventory.setContents(dfc.getContents());
            inventory.sendContents(dfc.getViewers());
        }

        public boolean canAdd(Item item){
            boolean add = false;
            if(ClearMainClass.trashSettingControl.ids.contains(item.getId()+"") ||
                    ClearMainClass.trashSettingControl.ids.contains(item.getId()+":"+item.getDamage())
            || ClearMainClass.trashSettingControl.ids.contains(item.getName())){
                if(ClearMainClass.trashSettingControl.ignoreNbt && item.hasCompoundTag()){
                   add = true;
                }
            }else{
                add = true;
            }
            if(add){
                add = inventory.canAddItem(item);
            }
            return add;
        }

        public void addItem(Item item){
            if(canAdd(item)){
                inventory.addItem(item);
            }
        }

    }
}
