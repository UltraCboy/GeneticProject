package Fitness;

import java.util.List;
import Schedule.*;
import Schedule.Class;

/**
 * Calculates fitness based on basic teacher preferences, including:
 * - Whiteboard vs Chalkboard
 * - Time of day
 * - Day of week
 * - Pure vs Applied Courses
 * - Number of Courses taught
 */
public class FitnessTeacherPreferenceModel extends FitnessBasicTeacherModel{
	int[][] courseTable, roomTable;
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
	public FitnessTeacherPreferenceModel (int w, boolean[][] con, List<Integer> ti, List<Integer> m, 
			List<Integer> a, List<Integer> e, List<Integer> mwf, int[][] te,
			int[][] cou, int[][] r) {
		super(w, con, ti, m, a, e, mwf, te);
		courseTable = cou; roomTable = r;
	}
	
	@Override
	protected int calculateConstraints(Schedule s) {
		int f = super.calculateConstraints(s);
		int[] coursesPerTeacher = new int[teacherTable.length];
		for(Class c : s.classes) {
			int t = c.teacher;
			int r = c.room;
			coursesPerTeacher[t - 1]++;
			// Whiteboard vs Chalkboard preferences [2.22, 2.23]
			int b1 = teacherTable[t - 1][3];
			if(b1 != 0) {
				int b2 = roomTable[r - 1][1];
				if(b1 == b2) f++;
			}
			// Timeslot category preferences [2.24, 2.25, 2.26]
			int t1 = teacherTable[t - 1][4];
			int t2 = c.timeslot;
			switch(t1) {
				case 0: break; // No preference
				case 1: // Morning
					if(!morning.contains(t2)) f++;
					break;
				case 2: // Afternoon
					if(!afternoon.contains(t2)) f++;
					break;
				case 3: // Evening
					if(!evening.contains(t2)) f++;
					break;
			}
			// MWF vs TR preferences [2.27, 2.28]
			int w1 = teacherTable[t - 1][5];
			switch(w1) {
				case 0: break; // No preference
				case 1: if(!mwfTimeslots.contains(t2)) f++; break; //MWF
				case 2: if(mwfTimeslots.contains(t2)) f++; break; // TR
			}
			// Pure vs Applied preferences [2.29, 2.30]
			// (Commented out due to exclusion in Teacher Satisfaction)
			/* int a1 = teacherTable[t - 1][6];
			if(a1 != 0) {
				int a2 = courseTable[c.section - 1][4];
				if(a1 != a2) f++;
			} */
		}
		// Number of courses preferences [2.20, 2.21]
		for(int i = 0; i < teacherTable.length; i++) {
			int lower = teacherTable[i][1], upper = teacherTable[i][2];
			int c = coursesPerTeacher[i];
			if(c < lower) f += lower - c;
			if(c > upper) f += c - upper;
		}
		return f;
	}
}
