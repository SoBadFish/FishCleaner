package org.sobadfish.fishclear;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.TextFormat;
import org.sobadfish.fishclear.command.ClearCommand;
import org.sobadfish.fishclear.config.ClearSettingControl;
import org.sobadfish.fishclear.config.MessageSettingControl;
import org.sobadfish.fishclear.config.OtherSettingControl;
import org.sobadfish.fishclear.config.TrashSettingControl;
import org.sobadfish.fishclear.listener.ClearListener;
import org.sobadfish.fishclear.manager.TrashManager;
import org.sobadfish.fishclear.thread.ClearRunnable;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */

public class ClearMainClass extends PluginBase {

    public static String TITLE;

    public static ClearMainClass mainClass;

    public static ClearSettingControl clearSettingControl;

    public static MessageSettingControl messageSettingControl;

    public static OtherSettingControl otherSettingControl;

    public static TrashSettingControl trashSettingControl;

    public static TrashManager trashManager;

    @Override
    public void onEnable() {
        mainClass = this;
        saveDefaultConfig();
        reloadConfig();
        long t1 = System.currentTimeMillis();
        TITLE = formatString(getConfig().getString("plugin-title"));
        clearSettingControl = ClearSettingControl.loadConfig(getConfig());
        messageSettingControl = MessageSettingControl.loadConfig(getConfig());
        otherSettingControl = OtherSettingControl.loadConfig(getConfig());
        trashSettingControl = TrashSettingControl.loadConfig(getConfig());
        trashManager = new TrashManager(trashSettingControl.size);
        this.getServer().getPluginManager().registerEvents(new ClearListener(),this);
        this.getServer().getCommandMap().register("clear",new ClearCommand("fcl","清道夫主指令"));
        this.getServer().getScheduler().scheduleRepeatingTask(this,new ClearRunnable(this),20);
        this.getLogger().info(TITLE+" 插件启动完成 用时: "+(System.currentTimeMillis() - t1)+" ms");

    }

    public static String formatString(String msg){
        return TextFormat.colorize('&',msg);
    }
}
