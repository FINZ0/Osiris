package me.finz0.osiris.module.modules.gui;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.finz0.osiris.settings.Setting;
import me.finz0.osiris.OsirisMod;
import me.finz0.osiris.module.Module;
import me.finz0.osiris.friends.Friends;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;

import java.text.DecimalFormat;

public class Players extends Module {
    public Players() {
        super("Players", Category.GUI);
        setDrawn(false);
    }


    Setting x;
    Setting y;
    Setting right;
    Setting customFont;
    Setting distance;

    String s = "";
    int count;
    DecimalFormat decimalFormat = new DecimalFormat("00.0");
    ChatFormatting cf;

    public void setup(){
        x = new Setting("X", this, 2, 0, 1000, true, "PlayersX");
        y = new Setting("Y", this, 2, 0, 1000, true, "PlayersY");
        OsirisMod.getInstance().settingsManager.rSetting(x);
        OsirisMod.getInstance().settingsManager.rSetting(y);
        right = new Setting("AlignRight", this, false, "PlayersAlignRight");
        OsirisMod.getInstance().settingsManager.rSetting(right);
        OsirisMod.getInstance().settingsManager.rSetting(customFont = new Setting("CFont", this, false, "PlayersCustomFont"));
        OsirisMod.getInstance().settingsManager.rSetting(distance = new Setting("Distance", this, false, "PlayersDistance"));
    }

    public void onRender(){
        boolean font = customFont.getValBoolean();
        count = 0;
        mc.world.loadedEntityList.stream()
                .filter(e->e instanceof EntityPlayer)
                .filter(e->e != mc.player)
                .forEach(e->{
                    if(Friends.isFriend(e.getName())) cf = ChatFormatting.AQUA;
                    else if(((EntityPlayer) e).isPotionActive(MobEffects.STRENGTH)) cf = ChatFormatting.RED;
                    else cf = ChatFormatting.GRAY;
                    if((((EntityPlayer) e).getHealth() + ((EntityPlayer) e).getAbsorptionAmount()) <= 5) s = ChatFormatting.RED +" "+ decimalFormat.format((((EntityPlayer) e).getHealth() + ((EntityPlayer) e).getAbsorptionAmount()));
                    if((((EntityPlayer) e).getHealth() + ((EntityPlayer) e).getAbsorptionAmount()) > 5 && (((EntityPlayer) e).getHealth() + ((EntityPlayer) e).getAbsorptionAmount()) <=15) s = ChatFormatting.YELLOW +" "+ decimalFormat.format((((EntityPlayer) e).getHealth() + ((EntityPlayer) e).getAbsorptionAmount()));
                    if((((EntityPlayer) e).getHealth() + ((EntityPlayer) e).getAbsorptionAmount()) >15) s = ChatFormatting.GREEN +" "+ decimalFormat.format((((EntityPlayer) e).getHealth() + ((EntityPlayer) e).getAbsorptionAmount()));
                    if(distance.getValBoolean()) s += " " + ChatFormatting.GRAY + (int)mc.player.getDistance(e);
                    if(right.getValBoolean()) {
                            if(font) OsirisMod.fontRenderer.drawStringWithShadow(cf + e.getName() + s, (int) x.getValDouble() - OsirisMod.fontRenderer.getStringWidth(cf + e.getName() + s), (int) y.getValDouble() + count, 0xffffffff);
                            else mc.fontRenderer.drawStringWithShadow(cf + e.getName() + s, (int) x.getValDouble() - mc.fontRenderer.getStringWidth(cf + e.getName() + s), (int) y.getValDouble() + count, 0xffffffff);
                    } else {
                            if(font) OsirisMod.fontRenderer.drawStringWithShadow(cf + e.getName() + s, (int) x.getValDouble(), (int) y.getValDouble() + count, 0xffffffff);
                            else mc.fontRenderer.drawStringWithShadow(cf + e.getName() + s, (int) x.getValDouble(), (int) y.getValDouble() + count, 0xffffffff);
                    }
                    count += 10;
                });
    }
}
