package org.sobadfish.fishclear.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.manager.ClearManager;
import org.sobadfish.fishclear.manager.TrashManager;
import org.sobadfish.fishclear.windows.DisPlayerPanel;
import org.sobadfish.fishclear.windows.lib.ChestInventoryPanel;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class ClearCommand extends Command {

    public ClearCommand(String name, String description) {
        super(name, description);
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        if(strings.length == 0){
            if(commandSender.isOp()){

                ClearManager.echoClearMessage();
                return true;
            }else{
                commandSender.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&c 请执行/"+getName()+" help 查看帮助"));
                return false;
            }
        }
        switch (strings[0]){
            case "help":
                commandSender.sendMessage(ClearMainClass.formatString("&7/"+getName()+" open &a开启垃圾桶"));
                if(commandSender.isOp()){
                    commandSender.sendMessage(ClearMainClass.formatString("&7/"+getName()+" clear &a清空垃圾桶"));
                }
                break;
            case "clear":
                TrashManager.clear(0);
                commandSender.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&a 垃圾箱已清空"));
                break;
            case "open":
                int page = 1;
                try{
                    page = Integer.parseInt(strings[1]);
                }catch (Exception ignore){}
                if(page >= ClearMainClass.trashManager.trashInventories.size()){
                    page = ClearMainClass.trashManager.trashInventories.size() - 1;
                }
                if(commandSender instanceof Player){
                    DisPlayerPanel disPlayerPanel = DisPlayerPanel.getDisPlayPanel((Player) commandSender,ClearMainClass.messageSettingControl.message.variable.trashTitle
                            .replace("${page}",page+""), ChestInventoryPanel.class);
                    if(disPlayerPanel != null){
                        disPlayerPanel.panel.setContents(ClearMainClass.trashManager.trashInventories.get(page).getSlot());
                        disPlayerPanel.displayPlayer();
                    }

                    break;
                }

            default:
                commandSender.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&c 请执行/"+getName()+" help 查看帮助"));
                break;
        }
        return true;
    }
}
