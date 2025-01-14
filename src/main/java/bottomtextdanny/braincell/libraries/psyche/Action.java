package bottomtextdanny.braincell.libraries.psyche;

import com.google.common.collect.Lists;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.PathfinderMob;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public abstract class Action<E extends PathfinderMob> {
    protected static final RandomSource UNSAFE_RANDOM = RandomSource.create();
    protected static final Random RANDOM = new Random();
    private final List<Integer> modules;
    @Nullable
    private List<Integer> blockOtherModules;
    protected E mob;
    private final Psyche<E> psyche;
    protected int ticksPassed;
    private boolean active;
    private int lastUpdatedActiveState;
    private boolean running;
    private int moduleRunningOn;

    public Action(E mob) {
        super();
        this.mob = mob;
        this.psyche = (Psyche<E>) ((PsycheEntity)mob).getPsyche();
        this.modules = Lists.newArrayList();
    }

    public boolean active() {
        if (this.lastUpdatedActiveState != this.mob.tickCount) {
            this.lastUpdatedActiveState = this.mob.tickCount;
            this.active = activeParameters();
        }
        return this.active;
    }

    protected boolean activeParameters() {
        if (this.psyche.isModuleDeactivated(this.moduleRunningOn)) return false;
        for (Integer module : this.modules) {
            if (this.psyche.isModuleDeactivated(module)) {
                return false;
            }
        }
        return true;
    }

    public boolean isRunning() {
        return this.running;
    }

    public boolean tryStart(int module) {
        this.moduleRunningOn = module;
        if (canStart()) {
            this.running = true;
            start();
            return true;
        }
        return false;
    }

    protected void start() {}

    public final void coreUpdate() {
        if (active()) {
            this.ticksPassed++;
            if (this.blockOtherModules != null) this.blockOtherModules.forEach(this.psyche::blockModule);
            update();
        }
    }

    protected void update() {}

    public boolean shouldKeepGoing() {
        return false;
    }

    public boolean canStart() {
        return true;
    }

    public void onEnd() {
        this.ticksPassed = 0;
        this.running = false;
        this.moduleRunningOn = -1;
    }

    public boolean cancelNext() {return false;}

    public void addModule(int module) {
        this.modules.add(module);
    }

    public void addBlockedModule(int module) {
        if (this.blockOtherModules == null) {
            this.blockOtherModules = Lists.newArrayList();
        }
        this.blockOtherModules.add(module);
    }

    public Psyche<E> getPsyche() {
        return this.psyche;
    }

    protected int getTicksPassed() {
        return this.ticksPassed;
    }
}
