package org.sobadfish.fishclear.config;

import cn.nukkit.utils.Config;
import org.sobadfish.fishclear.ClearMainClass;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class MessageSettingControl {

    public MessageType type;

    public boolean onlyDisplay;

    public Message message;


    public static MessageSettingControl loadConfig(Config config){
        MessageSettingControl messageSettingControl = new MessageSettingControl();
        MessageType t = null;
        try {
            t = MessageType.valueOf(config.getString("msg-settings.display-type"));
        }catch (Exception ignore){}
        if(t == null){
            t = MessageType.MSG;
        }
        messageSettingControl.type = t;
        messageSettingControl.onlyDisplay = config.getBoolean("msg-settings.only-display-player");
        Message message = new Message();

        message.runMsg = ClearMainClass.formatString(config.getString("msg-settings.message.running-msg"));
        message.clearMsg = ClearMainClass.formatString(config.getString("msg-settings.message.clear-success-msg"));
        message.dropCancelMsg = ClearMainClass.formatString(config.getString("msg-settings.message.drop-cancel"));
        message.dropOpenMsg = ClearMainClass.formatString(config.getString("msg-settings.message.drop-open"));
        message.dropCloseMsg = ClearMainClass.formatString(config.getString("msg-settings.message.drop-close"));
        message.clearLimitMsg = ClearMainClass.formatString(config.getString("msg-settings.message.clear-limit"));

        Message.MessageVariable mv = new Message.MessageVariable();

        mv.countItemTitle = ClearMainClass.formatString(config.getString("msg-settings.variable.count-item"));
        mv.countEntityTitle = ClearMainClass.formatString(config.getString("msg-settings.variable.count-entity"));

        mv.trashTitle = ClearMainClass.formatString(config.getString("msg-settings.variable.trash-title"));
        mv.control = ClearMainClass.formatString(config.getString("msg-settings.variable.control"));
        message.variable = mv;
        messageSettingControl.message = message;


        return messageSettingControl;

    }


    public enum MessageType{
        /**
         * Tip显示
         * */
        TIP,
        /**
         * Popup显示
         * */
        POPUP,
        /**
         * ActionBar显示
         * */
        ACTION,
        /**
         * Message显示
         * */
        MSG
    }

    public static class Message{

        public String runMsg;

        public String clearMsg;

        public String dropCancelMsg;

        public String dropOpenMsg;

        public String dropCloseMsg;

        public String clearLimitMsg;

        public MessageVariable variable;

        public static class MessageVariable{

            public String countItemTitle;

            public String countEntityTitle;

            public String trashTitle;

            public String control;


        }
    }
}
