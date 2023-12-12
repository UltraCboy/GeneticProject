package Fitness;

import Schedule.Schedule;

public interface FitnessCalculator {
	/**
	 * @param s A Schedule object
	 * @return The Schedule's fitness value (lower is better)
	 */
	public double calculateFitness(Schedule s);
}
