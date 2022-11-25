package org.sobadfish.fishclear.manager;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.entity.mob.EntityMob;
import cn.nukkit.entity.passive.EntityAnimal;
import cn.nukkit.level.Level;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.config.MessageSettingControl;

import java.util.ArrayList;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class ClearManager {

    public static ArrayList<ClearCount> toClear(){
        ArrayList<ClearCount> clearCounts = new ArrayList<>();
        for(Level level: ClearMainClass.clearSettingControl.clearWorld){
            ClearCount clearCount = new ClearCount();
            clearCount.level = level;
            Entity[] entity = level.getEntities();
            for(Entity entity1: entity){
                if(entity1 instanceof EntityItem){
                    if(ClearMainClass.clearSettingControl.addTrashWorld.contains(level)){
                        ClearMainClass.trashManager.addItem(((EntityItem) entity1).getItem());
                    }
                    clearCount.item++;
                    entity1.close();
                }
            }
            if(ClearMainClass.clearSettingControl.entityClearSetting.enable){
                if(ClearMainClass.clearSettingControl.entityClearSetting.clearWorld.contains(level)){
                    for(Entity entity1: entity){
                        if(chunkFilter(entity1,ClearMainClass.clearSettingControl.entityClearSetting.filter)){
                            entity1.close();
                            clearCount.entity++;
                        }
                    }
                }
            }
            clearCounts.add(clearCount);
        }
        return clearCounts;
    }

    public static void echoClearMessage(){
        MessageSettingControl.Message message = ClearMainClass.messageSettingControl.message;
        ArrayList<ClearManager.ClearCount> clearCounts = ClearManager.toClear();
        for(ClearManager.ClearCount clearCount: clearCounts){
            String msg =  ClearMainClass.formatString(message.clearMsg
                    .replace("${world-name}",clearCount.level.getFolderName())
                    .replace("${count-item}",(message.variable.countItemTitle.replace("x",clearCount.item+"")))
                    .replace("${count-entity}",(message.variable.countEntityTitle.replace("x",clearCount.entity+""))));

            if(ClearMainClass.messageSettingControl.onlyDisplay){
                for(Player player: clearCount.level.getPlayers().values()){
                    MessageManager.echoToPlayer(player,msg);
                }
            }else{
                Server.getInstance().broadcastMessage(ClearMainClass.TITLE+msg);
            }
        }
    }


    private static boolean chunkFilter(Entity entity, ArrayList<String> filter){
        for(String f: filter){
            if("animal".equalsIgnoreCase(f)){
                if(entity instanceof EntityAnimal){
                    return false;
                }
            }
            if("mob".equalsIgnoreCase(f)){
                if(entity instanceof EntityMob){
                    return false;
                }
            }
            int id = -1;
            try {
                id = Integer.parseInt(f);
            }catch (Exception ignore){}
            if(id > 0){
                if(entity.getNetworkId() == id){
                    return false;
                }
            }
            if(entity.getName().equalsIgnoreCase(f) || entity.getNameTag().equalsIgnoreCase(f)){
                return false;
            }
        }
        return true;
    }

    public static class ClearCount{
        public Level level;

        public int entity = 0;

        public int item = 0;

    }
}

