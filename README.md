# Genetic Project: The Class Scheduling Problem

## Description
This is an implementation of a genetic algorithm in order to produce a valid class schedule, as described by Calvin Hoang Thach in his paper "The University Class Scheduling Problem". Much inspiration for how this algorithm was developed, as well as the data used to train the algorithm, was taken from Thach's paper. In particular, this algorithm is modeled after his Teacher Tricriteria Model, as described in Section 2.2.6 of the paper.

## How to Run
- Open the terminal from the project folder.
- Run `java -jar GeneticProject.jar`.
- The program will create the first generation randomly, then produce new generations via evolution.
  - At each generation, the fitnesses of the best, median, and worst hypotheses will be printed. Lower fitness is better.
- Once either the fitness threshold was reached, or the maximum generations was reached, the simulation will halt.
- It will then print out the best hypothesis the simulation produced, along with its fitness.

## Configure Settings
`settings.json` contains a number of different settings that influence how the simulation runs. The settings are as follows:
- `DATA_FOLDER`: the folder name from which data .csv files are pulled from.
- `POPULATION_SIZE`: the number of hypotheses per generation.
- `MAX_GENS`: the maximum number of generations the simulation will run before halting.
- `FITNESS_THRESHOLD`: if the best hypothesis of a generation goes below this threshold, the simulation will halt.
- `CROSSOVER_POINTS`: the number of genes crossed during the mutation step.
- `MUTATION_CHANCE`: chance, out of 100, of a hypothesis receiving one mutation.
- `MAJOR_MUTATION_CHANCE`: chance, out of 100, of a hypothesis receiving multiple mutations.
- `CONSTRAINT_WEIGHT`: the weight given to constraints as compared to objective in calculating fitness.
- `WEIGHT_W`: the weight given to the Days Of Week objective.
- `WEIGHT_Q`: the weight given to the Teacher Difference objective.
- `WEIGHT_T`: the weight given to the satisfaction objective.
- `SEED`: the Random Number Generator seed. Set to -1 to use a random seed.
If all the above settings are kept the same, using the same random seed, the program will always produce the same output.

## Example Output
`example_output.txt` contains an example output of the program, using the following `settings.json` contents:
```json
{
	"DATA_FOLDER": "simulated-data",
	"POPULATION_SIZE": 10000,
	"MAX_GENS": 200,
	"FITNESS_THRESHOLD": 0.0,
	"CROSSOVER_POINTS": 3,
	"MUTATION_CHANCE": 10,
	"MAJOR_MUTATION_CHANCE": 2,
	"CONSTRAINT_WEIGHT": 100,
	"WEIGHT_W": 0.33, "WEIGHT_Q": 0.33, "WEIGHT_T": 0.33,
	"SEED": 0
}
```

## Sources
Thach, Calvin. "The University Class Scheduling Problem." (2020). https://scholarworks.calstate.edu/downloads/nk322g57s.
