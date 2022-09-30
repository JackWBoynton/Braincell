package bottomtextdanny.braincell.tables;

import bottomtextdanny.braincell.Braincell;
import bottomtextdanny.braincell.libraries.chart.help.*;
import bottomtextdanny.braincell.libraries.chart.schema.SchemaMemo;
import bottomtextdanny.braincell.libraries.chart.segment.Chart;
import bottomtextdanny.braincell.libraries.chart.segment_maker.LevelBlockPredicate;
import bottomtextdanny.braincell.libraries.chart.segment_maker.LevelBlockPredicates;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregeneration;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregenerations;
import bottomtextdanny.braincell.libraries.chart.segment_maker.LevelBlockThreshold;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentDecorator;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentDecorators;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPredicate;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPredicates;
import bottomtextdanny.braincell.libraries.registry.BCRegistry;
import bottomtextdanny.braincell.libraries.registry.RegistryHelper;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static bottomtextdanny.braincell.libraries.registry.Serializer.*;

public final class BCSerializers {
    public static final ResourceKey<Registry<Serializer<?>>> REGISTRY_KEY = ResourceKey.createRegistryKey(new ResourceLocation(Braincell.ID, "serializer"));
    private static final DeferredRegister<Serializer<?>> DEFERRED_REGISTERER = DeferredRegister.create(REGISTRY_KEY, Braincell.ID);
    public final static Supplier<IForgeRegistry<Serializer<?>>> REGISTRY = DEFERRED_REGISTERER.makeRegistry(() -> new RegistryBuilder<>());
    public static final BCRegistry<Serializer<?>> ENTRIES = new BCRegistry<>();
    public static final RegistryHelper<Serializer<?>> HELPER = new RegistryHelper<>(Braincell.DEFERRING_STATE, ENTRIES);

    public static final LevelBlockThreshold NO_LEVEL_BLOCK_THRESHOLD = new LevelBlockThreshold() {

        @Override
        public float process(WorldGenLevel level, BlockState state, BlockPos blockPos, float base) {
            return 0;
        }

        @Override
        public Wrap<? extends Serializer<? extends LevelBlockThreshold>> serializer() {
            return LEVEL_BLOCK_THRESHOLD_BLANK;
        }
    };

    public static final Wrap<Serializer<Direction>> DIRECTION = HELPER.defer("direction", enumSerializer(Direction.class));
    public static final Wrap<Serializer<Rotation>> ROTATION = HELPER.defer("rotation", enumSerializer(Rotation.class));
    public static final Wrap<Serializer<TerrainAdjustment>> TERRAIN_ADJUSTMENT = HELPER.defer("terraformer", enumSerializer(TerrainAdjustment.class));
    public static final Wrap<Serializer<Heightmap.Types>> HEIGHTMAP = HELPER.defer("heightmap", enumSerializer(Heightmap.Types.class));

    public static final Wrap<Serializer<Vec3i>> VEC3 = HELPER.defer("vec3", () -> new Serializer<>(
            (obj) -> {
                ListTag list = new ListTag();

                list.add(FloatTag.valueOf(obj.getX()));
                list.add(FloatTag.valueOf(obj.getY()));
                list.add(FloatTag.valueOf(obj.getZ()));

                return list;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new Vec3i(list.getFloat(0), list.getFloat(1), list.getFloat(2));
            }
    ));
    public static final Wrap<Serializer<Vec3i>> VEC3I = HELPER.defer("vec3i", () -> new Serializer<>(
            (obj) -> new IntArrayTag(new int[]{obj.getX(), obj.getY(), obj.getZ()}),
            (tag) -> {
                int[] array = ((IntArrayTag) tag).getAsIntArray();
                return new Vec3i(array[0], array[1], array[2]);
            }
    ));
    public static final Wrap<Serializer<BlockPos>> BLOCKPOS = HELPER.defer("blockpos", () -> new Serializer<>(
            (obj) -> LongTag.valueOf(obj.asLong()),
            (tag) -> BlockPos.of(((LongTag) tag).getAsLong())
    ));
    public static final Wrap<Serializer<BoundingBox>> BOUNDING_BOX = HELPER.defer("bounding_box", () -> new Serializer<>(
            (obj) -> new IntArrayTag(
                    new int[]{obj.minX(), obj.minY(), obj.minZ(),
                            obj.maxX(), obj.maxY(), obj.maxZ()
                    }
            ),
            (tag) -> {
                int[] bounds = ((IntArrayTag) tag).getAsIntArray();
                return new BoundingBox(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
            }
    ));
    public static final Wrap<Serializer<IntBox>> INT_BOX = HELPER.defer("int_box", () -> new Serializer<>(
            (obj) -> new IntArrayTag(
                    new int[]{obj.x1, obj.y1, obj.z1,
                            obj.x2, obj.y2, obj.z2
                    }
            ),
            (tag) -> {
                int[] bounds = ((IntArrayTag) tag).getAsIntArray();
                return new IntBox(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
            }
    ));

    public static final Wrap<Serializer<Chart>> CHART = HELPER.defer("chart", () -> new Serializer<>(
        (obj) -> StringTag.valueOf(obj.key().toString()),
        (tag) -> BCCharts.REGISTRY.get().getValue(new ResourceLocation(tag.getAsString()))
    ));

    public static final Wrap<Serializer<Fluid>> FLUID = HELPER.defer("fluid", registrySerializer(Fluid::builtInRegistryHolder, Registry.FLUID));
    public static final Wrap<Serializer<Block>> BLOCK = HELPER.defer("block", registrySerializer(Block::builtInRegistryHolder, Registry.BLOCK));

    public static final Wrap<Serializer<List<Block>>> BLOCK_LIST = HELPER.defer("block_list", listSerializer(BLOCK));

    public static final Wrap<Serializer<BlockState>> BLOCKSTATE = HELPER.defer("blockstate", () -> new Serializer<>(
            (obj) -> StringTag.valueOf(BlockStateParser.serialize(obj)),
            (tag) -> {
                try {
                    return BlockStateParser.parseForBlock(Registry.BLOCK, ((StringTag) tag).getAsString(), true).blockState();
                } catch (Exception e) {
                    Braincell.common().logger.error("Failed reading blockstate :(");
                    e.printStackTrace();
                }

                return Blocks.STONE.defaultBlockState();
            }
    ));

    public static final Wrap<Serializer<List<BlockState>>> BLOCKSTATE_LIST = HELPER.defer("blockstate_list", listSerializer(BLOCKSTATE));

    public static final Wrap<Serializer<SchemaMemo>> SCHEMA = HELPER.defer("schema", () -> new Serializer<>(
        (obj) -> StringTag.valueOf(obj.getLocation().toString()),
        (tag) -> Braincell.common().getSchemaManager().parseSchema(new ResourceLocation(tag.getAsString()))
    ));

    public static final Wrap<Serializer<Distance2s.Manhattan>> DISTANCE2_MANHATTAN = HELPER.defer("d2_manhattan", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> Distance2s.d2Manhat()
    ));
    public static final Wrap<Serializer<Distance2s.Euclidean>> DISTANCE2_EUCLIDEAN = HELPER.defer("d2_euclidean", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> Distance2s.d2Euclid()
    ));
    public static final Wrap<Serializer<Distance2s.Squared>> DISTANCE2_SQUARED = HELPER.defer("d2_squared", () -> new Serializer<>(
        (obj) -> EndTag.INSTANCE,
        (tag) -> Distance2s.d2Squared()
    ));
    public static final Wrap<Serializer<Distance2s.Chebyshev>> DISTANCE2_CHEBYSHEV = HELPER.defer("d2_chebyshev", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> Distance2s.d2Cheby()
    ));
    public static final Wrap<Serializer<Distance3s.Manhattan>> DISTANCE3_MANHATTAN = HELPER.defer("d3_manhattan", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> Distance3s.d3Manhat()
    ));
    public static final Wrap<Serializer<Distance3s.Euclidean>> DISTANCE3_EUCLIDEAN = HELPER.defer("d3_euclidean", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> Distance3s.d3Euclid()
    ));
    public static final Wrap<Serializer<Distance3s.Squared>> DISTANCE3_SQUARED = HELPER.defer("d3_squared", () -> new Serializer<>(
        (obj) -> EndTag.INSTANCE,
        (tag) -> Distance3s.d3Squared()
    ));
    public static final Wrap<Serializer<Distance3s.Chebyshev>> DISTANCE3_CHEBYSHEV = HELPER.defer("d3_chebyshev", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> Distance3s.d3Cheby()
    ));
    public static final Wrap<Serializer<GridSampler>> GRID_SAMPLER_BLANK = HELPER.defer("gs_blank", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> GridSamplers.gsBlank()
    ));
    public static final Wrap<Serializer<GridSamplers.SimplexLayer>> GRID_SAMPLER_SIMPLEX = HELPER.defer("gs_simplex", () -> new Serializer<>(
            (obj) -> IntTag.valueOf(obj.layer()),
            (tag) -> GridSamplers.gsSimplex(((IntTag) tag).getAsInt())
    ));
    public static final Wrap<Serializer<GridSamplers.ImprovedNoiseLayer>> GRID_SAMPLER_IMPROVED_NOISE = HELPER.defer("gs_improved_noise", () -> new Serializer<>(
            (obj) -> IntTag.valueOf(obj.layer()),
            (tag) -> GridSamplers.gsImprovedNoise(((IntTag) tag).getAsInt())
    ));
    public static final Wrap<Serializer<GridSamplers.Fixed>> GRID_SAMPLER_FIXED = HELPER.defer("gs_fixed", () -> new Serializer<>(
            (obj) -> FloatTag.valueOf(obj.value()),
            (tag) -> GridSamplers.gsFixed(((FloatTag) tag).getAsFloat())
    ));
    public static final Wrap<Serializer<GridSamplers.OperationConcat>> GRID_SAMPLER_OPERATION_CONCAT = HELPER.defer("gs_fixed", () -> new Serializer<>(
            (obj) -> {
                ListTag tag = new ListTag();

                obj.iterate(pair -> {
                    CompoundTag opTag = new CompoundTag();

                    opTag.put("op", ByteTag.valueOf(pair.left()));
                    opTag.put("sampler", pack(pair.right()));

                    tag.add(opTag);
                });

                return tag;
            },
            (tag) -> {
                GridSamplers.OperationConcat concat = new GridSamplers.OperationConcat();
                ListTag list = (ListTag) tag;

                list.forEach(entry -> {
                    CompoundTag opTag = (CompoundTag) entry;
                    concat.op(opTag.getByte("op"), Serializer.unpack(opTag.get("sampler"), GridSamplers.gsBlank()));
                });

                return concat;
            }
    ));
    public static final Wrap<Serializer<GridWarper>> GRID_WARPER_IDENTITY = HELPER.defer("gw_identity", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> GridWarpers.gwIdentity()
    ));
    public static final Wrap<Serializer<GridWarpers.Concat>> GRID_WARPER_CONCAT = HELPER.defer("gw_concat", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();

                obj.iterate(predicate -> {
                    root.add(pack(predicate));
                });

                return root;
            },
            (tag) -> {
                GridWarpers.Concat list = new GridWarpers.Concat();

                ((ListTag)tag).forEach(t -> list.add(Serializer.unpack(t, GridWarpers.gwIdentity())));

                return list;
            }
    ));
    public static final Wrap<Serializer<GridWarpers.Mul>> GRID_WARPER_MULTIPLICATION = HELPER.defer("gw_mul", () -> new Serializer<>(
            (obj) -> {
                ListTag list = new ListTag();
                list.add(FloatTag.valueOf(obj.x()));
                list.add(FloatTag.valueOf(obj.y()));
                list.add(FloatTag.valueOf(obj.z()));
                return list;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;

                return new GridWarpers.Mul(list.getFloat(0), list.getFloat(1), list.getFloat(2));
            }
    ));
    public static final Wrap<Serializer<GridWarpers.Scale>> GRID_WARPER_SCALE = HELPER.defer("gw_scale", () -> new Serializer<>(
            (obj) -> FloatTag.valueOf(obj.scalar()),
            (tag) -> new GridWarpers.Scale(((FloatTag)tag).getAsFloat())
    ));
    public static final Wrap<Serializer<GridWarpers.Offset>> GRID_WARPER_OFFSET = HELPER.defer("gw_offset", () -> new Serializer<>(
            (obj) -> {
                ListTag list = new ListTag();
                list.add(FloatTag.valueOf(obj.x()));
                list.add(FloatTag.valueOf(obj.y()));
                list.add(FloatTag.valueOf(obj.z()));
                return list;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;

                return new GridWarpers.Offset(list.getFloat(0), list.getFloat(1), list.getFloat(2));
            }
    ));
    public static final Wrap<Serializer<GridWarpers.FixX>> GRID_WARPER_FIX_X = HELPER.defer("gw_fix_x", () -> new Serializer<>(
            (obj) -> FloatTag.valueOf(obj.value()),
            (tag) -> new GridWarpers.FixX(((FloatTag)tag).getAsFloat())
    ));
    public static final Wrap<Serializer<GridWarpers.FixY>> GRID_WARPER_FIX_Y = HELPER.defer("gw_fix_y", () -> new Serializer<>(
            (obj) -> FloatTag.valueOf(obj.value()),
            (tag) -> new GridWarpers.FixY(((FloatTag)tag).getAsFloat())
    ));
    public static final Wrap<Serializer<GridWarpers.FixZ>> GRID_WARPER_FIX_Z = HELPER.defer("gw_fix_z", () -> new Serializer<>(
            (obj) -> FloatTag.valueOf(obj.value()),
            (tag) -> new GridWarpers.FixZ(((FloatTag)tag).getAsFloat())
    ));
    public static final Wrap<Serializer<SegmentPregeneration>> SEGMENT_PREGEN_CANCEL = HELPER.defer("spg_cancel", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> SegmentPregenerations.spgCancel()
    ));

    public static final Wrap<Serializer<SegmentPregeneration>> SEGMENT_PREGEN_PASS = HELPER.defer("spg_pass", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> SegmentPregenerations.spgPass()
    ));
    public static final Wrap<Serializer<SegmentPregenerations.Concat>> SEGMENT_PREGEN_CONCAT = HELPER.defer("spg_concat", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                obj.iterate(pregeneration -> {
                    root.add(pack(pregeneration));
                });
                return root;
            },
            (tag) -> {
                SegmentPregenerations.Concat list = new SegmentPregenerations.Concat();

                ((ListTag)tag).forEach(t -> list.append(Serializer.unpack(t, SegmentPregenerations.spgPass())));

                return list;
            }
    ));
    public static final Wrap<Serializer<SegmentPregenerations.If>> SEGMENT_PREGEN_IF = HELPER.defer("spg_if", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                root.add(pack(obj.conditional()));
                root.add(pack(obj.positiveOutcome()));
                return root;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new SegmentPregenerations.If(
                        Serializer.unpack(list.get(0), LevelBlockPredicates.lbpCancel()),
                        Serializer.unpack(list.get(1), SegmentPregenerations.spgPass()));
            }
    ));
    public static final Wrap<Serializer<SegmentPregenerations.Either>> SEGMENT_PREGEN_EITHER = HELPER.defer("spg_either", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                root.add(pack(obj.conditional()));
                root.add(pack(obj.positiveOutcome()));
                root.add(pack(obj.negativeOutcome()));
                return root;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new SegmentPregenerations.Either(
                        Serializer.unpack(list.get(0), LevelBlockPredicates.lbpCancel()),
                        Serializer.unpack(list.get(1), SegmentPregenerations.spgPass()),
                        Serializer.unpack(list.get(2), SegmentPregenerations.spgPass()));
            }
    ));
    public static final Wrap<Serializer<SegmentPregenerations.IfNull>> SEGMENT_PREGEN_IF_NULL = HELPER.defer("spg_if_null", () -> new Serializer<>(
            (obj) -> pack(obj.alternative()),
            (tag) -> SegmentPregenerations.spgIfNull(unpack(tag, SegmentPregenerations.spgPass()))
    ));
    public static final Wrap<Serializer<SegmentPregenerations.Height>> SEGMENT_PREGEN_HEIGHT = HELPER.defer("spg_height", () -> new Serializer<>(
            (obj) -> packDirectly(obj.heightType(), HEIGHTMAP.get()),
            (tag) -> SegmentPregenerations.spgHeight(unpackDirectly(tag, HEIGHTMAP.get()))
    ));
    public static final Wrap<Serializer<SegmentPregenerations.FindSurface>> SEGMENT_PREGEN_FIND_SURFACE = HELPER.defer("spg_find_surface", () -> new Serializer<>(
            (obj) -> {
                CompoundTag tag = new CompoundTag();

                tag.putInt("up_range", obj.upRange());
                tag.putInt("down_range", obj.downRange());
                tag.put("buried_predicate", pack(obj.buriedPredicate()));

                return tag;
            },
            (tagUncasted) -> {
                if (tagUncasted instanceof CompoundTag tag)
                    SegmentPregenerations.spgFindSurface(tag.getInt("up_range"), tag.getInt("down_range"), unpack(tag.get("buried_predicate"), LevelBlockPredicates.lbpCancel()));
                return null;
            }
    ));
    public static final Wrap<Serializer<SegmentDecorator>> SEGMENT_DECORATOR_BLANK = HELPER.defer("sd_blank", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> SegmentDecorators.sdBlank()
    ));
    public static final Wrap<Serializer<SegmentDecorators.Concat>> SEGMENT_DECORATOR_CONCAT = HELPER.defer("sd_concat", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                obj.iterate(predicate -> {
                    root.add(pack(predicate));
                });
                return root;
            },
            (tag) -> {
                SegmentDecorators.Concat list = new SegmentDecorators.Concat();

                ((ListTag)tag).forEach(t -> list.append(Serializer.unpack(t, SegmentDecorators.sdBlank())));

                return list;
            }
    ));
    public static final Wrap<Serializer<SegmentDecorators.If>> SEGMENT_DECORATOR_IF = HELPER.defer("sd_if", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                root.add(pack(obj.conditional()));
                root.add(pack(obj.positiveOutcome()));
                return root;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new SegmentDecorators.If(
                        Serializer.unpack(list.get(0), SegmentPredicates.spCancel()),
                        Serializer.unpack(list.get(1), SegmentDecorators.sdBlank()));
            }
    ));
    public static final Wrap<Serializer<SegmentDecorators.Either>> SEGMENT_DECORATOR_EITHER = HELPER.defer("sd_either", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                root.add(pack(obj.conditional()));
                root.add(pack(obj.positive()));
                root.add(pack(obj.negative()));
                return root;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new SegmentDecorators.Either(
                        Serializer.unpack(list.get(0), SegmentPredicates.spCancel()),
                        Serializer.unpack(list.get(1), SegmentDecorators.sdBlank()),
                        Serializer.unpack(list.get(2), SegmentDecorators.sdBlank()));
            }
    ));
    public static final Wrap<Serializer<SegmentDecorators.Place>> SEGMENT_DECORATOR_PLACE = HELPER.defer("sd_place", () -> new Serializer<>(
            (obj) -> Serializer.packDirectly(obj.state(), BCSerializers.BLOCK.get()),
            (tag) -> new SegmentDecorators.Place(Serializer.unpackDirectly(tag, BCSerializers.BLOCK.get()))
    ));
    public static final Wrap<Serializer<SegmentDecorators.PlaceBlockState>> SEGMENT_DECORATOR_PLACE_BLOCKSTATE = HELPER.defer("sd_place_blockstate", () -> new Serializer<>(
            (obj) -> Serializer.packDirectly(obj.state(), BCSerializers.BLOCKSTATE.get()),
            (tag) -> new SegmentDecorators.PlaceBlockState(Serializer.unpackDirectly(tag, BCSerializers.BLOCKSTATE.get()))
    ));
    public static final Wrap<Serializer<SegmentDecorators.PlaceInjected>> SEGMENT_DECORATOR_PLACE_INJECTED = HELPER.defer("sd_place_injected", () -> new Serializer<>(
            (obj) -> Serializer.packDirectly(obj.block(), BCSerializers.BLOCK.get()),
            (tag) -> new SegmentDecorators.PlaceInjected(Serializer.unpackDirectly(tag, BCSerializers.BLOCK.get()))
    ));
    public static final Wrap<Serializer<SegmentDecorators.ByRange>> SEGMENT_DECORATOR_BY_RANGE = HELPER.defer("sd_by_range", () -> new Serializer<>(
            (obj) -> {
                CompoundTag tag = new CompoundTag();
                CompoundTag mapperTag = pack(obj.valueMapper());
                ListTag outcomesTag = new ListTag();

                obj.iterate(pair -> {
                    CompoundTag entryTag = new CompoundTag();

                    entryTag.put("range", FloatTag.valueOf(pair.left()));
                    entryTag.put("outcome", pack(pair.right()));

                    outcomesTag.add(entryTag);
                });

                tag.put("mapper", mapperTag);
                tag.put("outcomes", outcomesTag);

                return tag;
            },
            (tag) -> {
                CompoundTag compoundTag = (CompoundTag) tag;
                SegmentDecorators.ByRange decoratorByRange =
                        new SegmentDecorators.ByRange(Serializer.unpack(compoundTag.get("mapper"), NO_LEVEL_BLOCK_THRESHOLD));

                compoundTag.getList("outcomes", 10).forEach(entry -> {
                    CompoundTag rangeTag = (CompoundTag) entry;
                    decoratorByRange.entry(rangeTag.getFloat("range"), Serializer.unpack(rangeTag.get("outcome"), SegmentDecorators.sdBlank()));
                });

                return decoratorByRange;
            }
    ));
    public static final Wrap<Serializer<SegmentDecorators.ByFlag>> SEGMENT_DECORATOR_BY_FLAG = HELPER.defer("sd_by_flag", () -> new Serializer<>(
            (obj) -> {
                CompoundTag tag = new CompoundTag();
                IntTag indexTag = IntTag.valueOf(obj.index());
                ListTag outcomesTag = new ListTag();

                obj.iterate((flag, outcome) -> {
                    CompoundTag entryTag = new CompoundTag();

                    entryTag.put("flag", IntTag.valueOf(flag));
                    entryTag.put("outcome", pack(outcome));

                    outcomesTag.add(entryTag);
                });

                tag.put("index", indexTag);
                tag.put("outcomes", outcomesTag);

                return tag;
            },
            (tag) -> {
                CompoundTag compoundTag = (CompoundTag) tag;
                SegmentDecorators.ByFlag decoratorByRange =
                        new SegmentDecorators.ByFlag(compoundTag.getInt("index"));

                compoundTag.getList("outcomes", 10).forEach(entry -> {
                    CompoundTag entryTag = (CompoundTag) entry;
                    decoratorByRange.entry(entryTag.getInt("flag"), Serializer.unpack(entryTag.get("outcome"), SegmentDecorators.sdBlank()));
                });

                return decoratorByRange;
            }
    ));
    public static final Wrap<Serializer<SegmentDecorators.ByFirstFlag>> SEGMENT_DECORATOR_BY_FIRST_FLAG = HELPER.defer("sd_by_first_flag", () -> new Serializer<>(
            (obj) -> {
                ListTag outcomesTag = new ListTag();

                obj.iterate((flag, outcome) -> {
                    CompoundTag entryTag = new CompoundTag();

                    entryTag.put("flag", IntTag.valueOf(flag));
                    entryTag.put("outcome", pack(outcome));

                    outcomesTag.add(entryTag);
                });

                return outcomesTag;
            },
            (tag) -> {
                ListTag listTag = (ListTag) tag;
                SegmentDecorators.ByFirstFlag decoratorByRange = new SegmentDecorators.ByFirstFlag();

                listTag.forEach(entry -> {
                    CompoundTag entryTag = (CompoundTag) entry;
                    decoratorByRange.entry(entryTag.getInt("flag"), Serializer.unpack(entryTag.get("outcome"), SegmentDecorators.sdBlank()));
                });

                return decoratorByRange;
            }
    ));
    public static final Wrap<Serializer<SegmentPredicate>> SEGMENT_PREDICATE_CANCEL = HELPER.defer("sp_cancel", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> SegmentPredicates.spCancel()
    ));
    public static final Wrap<Serializer<SegmentPredicates.Concat>> SEGMENT_PREDICATE_CONCAT = HELPER.defer("sp_concat", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                obj.iterate(predicate -> {
                    root.add(pack(predicate));
                });
                return root;
            },
            (tag) -> {
                SegmentPredicates.Concat list = new SegmentPredicates.Concat();

                ((ListTag)tag).forEach(t -> list.spAppend(Serializer.unpack(t, SegmentPredicates.spCancel())));

                return list;
            }
    ));
    public static final Wrap<Serializer<SegmentPredicates.ConcatOred>> SEGMENT_PREDICATE_CONCAT_ORED = HELPER.defer("sp_concat_ored", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                obj.iterate(predicate -> {
                    root.add(pack(predicate));
                });
                return root;
            },
            (tag) -> {
                SegmentPredicates.ConcatOred list = new SegmentPredicates.ConcatOred();
                ((ListTag)tag).forEach(t -> list.spOr(Serializer.unpack(t, SegmentPredicates.spCancel())));
                return list;
            }
    ));
    public static final Wrap<Serializer<SegmentPredicates.Either>> SEGMENT_PREDICATE_EITHER = HELPER.defer("sp_either", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                root.add(pack(obj.statement()));
                root.add(pack(obj.positive()));
                root.add(pack(obj.negative()));
                return root;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new SegmentPredicates.Either(
                        Serializer.unpack(list.get(0), SegmentPredicates.spCancel()),
                        Serializer.unpack(list.get(1), SegmentPredicates.spCancel()),
                        Serializer.unpack(list.get(2), SegmentPredicates.spCancel()));
            }
    ));
    public static final Wrap<Serializer<SegmentPredicates.OrElse>> SEGMENT_PREDICATE_OR_ELSE = HELPER.defer("sp_or_else", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                root.add(pack(obj.statement()));
                root.add(pack(obj.negative()));
                return root;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new SegmentPredicates.OrElse(
                        Serializer.unpack(list.get(0), SegmentPredicates.spCancel()),
                        Serializer.unpack(list.get(1), SegmentPredicates.spCancel()));
            }
    ));
    public static final Wrap<Serializer<SegmentPredicates.Negate>> SEGMENT_PREDICATE_NEGATE = HELPER.defer("sp_negate", () -> new Serializer<>(
            (obj) -> pack(obj),
            (tag) -> new SegmentPredicates.Negate(Serializer.unpack(tag, SegmentPredicates.spCancel()))
    ));
    public static final Wrap<Serializer<SegmentPredicates.FirstFlagEquals>> SEGMENT_PREDICATE_FIRST_FLAG_EQUALS = HELPER.defer("sp_first_flag_equals", () -> new Serializer<>(
            (obj) -> IntTag.valueOf(obj.flag()),
            (tag) -> new SegmentPredicates.FirstFlagEquals(((IntTag)tag).getAsInt())
    ));
    public static final Wrap<Serializer<SegmentPredicates.FlagEquals>> SEGMENT_PREDICATE_FLAG_EQUALS = HELPER.defer("sp_flag_equals", () -> new Serializer<>(
            (obj) -> {
                ListTag list = new ListTag();
                list.add(IntTag.valueOf(obj.index()));
                list.add(IntTag.valueOf(obj.flag()));
                return list;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new SegmentPredicates.FlagEquals(list.getInt(0), list.getInt(1));
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicate>> LEVEL_BLOCK_PREDICATE_CANCEL = HELPER.defer("lbp_cancel", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpCancel()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Concat>> LEVEL_BLOCK_PREDICATE_CONCAT = HELPER.defer("lbp_concat", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                obj.iterate(predicate -> {
                    root.add(pack(predicate));
                });
                return root;
            },
            (tag) -> {
                LevelBlockPredicates.Concat list = new LevelBlockPredicates.Concat();

                ((ListTag)tag).forEach(t -> list.lbpAppend(Serializer.unpack(t, LevelBlockPredicates.lbpCancel())));

                return list;
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.ConcatOred>> LEVEL_BLOCK_PREDICATE_CONCAT_ORED = HELPER.defer("lbp_concat_ored", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                obj.iterate(predicate -> {
                    root.add(pack(predicate));
                });
                return root;
            },
            (tag) -> {
                LevelBlockPredicates.ConcatOred list = new LevelBlockPredicates.ConcatOred();
                ((ListTag)tag).forEach(t -> list.lbpOr(Serializer.unpack(t, LevelBlockPredicates.lbpCancel())));
                return list;
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Either>> LEVEL_BLOCK_PREDICATE_EITHER = HELPER.defer("lbp_either", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                root.add(pack(obj.statement()));
                root.add(pack(obj.positive()));
                root.add(pack(obj.negative()));
                return root;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new LevelBlockPredicates.Either(
                        Serializer.unpack(list.get(0), LevelBlockPredicates.lbpCancel()),
                        Serializer.unpack(list.get(1), LevelBlockPredicates.lbpCancel()),
                        Serializer.unpack(list.get(2), LevelBlockPredicates.lbpCancel()));
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.OrElse>> LEVEL_BLOCK_PREDICATE_OR_ELSE = HELPER.defer("lbp_or_else", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();
                root.add(pack(obj.statement()));
                root.add(pack(obj.negative()));
                return root;
            },
            (tag) -> {
                ListTag list = (ListTag) tag;
                return new LevelBlockPredicates.OrElse(
                        Serializer.unpack(list.get(0), LevelBlockPredicates.lbpCancel()),
                        Serializer.unpack(list.get(1), LevelBlockPredicates.lbpCancel()));
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Negate>> LEVEL_BLOCK_PREDICATE_NEGATE = HELPER.defer("lbp_negate", () -> new Serializer<>(
            (obj) -> pack(obj),
            (tag) -> new LevelBlockPredicates.Negate(Serializer.unpack(tag, LevelBlockPredicates.lbpCancel()))
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Random>> LEVEL_BLOCK_PREDICATE_RANDOM = HELPER.defer("lbp_random", () -> new Serializer<>(
            (obj) -> FloatTag.valueOf(obj.probability()),
            (tag) -> new LevelBlockPredicates.Random(((FloatTag)tag).getAsFloat())
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsAir>> LEVEL_BLOCK_PREDICATE_IS_AIR = HELPER.defer("lbp_is_air", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpIsAir()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.HasBlockEntity>> LEVEL_BLOCK_PREDICATE_HAS_BLOCK_ENTITY = HELPER.defer("lbp_has_block_entity", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpHasBlockEntity()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsFluid>> LEVEL_BLOCK_PREDICATE_IS_FLUID = HELPER.defer("lbp_is_fluid", () -> new Serializer<>(
            (obj) -> Serializer.packDirectly(obj.fluid(), BCSerializers.FLUID.get()),
            (tag) -> LevelBlockPredicates.lbpIsFluid(Serializer.unpackDirectly(tag, BCSerializers.FLUID.get()))
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsEmptyFluid>> LEVEL_BLOCK_PREDICATE_IS_EMPTY_FLUID = HELPER.defer("is_empty_fluid", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpIsEmptyFluid()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsFluidSource>> LEVEL_BLOCK_PREDICATE_IS_SOURCE_FLUID = HELPER.defer("lbp_is_source_fluid", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpIsFluidSource()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsSolidRender>> LEVEL_BLOCK_PREDICATE_IS_SOLID_RENDER = HELPER.defer("lbp_is_solid_render", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpIsSolidRender()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.CanSurvive>> LEVEL_BLOCK_PREDICATE_CAN_SURVIVE = HELPER.defer("lbp_can_survive", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpCanSurvive()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsBurning>> LEVEL_BLOCK_PREDICATE_IS_BURNING = HELPER.defer("lbp_is_burning", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpIsBurning()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsFireSource>> LEVEL_BLOCK_PREDICATE_IS_FIRE_SOURCE = HELPER.defer("lbp_is_fire_source", () -> new Serializer<>(
            (obj) -> Serializer.packDirectly(obj.direction(), BCSerializers.DIRECTION.get()),
            (tag) -> LevelBlockPredicates.lbpIsFireSource(Serializer.unpackDirectly(tag, BCSerializers.DIRECTION.get()))
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsFlammable>> LEVEL_BLOCK_PREDICATE_IS_FLAMMABLE = HELPER.defer("lbp_is_flammable", () -> new Serializer<>(
            (obj) -> Serializer.packDirectly(obj.direction(), BCSerializers.DIRECTION.get()),
            (tag) -> LevelBlockPredicates.lbpIsFlammable(Serializer.unpackDirectly(tag, BCSerializers.DIRECTION.get()))
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.CanOcclude>> LEVEL_BLOCK_PREDICATE_CAN_OCCLUDE = HELPER.defer("lbp_can_occlude", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> LevelBlockPredicates.lbpCanOcclude()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Offset>> LEVEL_BLOCK_PREDICATE_OFFSET = HELPER.defer("lbp_offset", () -> new Serializer<>(
            (obj) -> {
                CompoundTag tag = new CompoundTag();
                tag.put("offset", Serializer.packDirectly(obj.offset(), BCSerializers.BLOCKPOS.get()));
                tag.put("conditional", pack(obj.conditional()));
                return tag;
            },
            (tag) -> {
                CompoundTag map = (CompoundTag) tag;
                return new LevelBlockPredicates.Offset(Serializer.unpackDirectly(map.get("offset"), BCSerializers.BLOCKPOS.get()), Serializer.unpack(map.get("conditional"), LevelBlockPredicates.lbpCancel()));
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.HasTag>> LEVEL_BLOCK_PREDICATE_HAS_TAG = HELPER.defer("lbp_has_tag", () -> new Serializer<>(
            (obj) -> StringTag.valueOf(obj.tag().location().toString()),
            (tag) -> LevelBlockPredicates.lbpHasTag(TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(tag.getAsString())))
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.IsBlock>> LEVEL_BLOCK_PREDICATE_IS_BLOCK = HELPER.defer("lbp_is_block", () -> new Serializer<>(
            (obj) -> Serializer.packDirectly(obj.block(), BCSerializers.BLOCK.get()),
            (tag) -> LevelBlockPredicates.lbpIs(Serializer.unpackDirectly(tag, BCSerializers.BLOCK.get()))
    ));
    public static final Wrap<Serializer<LevelBlockThreshold>> LEVEL_BLOCK_THRESHOLD_BLANK = HELPER.defer("lbt_blank", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> NO_LEVEL_BLOCK_THRESHOLD
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.ThresholdConcat>> LEVEL_BLOCK_THRESHOLD_CONCAT = HELPER.defer("lbt_concat", () -> new Serializer<>(
            (obj) -> {
                ListTag root = new ListTag();

                obj.iterate(predicate -> {
                    root.add(pack(predicate));
                });

                return root;
            },
            (tag) -> {
                LevelBlockPredicates.ThresholdConcat list = new LevelBlockPredicates.ThresholdConcat();

                ((ListTag)tag).forEach(t -> list.appendThreshold(Serializer.unpack(t, NO_LEVEL_BLOCK_THRESHOLD)));

                return list;
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.RandomRange>> LEVEL_BLOCK_THRESHOLD_RANDOM_RANGE = HELPER.defer("lbt_random_range", () -> new Serializer<>(
            (obj) -> EndTag.INSTANCE,
            (tag) -> new LevelBlockPredicates.RandomRange()
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Scale>> LEVEL_BLOCK_THRESHOLD_SCALE = HELPER.defer("lbt_scale", () -> new Serializer<>(
            (obj) -> FloatTag.valueOf(obj.scalar()),
            (tag) -> new LevelBlockPredicates.Scale(((FloatTag)tag).getAsFloat())
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.ScaleBy>> LEVEL_BLOCK_THRESHOLD_SCALE_BY = HELPER.defer("lbt_scale_by", () -> new Serializer<>(
            (obj) -> pack(obj),
            (tag) -> new LevelBlockPredicates.ScaleBy(Serializer.unpack(tag, NO_LEVEL_BLOCK_THRESHOLD))
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Add>> LEVEL_BLOCK_THRESHOLD_ADD = HELPER.defer("lbt_add", () -> new Serializer<>(
            (obj) -> FloatTag.valueOf(obj.addition()),
            (tag) -> new LevelBlockPredicates.Add(((FloatTag)tag).getAsFloat())
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.AddOther>> LEVEL_BLOCK_THRESHOLD_ADD_OTHER = HELPER.defer("lbt_add_other", () -> new Serializer<>(
            (obj) -> pack(obj),
            (tag) -> new LevelBlockPredicates.AddOther(Serializer.unpack(tag, NO_LEVEL_BLOCK_THRESHOLD))
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Layer>> LEVEL_BLOCK_THRESHOLD_LAYER = HELPER.defer("lbt_layer", () -> new Serializer<>(
            (obj) -> {
                CompoundTag tag = new CompoundTag();

                tag.put("sampler", pack(obj.sampler()));
                tag.put("warp", pack(obj.definition()));

                return tag;
            },
            (tag) -> {
                CompoundTag list = (CompoundTag) tag;
                return new LevelBlockPredicates.Layer(Serializer.unpack(list.get("sampler"), GridSamplers.gsBlank()), Serializer.unpack(list.get("warp"), GridWarpers.gwIdentity()));
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.Distance>> LEVEL_BLOCK_THRESHOLD_DISTANCE = HELPER.defer("lbt_distance", () -> new Serializer<>(
            (obj) -> {
                CompoundTag tag = new CompoundTag();

                tag.put("pivot", Serializer.packDirectly(obj.pivot(), BCSerializers.VEC3I.get()));
                tag.put("calc", pack(obj.calculator()));
                tag.put("mag", FloatTag.valueOf(obj.magnitude()));

                return tag;
            },
            (tag) -> {
                CompoundTag list = (CompoundTag) tag;

                return new LevelBlockPredicates.Distance(Serializer.unpackDirectly(list.get("pivot"), BCSerializers.VEC3I.get()), Serializer.unpack(list.get("calc"), Distance3s.d3Manhat()), list.getFloat("mag"));
            }
    ));
    public static final Wrap<Serializer<LevelBlockPredicates.DistanceInverted>> LEVEL_BLOCK_THRESHOLD_DISTANCE_INVERTED = HELPER.defer("lbt_distance_inv", () -> new Serializer<>(
            (obj) -> {
                CompoundTag tag = new CompoundTag();

                tag.put("pivot", Serializer.packDirectly(obj.pivot(), BCSerializers.VEC3I.get()));
                tag.put("calc", pack(obj.calculator()));
                tag.put("mag", FloatTag.valueOf(obj.magnitude()));

                return tag;
            },
            (tag) -> {
                CompoundTag list = (CompoundTag) tag;

                return new LevelBlockPredicates.DistanceInverted(Serializer.unpackDirectly(list.get("pivot"), BCSerializers.VEC3I.get()), Serializer.unpack(list.get("calc"), Distance3s.d3Manhat()), list.getFloat("mag"));
            }
    ));

    private static <T extends Enum<T>> Supplier<Serializer<T>> enumSerializer(Class<T> clazz) {

        return () -> new Serializer<>(
                (obj) -> ByteTag.valueOf((byte) ((byte) obj.ordinal())),
                (tag) -> clazz.getEnumConstants()[((ByteTag) tag).getAsByte()]
        );
    }

    private static <T> Supplier<Serializer<List<T>>> listSerializer(Wrap<Serializer<T>> serializer) {
        return () -> new Serializer<>(
            (obj) -> {
                ListTag tag = new ListTag();

                obj.forEach(o -> tag.add(packDirectly(o, serializer.get())));

                return tag;
            },
            (tagUncast) -> {
                ListTag tag = (ListTag) tagUncast;
                List<T> list = new ArrayList<>(tag.size());

                tag.forEach(t -> list.add(unpackDirectly(t, serializer.get())));

                return list;
            }
        );
    }

    private static <T> Supplier<Serializer<T>> registrySerializer(Function<T, Holder.Reference<T>> registryGetter, Registry<T> registry) {
        return () -> new Serializer<>(
                (obj) -> StringTag.valueOf(registryGetter.apply(obj).key().location().toString()),
                (tag) -> {
                    T value = registry.get(new ResourceLocation(tag.getAsString()));
                    return value == null ? registry.byId(0) : value;
                }
        );
    }

    public static void registerRegistry() {
        DEFERRED_REGISTERER.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
