package bottomtextdanny.braincell.libraries.shader;

import bottomtextdanny.braincell.base.pair.Pair;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Map;

import static org.lwjgl.opengl.GL43.glGetUniformLocation;

@OnlyIn(Dist.CLIENT)
public final class UniformManager {
    private final Map<Integer, int[]> uniformArrayDirMap = Maps.newHashMap();
    private final Object2IntOpenHashMap<String> uniformDirMap = new Object2IntOpenHashMap<>();
    private final GLProgram<?> program;

    public UniformManager(GLProgram<?> program, Pair<String, Integer>... arraySizes) {
        super();
        Map<Integer, int[]> uniformArrayDirMap = this.uniformArrayDirMap;
        this.program = program;
        if (arraySizes.length > 0) {
            int bracketIndex = arraySizes[0].left().indexOf('[');

            for (int i = 0; i < arraySizes.length; i++) {
                int size = arraySizes[i].right();
                int[] array = new int[size];
                String name = arraySizes[i].left();
                String firstPart = name.substring(0, bracketIndex + 1);
                String secondPart = name.substring(bracketIndex + 1);
                for (int j = 0; j < size; j++) {
                    array[j] = glGetUniformLocation(program.getProgramID(), firstPart + j + secondPart);
                }

                uniformArrayDirMap.put(i, array);
            }
        }
    }

    public int[] retrieveLocations(int pointer) {
        return this.uniformArrayDirMap.get(pointer);
    }

    public int retrieveLocation(String str) {
        if (!this.uniformDirMap.containsKey(str)) {
            int newLoc = glGetUniformLocation(this.program.getProgramID(), str);
            this.uniformDirMap.put(str, newLoc);
            return newLoc;
        }
        return this.uniformDirMap.getOrDefault(str, -1);
    }
}
