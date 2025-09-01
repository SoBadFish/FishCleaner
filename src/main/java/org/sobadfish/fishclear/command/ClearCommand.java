package org.sobadfish.fishclear.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import org.sobadfish.fishclear.ClearMainClass;
import org.sobadfish.fishclear.config.OtherSettingControl;
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
                    commandSender.sendMessage(ClearMainClass.formatString("&7/"+getName()+" clear <page>/all &a清空垃圾桶"));
                }
                break;
            case "clear":
                int page = 0;
                if(strings.length > 1){
                    if("all".equalsIgnoreCase(strings[1])){
                        //清空全部
                        TrashManager.clearAll();

                        commandSender.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&a 垃圾箱已全部清空"));
                    }
                    page = getStringToPage(strings[1]);
                }

                TrashManager.clear(page);
                commandSender.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&a 垃圾箱已清空"));
                break;
            case "open":
                if(ClearMainClass.trashSettingControl.enable){
                    page = 0;
                    if(strings.length > 1){
                        page = getStringToPage(strings[1]);
                    }

                    if(commandSender instanceof Player){
                        TrashManager.TrashInventory panel = ClearMainClass.trashManager.trashInventories.get(page);
                        DisPlayerPanel disPlayerPanel = DisPlayerPanel.getDisPlayPanel((Player) commandSender,ClearMainClass.messageSettingControl.message.variable.trashTitle
                                .replace("${page}",(page+1)+""),page, ChestInventoryPanel.class);
                       if(disPlayerPanel != null){
                           disPlayerPanel.panel.setContents(panel.getSlot());
                           disPlayerPanel.displayPlayer();
                       }


                    }
                }else{
                    commandSender.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&c 未开启垃圾桶功能"));
                }
                break;

            case "drop":
                if(commandSender instanceof Player){
                    boolean b;
                    if(ClearMainClass.otherSettingControl.playerDropSetting.containsKey(commandSender.getName())){
                        b = ClearMainClass.otherSettingControl.playerDropSetting.get(commandSender.getName());
                        ClearMainClass.otherSettingControl.playerDropSetting.put(commandSender.getName(),b = !b);
                    }else{
                        ClearMainClass.otherSettingControl.playerDropSetting.put(commandSender.getName(),b = true);
                    }
                    commandSender.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&e 已 "+(b?"&a允许":"&c禁止")+" &e丢物品"));
                }


                break;

            default:
                commandSender.sendMessage(ClearMainClass.TITLE+ClearMainClass.formatString("&c 请执行/"+getName()+" help 查看帮助"));
                break;
        }
        return true;
    }

    private int getStringToPage(String value){
        int page = 1;
        try{
            page = Integer.parseInt(value);
        }catch (Exception ignore){}
        if(page >= ClearMainClass.trashManager.trashInventories.size()){
            page = ClearMainClass.trashManager.trashInventories.size();
        }
        page -= 1;
        if(page < 0){
            page = 0;
        }
        return page;
    }
}
