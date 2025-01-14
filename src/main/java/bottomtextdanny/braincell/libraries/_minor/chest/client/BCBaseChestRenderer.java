package bottomtextdanny.braincell.libraries._minor.chest.client;

import bottomtextdanny.braincell.libraries._minor.chest.BCChestBlock;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries._minor.chest.BCChestBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BrightnessCombiner;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.DoubleBlockCombiner;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

public abstract class BCBaseChestRenderer<T extends BCChestBlockEntity & LidBlockEntity> implements BlockEntityRenderer<T> {
    protected BCChestModel singleModel;
    protected BCChestModel leftModel;
    protected BCChestModel rightModel;

    public BCBaseChestRenderer(
            BlockEntityRendererProvider.Context rendererDispatcherIn,
            BCChestModel singleModel,
            BCChestModel leftModel,
            BCChestModel rightModel) {
        this.singleModel = singleModel;
        this.leftModel = leftModel;
        this.rightModel = rightModel;
    }

    public void render(T tileEntityIn, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn, int combinedOverlayIn) {
        Level world = tileEntityIn.getLevel();
        boolean worldLoaded = world != null;
        BlockState blockstate = worldLoaded && tileEntityIn.getBlockState().getBlock() instanceof BCChestBlock ? tileEntityIn.getBlockState() : tileEntityIn.getAuxiliaryBlock().defaultBlockState().setValue(ChestBlock.FACING, Direction.SOUTH);
        ChestType chesttype = blockstate.hasProperty(ChestBlock.TYPE) ? blockstate.getValue(ChestBlock.TYPE) : ChestType.SINGLE;
        Block block = blockstate.getBlock();
	  
        if (block instanceof BCChestBlock chestBlock) {
            ChestMaterialManager materialManager = Braincell.client().getMaterialManager();
            boolean isDouble = chesttype != ChestType.SINGLE;
            BCChestModel model;

            DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> icallbackwrapper;
            VertexConsumer ivertexbuilder;

            if (worldLoaded) {
                icallbackwrapper = chestBlock.combine(blockstate, world, tileEntityIn.getBlockPos(), true);
            } else {
                icallbackwrapper = DoubleBlockCombiner.Combiner::acceptNone;
            }

            float calledLidDegree = icallbackwrapper.apply(ChestBlock.opennessCombiner(tileEntityIn)).get(partialTicks);
	        int calledFusedLight = icallbackwrapper.apply(new BrightnessCombiner<>()).applyAsInt(combinedLightIn);
	
	        matrixStackIn.pushPose();
            matrixStackIn.scale(1, -1, -1);
            matrixStackIn.translate(0.5D, 0.0D, -0.5D);
            matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(blockstate.getValue(ChestBlock.FACING).toYRot()));
            if (isDouble) {
                if (chesttype == ChestType.LEFT) {
                    ivertexbuilder = materialManager.getChestMaterial(chestBlock, ChestMaterial.DOUBLE_LEFT).buffer(bufferIn, RenderType::entityCutout);
                    model = this.leftModel;
                } else {
                    ivertexbuilder = materialManager.getChestMaterial(chestBlock, ChestMaterial.DOUBLE_RIGHT).buffer(bufferIn, RenderType::entityCutout);
                    model = this.rightModel;
                }
            } else {
                ivertexbuilder = materialManager.getChestMaterial(chestBlock, ChestMaterial.SINGLE).buffer(bufferIn, RenderType::entityCutout);
                model = this.singleModel;
            }
            model.runDefaultState();
            model.setRotationWLid(tileEntityIn, partialTicks, calledLidDegree);
            model.renderToBuffer(matrixStackIn, ivertexbuilder, calledFusedLight, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }
}
