package bottomtextdanny.braincell.libraries.accessory;

public interface IQueuedJump {

	boolean canPerform();

    void performJump();
    
    void reestablish();

    JumpPriority priority();

    IJumpQueuerAccessory getAccessory();
}
