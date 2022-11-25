package org.sobadfish.fishclear.config;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;

import java.util.ArrayList;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class ClearSettingControl {

    /**
     * 倒计时
     * */
    public int time;
    /**
     * 清理地图
     * */
    public ArrayList<Level> clearWorld = new ArrayList<>();

    public EntityClearSetting entityClearSetting;

    public ArrayList<Integer> timeMsg = new ArrayList<>();

    public ArrayList<Level> addTrashWorld = new ArrayList<>();

    public static ClearSettingControl loadConfig(Config config){
        ClearSettingControl clearSettingControl = new ClearSettingControl();
        clearSettingControl.time = config.getInt("clear-settings.time");
        String clearWorld = config.getString("clear-settings.clear-world");

        clearSettingControl.clearWorld = getLevelListByName(clearWorld);

        EntityClearSetting entityClearSetting = new EntityClearSetting();
        entityClearSetting.enable = config.getBoolean("clear-settings.clear-entity.enable");
        entityClearSetting.clearWorld = getLevelListByName( config.getString("clear-settings.clear-entity.clear-world"));
        entityClearSetting.filter = new ArrayList<>(config.getStringList("clear-settings.clear-entity.filter"));

        clearSettingControl.entityClearSetting = entityClearSetting;
        clearSettingControl.timeMsg = new ArrayList<>(config.getIntegerList("clear-settings.intervalTime"));

        clearSettingControl.addTrashWorld = getLevelListByName(config.getString("clear-settings.add-trash-world"));
        return clearSettingControl;
    }

    private static ArrayList<Level> getLevelListByName(String levelMsg){
        ArrayList<Level> cw = new ArrayList<>();
        if("all".equalsIgnoreCase(levelMsg)){
            cw.addAll(Server.getInstance().getLevels().values());
        }else{
            for(String s: levelMsg.split(";")){
                Level level = Server.getInstance().getLevelByName(s);
                if(level != null){
                    cw.add(level);
                }
            }
        }
        return cw;
    }


    public static class EntityClearSetting{
        /**
         * 是否启用
         * */
        public boolean enable;
        /**
         * 清理地图
         * */
        public ArrayList<Level> clearWorld = new ArrayList<>();
        /**
         * 清理过滤
         * */
        public ArrayList<String> filter = new ArrayList<>();


    }
}
