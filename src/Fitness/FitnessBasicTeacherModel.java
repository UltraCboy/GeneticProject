package Fitness;

import java.util.List;

import Schedule.Class;
import Schedule.Schedule;

/**
 * Calculates fitness based on basic teacher assignment, ensuring a teacher
 * isn't assigned to two classes with overlapping timeslots
 */
public class FitnessBasicTeacherModel extends FitnessBasicModel {
	int[][] teacherTable;
	
	/**
	 * Creates an instance.
	 * @param w   Constraint weight
	 * @param c   Timeslot conflict table
	 * @param ti  List of timeslots that are 3 credits long 
	 *            (timeslots not in this List are assumed to be 4 credit)
	 * @param m   List of morning timeslots
	 * @param a   List of afternoon timeslots
	 * @param e   List of evening timeslots
	 * @param mwf List of timeslots during Monday, Wednesday, or Friday
	 *            (timeslots not in this List are assumed to be during Tuesday or Thursday)
	 * @param te  Teacher table
	 */
	public FitnessBasicTeacherModel (int w, boolean[][] c, List<Integer> ti, List<Integer> m, 
			List<Integer> a, List<Integer> e, List<Integer> mwf, int[][] te) {
		super(w, c, ti, m, a, e, mwf);
		teacherTable = te;
	}
	
	@Override
	protected int calculateConstraints(Schedule s) {
		// A teacher cannot teach two classes in overlapping timeslots [2.18]
		int f = super.calculateConstraints(s);
		List<Class> c = s.classes;
		for(int i = 0; i < c.size(); i++) {
			for(int j = i + 1; j < c.size(); j++) {
				int t1 = c.get(i).timeslot, t2 = c.get(j).timeslot;
				int u1 = c.get(i).teacher, u2 = c.get(j).teacher;
				if(conflictTable[t1][t2] && u1 == u2) f++;
			}
		}
		return f;
	}
}
