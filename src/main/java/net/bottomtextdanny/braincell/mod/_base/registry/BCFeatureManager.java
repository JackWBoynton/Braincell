package net.bottomtextdanny.braincell.mod._base.registry;

import net.bottomtextdanny.braincell.mod._base.registry.managing.Wrap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.function.Supplier;

public abstract class BCFeatureManager<FC extends FeatureConfiguration> extends Wrap<Feature<FC>> {
	public static final UnsupportedOperationException CALL_WHEN_NOT_DEFERRED_EX = new UnsupportedOperationException("Cannot call any dependant object because this wrapper is not deferred.");
	public static final UnsupportedOperationException PRE_SOLVING_CALL_EX = new UnsupportedOperationException("Cannot call any dependant object before mod loading.");

	protected BCFeatureManager(ResourceLocation name, Supplier<Feature<FC>> feature) {
		super(name, feature);
	}
	
	@Override
	public void solve() {
		super.solve();
	}
	
	public Feature<FC> getFeature() {
		return this.obj;
	}

//	protected ConfiguredFeature<FC, ?> makeConfiguration(ResourceLocation key, FC featureConfiguration) {
//		ConfiguredFeature<FC, ?> configuredFeature = this.obj..configured(featureConfiguration);
//		Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, key, configuredFeature);
//		return configuredFeature;
//	}
//
//	protected PlacedFeature makePlacement(ResourceLocation key, ConfiguredFeature<FC, ?> configuredFeature, PlacementModifier... modifiers) {
//		PlacedFeature placedFeature = configuredFeature.feature().placed(modifiers);
//		Registry.register(BuiltinRegistries.PLACED_FEATURE, key, placedFeature);
//		return placedFeature;
//	}

	protected final void checkSolvingState() {
		if (getModSolvingState() == null) {
			throw CALL_WHEN_NOT_DEFERRED_EX;
		} else if (!getModSolvingState().isOpen()) {
			throw PRE_SOLVING_CALL_EX;
		}
	}
}
