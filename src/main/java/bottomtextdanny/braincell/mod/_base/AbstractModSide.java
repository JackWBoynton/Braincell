package bottomtextdanny.braincell.mod._base;

public abstract class AbstractModSide {
    protected final String modId;

    public AbstractModSide(String modId) {
        super();
        this.modId = modId;
    }

    public abstract void modLoadingCallOut();

    public abstract void postModLoadingPhaseCallOut();
}
