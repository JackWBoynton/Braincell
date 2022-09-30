package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.tables.BCSerializers;
import io.netty.util.collection.LongObjectHashMap;
import io.netty.util.collection.LongObjectMap;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.world.level.levelgen.structure.BoundingBox;

import java.util.*;
import java.util.function.BiConsumer;

//import static bottomtextdanny.braincell.libraries.chart.help.GridSamplers.*;
//import static bottomtextdanny.braincell.libraries.chart.help.GridWarpers.*;
//import static bottomtextdanny.braincell.libraries.chart.segment_maker.LevelBlockPredicates.*;

public abstract class AbstractCollisionManager implements SegmentAdmin {
    public static final String SERIAL_TAG = "serial";
    public static final String BOXES_TAG = "boxes";
    public static final String BOX_TAG = "box";
    public static final String SECTIONS_TAG = "sections";
    public static final String BOXES_BY_SECTION_TAG = "boxes_by_section";
    public static final String SECTIONS_BY_BOX_TAG = "sections_by_box";
    protected final LongObjectMap<List<BoundingBox>> boxesBySection;
    protected final Map<BoundingBox, LongSet> sectionsByBox;

    /*
    section storage!
     */
    public AbstractCollisionManager() {
        boxesBySection = new LongObjectHashMap<>();
        sectionsByBox = new HashMap<>();
    }

    public AbstractCollisionManager(CompoundTag tag) {
        this();

        try {
            ListTag boxesBySectionTag = tag.getList(BOXES_BY_SECTION_TAG, 10);
            ListTag sectionsByBoxTag = tag.getList(SECTIONS_BY_BOX_TAG, 10);

            boxesBySectionTag.forEach(sectionTagUncasted -> {
                if (!(sectionTagUncasted instanceof CompoundTag sectionTag)) return;

                List<BoundingBox> boxes = new LinkedList<>();

                sectionTag.getList(BOXES_TAG, 11).forEach(boxTag -> {
                    boxes.add(BCSerializers.BOUNDING_BOX.get().fromTag(boxTag));
                });

                boxesBySection.put(sectionTag.getLong(SERIAL_TAG), boxes);
            });

            sectionsByBoxTag.forEach(boxTagUncasted -> {
                if (!(boxTagUncasted instanceof CompoundTag boxTag)) return;

                LongSet sections = new LongOpenHashSet();

                boxTag.getList(SECTIONS_TAG, 4).forEach(sectionTagUncasted -> {
                    if (!(sectionTagUncasted instanceof LongTag sectionTag)) return;
                    sections.add(sectionTag.getAsLong());
                });

                sectionsByBox.put(BCSerializers.BOUNDING_BOX.get().fromTag(boxTag.get(BOX_TAG)), sections);
            });

        } catch (Exception e) {
            boxesBySection.clear();
            sectionsByBox.clear();
            e.printStackTrace();
        }
    }

    public CompoundTag serializeBase(Segment<?> segment) {
        BlockPos structurePos = segment.structurePos;

        if (structurePos == null) return new CompoundTag();

        CompoundTag tag = new CompoundTag();
        ListTag boxesBySectionTag = new ListTag();
        ListTag sectionsByBoxTag = new ListTag();

        BlockPos pos = new BlockPos(structurePos.getX() >> 4,
                structurePos.getY() >> 4,
                structurePos.getZ() >> 4);

        boxesBySection.forEach((section, boxes) -> {
            if (!sectionIsNeighbourOrItself(pos, section)) return;

            CompoundTag sectionTag = new CompoundTag();
            ListTag boxesTag = new ListTag();

            boxes.forEach(box -> {
                boxesTag.add(BCSerializers.BOUNDING_BOX.get().toTag(box));
            });

            sectionTag.put(SERIAL_TAG, LongTag.valueOf(section));
            sectionTag.put(BOXES_TAG, boxesTag);

            boxesBySectionTag.add(sectionTag);
        });

        sectionsByBox.forEach((box, sections) -> {
            CompoundTag boxTag = new CompoundTag();
            ListTag sectionsTag = new ListTag();

            sections.forEach(section -> {
                if (sectionIsNeighbourOrItself(pos, section))
                    sectionsTag.add(LongTag.valueOf(section));
            });

            if (!sectionsTag.isEmpty()) {
                boxTag.put(SECTIONS_TAG, sectionsTag);
                boxTag.put(BOX_TAG, BCSerializers.BOUNDING_BOX.get().toTag(box));

                sectionsByBoxTag.add(boxTag);
            }
        });

        tag.put(BOXES_BY_SECTION_TAG, boxesBySectionTag);
        tag.put(SECTIONS_BY_BOX_TAG, sectionsByBoxTag);

        return tag;
    }

    public BoundingBox getFirstCollidingBox(LongSet sections, Segment<?> segment) {
        Set<BoundingBox> discarded = new HashSet<>();
        return sections.longStream().mapToObj(section -> {
            if (!boxesBySection.containsKey(section)) return null;
            List<BoundingBox> boxes = boxesBySection.get(section);
            if (boxes.isEmpty()) return null;

            for (BoundingBox testedBox : boxes) {
                if (!discarded.contains(testedBox) && segment.getStructureBoundingBox() != testedBox
                        && segment.getStructureBoundingBox().intersects(testedBox)) {
                    return testedBox;
                } else {
                    discarded.add(testedBox);
                }
            }
            return null;
        }).findFirst().orElse(null);
    }

    public boolean isBoxColliding(LongSet sections, BoundingBox box) {
        Set<BoundingBox> discarded = new HashSet<>();
        return sections.longStream().anyMatch(section -> {
            if (!boxesBySection.containsKey(section)) return false;
            List<BoundingBox> boxes = boxesBySection.get(section);
            if (boxes.isEmpty()) return false;

            for (BoundingBox testedBox : boxes) {
                if (!discarded.contains(testedBox) && !box.equals(testedBox) && box.intersects(testedBox)) {
                    return true;
                } else {
                    discarded.add(testedBox);
                }
            }
            return false;
        });
    }

//    public boolean isIntegratedSegmentColliding(Segment<?> segment) {
//        if (!sectionsBySegment.containsKey(segment)) return false;
//
//        LongSet sections = sectionsBySegment.get(segment);
//
//        return sections.longStream().anyMatch(section -> {
//            if (!segmentsBy16Section.containsKey(section)) return false;
//            List<Segment<?>> segments = segmentsBy16Section.get(section);
//            if (segments.isEmpty()) return false;
//
//            for (Segment<?> testedSegment : segments) {
//                if (!testedSegment.isDiscarded() && segment != testedSegment
//                        && segment.getStructureBoundingBox().intersects(testedSegment.getStructureBoundingBox())) {
//                    return true;
//                }
//            }
//            return false;
//        });
//    }

    public void integrateSegmentSectionsToMap(BoundingBox segment, LongSet sections) {
        if (sections == null || sections.isEmpty()) return;

        if (sectionsByBox.containsKey(segment))
            sectionsByBox.get(segment).addAll(sections);
        else {
            sectionsByBox.put(segment, sections);
        }

        sections.longStream().forEach(section -> {
            if (boxesBySection.containsKey(section))
                boxesBySection.get(section).add(segment);
            else {
                List<BoundingBox> segmentList = new LinkedList<>();
                segmentList.add(segment);
                boxesBySection.put(section, segmentList);
            }
        });
    }

    public LongSet computeSegmentSections(List<BoundingBox> boxes) {
        if (boxes == null || boxes.isEmpty()) return LongSet.of();

        LongSet segmentSections = new LongOpenHashSet();

        for (BoundingBox box : boxes) {
            computeBoxSections(segmentSections, box);
        }

        long g = BlockPos.asLong(1, 2, 3);
        return segmentSections;
    }

    public void computeBoxSections(LongSet segmentSections, BoundingBox box) {
        if (box != null) {
            int sectionX = (box.minX() >> 4);
            int sectionY = (box.minY() >> 4);
            int sectionZ = (box.minZ() >> 4);
            int deltaX = (box.maxX() >> 4);
            int deltaY = (box.maxY() >> 4);
            int deltaZ = (box.maxZ() >> 4);

            for (int x = sectionX; x <= deltaX; x++) {
                for (int z = sectionZ; z <= deltaZ; z++) {
                    for (int y = sectionY; y <= deltaY; y++) {
                        segmentSections.add(BlockPos.asLong(x, y, z));
                    }
                }
            }
        }
    }

    public void iterateBySection(BiConsumer<Long, List<BoundingBox>> function) {
        boxesBySection.forEach(function);
    }

    public void iterateByBox(BiConsumer<BoundingBox, LongSet> function) {
        sectionsByBox.forEach(function);
    }

    public static boolean sectionIsNeighbourOrItself(BlockPos boundingBoxPos, long otherSection) {
        BlockPos otherPos = BlockPos.of(otherSection);

        return Math.abs(boundingBoxPos.getX() - otherPos.getX()) <= 1 && Math.abs(boundingBoxPos.getZ() - otherPos.getZ()) <= 1;
    }
}
