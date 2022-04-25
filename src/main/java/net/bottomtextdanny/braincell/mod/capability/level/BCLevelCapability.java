package net.bottomtextdanny.braincell.mod.capability.level;

import com.google.common.collect.ImmutableList;
import net.bottomtextdanny.braincell.mod.capability.CapabilityModule;
import net.bottomtextdanny.braincell.mod.capability.CapabilityWrap;
import net.bottomtextdanny.braincell.mod.network.Connection;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class BCLevelCapability extends CapabilityWrap<BCLevelCapability, Level> {
    public static Capability<BCLevelCapability> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    @OnlyIn(Dist.CLIENT)
    private SpeckManagerModule speckManager;

    public BCLevelCapability(Level holder) {
        super(holder);
    }

    @Override
    protected void populateModuleList(ImmutableList.Builder<CapabilityModule<Level, BCLevelCapability>> moduleList) {
        Connection.doClientSide(() -> {
            this.speckManager = new SpeckManagerModule(this);
            moduleList.add(this.speckManager);
        });
    }

    @OnlyIn(Dist.CLIENT)
    public SpeckManagerModule getSpeckManager() {
        return speckManager;
    }

    @Override
    protected Capability<?> getToken() {
        return TOKEN;
    }
}
