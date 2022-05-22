package bottomtextdanny.braincell.mod.capability.level;

import bottomtextdanny.braincell.base.BCLerp;
import bottomtextdanny.braincell.mod.capability.CapabilityModule;
import bottomtextdanny.braincell.mod.capability.level.speck.RenderableSpeck;
import bottomtextdanny.braincell.mod.capability.level.speck.Speck;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import bottomtextdanny.braincell.mod._base.BCStaticData;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class SpeckManagerModule extends CapabilityModule<Level, BCLevelCapability> {
    private final List<Speck> queuedSpecks;
    private final List<Speck> specks;

    public SpeckManagerModule(BCLevelCapability capability) {
        super("specks", capability);
        this.queuedSpecks = Lists.newLinkedList();
        this.specks = Lists.newLinkedList();
    }

    public void tick() {
        if (Minecraft.getInstance().isPaused()) return;

        final Iterator<Speck> newSpecks = queuedSpecks.iterator();
        Speck newSpeck;
        while (newSpecks.hasNext()) {
            newSpeck = newSpecks.next();
            this.specks.add(newSpeck);
            newSpecks.remove();
        }
        this.specks.removeIf(speck -> {
            speck.baseTick();
            return speck.isRemoved();
        });
    }

    public void render(PoseStack stack, MultiBufferSource buffer) {

        final float partialTick = BCStaticData.partialTick();
        final Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
        final double camX = camera.getPosition().x;
        final double camY = camera.getPosition().y;
        final double camZ = camera.getPosition().z;

        stack.pushPose();
        stack.translate(-camX, -camY, -camZ);
        for (Speck speck : this.specks) {
            if (speck instanceof RenderableSpeck renderable) {
                if (renderable.shouldRender()) {
                    final double easedX = BCLerp.get(partialTick, speck.xo, speck.getPosX());
                    final double easedY = BCLerp.get(partialTick, speck.yo, speck.getPosY());
                    final double easedZ = BCLerp.get(partialTick, speck.zo, speck.getPosZ());
                    stack.pushPose();
                    stack.translate(easedX, easedY, easedZ);
                    renderable.render((float)easedX, (float)easedY, (float)easedZ, stack, buffer, partialTick);
                    stack.popPose();
                }
            }
        }
        stack.popPose();
    }

    public boolean addSpeck(Speck speck) {
        return this.queuedSpecks.add(speck);
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {}

    @Override
    public void deserializeNBT(CompoundTag nbt) {}
}
