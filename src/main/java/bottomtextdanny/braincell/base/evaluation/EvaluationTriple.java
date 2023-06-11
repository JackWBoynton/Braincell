package bottomtextdanny.braincell.base.evaluation;

import bottomtextdanny.braincell.base.function.TriFunction;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.function.Consumer;
import java.util.function.Function;

public final class EvaluationTriple extends AbstractEvaluation {
   private final LinkedList evaluatorList;
   private final String name;
   private final TriFunction evalFactory;

   private EvaluationTriple(String name, TriFunction evalFactory, LinkedList commonPredicates) {
      this.name = name;
      this.evalFactory = evalFactory;
      this.evaluatorList = commonPredicates;
      evaluations.add(this);
   }

   public static EvaluationTriple create(String name, TriFunction evalFactory, Function commonPredicates) {
      if (built) {
         throw new UnsupportedOperationException("Can't create evaluations after building!");
      } else {
         return new EvaluationTriple(name, evalFactory, (LinkedList)commonPredicates.apply(Lists.newLinkedList()));
      }
   }

   public static EvaluationTriple create(String name, TriFunction evalFactory) {
      if (built) {
         throw new UnsupportedOperationException("Can't create evaluations after building!");
      } else {
         return new EvaluationTriple(name, evalFactory, Lists.newLinkedList());
      }
   }

   public void addTest(Consumer pred) {
      this.evaluatorList.add(pred);
   }

   public Object test(Object subject0, Object subject1, Object subject2) {
      Evaluator current = EvaluationTriple.Evaluator.of(subject0, subject1, subject2, this.evalFactory.apply(subject0, subject1, subject2));
      Iterator var5 = this.evaluatorList.iterator();

      while(var5.hasNext()) {
         Consumer cons = (Consumer)var5.next();
         cons.accept(current);
         if (current.cancelled) {
            break;
         }
      }

      return current.get();
   }

   public static class Evaluator {
      private boolean cancelled;
      private Object value;
      private final Object evaluated0;
      private final Object evaluated1;
      private final Object evaluated2;

      private Evaluator(Object newEvaluated0, Object newEvaluated1, Object newEvaluated2, Object newValue) {
         this.evaluated0 = newEvaluated0;
         this.evaluated1 = newEvaluated1;
         this.evaluated2 = newEvaluated2;
         this.value = newValue;
      }

      public static Evaluator of(Object newEvaluated0, Object newEvaluated1, Object newEvaluated2, Object newValue) {
         return new Evaluator(newEvaluated0, newEvaluated1, newEvaluated2, newValue);
      }

      public void cancelEvaluation() {
         this.cancelled = true;
      }

      public void set(Object value) {
         this.value = value;
      }

      public Object get() {
         return this.value;
      }

      public Object getEvaluated0() {
         return this.evaluated0;
      }

      public Object getEvaluated1() {
         return this.evaluated1;
      }

      public Object getEvaluated2() {
         return this.evaluated2;
      }
   }
}
