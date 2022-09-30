package bottomtextdanny.braincell.events;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.lang3.mutable.MutableDouble;

import java.util.Comparator;
import java.util.LinkedList;

@OnlyIn(Dist.CLIENT)
public class MouseMovementEvent extends Event {
    public static final int HIGH_PRIORITY = 0, MED_PRIORITY = 500, LOW_PRIORITY = 1000;
    private final LinkedList<TicketedOperation> sensibilityOperations;

    public MouseMovementEvent() {
        super();
        this.sensibilityOperations = Lists.newLinkedList();
    }

    public void operateSensibilityAddition(int priority, double attribution) {
        this.sensibilityOperations.add(new TicketedOperation(priority, (sens) -> sens + attribution));
    }

    public void operateSensibilityMultiplication(int priority, double factor) {
        this.sensibilityOperations.add(new TicketedOperation(priority, (sens) -> sens * factor));
    }

    public void operateSensibilityCustom(int priority, Double2DoubleFunction function) {
        if (function != null) {
            this.sensibilityOperations.add(new TicketedOperation(priority, function));
        }
    }

    public double computeResultingSensibility() {
        final MutableDouble finalValue = new MutableDouble(1.0F);
        this.sensibilityOperations.stream()
                .sorted(Comparator.comparingInt(TicketedOperation::priority))
                .forEach((op) -> finalValue.setValue(op.function.get(finalValue.doubleValue())));
        return finalValue.doubleValue();
    }

    record TicketedOperation(int priority, Double2DoubleFunction function) {}
}
