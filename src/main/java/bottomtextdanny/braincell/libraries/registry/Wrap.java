package bottomtextdanny.braincell.libraries.registry;

import bottomtextdanny.braincell.Braincell;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Wrap<T> implements Supplier<T> {
	protected final ResourceLocation key;
	protected ResourceKey<Registry<T>> type;
	protected @Nullable Supplier<T> sup;
	protected LinkedList<Consumer<T>> callOutStack;
	protected T obj;
	@Nullable
	private ModDeferringManager modSolvingState;
	
	public Wrap(ResourceLocation name, Supplier<T> sup) {
		Objects.requireNonNull(sup);
		this.sup = sup;
		this.key = name;
	}

	public void setupForDeferring(ModDeferringManager modSolvingState) {
		if (modSolvingState.isOpen()) {
			this.modSolvingState = modSolvingState;
		}
	}
	
	public void addSolvingCallout(Consumer<T> callOut) {
		if (this.callOutStack == null) {
            this.callOutStack = new LinkedList<>();
		}
        this.callOutStack.add(callOut);
	}
	
	public void solve() {
        this.obj = this.sup.get();

		Object maybeHook = Braincell.common().getSolvingHooks().getHook(this.type);

		if (maybeHook instanceof Consumer<?> hook) {
			((Consumer<? super T>)hook).accept(this.obj);
		}

		if (this.callOutStack != null) {
            this.callOutStack.forEach(call -> call.accept(this.obj));
			this.callOutStack = null;
		}

        this.sup = null;
	}
	
	public T get() {
		return Objects.requireNonNull(this.obj, this.key.getNamespace() + " object called before being solved! key: " + this.key);
	}

	@Nullable
	public ModDeferringManager getModSolvingState() {
		return this.modSolvingState;
	}

	public ResourceLocation getKey() {
		return this.key;
	}
}
