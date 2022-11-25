package org.sobadfish.fishclear.manager;

import cn.nukkit.Player;
import org.sobadfish.fishclear.ClearMainClass;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class MessageManager {

    public static void echoToPlayer(Player player,String msg){
        switch (ClearMainClass.messageSettingControl.type){
            case TIP:
                player.sendTip(ClearMainClass.TITLE+ClearMainClass.formatString(msg));
                break;
            case POPUP:
                player.sendPopup(ClearMainClass.TITLE+ClearMainClass.formatString(msg));
                break;
            case ACTION:
                player.sendActionBar(ClearMainClass.TITLE+ClearMainClass.formatString(msg));
                break;
            default:
                player.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString(msg));
                break;
        }
    }
}
