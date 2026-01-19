package org.sobadfish.fishclear.windows.items;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import org.sobadfish.fishclear.manager.TrashManager;


/**
 * @author Sobadfish
 * @date 2022/9/9
 */
public abstract class BasePlayPanelItemInstance {

    /**
     * 游戏内物品
     * @return 物品
     * */
    public abstract Item getItem();
    /**
     * 当玩家触发
     *
     * @param inventory 商店
     * @param player 玩家
     *
     * */
    public abstract void onClick(TrashManager.TrashInventory inventory, Player player);

    /**
     * 箱子菜单展示物品
     * @param index 位置
     * @param info 玩家信息
     * @return 物品
     *
     * */
    public abstract Item getPanelItem(Player info, int index);



    @Override
    public String toString() {
        return getItem().toString();
    }



}
