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

  // TODO: remove return, we use Output interface
  /**
   * Creates the results and gets the results of this planner.
   *
   * @return
   */
  @Deprecated
  T results();
}
