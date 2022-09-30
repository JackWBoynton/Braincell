/*
 * Copyright Wednesday August 10 of 2022. BottomTextDanny.
 * All rights reserved.
 */

package bottomtextdanny.braincell.libraries.chart.segment;

import bottomtextdanny.braincell.base.ObjectFetcher;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregeneration;
import bottomtextdanny.braincell.libraries.chart.segment_maker.SegmentPregenerations;
import bottomtextdanny.braincell.libraries.registry.Serializer;
import bottomtextdanny.braincell.libraries.registry.Wrap;

import java.util.List;
import java.util.function.BiFunction;

public record Piece(BiFunction<ChartPiece, ObjectFetcher, Segment<?>> function, SegmentPregeneration posProcessor, List<Wrap<? extends Serializer<?>>> args) {
	public static Piece make(BiFunction<ChartPiece, ObjectFetcher, Segment<?>> function, List<Wrap<? extends Serializer<?>>> args) {
		return new Piece(function, SegmentPregenerations.spgPass(), args);

	}
}
