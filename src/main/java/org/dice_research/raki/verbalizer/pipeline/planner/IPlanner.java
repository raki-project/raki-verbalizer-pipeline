package org.dice_research.raki.verbalizer.pipeline.planner;

/**
 * @param <T>
 *
 * @author Rene Speck
 *
 *
 */
public interface IPlanner<T> {

  /**
   * Executes the planner.
   */
  IPlanner<T> build();
}
