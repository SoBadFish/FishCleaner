package org.sobadfish.fishclear.config;

import cn.nukkit.level.Level;
import cn.nukkit.utils.Config;

import java.util.*;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class OtherSettingControl {

    //玩家指令设置是否丢弃
    public LinkedHashMap<String,Boolean> playerDropSetting = new LinkedHashMap<>();

    public LinkedHashMap<DropType, List<String>> controlDrop = new LinkedHashMap<>();

    public boolean addTrash;

    public ItemTiming itemTiming;

    public static OtherSettingControl loadConfig(Config config){
        OtherSettingControl otherSettingControl = new OtherSettingControl();
        String[] s1 = config.getString("other-setting.control-drop").split("&");
        LinkedHashMap<DropType,List<String>> controlDrop = new LinkedHashMap<>();
        for(String ss: s1){
            String[] ss1 = ss.split(":");
            DropType dt = null;
            try{
                dt = DropType.valueOf(ss1[0]);
            }catch (Exception ignore){}
            if(dt != null){
                ArrayList<String> levels = new ArrayList<>();
                if(ss1.length > 1){
                    levels.addAll(Arrays.asList(ss1[1].split(";")));
                }
                controlDrop.put(dt,levels);
            }
        }
        otherSettingControl.controlDrop = controlDrop;
        otherSettingControl.addTrash = config.getBoolean("other-setting.add-trash");
        ItemTiming timing = new ItemTiming();
        timing.enable = config.getBoolean("other-setting.item-timing-setting.enable");
        timing.time = config.getInt("other-setting.item-timing-setting.time");
        otherSettingControl.itemTiming = timing;

        return otherSettingControl;
    }

    public DropType getDropTypeByWorldName(String worldName) {
        for (Map.Entry<DropType,List<String>> dropTypeListEntry : controlDrop.entrySet()){
            if(dropTypeListEntry.getValue().size() > 0){
                if(dropTypeListEntry.getValue().contains(worldName)){
                    return dropTypeListEntry.getKey();
                }
            }else{
                return dropTypeListEntry.getKey();
            }

        }
        return DropType.NULL;
    }

    public static class ItemTiming{

        public boolean enable;

        public int time;
    }


    public enum DropType{
        /**
         * 不控制
         * */
        NULL,
        /**
         * 指令开关
         * */
        CMD,
        /**
         * 强制禁止丢弃
         * */
        FORCE
    }
}
