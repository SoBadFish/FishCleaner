package org.sobadfish.fishclear.thread;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.scheduler.PluginTask;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.config.MessageSettingControl;
import org.sobadfish.fishclear.manager.ClearManager;
import org.sobadfish.fishclear.manager.MessageManager;

import java.util.ArrayList;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class ClearRunnable extends PluginTask<ClearMainClass> {

    public int runnable;

    public ClearRunnable(ClearMainClass owner) {
        super(owner);
        runnable = ClearMainClass.clearSettingControl.time;
    }



    @Override
    public void onRun(int i) {
        MessageSettingControl.Message message =   ClearMainClass.messageSettingControl.message;
        if(runnable > 0){
            runnable--;
            for(Level level: ClearMainClass.clearSettingControl.clearWorld) {
                if (ClearMainClass.clearSettingControl.timeMsg.contains(runnable)) {
                    String msg = ClearMainClass.formatString(message.runMsg
                            .replace("${time}", runnable + "")
                            .replace("${world-name}",level.getFolderName()));
                    if (ClearMainClass.messageSettingControl.onlyDisplay) {
                        for (Player player : level.getPlayers().values()) {
                            MessageManager.echoToPlayer(player, msg);
                        }
                    } else {
                        Server.getInstance().broadcastMessage(ClearMainClass.TITLE+msg);
                    }
                }
            }

        }else{
            runnable = ClearMainClass.clearSettingControl.time;
            ClearManager.echoClearMessage();
        }
    }
}
