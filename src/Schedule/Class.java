package Schedule;

import java.util.Random;

import GeneticProject.RandomSingleton;

/**
 * A class used to represent a single course assignment, used as
 * a gene by the genetic algorithm
 */
public class Class implements Cloneable{
	public int section, course, credits, room, timeslot, teacher;
	static Random rand = RandomSingleton.getInstance().getRandom();
	
	/**
	 * Creates an instance
	 * @param s Section
	 * @param co Course
	 * @param cr Credits
	 * @param r Room
	 * @param ti Timeslot
	 * @param te Teacher
	 */
	public Class(int s, int co, int cr, int r, int ti, int te) {
		section = s; course = co; credits = cr;
		room = r; timeslot = ti; teacher = te;
	}
	
	/**
	 * Generates a class assignment at random
	 * @param s Section
	 * @param co Course
	 * @param cr Credits
	 * @param r Room upper bound
	 * @param ti Timeslot upper bound
	 * @return te Teacher upper bound
	 */
	public static Class randomClass(int s, int co, int cr, int r, int ti, int te) {
		return new Class(s, co, cr, rand.nextInt(r) + 1, rand.nextInt(ti) + 1, rand.nextInt(te) + 1);
	}
	
	@Override
	public Class clone() {
		return new Class(section, course, credits, room, timeslot, teacher);
	}
	
	@Override
	public String toString() {
		return section + ", " + course + ", " + room + ", " + teacher + ", " + timeslot;
	}
}
