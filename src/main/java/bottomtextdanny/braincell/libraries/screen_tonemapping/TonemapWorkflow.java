package bottomtextdanny.braincell.libraries.screen_tonemapping;

import bottomtextdanny.braincell.Braincell;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import bottomtextdanny.braincell.libraries.mod.BCStaticData;
import bottomtextdanny.braincell.libraries.shader.ShaderWorkflow;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.concurrent.ConcurrentLinkedDeque;

@OnlyIn(Dist.CLIENT)
public class TonemapWorkflow extends ShaderWorkflow {
    private final TonemapperPixelProgram tonemapperProgram = new TonemapperPixelProgram(this);
    private final ConcurrentLinkedDeque<TonemapAgent> agents = new ConcurrentLinkedDeque<>();
    private float outDesaturation;
    private Vector3f outChannelMultiplier = new Vector3f();

    @Override
    protected void execute() {
        Vector4f agentOutput = getAgentOutput();
        this.outChannelMultiplier = new Vector3f(agentOutput.x(), agentOutput.y(), agentOutput.z());
        this.outDesaturation = agentOutput.w();
        this.tonemapperProgram.flow();
    }

    @Override
    protected void tick() {
        if (this.agents.isEmpty()) return;

        if (!Minecraft.getInstance().isPaused()) {
            agents.removeIf(agent -> {
                agent.tick();
                return agent.removeIf();
            });
        }
    }

    private Vector4f getAgentOutput() {
        Vector4f output = new Vector4f();

        for (TonemapAgent agent : this.agents) {
            agent.render(BCStaticData.partialTick());
            output.add(agent.getChannelModifier().x(), agent.getChannelModifier().y(), agent.getChannelModifier().z(), agent.getSaturationModifier());
        }

        return output;
    }

    public float getOutDesaturation() {
        return this.outDesaturation;
    }

    public Vector3f getOutChannelMultiplier() {
        return this.outChannelMultiplier;
    }

    public boolean addAgent(TonemapAgent agent) {
        if (!invalidated && shouldApply() && agent != null) {
            return this.agents.add(agent);
        }
        return false;
    }

    @Override
    protected boolean shouldApply() {
        return Braincell.client().config().screenTonemapping();
    }
}
