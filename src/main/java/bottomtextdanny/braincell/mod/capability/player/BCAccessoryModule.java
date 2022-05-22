package bottomtextdanny.braincell.mod.capability.player;

import bottomtextdanny.braincell.base.function.Clearable;
import bottomtextdanny.braincell.mod.capability.CapabilityModule;
import bottomtextdanny.braincell.mod.capability.player.accessory.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.base.SparedHashCollection;
import bottomtextdanny.braincell.mod._base.BCEvaluations;
import bottomtextdanny.braincell.mod.capability.player.accessory.*;
import net.minecraft.Util;
import net.minecraft.client.Options;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.util.*;

public class BCAccessoryModule extends CapabilityModule<Player, BCPlayerCapability> {
    public static final int STACK_ACCESSORY_CACHE_ROW = 10;
    private final float[] lesserAttributes = Util.make(() -> {
        float[] attributes = new float[MiniAttribute.size()];
        for (int i = 0; i < attributes.length; i++) {
            attributes[i] = MiniAttribute.getModifierByIndex(i).baseValue;
        }
        return attributes;
    });
    private final Clearable<List<IAccessory>> allAccessoryList;
    private final List<AttributeModifierAccessory> modifierAccessoryList;
    private final EnumMap<ModifierType, LinkedList<AttributeModifierAccessory>> accessoryAttributeModifiers;
    private final Map<Class<? extends IAccessory>, IAccessory> currentAccessoryTypes;
    private final TreeSet<IQueuedJump> jumps;
    private final SparedHashCollection<AccessoryKey<?>, Object[]> stackAccessoryCache;
    private final HandAccessories handManager;
    public boolean goodOnGround;


    public BCAccessoryModule(BCPlayerCapability capability) {
        super("accessories", capability);
        this.modifierAccessoryList = Lists.newLinkedList();
        this.handManager = new HandAccessories();
        this.allAccessoryList = Clearable.of(() -> {
            ImmutableList.Builder<IAccessory> list = ImmutableList.builder();
            list.add(this.handManager.getMain());
            list.add(this.handManager.getOff());
            AllAccessoryCollectorEvent event = new AllAccessoryCollectorEvent(list, getHolder());
            MinecraftForge.EVENT_BUS.post(event);
            return list.build();
        });
        this.accessoryAttributeModifiers = new EnumMap<>(ModifierType.class);
        this.currentAccessoryTypes = Maps.newIdentityHashMap();
        this.jumps = new TreeSet<>(Comparator.comparingInt(right -> right.priority().ordinal()));
        this.stackAccessoryCache = new SparedHashCollection<>(STACK_ACCESSORY_CACHE_ROW);
    }

    public void tick() {
        this.allAccessoryList.clear();
        this.allAccessoryList.get().forEach(IAccessory::tick);

        if (!getHolder().level.isClientSide) {
            List<MiniAttribute> miniValues = MiniAttribute.values();

            for (MiniAttribute miniValue : miniValues) {
                resetLesserAttribute(miniValue);
                modifyLesserAttribute(miniValue, BCEvaluations.LESSER_MODIFIER_VALUE.test(this, miniValue));
            }

            replaceAttributeModifier(ModifierType.ATTACK_DAMAGE_ADD, "Accessory attack damage addition", BCEvaluations.MODIFIER_VALUE.test(this, ModifierType.ATTACK_DAMAGE_ADD));
            replaceAttributeModifier(ModifierType.ATTACK_SPEED_ADD, "Accessory attack speed addition", BCEvaluations.MODIFIER_VALUE.test(this, ModifierType.ATTACK_SPEED_ADD));
            replaceAttributeModifier(ModifierType.ATTACK_KNOCKBACK_ADD, "Accessory attack knockback addition", BCEvaluations.MODIFIER_VALUE.test(this, ModifierType.ATTACK_KNOCKBACK_ADD));
            replaceAttributeModifier(ModifierType.KNOCKBACK_RESISTANCE_ADD, "Accessory knockback resistance addition", BCEvaluations.MODIFIER_VALUE.test(this, ModifierType.KNOCKBACK_RESISTANCE_ADD));
            replaceAttributeModifier(ModifierType.ARMOR_ADD, "Accessory armor addition", BCEvaluations.MODIFIER_VALUE.test(this, ModifierType.ARMOR_ADD));
            replaceAttributeModifier(ModifierType.LUCK_ADD, "Accessory luck addition", BCEvaluations.MODIFIER_VALUE.test(this, ModifierType.LUCK_ADD));
            replaceAttributeModifier(ModifierType.MOVEMENT_SPEED_MLT, "Accessory speed multiplier", BCEvaluations.MODIFIER_VALUE.test(this, ModifierType.MOVEMENT_SPEED_MLT));
            replaceAttributeModifier(ModifierType.KNOCKBACK_RESISTANCE_MLT, "Accessory knockback resistance multiplier", BCEvaluations.MODIFIER_VALUE.test(this, ModifierType.KNOCKBACK_RESISTANCE_MLT));
        }

        handleItemstackAccessories();
    }

    public void handleItemstackAccessories() {
        this.handManager.updatePrevious();
        handleHand(InteractionHand.MAIN_HAND);
        handleHand(InteractionHand.OFF_HAND);
    }

    public void handleHand(InteractionHand handSide) {
        int handId = handSide.ordinal();
        StackAccessory accessory = HandAccessories.ACCESSORY_FOR_HAND.get(handId).apply(this);
        StackAccessory accessoryO = HandAccessories.ACCESSORYO_FOR_HAND.get(handId).apply(this);
        ItemStack itemStack = getHolder().getItemInHand(handSide);

        if (!(itemStack.getItem() instanceof StackAccessoryProvider)) {
            if (accessoryO instanceof StackAccSoftSave softSave) {
                this.stackAccessoryCache.insert(accessoryO.getKey(), softSave.save());
            }
            HandAccessories.SET_BY_HAND.get(handId).accept(this, StackAccessory.EMPTY);
        } else if (accessory.getItem() != itemStack.getItem()) {
            onAccessoryRemoval(accessory);
            StackAccessoryProvider provider = (StackAccessoryProvider)itemStack.getItem();
            StackAccessory newAccessory = provider.getKey().create(getHolder());
            newAccessory.prepare(itemStack, handSide);
            onAccessoryAddition(newAccessory);
            HandAccessories.SET_BY_HAND.get(handId).accept(this, newAccessory);

            if (accessory instanceof StackAccSoftSave softSave) {
                this.stackAccessoryCache.insert(accessoryO.getKey(), softSave.save());
            }

            if (newAccessory instanceof StackAccSoftSave saveAcc) {
                Object[] fetchedSave = this.stackAccessoryCache.look(newAccessory.getKey());

                if (fetchedSave != null) {
                    saveAcc.retrieve(ObjectFetcher.of(fetchedSave));
                }
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handleClientKeybinds(Options settings) {
        this.allAccessoryList.get().forEach(accessory -> accessory.keyHandler(settings));
    }

    public final void replaceAttributeModifier(ModifierType modifierType, String name, double newValue) {
        Attribute attribute = modifierType.attribute.get();
        UUID uuid = modifierType.uuid;
        AttributeInstance attributeInst = getHolder().getAttribute(attribute);

        if (attributeInst != null) {
            if (attributeInst.getModifier(uuid) != null) {
                if (attributeInst.getModifier(uuid).getAmount() != newValue) {
                    attributeInst.removeModifier(uuid);
                    attributeInst.addTransientModifier(new AttributeModifier(uuid, name, newValue, modifierType.operation));
                }
            } else {
                attributeInst.addTransientModifier(new AttributeModifier(uuid, name, newValue, modifierType.operation));
            }
        }
    }

    private void modifyLesserAttribute(MiniAttribute attribute, float factor) {
        this.lesserAttributes[attribute.getIndex()] += factor;
        this.lesserAttributes[attribute.getIndex()] = Mth.clamp(this.lesserAttributes[attribute.getIndex()], attribute.clampMin, attribute.clampMax);
    }

    private void resetLesserAttribute(MiniAttribute attribute) {
        this.lesserAttributes[attribute.getIndex()] = attribute.baseValue;
    }

    public float[] getLesserAttributes() {
        return this.lesserAttributes;
    }

    public List<AttributeModifierAccessory> getOrCreateAccessoryAttributeModifiers(ModifierType attributeModifierTarget) {
        if (!this.accessoryAttributeModifiers.containsKey(attributeModifierTarget)) {
            this.accessoryAttributeModifiers.put(attributeModifierTarget, Lists.newLinkedList());
        }
        return this.accessoryAttributeModifiers.get(attributeModifierTarget);
    }

    public void addAccessoryAttributeModifier(ModifierType attributeModifierTarget, AttributeModifierAccessory accessory) {
        if (!this.accessoryAttributeModifiers.containsKey(attributeModifierTarget)) {
            this.accessoryAttributeModifiers.put(attributeModifierTarget, Lists.newLinkedList());
        }
        this.accessoryAttributeModifiers.get(attributeModifierTarget).add(accessory);
    }

    public void deleteAccessoryAttributeModifier(ModifierType attributeModifierTarget, AttributeModifierAccessory accessory) {
        this.accessoryAttributeModifiers.get(attributeModifierTarget).remove(accessory);
    }

    public void addToCurrentAccessorySet(IAccessory accessory) {
        if (!(accessory instanceof StackAccessory)) {
            this.currentAccessoryTypes.putIfAbsent(accessory.getClass(), accessory);
        }
    }

    public void deleteFromCurrentAccessorySet(Class<? extends IAccessory> accessoryClazz) {
        this.currentAccessoryTypes.remove(accessoryClazz);
    }

    public boolean isAccessoryTypeCurrent(Class<? extends IAccessory> accessoryClazz) {
        return this.currentAccessoryTypes.containsKey(accessoryClazz);
    }

    public void onAccessoryAddition(IAccessory accessory) {
        this.addToCurrentAccessorySet(accessory);

        if (accessory instanceof IJumpQueuerAccessory jumpQueuer) {
            Collections.addAll(this.jumps, jumpQueuer.provideJumps());
        }

        if (accessory instanceof AttributeModifierAccessory attributeModifier) {
            List<ModifierType> attributes = attributeModifier.modifiedAttributes();

            for (ModifierType attribute : attributes) {
                addAccessoryAttributeModifier(attribute, attributeModifier);
            }

            this.modifierAccessoryList.add(attributeModifier);
        }
    }

    public void onAccessoryRemoval(IAccessory accessory) {
        deleteFromCurrentAccessorySet(accessory.getClass());

        if (accessory instanceof IJumpQueuerAccessory jumpQueuer) {
            for (IQueuedJump iQueuedJump : jumpQueuer.provideJumps()) {
                this.jumps.remove(iQueuedJump);
            }
        }

        if (accessory instanceof AttributeModifierAccessory attributeModifier) {
            List<ModifierType> attributes = attributeModifier.modifiedAttributes();

            for (ModifierType attribute : attributes) {
                deleteAccessoryAttributeModifier(attribute, attributeModifier);
            }

            this.modifierAccessoryList.remove(attributeModifier);
        }
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <A extends IAccessory> A getAccessoryByType(Class<A> accessory) {
        if (this.currentAccessoryTypes.containsKey(accessory)) {
            return (A) this.currentAccessoryTypes.get(accessory);
        }
        return null;
    }

    public float getLesserModifier(MiniAttribute attribute) {
        return this.lesserAttributes[attribute.getIndex()];
    }

    public TreeSet<IQueuedJump> getJumpSet() {
        return this.jumps;
    }

    public List<AttributeModifierAccessory> getAttributeModifierAccessoryList() {
        return this.modifierAccessoryList;
    }

    public List<IAccessory> getAllAccessoryList() {
        return this.allAccessoryList.get();
    }

    public HandAccessories getHandManager() {
        return this.handManager;
    }

    @Override
    public void serializeNBT(CompoundTag nbt) {}

    @Override
    public void deserializeNBT(CompoundTag nbt) {}
}
