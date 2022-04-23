package net.bottomtextdanny.braincell.mod.capability.player.accessory.extensions;

import org.apache.commons.lang3.mutable.MutableFloat;

public interface FinnFall {
    int FALL_PRIORITY_HIGH = 0, FALL_PRIORITY_MED = 10, FALL_PRIORITY_LOW = 20, FALL_CANCEL_PRIORITY = 200;

    void fallDamageMultModifier(float baseDistance, MutableFloat fallDamage, MutableFloat fallDistance);

    int fallModificationPriority();
}
