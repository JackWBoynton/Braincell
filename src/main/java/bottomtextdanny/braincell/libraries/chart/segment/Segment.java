package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregenerations;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentDecorator;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentDecorators;
import bottomtextdanny.braincell.libraries.registry.ContextualSerializable;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.tables.BCContextualSerializers;
import bottomtextdanny.braincell.tables.BCSerializers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import javax.annotation.Nullable;
import java.util.*;

import static bottomtextdanny.braincell.libraries.registry.Serializer.*;

public abstract class Segment<DATA extends SegmentData> implements ContextualSerializable<SegmentAdmin> {
    public static final String DECORATOR_LABEL = "decorator";
    public static final String TICKET_LABEL = "ticket";
    public static final String ROTATION_LABEL = "rotation";
    public static final String PREGEN_LABEL = "pregen";
    public static final String POS_LABEL = "pos";
    public static final String SEGMENT_LABEL = "segment";
    public static final String LOCAL_BOX_LABEL = "local_box";
    public static final String DISCARDED_LABEL = "discarded";
    public static final String OP_LABEL = "op";
    public static final String CHILDREN_LABEL = "children";
    protected static final RandomSource NT_RANDOM = RandomSource.create();
    public static final String ADMIN_LABEL = "admin";
    protected static BoundingBox ZERO = new BoundingBox(BlockPos.ZERO);
    @Nullable
    private SegmentDecorator decorators;
    @Nullable
    private SegmentTicket ticket = null;
    @Nullable
    private List<SegmentTicket> children;
    @Nullable
    SegmentAdmin administrator;
    @Nullable
    BlockPos structurePos;
    @DecorationStage
    @Nullable
    private BlockPos levelPos;
    @Nullable
    private BoundingBox localBox;
    int op;
    boolean discarded;

    public Segment() {
    }

    public Segment(CompoundTag root, @Nullable SegmentAdmin admin) {
        op = root.getInt(OP_LABEL);
        discarded = root.getBoolean(DISCARDED_LABEL);

        if (admin != null) administrator = admin;
        else if (root.contains(ADMIN_LABEL)) {
            administrator = unpack(root.get(ADMIN_LABEL), (SegmentAdmin) null, this);
        }

        if (root.contains(LOCAL_BOX_LABEL)) {
            localBox = BCSerializers.BOUNDING_BOX.get().fromTag(root.get(LOCAL_BOX_LABEL));
        }

        if (root.contains(CHILDREN_LABEL)) {
            children = new LinkedList<>();

            root.getList(CHILDREN_LABEL, 10).forEach(childTagUncasted -> {
                if (childTagUncasted instanceof CompoundTag childTag) {
                    SegmentTicket unpackedTicket = Serializer.unpackDirectly(childTag, admin, BCContextualSerializers.SEGMENT_TICKET.get());

                    if (unpackedTicket != null) children.add(unpackedTicket);
                }
            });
        }

        if (root.contains(TICKET_LABEL)) {
            CompoundTag ticketTag = root.getCompound(TICKET_LABEL);

            ticket = SegmentTicket.orientable(
                    unpackDirectly(ticketTag.get(POS_LABEL), BCSerializers.BLOCKPOS.get()),
                    Serializer.unpack(ticketTag.get(PREGEN_LABEL), SegmentPregenerations.spgPass()),
                    unpackDirectly(ticketTag.get(ROTATION_LABEL), BCSerializers.ROTATION.get()),
                    this
            );
        }

        if (root.contains(DECORATOR_LABEL)) {
            decorators = unpack(root.get(DECORATOR_LABEL), SegmentDecorators.sdBlank());
        }
    }

    public CompoundTag rootData() {
        CompoundTag root = childData();

        if (administrator != null) {
            root.put(ADMIN_LABEL, pack(administrator, this));
        }

        return root;
    }

    public CompoundTag childData() {
        CompoundTag root = new CompoundTag();

        root.putInt(OP_LABEL, op);
        root.putBoolean(DISCARDED_LABEL, discarded);

        if (localBox != null)
            root.put(LOCAL_BOX_LABEL, BCSerializers.BOUNDING_BOX.get().toTag(localBox));

        if (children != null) {
            ListTag childrenTag = new ListTag();

            children.forEach(childTicket -> {
                childrenTag.add(Serializer.packDirectly(childTicket, administrator, BCContextualSerializers.SEGMENT_TICKET.get()));
            });

            root.put(CHILDREN_LABEL, childrenTag);
        }

        if (ticket != null) {
            CompoundTag ticketTag = new CompoundTag();

            ticketTag.put(POS_LABEL, packDirectly(ticket.position(), BCSerializers.BLOCKPOS.get()));
            ticketTag.put(PREGEN_LABEL, pack(ticket.modifier()));
            ticketTag.put(ROTATION_LABEL, packDirectly(ticket.rotation(), BCSerializers.ROTATION.get()));

            root.put(TICKET_LABEL, ticketTag);
        }

        if (decorators != null) {
            root.put(DECORATOR_LABEL, pack(decorators));
        }

        return root;
    }

    public void addLocalDecorator(SegmentDecorator decorator) {
        if (decorators == null) decorators = decorator;

        decorators.append(decorator);
    }

    public boolean addChild(int adminOp, SegmentTicket childTicket) {
        if (ticket != null) childTicket = SegmentTicket.offset(ticket, childTicket);

        Segment<?> childSegment = childTicket.segment();

        if (!(childSegment.ticket == null) || childSegment == this) return false;

        if (administrator != null) {
            childSegment.ticket = childTicket;
            childSegment.structurePos = childTicket.position();
            if (administrator.onChildAddition(adminOp, childTicket)) {
                if (children == null) children = new LinkedList<>();
                childSegment.administrator = administrator;
                childSegment.op = adminOp;
                childSegment.discarded = false;
                return children.add(childTicket);
            }
            childSegment.structurePos = null;
            childSegment.ticket = null;
            return false;
        } else {
            if (children == null) children = new LinkedList<>();
            childSegment.structurePos = childTicket.position();
            childSegment.ticket = childTicket;
            childSegment.discarded = false;
            return children.add(childTicket);
        }
    }

    public boolean addChild(SegmentTicket childTicket) {
        return addChild(0, childTicket);
    }

    protected void makeRecursive(WorldGenLevel level,
                              BlockPos root,
                              BlockPos pos,
                              Rotation rotation) {

        if (children != null) {

            if (decorators != null)
                makeLocal(level, decorators, pos, rotation);

            SegmentAdmin administrator = this.administrator;

            if (administrator != null) {
                if (administrator.doesPreSolving()) {
                    children.removeIf(childTicket -> {
                        Segment<?> childSegment = childTicket.segment();

                        if (!administrator.onSolving(childSegment.op, childTicket)) {
                            childSegment.discarded = true;
                            return true;
                        }

                        childSegment.makeRecursive(
                                level,
                                root,
                                childSegment.levelPos,
                                childTicket.rotation());
                        return false;
                    });
                } else {
                    children.removeIf(childTicket -> {
                        Segment<?> childSegment = childTicket.segment();
                        childSegment.levelPos = childTicket.modifier().process(level, root.offset(childTicket.position()));
                        childSegment.structurePos = childSegment.levelPos.subtract(root);

                        if (childSegment.levelPos == null) {
                            childSegment.discarded = true;
                            return true;
                        }

                        if (!administrator.onSolving(childSegment.op, childTicket)) {
                            childSegment.discarded = true;
                            return true;
                        }

                        childSegment.makeRecursive(
                                level,
                                root,
                                childSegment.levelPos,
                                childTicket.rotation());

                        return false;
                    });
                }
            } else {
                children.removeIf(childTicket -> {
                    Segment<?> childSegment = childTicket.segment();
                    childSegment.levelPos = childTicket.modifier().process(level, root.offset(childTicket.position()));

                    if (childSegment.levelPos == null) {
                        childSegment.discarded = true;
                        return true;
                    }

                    childSegment.structurePos = childSegment.levelPos.subtract(root);

                    childSegment.makeRecursive(
                            level,
                            root,
                            childSegment.levelPos,
                            childTicket.rotation());

                    return false;
                });
            }
        } else if (decorators != null) makeLocal(level, decorators, pos, rotation);
    }

    protected void recursivePreSolving(SegmentAdmin admin,
                                       WorldGenLevel level,
                                       BlockPos root,
                                       BlockPos pos,
                                       Rotation rotation) {
        if (children != null) {
            children.removeIf(childTicket -> {
                Segment<?> childSegment = childTicket.segment();
                childSegment.levelPos = childTicket.modifier().process(level, root.offset(childTicket.position()));

                if (childSegment.levelPos == null) {
                    childSegment.discarded = true;
                    return true;
                }

                childSegment.structurePos = childSegment.levelPos.subtract(root);
                admin.onPreSolving(childSegment.op, childTicket);
                childSegment.recursivePreSolving(admin, level, root, childSegment.levelPos, rotation.getRotated(childTicket.rotation()));

                return false;
            });
        }
    }

    protected void makeLocal(WorldGenLevel level, SegmentDecorator decorator, BlockPos pos, Rotation rotation) {
        construct(level, decorator, pos, rotation);
    }

    public void start(WorldGenLevel level, BlockPos pos, Rotation rotation) {
        if (discarded) return;
        SegmentAdmin administrator = this.administrator;
        if (administrator != null) {
            if (administrator.doesPreSolving()) {
                administrator.onPreSolving(op, getTicket());
                recursivePreSolving(administrator, level, pos, pos, rotation);
            }

            if (administrator.onSolving(op, getTicket())) {
                makeRecursive(level, pos, pos, rotation);
            }
        } else makeRecursive(level, pos, pos, rotation);
    }

    public abstract void construct(WorldGenLevel level, SegmentDecorator iterator, BlockPos pos, Rotation rotation);

    protected BoundingBox localBoxToStructureSpace(BoundingBox box) {
        SegmentTicket ticket = getTicket();
        BlockPos segmentPosition = structurePos == null ? ticket.position() : structurePos;

        BlockPos pos0 = segmentPosition.offset(ChartRotator.rotatedOf(ticket.rotation(), box.minX(), box.minY(), box.minZ()));
        BlockPos pos1 = segmentPosition.offset(ChartRotator.rotatedOf(ticket.rotation(), box.maxX(), box.maxY(), box.maxZ()));
        int minX = Math.min(pos0.getX(), pos1.getX());
        int minY = segmentPosition.getY() + box.minY();
        int minZ = Math.min(pos0.getZ(), pos1.getZ());
        int maxX = Math.max(pos0.getX(), pos1.getX());
        int maxY = segmentPosition.getY() + box.maxY();
        int maxZ = Math.max(pos0.getZ(), pos1.getZ());

        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Nullable
    public SegmentAdmin getAdministrator() {
        return administrator;
    }

    @Nullable
    public BlockPos getStructurePos() {
        return structurePos;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public SegmentTicket getTicket() {
        return ticket == null ? SegmentTicket.identity(this) : ticket;
    }

    public void setTicket(SegmentTicket ticket) {
        if (ticket != null)
            this.ticket = ticket;
    }

    public void setLocalBox(@Nullable BoundingBox localBox) {
        this.localBox = localBox;
    }

    public abstract BoundingBox getDefaultLocalBoundingBox();

    public BoundingBox getLocalBoundingBox() {
        return localBox;
    }

    public BoundingBox getStructureBoundingBox() {
        BoundingBox localBoundingBox = getLocalBoundingBox();
        return localBoxToStructureSpace(localBoundingBox == null ? getDefaultLocalBoundingBox() : localBoundingBox);
    }

}
