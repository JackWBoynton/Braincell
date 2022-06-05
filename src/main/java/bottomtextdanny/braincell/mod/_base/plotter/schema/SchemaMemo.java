package bottomtextdanny.braincell.mod._base.plotter.schema;

import net.minecraft.resources.ResourceLocation;

public final class SchemaMemo {
    private final ResourceLocation location;
    private Schema schema;
    private boolean built;

    public SchemaMemo(ResourceLocation location) {
        super();
        this.location = location;
        this.schema = Schema.DUMMY;
    }

    public void setBuilt(Schema schema, SchemaManager.Key key) {
        if (key != null) {
            this.schema = schema;
            this.built = true;
        }
    }

    public Schema getSchema() {
        return schema;
    }

    public ResourceLocation getLocation() {
        return location;
    }

    public boolean isBuilt() {
        return built;
    }

    public static ResourceLocation schDir(String namespace, String localPathNoExtension) {
        return new ResourceLocation(namespace, "schemas/" + localPathNoExtension + ".json");
    }
}
