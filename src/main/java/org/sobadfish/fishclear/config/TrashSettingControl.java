package org.sobadfish.fishclear.config;

import cn.nukkit.utils.Config;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class TrashSettingControl {

    public boolean enable;

    public int size;

    public static TrashSettingControl loadConfig(Config config){
        TrashSettingControl trashSettingControl = new TrashSettingControl();
        trashSettingControl.enable = config.getBoolean("trash-settings.enable");
        trashSettingControl.size = config.getInt("trash-settings.page-size");
        return trashSettingControl;
    }
}
