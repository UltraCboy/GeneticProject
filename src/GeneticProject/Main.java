package GeneticProject;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

import Fitness.*;
import Schedule.*;

public class Main {
	// Settings to load from settings.json
	static String FOLDER;				// Name of folder containing data
	static int POPULATION_SIZE;			// # of specimens per generation
	static int MAX_GENS;				// Max # of gens this simulation will run
	static double FITNESS_THRESHOLD;	// When the best reaches this threshold, evolution stops
	static int CROSSOVER_POINTS;		// Number of crossover points during reproduction
	static int MUTATION_CHANCE;			// Odds of mutation per child, out of 100
	static int MAJOR_MUTATION_CHANCE; 	// Odds of multiple mutated genes, out of 100
	static int CONSTRAINT_WEIGHT; 		// Weight of constraints vs objective function
	static double[] CRITERIA_WEIGHTS;	// Tricriteria weights
	static int SEED;					// RNG seed (for random seed, use -1)
	
	@SuppressWarnings("unused")
	public static void main(String [] args) {
		
		// Read in settings
		System.out.println("Loading settings.json...\n");
		JSONParser parser = new JSONParser();
		Object obj;
		try {
			obj = parser.parse(new FileReader("settings.json"));
			JSONObject json = (JSONObject)obj;
			FOLDER = (String)json.get("DATA_FOLDER");
			POPULATION_SIZE = (int)((long)json.get("POPULATION_SIZE"));
			MAX_GENS = (int)((long)json.get("MAX_GENS"));
			FITNESS_THRESHOLD = (double)json.get("FITNESS_THRESHOLD");
			CROSSOVER_POINTS = (int)((long)json.get("CROSSOVER_POINTS"));
			MUTATION_CHANCE = (int)((long)json.get("MUTATION_CHANCE"));
			MAJOR_MUTATION_CHANCE = (int)((long)json.get("MAJOR_MUTATION_CHANCE"));
			CONSTRAINT_WEIGHT = (int)((long)json.get("CONSTRAINT_WEIGHT"));
			CRITERIA_WEIGHTS = new double[]{
				(double)json.get("WEIGHT_W"),
				(double)json.get("WEIGHT_Q"),
				(double)json.get("WEIGHT_T")
			};
			SEED = (int)((long)json.get("SEED"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Seed the Random
		if (SEED == -1) {
			int seed = new Random().nextInt(1000000);
			RandomSingleton.getInstance(seed);
			System.out.println("Using random seed " + seed);
		}
		else {
			RandomSingleton.getInstance(SEED);
			System.out.println("Using set seed " + SEED);
		} System.out.println();
		
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
		
		// Make first generation
		System.out.println("Beginning evolution...\n");
		FitnessCalculator f = new FitnessTeacherTricriteriaModel(
			CONSTRAINT_WEIGHT, conflictTable, threeCredits, morningTimes, afternoonTimes, 
			eveningTimes, mwfTimeslots, teachers, courselist, rooms, satisfaction, CRITERIA_WEIGHTS
		);
		List<Schedule> gen = new ArrayList<Schedule>();
		for(int i = 0; i < POPULATION_SIZE; i++)
			gen.add(new Schedule(courselist, rooms, numTimeslots, numTeachers, CROSSOVER_POINTS, f));
		Collections.sort(gen);
		
		// Evolve
		int genNum = 1;
		System.out.println(Misc.printGeneration(genNum, POPULATION_SIZE, gen));
		while(genNum < MAX_GENS && gen.get(0).getFitness() > FITNESS_THRESHOLD) {
			Misc.killHalf(gen);
			Misc.reproduce(gen, MUTATION_CHANCE, MAJOR_MUTATION_CHANCE);
			Collections.sort(gen);
			genNum++;
			System.out.println(Misc.printGeneration(genNum, POPULATION_SIZE, gen));
		}
		System.out.print("\nStopping evolution: ");
		if(genNum >= MAX_GENS) System.out.println("Maximum generations reached.\n");
		else System.out.println("Fitness threshold reached.\n");
		//System.out.println(gen.get(0));
		System.out.println("Best speciman:\n" + Misc.printFullSchedule(gen.get(0), timeslotsRecords, courselist));
		System.out.println("Fitness: " + f.calculateFitness(gen.get(0)) + "\n");
	}
}
