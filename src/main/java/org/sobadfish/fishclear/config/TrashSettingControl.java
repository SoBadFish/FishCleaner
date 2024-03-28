package org.sobadfish.fishclear.config;

import cn.nukkit.utils.Config;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class TrashSettingControl {

    public boolean enable;

    public int size;

    public boolean ignoreNbt;

    public List<String> ids = new ArrayList<>();

    public static TrashSettingControl loadConfig(Config config){
        TrashSettingControl trashSettingControl = new TrashSettingControl();
        trashSettingControl.enable = config.getBoolean("trash-settings.enable");
        trashSettingControl.size = config.getInt("trash-settings.page-size");
        trashSettingControl.ignoreNbt = config.getBoolean("trash-settings.ignoreNbt",true);
        trashSettingControl.ids = new ArrayList<>(config.getStringList("trash-settings.ids"));

        return trashSettingControl;
    }
}
