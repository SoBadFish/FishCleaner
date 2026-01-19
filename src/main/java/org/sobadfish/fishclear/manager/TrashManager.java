package org.sobadfish.fishclear.manager;


import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.windows.DisPlayerPanel;
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
        for(Map.Entry<String, DisPlayerPanel> pe: DisPlayerPanel.panelLib.entrySet()){
            TrashInventory pc = pe.getValue().panel;
            pc.inventory.clearAll();
//            pc.inventory.
//            pc.putAll(ClearMainClass.trashManager.trashInventories.get(index).getSlot());
            pc.inventory.sendContents(pc.inventory.getViewers());

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
            trashInventories.add(new TrashInventory(
                    ClearMainClass.messageSettingControl.message.variable.trashTitle
                            .replace("${page}",(size+1)+""),null
            ));
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
        public DoubleChestFakeInventory inventory;


        public TrashInventory(String title, InventoryHolder inventoryHolder){
            inventory = new DoubleChestFakeInventory(inventoryHolder,title);
        }


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
