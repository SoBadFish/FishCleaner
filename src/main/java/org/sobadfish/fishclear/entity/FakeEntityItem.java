package org.sobadfish.fishclear.entity;

import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;
import org.sobadfish.fishclear.ClearMainClass;

/**
 * @author Sobadfish
 * @date 2022/11/25
 */
public class FakeEntityItem extends EntityItem {

    public int deleteTime;

    public FakeEntityItem(FullChunk chunk, CompoundTag nbt) {
        super(chunk, nbt);
    }

    public int lastTime = 0;

    @Override
    public boolean onUpdate(int currentTick) {
        boolean b = super.onUpdate(currentTick);
        if(lastTime == 0){
            lastTime = currentTick;
        }
        if(currentTick - lastTime >= 20){
            deleteTime--;
            lastTime = currentTick;
        }
        if(deleteTime > 0){
            setNameTag(ClearMainClass.formatString("&7[&a"+timeToString()+"&7]"));
        }else{
            this.close();
        }
        return b;
    }

    public String timeToString(){
        double m = deleteTime / 60.0;
        double s = (deleteTime -  (int)m * 60);
        String ss,ms;
        if(s < 10){
            ss = "0"+((int)s);
        }else{
            ss = ""+((int)s);
        }
        if(m < 10){
            ms = "0"+((int)m);
        }else{
            ms = ""+((int)m);
        }
        return (ms+":"+ss);
    }
}
