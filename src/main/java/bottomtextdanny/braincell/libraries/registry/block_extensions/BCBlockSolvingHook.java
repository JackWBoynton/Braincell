package bottomtextdanny.braincell.libraries.registry.block_extensions;

import bottomtextdanny.braincell.libraries.registry.SolvingHook;
import bottomtextdanny.braincell.libraries.network.Connection;
import bottomtextdanny.braincell.libraries._minor.chest.ChestMaterialProvider;
import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.registry.ModDeferringManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class BCBlockSolvingHook<T extends Block> implements SolvingHook<T> {

    @Override
    public void execute(ResourceLocation key, T object, ModDeferringManager solving) {
        if (object instanceof ExtraBlockRegisterer) {
            ExtraBlockRegisterer<T> extraRegister = (ExtraBlockRegisterer<T>) object;
            if (extraRegister.executeSideRegistry()) extraRegister.registerExtra(object, key, solving);
        }
        Connection.doClientSide(() -> {
            if (object instanceof ChestMaterialProvider chestMaterialProvider) {
                Braincell.client().getMaterialManager().deferMaterialsForChest(key, chestMaterialProvider);
            }
        });
    }
}
