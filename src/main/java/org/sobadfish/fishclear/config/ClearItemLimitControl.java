package org.sobadfish.fishclear.config;

import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;

import java.util.ArrayList;

/**
 * @author Sobadfish
 * @date 2024/7/23
 */
public class ClearItemLimitControl {

    public ClearItemSetting clearItemSetting;


    public static ClearItemLimitControl loadConfig(Config config){
        ClearItemLimitControl clearItemLimitControl = new ClearItemLimitControl();
        ClearItemSetting setting = new ClearItemSetting();
        setting.limit = config.getInt("other-setting.clear-item-limit.limit",500);
        String clearWorld = config.getString("other-setting.clear-item-limit.world");
        setting.clearWorld = ClearSettingControl.getLevelListByName(clearWorld);
        clearItemLimitControl.clearItemSetting = setting;
        return clearItemLimitControl;

    }



    public static class ClearItemSetting{
        /**
         * 是否启用
         * */
        public int limit;
        /**
         * 清理地图
         * */
        public ArrayList<Level> clearWorld = new ArrayList<>();



    }
}
