package bottomtextdanny.braincell.mod.graphics.screen_tonemapping;

import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import bottomtextdanny.braincell.mod._base.BCStaticData;
import bottomtextdanny.braincell.mod._base.opengl.ShaderWorkflow;
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
        if (!Minecraft.getInstance().isPaused() && !this.agents.isEmpty()) {
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
        if (agent != null) {
            return this.agents.add(agent);
        }
        return false;
    }

    @Override
    protected boolean shouldApply() {
        return MC.player != null;
    }
}
