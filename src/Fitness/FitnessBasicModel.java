package Fitness;

import java.util.List;

import Schedule.Class;
import Schedule.Schedule;

/**
 * Calculates fitness based on having equal distribution between
 * Monday-Wednesday-Friday classes and Tuesday-Thursday classes
 */
public class FitnessBasicModel extends FitnessVerySimple {
	List<Integer> mwfTimeslots;

	/**
	 * Creates an instance.
	 * @param w   Constraint weight
	 * @param c   Timeslot conflict table
	 * @param t   List of timeslots that are 3 credits long 
	 *            (timeslots not in this List are assumed to be 4 credit)
	 * @param m   List of morning timeslots
	 * @param a   List of afternoon timeslots
	 * @param e   List of evening timeslots
	 * @param mwf List of timeslots during Monday, Wednesday, or Friday
	 *            (timeslots not in this List are assumed to be during Tuesday or Thursday)
	 */
	public FitnessBasicModel(int w, boolean[][] c, List<Integer> t, List<Integer> m, List<Integer> a, List<Integer> e, List<Integer> mwf) {
		super(w, c, t, m, a, e);
		mwfTimeslots = mwf;
	}
	
	/**
	 * @param s A Schedule object
	 * @return The Schedule's fitness value, based on objective only (lower is better)
	 */
	protected double calculateObjective(Schedule s) {
		int w = 0;
		// MWF vs TR proportion [2.6]
		for(Class c : s.classes) {
			if(mwfTimeslots.contains(c.timeslot)) w++;
			else w--;
		}
		return Math.abs(w);
	}
	
	/**
	 * @param s A Schedule object
	 * @return The Schedule's fitness value, based on constraints only (lower is better)
	 */
	protected int calculateConstraints(Schedule s) {
		return (int)super.calculateFitness(s);
	}
	
	@Override
	public double calculateFitness(Schedule s) {
		return calculateObjective(s) + CONSTRAINT_MULTIPLIER * calculateConstraints(s);
	}
}
