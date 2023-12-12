package GeneticProject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Fitness.FitnessTeacherTricriteriaModel;
import Schedule.Schedule;
import Schedule.Class;

/**
 * Some extra code used to compare my evolution results to Thach's results,
 * as well as random specimen
 */
public class CompareResults {
	public static void main(String[] args) {
		String FOLDER = "simulated-data";
		int CONSTRAINT_WEIGHT = 100;
		int CROSSOVER_POINTS = 3;
		double[] CRITERIA_WEIGHTS = {0.33, 0.33, 0.33};
		RandomSingleton.getInstance(0);
		// Read in records
		List<List<String>> courselistRecords, roomsRecords, timeslotsRecords;
		List<List<String>> similarTimesMWFRecords, similarTimesTRRecords;
		List<List<String>> threeCreditRecords;
		List<List<String>> morningTimeRecords, afternoonTimeRecords, eveningTimeRecords;
		List<List<String>> teacherRecords, satisfactionRecords;
		System.out.println("Loading csv files...\n");
		try {
			courselistRecords = Misc.readCSV(FOLDER, "courselist-simulated.csv");
			roomsRecords = Misc.readCSV(FOLDER, "rooms-simulated.csv");
			timeslotsRecords = Misc.readCSV(FOLDER, "timeslots-all.csv");
			similarTimesMWFRecords = Misc.readCSV(FOLDER, "similar-times-mwf.csv");
			similarTimesTRRecords = Misc.readCSV(FOLDER, "similar-times-tr.csv");
			threeCreditRecords = Misc.readCSV(FOLDER, "timeslots-3credits.csv");
			morningTimeRecords = Misc.readCSV(FOLDER, "timeslots-morning.csv");
			afternoonTimeRecords = Misc.readCSV(FOLDER, "timeslots-afternoon.csv");
			eveningTimeRecords = Misc.readCSV(FOLDER, "timeslots-evening.csv");
			teacherRecords = Misc.readCSV(FOLDER, "preference-simulated.csv");
			satisfactionRecords = Misc.readCSV(FOLDER, "satisfaction-simulated.csv");
		} catch(IOException e) {
			System.out.println("Failed to read files. Aborting program.");
			return;
		}
		
		// Convert records to int tables
		int[][] courselist = Misc.recordsToInts(courselistRecords, 5);
		int[][] rooms = Misc.recordsToInts(roomsRecords, 2);
		int numTimeslots = timeslotsRecords.size() - 1;
		boolean[][] conflictTable = Misc.createConflictTable(similarTimesMWFRecords, similarTimesTRRecords, numTimeslots);
		int[][] tCTemp = Misc.recordsToInts(threeCreditRecords, 1);
		List<Integer> threeCredits = new ArrayList<Integer>();
		for(int[] i : tCTemp)
			threeCredits.add(i[0]);
		List<Integer> mwfTimeslots = new ArrayList<Integer>();
		for(String s : similarTimesMWFRecords.get(1)) {
			if(s.equals("")) continue;
			mwfTimeslots.add(Integer.parseInt(s));
		}
		int[][] mTTemp = Misc.recordsToInts(morningTimeRecords, 1);
		List<Integer> morningTimes = new ArrayList<Integer>();
		for(int[] i : mTTemp)
			morningTimes.add(i[0]);
		int[][] aTTemp = Misc.recordsToInts(afternoonTimeRecords, 1);
		List<Integer> afternoonTimes = new ArrayList<Integer>();
		for(int[] i : aTTemp)
			afternoonTimes.add(i[0]);
		int[][] eTTemp = Misc.recordsToInts(eveningTimeRecords, 1);
		List<Integer> eveningTimes = new ArrayList<Integer>();
		for(int[] i : eTTemp)
			eveningTimes.add(i[0]);
		int[][] teachers = Misc.recordsToInts(teacherRecords, 7);
		int numTeachers = teachers.length;
		satisfactionRecords.remove(0);
		int[][] satisfaction = Misc.recordsToInts(satisfactionRecords, 35);
		// Make Fitness object
		FitnessTeacherTricriteriaModel f = new FitnessTeacherTricriteriaModel(
				CONSTRAINT_WEIGHT, conflictTable, threeCredits, morningTimes, afternoonTimes, 
				eveningTimes, mwfTimeslots, teachers, courselist, rooms, satisfaction, CRITERIA_WEIGHTS
			);
		// Random speciman
		Schedule rand = new Schedule(courselist, rooms, numTimeslots, numTeachers, CROSSOVER_POINTS, f);
		System.out.println("Random speciman:");
		System.out.println(Misc.printFullSchedule(rand, timeslotsRecords, courselist));
		System.out.println("Fitness: " + f.calculateFitness(rand) + "\n");
		// Best Speciman from Evolution
		Schedule myBest = new Schedule(courselist, rooms, numTimeslots, numTeachers, CROSSOVER_POINTS, f);
		int[][] output = {
				{1, 1, 10, 10, 29},
				{2, 1, 8, 9, 31},
				{3, 1, 11, 3, 32},
				{4, 2, 4, 5, 84},
				{5, 2, 6, 4, 81},
				{6, 2, 4, 5, 79},
				{7, 3, 1, 6, 84},
				{8, 3, 9, 7, 58},
				{9, 3, 1, 2, 81},
				{10, 4, 2, 2, 48},
				{11, 4, 9, 9, 67},
				{12, 4, 7, 9, 69},
				{13, 5, 7, 3, 50},
				{14, 5, 3, 5, 80},
				{15, 5, 6, 1, 80},
				{16, 6, 5, 6, 86},
				{17, 6, 3, 8, 82},
				{18, 6, 9, 2, 82},
				{19, 7, 4, 10, 74},
				{20, 7, 6, 9, 59},
				{21, 7, 8, 3, 79},
				{22, 8, 11, 1, 79},
				{23, 8, 8, 7, 49},
				{24, 8, 10, 7, 70},
				{25, 9, 3, 6, 66},
				{26, 9, 10, 3, 83},
				{27, 9, 7, 2, 61},
				{28, 10, 5, 4, 82},
				{29, 10, 11, 10, 71}
		};
		for(int i = 0; i < output.length; i++) {
			Class c = myBest.classes.get(i);
			c.room = output[i][2];
			c.teacher = output[i][3];
			c.timeslot = output[i][4];
		}
		System.out.println("Best speciman from evolution:");
		System.out.println(Misc.printFullSchedule(myBest, timeslotsRecords, courselist));
		System.out.println("Fitness: " + f.calculateFitness(myBest) + "\n");
		// Thach's Solution
		Schedule thach = new Schedule(courselist, rooms, numTimeslots, numTeachers, CROSSOVER_POINTS, f);
		int[][] thachOut = {
				{1, 1, 10, 2, 42},
				{2, 1, 9, 4, 42},
				{3, 1, 11, 8, 42},
				{4, 2, 11, 7, 78},
				{5, 2, 8, 2, 74},
				{6, 2, 11, 10, 60},
				{7, 3, 11, 7, 69},
				{8, 3, 5, 5, 83},
				{9, 3, 9, 3, 75},
				{10, 4, 1, 6, 83},
				{11, 4, 10, 9, 70},
				{12, 4, 11, 9, 75},
				{13, 5, 5, 6, 86},
				{14, 5, 4, 5, 86},
				{15, 5, 11, 4, 81},
				{16, 6, 10, 8, 81},
				{17, 6, 11, 3, 86},
				{18, 6, 11, 1, 80},
				{19, 7, 10, 10, 74},
				{20, 7, 6, 3, 80},
				{21, 7, 9, 3, 73},
				{22, 8, 10, 9, 78},
				{23, 8, 6, 7, 49},
				{24, 8, 11, 1, 79},
				{25, 9, 5, 6, 78},
				{26, 9, 5, 5, 80},
				{27, 9, 9, 2, 81},
				{28, 10, 10, 10, 72},
				{29, 10, 11, 9, 73}
		};
		for(int i = 0; i < thachOut.length; i++) {
			Class c = thach.classes.get(i);
			c.room = thachOut[i][2];
			c.teacher = thachOut[i][3];
			c.timeslot = thachOut[i][4];
		}
		//thach.classes.remove(6);
		System.out.println("Thach's solution:");
		System.out.println(Misc.printFullSchedule(thach, timeslotsRecords, courselist));
		System.out.println("Fitness: " + f.calculateFitness(thach) + "\n");
	}
}
