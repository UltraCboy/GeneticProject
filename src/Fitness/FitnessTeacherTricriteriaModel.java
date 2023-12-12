package Fitness;

import java.util.List;
import Schedule.Schedule;

/**
 * Calculates fitness based on 3 objective criteria:
 * - In order to equalize number of MWF vs TR classes
 * - In order to make the number of classes each teacher teaches as even as possible
 * - Via a Teacher Satisfaction Matrix
 */
public class FitnessTeacherTricriteriaModel extends FitnessTeacherPreferenceModel {
	int[][] satisfaction;
	double[] criteriaWeights;
	FitnessBasicModel f1, f2, f3;
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
	 * @param cw  Criteria weights
	 */
	public FitnessTeacherTricriteriaModel(int w, boolean[][] con, List<Integer> ti, List<Integer> m, List<Integer> a,
			List<Integer> e, List<Integer> mwf, int[][] te, int[][] cou, int[][] r, int[][] sat, double[] cw) {
		super(w, con, ti, m, a, e, mwf, te, cou, r);
		satisfaction = sat;
		criteriaWeights = cw;
		f1 = new FitnessBasicModel(w, con, ti, m, a, e, mwf);
		f2 = new FitnessTeacherDifferenceModel(w, con, ti, m, a, e, mwf, te, cou, r);
		f3 = new FitnessTeacherSatisfactionModel(w, con, ti, m, a, e, mwf, te, cou, r, sat);
	}
	
	@Override
	public double calculateObjective(Schedule s) {
		double w = f1.calculateObjective(s);
		double q = f2.calculateObjective(s);
		double t = f3.calculateObjective(s);
		return criteriaWeights[0] * w + criteriaWeights[1] * q + criteriaWeights[2] * t;
	}

}
