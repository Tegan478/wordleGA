import java.util.Map;
import java.util.Random;

public class Game{
	public int maxGuesses = 20;
	public static String answer; 
	public Chromo chrom;



	//public String[][] information = new String[26][3]; //2d array that hold letters and information known about that letter
	//public String correct;
	//public static Map<String, int[]> freq;

	public Game (String answer, Chromo chrom) {
		this.answer = answer;
		this.chrom = chrom;
	}

	public void testFitness (){
		Guess guess = new Guess();
		String choice = chrom.word;
		int tries = 0;
		//System.out.println(guess.searchSpace);
		while (!guess.newGuess(choice) && tries < maxGuesses) {
			//System.out.println(guess.searchSpace);
			//have choice selection in here
			//System.out.println(guess.searchSpace + " " + choice + " " + answer);
			choice = randSelect(guess);
			tries++;
		}
		//System.out.println(tries);
		chrom.setFitness(chrom.getFitness() + tries);
		/*
		if (guess.correct) {
			System.out.println(this.answer + " " +tries);
		} else {
			System.out.println(this.answer + " " + "did not get");
		}
		*/

		//have selection for next word based on policy and searchspace
	}

	public String randSelect(Guess guess) {
		Random r = new Random();
		//System.out.println(guess.searchSpace);
		int rand = guess.searchSpace;
		if (guess.searchSpace == 1) return guess.remGuess.get(0);
		return guess.remGuess.get(r.nextInt(rand));
	}


}

