package net.bottomtextdanny.braincell.mod.capability.player.accessory;

import com.google.common.collect.Lists;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

public class EmptyAccessoryKeyCollectorEvent extends Event {
    private final LinkedList<AccessoryKey<?>> externalEmptyKeys;

    public EmptyAccessoryKeyCollectorEvent() {
        super();
        this.externalEmptyKeys = Lists.newLinkedList();
    }

    public boolean addKey(AccessoryKey<?> key) {
        return this.externalEmptyKeys.add(key);
    }

    public Collection<AccessoryKey<?>> getCollection() {
        return Collections.unmodifiableList(this.externalEmptyKeys);
    }
}
