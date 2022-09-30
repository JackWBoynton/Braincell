package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.libraries.registry.ContextualSerializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import bottomtextdanny.braincell.tables.BCContextualSerializers;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

public class CollisionManager extends AbstractCollisionManager {
    public static final int EARLY_ACCESS_DO_NOT_INTEGRATE = -4;
    public static final int EARLY_ACCESS_DO_INTEGRATE = -3;
    public static final int EARLY_DISCARD_IF_COLLIDING = -2;
    public static final int EARLY_DISCARD_IF_COLLIDING_OR_INTEGRATE = -1;
    public static final int ACCESS_DO_NOT_INTEGRATE = 0;
    public static final int ACCESS_DO_INTEGRATE = 1;
    public static final int DISCARD_IF_COLLIDING = 2;
    public static final int DISCARD_IF_COLLIDING_OR_INTEGRATE = 3;

    public CollisionManager() {
        super();
    }

    public CollisionManager(CompoundTag tag) {
        super(tag);
    }

    @Override
    public boolean onAddition(int op, SegmentTicket ticket) {
        return onChildAddition(op, ticket);
    }

    @Override
    public boolean onChildAddition(int op, SegmentTicket ticket) {
        if (op == EARLY_ACCESS_DO_NOT_INTEGRATE) return true;

        Segment<?> segment = ticket.segment();
        LongSet sections = new LongOpenHashSet();
        BoundingBox boundingBox = segment.getStructureBoundingBox();
        computeBoxSections(sections, boundingBox);

        if (op == EARLY_ACCESS_DO_INTEGRATE) {
            integrateSegmentSectionsToMap(boundingBox, sections);
            return true;
        }

        boolean colliding = isBoxColliding(sections, boundingBox);

        if (op == EARLY_DISCARD_IF_COLLIDING_OR_INTEGRATE) {
            if (colliding) return false;
            else {
                integrateSegmentSectionsToMap(boundingBox, sections);
                return true;
            }
        } else return op != EARLY_DISCARD_IF_COLLIDING || !colliding;
    }

    public boolean onBoxAddition(int op, BoundingBox boundingBox) {
        if (op == EARLY_ACCESS_DO_NOT_INTEGRATE) return true;

        LongSet sections = new LongOpenHashSet();
        computeBoxSections(sections, boundingBox);

        if (op == EARLY_ACCESS_DO_INTEGRATE) {
            integrateSegmentSectionsToMap(boundingBox, sections);
            return true;
        }

        boolean colliding = isBoxColliding(sections, boundingBox);

        if (op == EARLY_DISCARD_IF_COLLIDING_OR_INTEGRATE) {
            if (colliding) return false;
            else {
                integrateSegmentSectionsToMap(boundingBox, sections);
                return true;
            }
        } else return op != EARLY_DISCARD_IF_COLLIDING || !colliding;
    }

    @Override
    public boolean doesPreSolving() {
        return true;
    }

    @Override
    public boolean onPreSolving(int op, SegmentTicket ticket) {
        if (op == ACCESS_DO_NOT_INTEGRATE || op == DISCARD_IF_COLLIDING) return true;
        else if (op != ACCESS_DO_INTEGRATE && op != DISCARD_IF_COLLIDING_OR_INTEGRATE) return true;

        Segment<?> segment = ticket.segment();
        LongSet sections = new LongOpenHashSet();
        computeBoxSections(sections, segment.getStructureBoundingBox());

        integrateSegmentSectionsToMap(segment.getStructureBoundingBox(), sections);
        return true;
    }

    @Override
    public boolean onSolving(int op, SegmentTicket ticket) {
        if (op == ACCESS_DO_INTEGRATE || op == ACCESS_DO_NOT_INTEGRATE) return true;
        else if (op != DISCARD_IF_COLLIDING && op != DISCARD_IF_COLLIDING_OR_INTEGRATE) return true;

        Segment<?> segment = ticket.segment();
        LongSet sections = new LongOpenHashSet();
        computeBoxSections(sections, segment.getStructureBoundingBox());

        boolean colliding = isBoxColliding(sections, segment.getStructureBoundingBox());

        return !colliding;
    }

    @Override
    public Wrap<? extends ContextualSerializer<? extends SegmentAdmin, Segment<?>>> serializer() {
        return BCContextualSerializers.COLLISION_MANAGER;
    }
}
