/*
 * Copyright Sunday August 07 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.base.pair.Pair;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

public record Chart(ResourceLocation key, List<Piece> actors) {

	public ObjectFetcher deserialize(ListTag root) {
		Object[] objects = new Object[Math.max(actors.size(), root.size())];

		int index = 0;
		for (Tag tag : root) {
			if (tag instanceof CompoundTag compound && !compound.isEmpty())
				objects[index] = Serializer.unpack(compound, null);
			index++;
		}

		return ObjectFetcher.of(objects);
	}

	public ListTag serialize(int actor, ObjectFetcher objects) {
		List<Wrap<? extends Serializer<?>>> args = actors.get(actor).args();
		ListTag tag = new ListTag();

		if (args == null) return tag;

		int size = objects.size();

		for (int i = 0; i < size; i++) {
			Object obj = objects.get(i);

			if (obj != null) {
				tag.add(Serializer.pack(args.get(i), obj));
			} else tag.add(new CompoundTag());
		}

		return tag;
	}
}
