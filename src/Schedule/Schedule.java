package Schedule;

import java.util.*;

import Fitness.FitnessCalculator;
import GeneticProject.Misc;
import GeneticProject.RandomSingleton;

/**
 * A class used to represent a school course curriculum, used as a hypothesis
 * for the genetic algorithm.
 */
public class Schedule implements Comparable<Schedule>{
	public List<Class> classes;
	private FitnessCalculator fitness;
	public int numTimeslots, numRooms, numTeachers;
	private int CROSSOVER_POINTS;
	
	/**
	 * Randomly generates a schedule
	 * @param courselist Course list
	 * @param rooms Rooms list
	 * @param timeslots Number of timeslots
	 * @param teachers Number of teachers
	 * @param cross Number of crossover points during reproduction
	 * @param f Fitness Calculator
	 */
	public Schedule(int[][] courselist, int[][] rooms, int timeslots, int teachers, int cross, FitnessCalculator f) {
		classes = new ArrayList<Class>();
		fitness = f;
		CROSSOVER_POINTS = cross;
		for(int[] c : courselist) 
			classes.add(Class.randomClass(c[0], c[1], c[3], rooms.length, timeslots, teachers));
		numTimeslots = timeslots; numRooms = rooms.length; numTeachers = teachers;
	}
	
	/**
	 * Creates an instance by crossing two parent schedules
	 * @param s1 Primary parent
	 * @param s2 Secondary parent
	 */
	public Schedule(Schedule s1, Schedule s2) {
		fitness = s1.fitness;
		CROSSOVER_POINTS = s1.CROSSOVER_POINTS;
		List<Class> c1 = s1.classes, c2 = s2.classes;
		List<Integer> crossPoints = Misc.generateCrossoverPoints(CROSSOVER_POINTS, c1.size());
		classes = new ArrayList<Class>();
		for(int i = 0; i < c1.size(); i++) {
			if(crossPoints.contains(i)) classes.add(c2.get(i));
			else classes.add(c1.get(i));
		}
		numTimeslots = s1.numTimeslots; numRooms = s1.numRooms; numTeachers = s1.numTeachers;
	}
	
	/**
	 * @return This schedule's fitness value (lower is better)
	 */
	public double getFitness() {
		return fitness.calculateFitness(this);
	}
	
	/**
	 * Mutates this Schedule by selecting 1 Class & one property from that class
	 * to randomize
	 */
	public void mutate() {
		Random r = RandomSingleton.getInstance().getRandom();
		int index = r.nextInt(classes.size());
		Class c = classes.get(index).clone(); // Class to mutate
		int property = r.nextInt(3); // Property to mutate
		switch(property) {
			case 0: // Room
				c.room = r.nextInt(numRooms) + 1; break;
			case 1: // Timeslot
				c.timeslot = r.nextInt(numTimeslots) + 1; break;
			case 2: // Teachers
				c.teacher = r.nextInt(numTeachers) + 1; break;
		}
		classes.remove(index);
		classes.add(index, c);
	}
	
	/**
	 * Mutates this Schedule a number of times
	 * @param num Number of times to mutate
	 */
	public void mutateMultiple(int num) {
		for(int i = 0; i < num; i++) mutate();
	}
	
	@Override
	public String toString() {
		String out = "";
		for(Class c : classes) out += c + "\n";
		out = out.substring(0, out.length() - 1);
		return out;
	}

	@Override
	public int compareTo(Schedule o) {
		return (int)((fitness.calculateFitness(this) - fitness.calculateFitness(o)) * 100);
	}
}
