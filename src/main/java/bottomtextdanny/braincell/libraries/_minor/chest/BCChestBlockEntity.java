package bottomtextdanny.braincell.libraries._minor.chest;

import bottomtextdanny.braincell.libraries._minor.block_entity_messenger.BlockEntityClientMessenger;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.CompoundContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;

import javax.annotation.Nonnull;

public abstract class BCChestBlockEntity extends ChestBlockEntity implements BlockEntityClientMessenger {
    protected BCChestBlock auxiliaryBlock;
    protected static final int EVENT_SET_OPEN_COUNT = 1;

    public BCChestBlockEntity(BlockEntityType<? extends BCChestBlockEntity> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        if (state.getBlock() instanceof BCChestBlock chst) {
            this.auxiliaryBlock = chst;
        }
        this.openersCounter = new ContainerOpenersCounter() {
            protected void onOpen(Level p_155357_, BlockPos p_155358_, BlockState p_155359_) {
                BCChestBlockEntity.playSound(p_155357_, p_155358_, p_155359_, BCChestBlockEntity.this.auxiliaryBlock != null ? BCChestBlockEntity.this.auxiliaryBlock.getOpenSound() : SoundEvents.CHEST_OPEN);
            }

            protected void onClose(Level p_155367_, BlockPos p_155368_, BlockState p_155369_) {
                BCChestBlockEntity.playSound(p_155367_, p_155368_, p_155369_, BCChestBlockEntity.this.auxiliaryBlock != null ? BCChestBlockEntity.this.auxiliaryBlock.getCloseSound() : SoundEvents.CHEST_CLOSE);
            }

            protected void openerCountChanged(Level p_155361_, BlockPos p_155362_, BlockState p_155363_, int p_155364_, int p_155365_) {
                BCChestBlockEntity.this.signalOpenCount(p_155361_, p_155362_, p_155363_, p_155364_, p_155365_);
            }

            protected boolean isOwnContainer(Player p_155355_) {
                if (!(p_155355_.containerMenu instanceof ChestMenu)) {
                    return false;
                } else {
                    Container container = ((ChestMenu)p_155355_.containerMenu).getContainer();
                    return container == BCChestBlockEntity.this || container instanceof CompoundContainer && ((CompoundContainer)container).contains(BCChestBlockEntity.this);
                }
            }
        };
    }

    public void forceAuxiliaryBlock(@Nonnull BCChestBlock auxiliaryBlock) {
        this.auxiliaryBlock = auxiliaryBlock;
    }

    public int getContainerSize() {
        return 27;
    }

    protected Component getDefaultName() {
        return Component.translatable("container.chest");
    }

    public void load(CompoundTag p_155349_) {
        super.load(p_155349_);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
    }

    public static void lidAnimateTick(Level p_155344_, BlockPos p_155345_, BlockState p_155346_, BCChestBlockEntity p_155347_) {
        p_155347_.chestLidController.tickLid();
    }

    static void playSound(Level p_155339_, BlockPos p_155340_, BlockState p_155341_, SoundEvent p_155342_) {
        ChestType chesttype = p_155341_.getValue(ChestBlock.TYPE);
        if (chesttype != ChestType.LEFT) {
            double d0 = (double)p_155340_.getX() + 0.5D;
            double d1 = (double)p_155340_.getY() + 0.5D;
            double d2 = (double)p_155340_.getZ() + 0.5D;
            if (chesttype == ChestType.RIGHT) {
                Direction direction = ChestBlock.getConnectedDirection(p_155341_);
                d0 += (double)direction.getStepX() * 0.5D;
                d2 += (double)direction.getStepZ() * 0.5D;
            }

            p_155339_.playSound(null, d0, d1, d2, p_155342_, SoundSource.BLOCKS, 0.5F, p_155339_.random.nextFloat() * 0.1F + 0.9F);
        }
    }

    public boolean triggerEvent(int p_59114_, int p_59115_) {
        if (p_59114_ == 1) {
            this.chestLidController.shouldBeOpen(p_59115_ > 0);
            return true;
        } else {
            return super.triggerEvent(p_59114_, p_59115_);
        }
    }

    public void startOpen(Player p_59120_) {
        if (!this.remove && !p_59120_.isSpectator()) {
            this.openersCounter.incrementOpeners(p_59120_, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    public void stopOpen(Player p_59118_) {
        if (!this.remove && !p_59118_.isSpectator()) {
            this.openersCounter.decrementOpeners(p_59118_, this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    protected NonNullList<ItemStack> getItems() {
        return this.items;
    }

    protected void setItems(NonNullList<ItemStack> p_59110_) {
        this.items = p_59110_;
    }

    public float getOpenNess(float p_59080_) {
        return this.chestLidController.getOpenness(p_59080_);
    }

    public static int getOpenCount(BlockGetter p_59087_, BlockPos p_59088_) {
        BlockState blockstate = p_59087_.getBlockState(p_59088_);
        if (blockstate.hasBlockEntity()) {
            BlockEntity blockentity = p_59087_.getBlockEntity(p_59088_);
            if (blockentity instanceof BCChestBlockEntity) {
                return ((BCChestBlockEntity)blockentity).openersCounter.getOpenerCount();
            }
        }

        return 0;
    }

    public static void swapContents(BCChestBlockEntity p_59104_, BCChestBlockEntity p_59105_) {
        NonNullList<ItemStack> nonnulllist = p_59104_.getItems();
        p_59104_.setItems(p_59105_.getItems());
        p_59105_.setItems(nonnulllist);
    }

    protected AbstractContainerMenu createMenu(int p_59082_, Inventory p_59083_) {
        return ChestMenu.threeRows(p_59082_, p_59083_, this);
    }



    public void recheckOpen() {
        if (!this.remove) {
            this.openersCounter.recheckOpeners(this.getLevel(), this.getBlockPos(), this.getBlockState());
        }

    }

    protected void signalOpenCount(Level p_155333_, BlockPos p_155334_, BlockState p_155335_, int p_155336_, int p_155337_) {
        Block block = p_155335_.getBlock();
        p_155333_.blockEvent(p_155334_, block, 1, p_155337_);
    }


    public BCChestBlock getAuxiliaryBlock() {
        return this.auxiliaryBlock;
    }
}
