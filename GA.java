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
  static int[] trainingWords;
  static int numTrainingWords = 5;
  static int population = 5;
  static Chromo[] chromos;
	
	public GA() throws FileNotFoundException {
		//System.out.println("happening");
		trainingWords = new int[numTrainingWords];
		
		File file = new File("./src/words.txt");
		wordsLen = 0;
	    guessesLen = 0;
	    Scanner sc = new Scanner(file);
	    while(sc.hasNext()){
	      String line = sc.nextLine();       
	      wordsLen++;
	    }
	    sc.close();
	    File file1 = new File("./src/guesses.txt");
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
    Scanner sc1 = new Scanner(file);
    for (int i=0; i<wordsLen; i++) {
      String line = sc1.nextLine();  
      words[i] = line;
    }
    sc1.close();
    File file1 = new File("./src/guesses.txt");
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
  
  public static void runGame() {
	  for (int i=0; i<population; i++) {
		  for (int j=0; j<numTrainingWords; j++) {
			  //System.out.println(trainingWords[j]);
			  Game game = new Game(words[trainingWords[j]], chromos[i]);
			  game.testFitness();
		  }
		  chromos[i].fitness = chromos[i].fitness / numTrainingWords; 
		  System.out.println("chromo: " + chromos[i].chromoID + " with fitness "+ chromos[i].fitness);
	  }
  }
  
  
  public static void main(String[] args) throws FileNotFoundException {
      GA wordle = new GA();
      wordle.fillArrays();
      wordle.getWords();
      wordle.createPop();
      
      //System.out.println(Arrays.toString(words));
      runGame();
      //have a method that chooses random __ words to run a game on and have the population do that.
      //run that inside as many training rounds as we want.
    }
}