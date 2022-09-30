package bottomtextdanny.braincell.libraries.speck;

import bottomtextdanny.braincell.libraries.capability.CapabilityModule;
import bottomtextdanny.braincell.libraries.capability.CapabilityWrap;
import bottomtextdanny.braincell.libraries.network.Connection;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

public class BCSpeckCapability extends CapabilityWrap<BCSpeckCapability, Level> {
    public static Capability<BCSpeckCapability> TOKEN = CapabilityManager.get(new CapabilityToken<>(){});
    @OnlyIn(Dist.CLIENT)
    private SpeckManagerModule speckManager;

    public BCSpeckCapability(Level holder) {
        super(holder);
        Connection.doClientSide(() -> {
            this.speckManager = new SpeckManagerModule(this);
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
