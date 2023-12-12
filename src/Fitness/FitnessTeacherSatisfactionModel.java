package Fitness;

import java.util.List;

import Schedule.Class;
import Schedule.Schedule;

/**
 * Calculates fitness via a Teacher Satisfaction Matrix
 */
public class FitnessTeacherSatisfactionModel extends FitnessTeacherPreferenceModel{
	int[][] satisfaction;

	/**
	 * Creates an instance.
	 * @param w   Constraint weight
	 * @param con Timeslot conflict table
	 * @param ti  List of timeslots that are 3 credits long 
	 *            (timeslots not in this List are assumed to be 4 credit)
	 * @param m   List of morning timeslots
	 * @param a   List of afternoon timeslots
	 * @param e   List of evening timeslots
	 * @param mwf List of timeslots during Monday, Wednesday, or Friday
	 *            (timeslots not in this List are assumed to be during Tuesday or Thursday)
	 * @param te  Teacher table
	 * @param cou Course table
	 * @param r   Room table
	 * @param sat Satisfaction table
	 */
	public FitnessTeacherSatisfactionModel(int w, boolean[][] con, List<Integer> ti, List<Integer> m, List<Integer> a,
			List<Integer> e, List<Integer> mwf, int[][] te, int[][] cou, int[][] r, int[][] sat) {
		super(w, con, ti, m, a, e, mwf, te, cou, r);
		satisfaction = sat;
	}
	
	@Override
	protected double calculateObjective(Schedule s) {
		double t = 0;
		for(Class c : s.classes)
			t += satisfaction[c.teacher - 1][c.section + 5];
		return t;
	}
	
}
