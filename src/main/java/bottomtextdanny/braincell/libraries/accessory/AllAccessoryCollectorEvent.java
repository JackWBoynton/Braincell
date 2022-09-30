package bottomtextdanny.braincell.libraries.accessory;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

public class AllAccessoryCollectorEvent extends Event {
    public final Player player;
    private final ImmutableList.Builder<IAccessory> unbuiltAccessoryList;

    public AllAccessoryCollectorEvent(ImmutableList.Builder<IAccessory> listBuilder, Player player) {
        super();
        this.player = player;
        this.unbuiltAccessoryList = listBuilder;
    }

    public void addAccessory(IAccessory accessory) {
        this.unbuiltAccessoryList.add(accessory);
    }
}
