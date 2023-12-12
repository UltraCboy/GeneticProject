package Fitness;

import java.util.List;
import Schedule.*;
import Schedule.Class;

/**
 * Calculates fitness in order to make the number of classes each teacher
 * teaches as even as possible
 */
public class FitnessTeacherDifferenceModel extends FitnessTeacherPreferenceModel {

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
	 */
	public FitnessTeacherDifferenceModel(int w, boolean[][] con, List<Integer> ti, List<Integer> m, List<Integer> a,
			List<Integer> e, List<Integer> mwf, int[][] te, int[][] cou, int[][] r) {
		super(w, con, ti, m, a, e, mwf, te, cou, r);
	}
	
	@Override
	protected double calculateObjective(Schedule s) {
		int[] coursesPerTeacher = new int[teacherTable.length];
		for(Class c : s.classes)
			coursesPerTeacher[c.teacher - 1]++;
		double target = (double)s.numTeachers / s.classes.size();
		double q = 0.0;
		for(int i : coursesPerTeacher) 
			q += Math.abs(i - target);
		return q;
	}

}
