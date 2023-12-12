package GeneticProject;

import java.util.Random;

/**
 * A Random Number Generator following Singleton Pattern, so that
 * all classes across the project use the same RNG, allowing results
 * to be reproducible.
 */
public class RandomSingleton {
	private static volatile RandomSingleton instance = null;
	private Random r;
	
	private RandomSingleton(int seed) {
		if(seed < 0) r = new Random();
		else r = new Random(seed);
	}
	
	/**
	 * Gets the instance. If it doesn't exist, it makes one with a random seed.
	 * @return RandomSingleton instance
	 */
	public static synchronized RandomSingleton getInstance() {
		if(instance == null) instance = new RandomSingleton(-1);
		return instance;
	}
	
	/**
	 * Gets the instance. If it doesn't exist, it makes one with a set seed.
	 * @param seed RNG seed
	 * @return RandomSingleton instance
	 */
	public static synchronized RandomSingleton getInstance(int seed) {
		if(instance == null) instance = new RandomSingleton(seed);
		return instance;
	}
	
	public Random getRandom() { return r; }
}
