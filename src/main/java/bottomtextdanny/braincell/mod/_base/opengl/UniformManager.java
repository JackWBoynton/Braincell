package bottomtextdanny.braincell.mod._base.opengl;

import bottomtextdanny.braincell.base.pair.Pair;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Map;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.opengl.GL43;

@OnlyIn(Dist.CLIENT)
public final class UniformManager {
   private final Map uniformArrayDirMap = Maps.newHashMap();
   private final Object2IntOpenHashMap uniformDirMap = new Object2IntOpenHashMap();
   private final GLProgram program;

   public UniformManager(GLProgram program, Pair... arraySizes) {
      this.program = program;
      if (arraySizes.length > 0) {
         int bracketIndex = ((String)arraySizes[0].left()).indexOf(91);

         for(int i = 0; i < arraySizes.length; ++i) {
            int size = (Integer)arraySizes[i].right();
            int[] array = new int[size];
            String name = (String)arraySizes[i].left();
            String firstPart = name.substring(0, bracketIndex + 1);
            String secondPart = name.substring(bracketIndex + 1);

            for(int j = 0; j < size; ++j) {
               array[j] = GL43.glGetUniformLocation(program.getProgramID(), firstPart + j + secondPart);
            }

            this.uniformArrayDirMap.put(i, array);
         }
      }

   }

   public int[] retrieveLocations(int pointer) {
      return (int[])this.uniformArrayDirMap.get(pointer);
   }

   public int retrieveLocation(String str) {
      if (!this.uniformDirMap.containsKey(str)) {
         int newLoc = GL43.glGetUniformLocation(this.program.getProgramID(), str);
         this.uniformDirMap.put(str, newLoc);
         return newLoc;
      } else {
         return this.uniformDirMap.getOrDefault(str, -1);
      }
   }
}
