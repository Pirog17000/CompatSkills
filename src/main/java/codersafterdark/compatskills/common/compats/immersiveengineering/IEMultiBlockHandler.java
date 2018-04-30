package codersafterdark.compatskills.common.compats.immersiveengineering;

import blusunrize.immersiveengineering.api.MultiblockHandler.IMultiblock;
import blusunrize.immersiveengineering.api.MultiblockHandler.MultiblockFormEvent;
import codersafterdark.compatskills.utils.MessageStorage;
import codersafterdark.compatskills.utils.multiblock.MultiBlockGate;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.base.LevelLockHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class IEMultiBlockHandler {
    @SubscribeEvent
    public void multiBlockForm(MultiblockFormEvent event) {
        IMultiblock multiblock = event.getMultiblock();
        EntityPlayer player = event.getEntityPlayer();
        PlayerData data = PlayerDataHandler.get(player);
        MultiBlockGate gate = new IEMultiBlockGate(multiblock.getUniqueName());
        RequirementHolder requirementHolder = LevelLockHandler.getLockByKey(gate);
        if (requirementHolder != null && !requirementHolder.equals(LevelLockHandler.EMPTY_LOCK) && !data.matchStats(requirementHolder)) {
            event.setCanceled(true);
            if (player.getEntityWorld().isRemote) {
                player.sendStatusMessage(new TextComponentString(MessageStorage.getFailureMessage(gate)), true);
            }
        }
    }
}