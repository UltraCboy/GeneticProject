package Fitness;

import java.util.List;

import Schedule.Class;
import Schedule.Schedule;

/**
 * A FitnessCalculator that only calculates fitness based on schedule validity.
 * - Each course is assigned exactly once
 * - Two courses cannot be assigned to the same room in overlapping timeslots
 */
public class FitnessVerySimple implements FitnessCalculator{
	int CONSTRAINT_MULTIPLIER;
	
	boolean[][] conflictTable;
	List<Integer> threeCredits;
	List<Integer> morning, afternoon, evening;
	
	/**
	 * Creates an instance
	 * @param w Constraint weight
	 * @param c Timeslot conflict table
	 * @param t List of timeslots that are 3 credits long
	 *          (timeslots not in this List are assumed to be 4 credit)
	 * @param m List of morning timeslots
	 * @param a List of afternoon timeslots
	 * @param e List of evening timeslots
	 */
	public FitnessVerySimple(int w, boolean[][] c, List<Integer> t, List<Integer> m, List<Integer> a, List<Integer> e) { 
		CONSTRAINT_MULTIPLIER = w; conflictTable = c; threeCredits = t;
		morning = m; afternoon = a; evening = e;
	}
	
	public double calculateFitness(Schedule s) {
		List<Class> c = s.classes;
		int out = 0;
		for(int i = 0; i < c.size(); i++) {
			// Check for 3 credits vs 4 credits [2.3, 2.4]
			int cr = c.get(i).credits;
			if(cr == 3) {
				if(!threeCredits.contains(c.get(i).timeslot)) out++;
			} else {
				if(threeCredits.contains(c.get(i).timeslot)) out++;
			}
			
			for(int j = i + 1; j < c.size(); j++) {
				int t1 = c.get(i).timeslot, t2 = c.get(j).timeslot;
				int r1 = c.get(i).room, r2 = c.get(j).room;
				// Check for overlapping classes [2.1]
				if(conflictTable[t1][t2] && r1 == r2) out++;
				// Check for overlapping time categories [2.5]
				if(r1 == r2) {
					if(morning.contains(t1) && morning.contains(t2)) out++;
					else if(afternoon.contains(t1) && afternoon.contains(t2)) out++;
					else if(evening.contains(t1) && evening.contains(t2)) out++;
				}
			}
		}
		return out;
	}
}
