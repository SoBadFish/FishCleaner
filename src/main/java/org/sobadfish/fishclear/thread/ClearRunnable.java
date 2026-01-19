package org.sobadfish.fishclear.thread;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.projectile.EntityProjectile;
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

    public boolean enableClean;

    public ClearRunnable(ClearMainClass owner) {
        super(owner);
        runnable = ClearMainClass.clearSettingControl.time;
    }



    @Override
    public void onRun(int i) {
        enableClean = false;
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
            enableClean = true;
        }
        if(enableClean) {
            if (ClearMainClass.clearItemLimitControl.clearItemSetting.limit > 0) {
                //TODO 遍历地图检测
                for (Level level : ClearMainClass.clearSettingControl.clearWorld) {
                    int count = 0;
                    ArrayList<EntityItem> entityItems = new ArrayList<>();
                    for (Entity entity1 : level.getEntities()) {
                        if (entity1 instanceof Player) {
                            continue;
                        }
                        if (entity1 instanceof EntityHuman) {
                            continue;
                        }
                        if (entity1 instanceof EntityProjectile) {
                            continue;
                        }
                        if (entity1 instanceof EntityItem) {
                            count++;
                            entityItems.add((EntityItem) entity1);
                        }

                    }
                    if (count >= ClearMainClass.clearItemLimitControl.clearItemSetting.limit) {
                        for (EntityItem entityItem : entityItems) {
                            if (ClearMainClass.clearSettingControl.addTrashWorld.contains(level)) {
                                ClearMainClass.trashManager.addItem(entityItem.getItem());
                            }
                            entityItem.close();
                        }
                        String msg = ClearMainClass.formatString(message.clearLimitMsg
                                .replace("${world-name}", level.getFolderName())
                                .replace("${limit}", ClearMainClass.clearItemLimitControl.clearItemSetting.limit + "")
                        );
                        if (ClearMainClass.messageSettingControl.onlyDisplay) {
                            for (Player player : level.getPlayers().values()) {
                                MessageManager.echoToPlayer(player, msg);
                            }
                        } else {
                            Server.getInstance().broadcastMessage(ClearMainClass.TITLE + msg);
                        }
                    }
                }
            }
        }

    }
}
