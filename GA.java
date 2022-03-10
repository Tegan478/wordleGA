import java.util.Scanner;

import java.util.Arrays;
import java.util.Collections;
import java.util.Random;
import java.io.File;
import java.io.FileNotFoundException;

public class GA {
	static //haha these might have to be lists!!! uh oh!! would make it easier to remove i believe. ask vera what would be best to use
	String[] words; //fill with possible answers
  public static String[] guesses; //fill with possible guesses
  int wordsLen;
  static int guessesLen;
  static int elites = 2;
  static int[] trainingWords;
  static int numTrainingWords = 100;
  static int population = 20;
  static Chromo[] chromos;
  private static double crossoverRate = .5;
  private static double mutationRate = .5;
  private static double mutationRateE = .7;
  static int generations = 300;
	
	public GA() throws FileNotFoundException {
		//System.out.println("happening");
		trainingWords = new int[numTrainingWords];
		
		File file = new File("./src/words.txt");
		//File file = new File("./src/tester.txt");
		wordsLen = 0;
	    guessesLen = 0;
	    Scanner sc = new Scanner(file);
	    while(sc.hasNext()){
	      String line = sc.nextLine();       
	      wordsLen++;
	    }
	    sc.close();
	    File file1 = new File("./src/guesses.txt");
	    //File file1 = new File("./src/tester.txt");
	    Scanner scc = new Scanner(file1);
	    while(scc.hasNext()){
	      String line = scc.nextLine();       
	      guessesLen++;
	    }
	    scc.close();
	    words = new String[wordsLen];
	    guesses = new String[guessesLen];
	    chromos = new Chromo[population];
	    
	}

  public void fillArrays() throws FileNotFoundException {
	File file = new File("./src/words.txt");
	//File file = new File("./src/tester.txt");
    Scanner sc1 = new Scanner(file);
    for (int i=0; i<wordsLen; i++) {
      String line = sc1.nextLine();  
      words[i] = line;
    }
    sc1.close();
    File file1 = new File("./src/guesses.txt");
    //File file1 = new File("./src/tester.txt");
    Scanner sc2 = new Scanner(file1);
    for (int i=0; i<guessesLen; i++) {
      String line = sc2.nextLine();  
      guesses[i] = line;
    }
    sc2.close();
  }
  
  public void createPop() {
	  for (int i=0; i<population; i++) {
	    	chromos[i] = new Chromo(); //creates initial population
	    }
  }
  
  public void getWords() {
	  Random rand = new Random();
	  for (int i=0; i<numTrainingWords; i++) {
		  trainingWords[i] = rand.nextInt(wordsLen);
	  }
  }
  
  public void runGame() {
	  for (int i=0; i<population; i++) {
		  //System.out.println("before " + chromos[i].getFitness());
		  chromos[i].setFitness(0.0); //i know this is bad practice but... before it was using the previous fitness somehow...
		  int maxCount = 0;
		  for (int j=0; j<numTrainingWords; j++) {
			  //System.out.println(trainingWords[j]);
			  Game game = new Game(words[trainingWords[j]], chromos[i]);
			  if(!game.testFitness()) maxCount++;
			  if (maxCount > 1) {
				  chromos[i].setFitness(numTrainingWords * 10);
				  break;
			  }
		  }
		  //System.out.println(chromos[i].getFitness() + " / " + numTrainingWords);
		  chromos[i].setFitness(chromos[i].getFitness() / numTrainingWords); 
		  //System.out.println("chromo: " + chromos[i].chromoID + " with fitness "+ chromos[i].getFitness() + " " + chromos[i].word);
	  }
  }
  
  public Chromo selectParent() {
		
		//---------------- RANK PROPORTIONAL SELECTION --------------------
		//since chromos in current generation have been sorted, their spot is their rank

		//System.out.print("\n");
		Random rand = new Random();
		
		double sumRank = 0;
		double overallSumRanks = (this.population+1)*this.population/2.0;
		
		double probability = rand.nextDouble();
		//System.out.println("selection probability: "+probability);
		for (int c=0; c<this.population; c++) {
			sumRank += (c+1);
			//System.out.println("running fitness: "+runningFitness + "/total = "+runningFitness/totalFitness);
			if (probability < sumRank/overallSumRanks)
				return this.chromos[c];			
		}
		//dummy return; let's pick a parent at random
		return this.chromos[rand.nextInt(this.population)];
		
		
		/*
		int parentIndex = rand.nextInt(populationSize);
      for (int t=1; t<3; t++) {
          int newIndex = rand.nextInt(populationSize);
          if (this.population[newIndex].fitness>this.population[parentIndex].fitness)//since they are sorted, selecting lowest index is enough
              parentIndex=newIndex;
      }
      return this.population[parentIndex];
      */
	}
 
	public void crossover() {
		
		Chromo[] tng = new Chromo[this.population];
		int tngSize = 0;
		Random rand = new Random();
		//------------ CROSSOVER
		
		while (tngSize < this.population-elites) {
		
			Chromo parent1 = selectParent();
			Chromo parent2 = selectParent();
			while (parent1 == parent2)
				parent2 = selectParent();
			
			//System.out.println(Arrays.deepToString(parent1.getPolicy()));
			Chromo child1 = new Chromo (parent1.getStrat());
			Chromo child2 = new Chromo (parent2.getStrat());
			
			if (rand.nextDouble()<crossoverRate)
				//Chromo.uniform_crossover(child1,child2);
				//Chromo.singlepoint_crossover(child1,child2);
				Chromo.crossover(child1,child2);
			if (rand.nextDouble()<mutationRate)
				child1.mutate();
			if (rand.nextDouble()<mutationRate)
				child2.mutate();
			
			
			tng[tngSize] = child1;
			tngSize++;
			tng[tngSize] = child2;
			tngSize++;
		}
		// -------------------ELITISM----------------------------
		//have a seperate mutation on the elites that just messes with ss values
		if (rand.nextDouble() < mutationRateE) {
			this.chromos[0].mutateE();
			this.chromos[1].mutateE();
		}
		
		tng[tngSize] = this.chromos[0];
		tngSize++;
		
		tng[tngSize] = this.chromos[1];
		tngSize++;
		
		
		this.chromos = tng;
	}
  
  public static void main(String[] args) throws FileNotFoundException {
      GA wordle = new GA();
      wordle.fillArrays();
      wordle.getWords();
      wordle.createPop();
      
      //System.out.println(Arrays.toString(words));
      for (int i=0; i<generations; i++) {
    	  System.out.println("\n\nnext gen " + i);
    	  wordle.runGame();
    	  Arrays.sort(wordle.chromos);
    	  System.out.println("best? " + wordle.chromos[0].getFitness() + " " + wordle.chromos[0].word + " strat: " + wordle.chromos[0].strat.toString());
    	  wordle.crossover();
      }
      
      //have a method that chooses random __ words to run a game on and have the population do that.
      //run that inside as many training rounds as we want.
    }
}
