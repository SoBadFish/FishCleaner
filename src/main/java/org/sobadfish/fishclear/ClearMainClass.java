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
import org.sobadfish.fishclear.windows.lib.AbstractFakeInventory;

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

    public static String PLUGIN_CMD = "fcl";

    @Override
    public void onEnable() {
        mainClass = this;
        saveDefaultConfig();
        reloadConfig();
        PLUGIN_CMD = getConfig().getString("plugin-cmd","fcl");
        checkServer();
        long t1 = System.currentTimeMillis();
        TITLE = formatString(getConfig().getString("plugin-title"));
        clearSettingControl = ClearSettingControl.loadConfig(getConfig());
        messageSettingControl = MessageSettingControl.loadConfig(getConfig());
        otherSettingControl = OtherSettingControl.loadConfig(getConfig());
        trashSettingControl = TrashSettingControl.loadConfig(getConfig());
        trashManager = new TrashManager(trashSettingControl.size);
        this.getServer().getPluginManager().registerEvents(new ClearListener(),this);
        this.getServer().getCommandMap().register("clear",new ClearCommand(PLUGIN_CMD,"清道夫主指令"));
        this.getServer().getScheduler().scheduleRepeatingTask(this,new ClearRunnable(this),20);
        this.getLogger().info(TITLE+" 插件启动完成 用时: "+(System.currentTimeMillis() - t1)+" ms");

    }

    public static String formatString(String msg){
        return TextFormat.colorize('&',msg);
    }

    private void checkServer(){
        boolean ver = false;
        //双核心兼容
        try {
            Class<?> c = Class.forName("cn.nukkit.Nukkit");
            c.getField("NUKKIT_PM1E");
            ver = true;

        } catch (ClassNotFoundException | NoSuchFieldException ignore) { }
        try {
            Class<?> c = Class.forName("cn.nukkit.Nukkit");
            c.getField("NUKKIT").get(c).toString().equalsIgnoreCase("Nukkit PetteriM1 Edition");
            ver = true;
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException ignore) {
        }

        AbstractFakeInventory.IS_PM1E = ver;
        if(ver){
            this.getLogger().info(formatString("&e核心 Nukkit PM1E"));
        }else{
            this.getLogger().info(formatString("&e核心 Nukkit"));
        }
    }
}
