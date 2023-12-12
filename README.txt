CSC 462 Artificial Intelligence
Genetic Algorithm
Christian Honicker

How To Run
----------
- Open the project folder
- Configure settings
	- Open "settings.json"
	- From here you can configure a number of settings pertaining to the algorithm
	- The same settings will always produce the same output
	- The settings & what they do are as follows:
		- DATA_FOLDER: the folder name from which data .csv files are pulled from
		- POPULATION_SIZE: the number of hypotheses per generation
		- MAX_GENS: the maximum number of generations the simulation will run before halting
		- FITNESS_THRESHOLD: if the best hypothesis of a generation goes below this threshold, the simulation will halt
		- CROSSOVER_POINTS: the number of genes crossed during the mutation step
		- MUTATION_CHANCE: chance, out of 100, of a hypothesis receiving one mutation
		- MAJOR_MUTATION_CHANCE: chance, out of 100, of a hypothesis receiving multiple mutations
		- CONSTRAINT_WEIGHT: the weight given to constraints as compared to objective in calculating fitness
		- WEIGHT_W: the weight given to the Days Of Week objective
		- WEIGHT_Q: the weight given to the Teacher Difference objective
		- WEIGHT_T: the weight given to the satisfaction objective
		- SEED: the Random Number Generator seed
- Run the program
	- Open the terminal from the project folder
	- Run: java -jar GeneticProject.jar
	- The program will create the first generation randomly, then produce new generations via evolution
		- At each generation, the fitnesses of the best, median, and worst hypotheses will be printed
	- Once either the fitness threshold was reached, or the maximum generations was reached, the simulation will halt
	- It will then print out the best hypothesis the simulation produced, along with its fitness

For a more in-depth description, please read the project report attached separately.