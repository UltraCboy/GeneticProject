package GeneticProject;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

import Schedule.*;
import Schedule.Class;

public class Misc {
	/**
	 * Reads in a CSV
	 * @param folder Folder name
	 * @param file File name
	 * @return A 2D list representation of the CSV
	 * @throws IOException
	 */
	public static List<List<String>> readCSV(String folder, String file) throws IOException{
		String path = folder + "/" + file;
		List<List<String>> records = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		        String[] values = line.split(",");
		        records.add(Arrays.asList(values));
		    }
		}
		return records;
	}
	
	/**
	 * Converts a List of records into a 2D array of integers
	 * @param records Records List
	 * @param cols Number of columns in the final array
	 * @return A int[][] representation of records
	 */
	public static int[][] recordsToInts(List<List<String>> records, int cols) {
		int[][] out = new int[records.size() - 1][cols];
		int curRow = -1;
		for(int i = 0; i < out.length; i++)
			for(int j = 0; j < cols; j++)
				out[i][j] = Integer.parseInt(records.get(i + 1).get(j));
		return out;
	}
	
	/**
	 * Converts two similar time records lists into a single conflict table
	 * @param mwf MWF similar times
	 * @param tr TR similar times
	 * @param timeslots Total number of timeslots
	 * @return A merged table of time conflicts
	 */
	public static boolean[][] createConflictTable(List<List<String>> mwf, List<List<String>> tr, int timeslots) {
		boolean[][] out = new boolean[timeslots + 1][timeslots + 1];
		cctSubMethod(out, mwf);
		cctSubMethod(out, tr);
		return out;
	}
	private static void cctSubMethod(boolean[][] out, List<List<String>> r) {
		for(int i = 2; i < r.size(); i++) {
			List<String> row = r.get(i);
			if(row.size() == 0 || row.get(1).equals("")) continue;
			for(int j = 2; j < row.size(); j++) {
				if(r.get(1).get(j).equals("")) continue;
				int outRow = Integer.parseInt(r.get(i).get(1));
				int outCol = Integer.parseInt(r.get(1).get(j));
				if(row.get(j).equals("X")) out[outRow][outCol] = true;
				else out[outRow][outCol] = false;
			}
		}
	}
	
	/**
	 * Kills off members of generation until its half its size
	 * @param gen Schedule Generation
	 */
	public static void killHalf(List<Schedule> gen) {
		int target = gen.size() / 2;
		Random r = RandomSingleton.getInstance().getRandom();
		while(gen.size() > target) {
			int num = r.nextInt((int)Math.pow(gen.size(), 2));
			int index = (int)Math.ceil(Math.sqrt(num)) - 1;
			if(index <= 0) continue;
			gen.remove(index);
		}
	}
	
	/**
	 * Reproduces members of generation until its double its size
	 * @param gen Schedule Generation
	 */
	public static void reproduce(List<Schedule> gen, int mutateChance, int majorMutate) {
		List<Schedule> foo = new ArrayList<Schedule>();
		for(Schedule s : gen) foo.add(s);
		Random r = RandomSingleton.getInstance().getRandom();
		while(foo.size() > 0) {
			// Pick two parents at random
			int i1 = r.nextInt(foo.size());
			Schedule s1 = foo.get(i1); foo.remove(i1);
			int i2 = r.nextInt(foo.size());
			Schedule s2 = foo.get(i2); foo.remove(i2);
			// Crossover to create children
			Schedule s3 = new Schedule(s1, s2);
			Schedule s4 = new Schedule(s2, s1);
			// Attempt mutation
			int mm = 10; // Max mutations per major mutation
			int r1 = r.nextInt(100);
			if(r1 < majorMutate) {
				int bar = r.nextInt(mm - 1) + 2;
				s3.mutateMultiple(bar);
			} else if(r1 < mutateChance) s3.mutate();
			int r2 = r.nextInt(100);
			if(r2 < majorMutate) {
				int bar = r.nextInt(mm - 1) + 2;
				s4.mutateMultiple(bar);
			} else if(r2 < mutateChance) s4.mutate();
			// Add children to generation
			gen.add(s3);
			gen.add(s4);
		}
	}
	
	/**
	 * Generates a list of unique crossover points for use in reproduction
	 * @param numPoints Number of points
	 * @param bound Upper bound (inclusive)
	 * @return A List of unique integers
	 */
	public static List<Integer> generateCrossoverPoints(int numPoints, int bound) {
		List<Integer> out = new ArrayList<Integer>();
		List<Integer> foo = new ArrayList<Integer>();
		for(int i = 1; i <= bound; i++) foo.add(i);
		Random r = RandomSingleton.getInstance().getRandom();
		while(out.size() < numPoints) {
			int bar = r.nextInt(foo.size());
			out.add(foo.get(bar));
			foo.remove(bar);
		}
		return out;
	}

	static final DecimalFormat df = new DecimalFormat("0.#");
	/**
	 * @param genNum Current generation number
	 * @param genSize Size of gen
	 * @param gen Schedule Generation
	 * @return A report of gen's best, median, and worst specimen
	 */
	public static String printGeneration(int genNum, int genSize, List<Schedule> gen) {
		String printGen = "Generation " + genNum + " | ";
		String printBest = "Best: " + df.format(gen.get(0).getFitness()) + " | ";
		String printMed = "Median: " + df.format(gen.get(genSize / 2 - 1).getFitness()) + " | ";
		String printWor = "Worst: " + df.format(gen.get(genSize - 1).getFitness());
		return printGen + printBest + printMed + printWor;
	}
	
	/**
	 * @param s Schedule
	 * @param timeslotRecords A records list mapping timeslot ID's to names
	 * @return A complete representation of the schedule
	 */
	public static String printFullSchedule(Schedule s, List<List<String>> timeslotRecords, int[][] courses) {
		String out = "";
		for(Class c : s.classes) {
			out += "Course " + c.section;
			out += " (" + courses[c.section - 1][1] + "-" + courses[c.section - 1][2] + ")";
			if (c.section < 10) out += " ";
			if (courses[c.section - 1][1] < 10) out += " ";
			out += " | Room " + c.room;
			if (c.room < 10) out += " ";
			out += " | Teacher " + c.teacher;
			if (c.teacher < 10) out += " ";
			out += " | " + timeslotRecords.get(c.timeslot).get(1) + "\n";
		}
		out = out.substring(0, out.length() - 1);
		return out;
	}
}
