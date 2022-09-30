package bottomtextdanny.braincell.libraries.registry.entity_type_helper;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries._minor.entity.EntityParams;
import bottomtextdanny.braincell.libraries.entity_variant.Form;
import bottomtextdanny.braincell.libraries.entity_variant.IndexedFormManager;
import bottomtextdanny.braincell.libraries.entity_variant.StringedFormManager;
import bottomtextdanny.braincell.libraries.entity_variant.VariableModule;
import bottomtextdanny.braincell.tables.BCEntityKeys;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public class BCSpawnEggItem extends SpawnEggItem {
    public static final String LAYER_1_LABEL = "layer1";
    public static final String LAYER_2_LABEL = "layer2";
    public static final SpawnLogic DEFAULT_LOGIC = (type, world, direction, blockPos, itemStack, player) -> {
        BlockPos blockPos1;
        BlockState blockState = world.getBlockState(blockPos);
        if (blockState.getCollisionShape(world, blockPos).isEmpty()) {
            blockPos1 = blockPos;
        } else {
            blockPos1 = blockPos.relative(direction);
        }

        if (type.spawn((ServerLevel)world, itemStack, player, blockPos1, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockPos, blockPos1) && direction == Direction.UP) != null) {
            itemStack.shrink(1);
        }
    };
    private final Supplier<? extends EntityType<? extends Mob>> typeSupplier;
    private final SpawnLogic spawnLogic;
    private final boolean unstable;

    public BCSpawnEggItem(int firstTint, int secondTint, boolean unstable, SpawnLogic logic, Supplier<? extends EntityType<? extends Mob>> typeSupplier, Properties properties) {
        super(null, firstTint, secondTint, properties);
        Braincell.common().getEntityCoreDataDeferror().putEgg(this);
        this.typeSupplier = typeSupplier;
        this.spawnLogic = logic;
        this.unstable = unstable;
    }

    public static Builder createBuilder(int firstTint, int secondTint) {
        return new Builder(firstTint, secondTint);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (!(world instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = context.getItemInHand();
            BlockPos blockpos = context.getClickedPos();
            Direction direction = context.getClickedFace();
            BlockState blockstate = world.getBlockState(blockpos);
            if (blockstate.is(Blocks.SPAWNER)) {
                BlockEntity tileentity = world.getBlockEntity(blockpos);
                if (tileentity instanceof SpawnerBlockEntity spawner) {
                    BaseSpawner abstractspawner = spawner.getSpawner();
                    EntityType<?> entitytype1 = this.getType(itemstack.getTag());
                    abstractspawner.setEntityId(entitytype1);
                    tileentity.setChanged();
                    world.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }
            this.spawnLogic.forSpawn(
                    this.getType(itemstack.getTag()),
                    world,
                    direction,
                    blockpos,
                    itemstack,
                    context.getPlayer());
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);

        if (!(worldIn instanceof ServerLevel)) return InteractionResultHolder.pass(itemstack);

        HitResult raytraceresult = getPlayerPOVHitResult(worldIn, playerIn, ClipContext.Fluid.SOURCE_ONLY);

        if (raytraceresult.getType() == HitResult.Type.MISS && playerIn.isCreative() && playerIn.isShiftKeyDown()) {
            CompoundTag tag = itemstack.getOrCreateTag();

            EntityType<? extends Mob> type = getType(tag);

            if (EntityParams.has(type, BCEntityKeys.STRINGED_FORMS.get())) {
                StringedFormManager stringedFormManager = EntityParams.get(type, BCEntityKeys.STRINGED_FORMS.get());
                Iterator<? extends Map.Entry<String, ? extends Form<?>>> iterator = stringedFormManager.iterator();

                if (!iterator.hasNext()) return InteractionResultHolder.pass(itemstack);

                String data = tag.getString(VariableModule.VARIANT_TAG);
                String value = null;

                if (data.equals("")) {
                    value = iterator.next().getKey();
                } else {
                    boolean get = false;
                    String initialId = iterator.next().getKey();

                    if (data.equals(initialId)) {
                        get = true;
                    }

                    one: {
                        while (iterator.hasNext()) {
                            String id = iterator.next().toString();

                            if (data.equals(id)) {
                                get = true;
                            } else if (get) {
                                value = id;
                                //tag.putString(VariableModule.VARIANT_TAG, id);
                                break one;
                            }
                        }

                        if (get) {
                            value = initialId;
                            //tag.putString(VariableModule.VARIANT_TAG, initialId);
                        }
                    }
                }

                if (value != null) {
                    tag.putString(VariableModule.VARIANT_TAG, value);
                    Form<?> form = stringedFormManager.getForm(value);
                    tag.putInt(LAYER_1_LABEL, form.primaryColor());
                    tag.putInt(LAYER_2_LABEL, form.secondaryColor());
                    return InteractionResultHolder.success(itemstack);
                }
            } else if (EntityParams.has(type, BCEntityKeys.INDEXED_FORMS.get())) {
                IndexedFormManager indexedFormManager = EntityParams.get(type, BCEntityKeys.INDEXED_FORMS.get());
                Tag variantTag = tag.get(VariableModule.VARIANT_TAG);
                Form<?> form;

                if (variantTag instanceof IntTag valueTag) {
                    int index = (valueTag.getAsInt() + 1) % indexedFormManager.getSize();
                    form = indexedFormManager.getForm(index);
                    tag.putInt(VariableModule.VARIANT_TAG, index);
                } else {
                    form = indexedFormManager.getForm(0);
                    tag.putInt(VariableModule.VARIANT_TAG, 0);
                }

                tag.putInt(LAYER_1_LABEL, form.primaryColor());
                tag.putInt(LAYER_2_LABEL, form.secondaryColor());
            }

        } else if (raytraceresult.getType() == HitResult.Type.BLOCK) {
            BlockHitResult blockraytraceresult = (BlockHitResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getBlockPos();

            if (!worldIn.getBlockState(blockpos).getFluidState().isSource()) {
                return InteractionResultHolder.pass(itemstack);
            } else if (worldIn.mayInteract(playerIn, blockpos) && playerIn.mayUseItemAt(blockpos, blockraytraceresult.getDirection(), itemstack)) {
                this.spawnLogic.forSpawn(this.getType(itemstack.getTag()), worldIn, blockraytraceresult.getDirection(), blockpos, itemstack, playerIn);
                return InteractionResultHolder.success(itemstack);
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }

        return InteractionResultHolder.pass(itemstack);
    }

    @Override
    public Component getName(ItemStack itemStack) {
        Component name = super.getName(itemStack);

        if (!(name instanceof MutableComponent mu)) return name;

        CompoundTag tag = itemStack.getTag();

        if (tag != null && !tag.isEmpty()) {
            Tag variantTag = tag.get(VariableModule.VARIANT_TAG);
            EntityType<?> type = getType(tag);
            if (variantTag instanceof StringTag && EntityParams.has(type, BCEntityKeys.STRINGED_FORMS.get())) {
                String value = variantTag.getAsString();
                StringedFormManager stringedFormManager = EntityParams.get(type, BCEntityKeys.STRINGED_FORMS.get());

                if (stringedFormManager.hasForm(value)) {
                    mu.append(" | ");
                    mu.append(stringedFormManager.getForm(value).name());
                }
            } else if (variantTag instanceof IntTag && EntityParams.has(type, BCEntityKeys.INDEXED_FORMS.get())) {
                int value = ((IntTag) variantTag).getAsInt();
                IndexedFormManager indexedFormManager = EntityParams.get(type, BCEntityKeys.INDEXED_FORMS.get());

                if (value >= 0 && value < indexedFormManager.getSize()) {
                    mu.append(" | ");
                    mu.append(indexedFormManager.getForm(value).name());
                }
            }
        }

        return name;
    }

    public static int getColor(ItemStack itemStack, int layer) {
        CompoundTag tag = itemStack.getTag();
        if (tag != null && !tag.isEmpty() && tag.contains(LAYER_1_LABEL) && tag.contains(LAYER_2_LABEL)) {
            int color = layer == 0 ? tag.getInt(LAYER_1_LABEL) : tag.getInt(LAYER_2_LABEL);
            if (color >= 0) return color;
        }
        return ((BCSpawnEggItem)itemStack.getItem()).getColor(layer);
    }

    @Deprecated
    @Override
    public int getColor(int layer) {
        return super.getColor(layer);
    }

    @Override
    public EntityType<? extends Mob> getType(@Nullable final CompoundTag p_208076_1_) {
        return this.typeSupplier.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (this.unstable) {
            tooltip.add(Component.translatable("description.braincell.unstable").setStyle(Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED)).withBold(true)));
        }
    }

    @FunctionalInterface
    public interface SpawnLogic {
        void forSpawn(EntityType<?> type, Level world, Direction direction, BlockPos blockPos, ItemStack itemStack, Player player);
    }

    public static final class Builder {
        private final int firstTint;
        private final int secondTint;
        private short sort = -1;
        private boolean unstable;
        private SpawnLogic logic = DEFAULT_LOGIC;
        private Properties properties;
        private Supplier<? extends EntityType<? extends Mob>> typeSupplier;

        private Builder(int firstTint, int secondTint) {
            super();
            this.firstTint = firstTint;
            this.secondTint = secondTint;
        }

        public Builder unstable() {
            this.unstable = true;
            return this;
        }

        public Builder sorted(short sortValue) {
            this.sort = sortValue;
            return this;
        }

        public Builder setLogic(SpawnLogic logic) {
            this.logic = logic;
            return this;
        }

        public Builder setTypeSupplier(Supplier<? extends EntityType<? extends Mob>> typeSupplier) {
            this.typeSupplier = typeSupplier;
            return this;
        }

        public Builder properties(Properties properties) {
            this.properties = properties;
            return this;
        }

        public BCSpawnEggItem build() {
            return new BCSpawnEggItem(this.firstTint, this.secondTint, this.unstable, this.logic, this.typeSupplier, this.properties);
        }

        public short getSort() {
            return this.sort;
        }
    }
}