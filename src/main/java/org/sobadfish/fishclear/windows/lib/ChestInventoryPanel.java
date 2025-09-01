package org.sobadfish.fishclear.windows.lib;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.inventory.InventoryType;
import cn.nukkit.item.Item;
import cn.nukkit.network.protocol.ContainerOpenPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.command.ClearCommand;
import org.sobadfish.fishclear.manager.TrashManager;
import org.sobadfish.fishclear.windows.DisPlayerPanel;
import org.sobadfish.fishclear.windows.items.BasePlayPanelItemInstance;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author BadFish
 */
public class ChestInventoryPanel extends DoubleChestFakeInventory implements InventoryHolder {

    public long id;

    protected final Player player;

    public int page = 0;

    public int clickSolt;

    protected Map<Integer, BasePlayPanelItemInstance> panel = new LinkedHashMap<>();

    public ChestInventoryPanel(Player player, InventoryHolder holder, String name) {
        super(holder);
        this.player = player;
        this.setName(name);
    }

    public void setPanel(Map<Integer, BasePlayPanelItemInstance> panel){
        Map<Integer, BasePlayPanelItemInstance> m = new LinkedHashMap<>();
        for(Map.Entry<Integer,BasePlayPanelItemInstance> entry : panel.entrySet()){
            Item value = entry.getValue().getPanelItem(getPlayer(),entry.getKey()).clone();
            m.put(entry.getKey(),entry.getValue());
            setItem(entry.getKey(),value);
        }
        this.panel = m;
    }



    public void setPage(int page) {
        this.page = page;
    }

    public int getPage() {
        return page;
    }

    public void update(){
        setPanel(panel);
    }


    public Player getPlayer(){
        return player;
    }


    public Map<Integer, BasePlayPanelItemInstance> getPanel() {
        return panel;
    }



    @Override
    public void setName(String name) {
        super.setName(name);
    }



    @Override
    public void onOpen(Player who) {
        super.onOpen(who);
        ContainerOpenPacket pk = new ContainerOpenPacket();
        pk.windowId = who.getWindowId(this);
        pk.entityId = id;
        pk.type = InventoryType.DOUBLE_CHEST.getNetworkType();
        who.dataPacket(pk);
    }

    @Override
    public void onClose(Player who) {
        RemoveEntityPacket pk = new RemoveEntityPacket();
        pk.eid = id;
        who.dataPacket(pk);
        super.onClose(who);
//        DisPlayerPanel.panelLib.remove(who.getName());

    }

    @Override
    public Inventory getInventory() {
        return this;
    }

    @Override
    public void onSlotChange(int index, Item before, boolean send) {
        super.onSlotChange(index, before, send);
        TrashManager.TrashInventory tr = ClearMainClass.trashManager.trashInventories.get(page);
        tr.update(this);
//        System.out.println("测试刷新机制..");
        for(Map.Entry<String,DisPlayerPanel> pe: DisPlayerPanel.panelLib.entrySet()){
            ChestInventoryPanel pc = pe.getValue().panel;
//            System.out.println("pc: "+pc.slots);

//            DisPlayerPanel disPlayerPanel = pe.getValue();
//            disPlayerPanel.getInventory().setContents(getContents());
//            disPlayerPanel.getInventory().sendContents(disPlayerPanel.getInventory().getViewers());
            if(pc.page == this.page){
//                System.out.println("尝试刷新: "+pc.slots);
//                if(pc != this) {
                tr.inventory.setContents(getContents());
                tr.inventory.sendContents(tr.inventory.getViewers());
                if(!send){
                    pc.setContents(getContents());
                    pc.sendContents(pc.getPlayer());
                }

                pc.sendContents(pc.getPlayer());
//                }
            }
        }
        //更新玩家页面

    }

    //    public void onUpdate(Player player){
//        //TODO 界面刷新
//        TrashManager.TrashInventory tr = ClearMainClass.trashManager.trashInventories.get(page);
//        tr.update(this);
//        for(Map.Entry<Player,DisPlayerPanel> pe: DisPlayerPanel.panelLib.entrySet()){
//            ChestInventoryPanel pc = pe.getValue().panel;
//            if(pc.page == this.page){
//
//                pc.setContents(ClearMainClass.trashManager.trashInventories.get(pc.page).getSlot());
//                pc.sendContents(pe.getKey());
//            }
//        }
//    }
}
