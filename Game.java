import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Game{
	public int maxGuesses = 10;
	public static String answer; 
	public Chromo chrom;
	private List<String> possible;
	//public String[][] information = new String[26][3]; //2d array that hold letters and information known about that letter
	//public String correct;
	//public static Map<String, int[]> freq;
	public Game (String answer, Chromo chrom) {
		this.answer = answer;
		this.chrom = chrom;
		possible = new LinkedList<String>();
	}

	public boolean testFitness (){
		Guess guess = new Guess();
		String choice = chrom.word;
		int tries = 0;
		//System.out.println(guess.searchSpace);
		while (!guess.newGuess(choice) && tries < maxGuesses) {
			//System.out.println(guess.searchSpace);
			//have choice selection in here
			//System.out.println(guess.searchSpace + " " + choice + " " + answer);
			int k = chrom.strat.ceilingKey(guess.searchSpace);
			if (chrom.strat.get(k) == 0) {
				choice = randSelect(guess);
				//System.out.println("spec");
				/*
			} else if (chrom.strat.get(k) == 1){
				choice = randSelectFull(guess);
				//System.out.println("full");
				 * 
				 */
			} else {
				choice = noverlap(guess);
				//System.out.println("nov");
			}
			
			tries++;
		}
		//System.out.println(tries);
		chrom.setFitness(chrom.getFitness() + tries);
		return guess.correct;
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
	
	public String randSelectFull(Guess guess) {
		Random r = new Random();
		//System.out.println(guess.searchSpace);
		int rand = r.nextInt(GA.guessesLen);
		return GA.guesses[rand];
	}
	
	public String noverlap(Guess guess) {
		Random rand = new Random();
		String comb = "";
		boolean clean = true;
		for (int i=0; i<guess.tries.size(); i++) {
			comb+=guess.tries.get(i);
		}
		if (possible.size() == 0) {
			for (int i=0; i<GA.guessesLen; i++) {
				for (int j=0; j<5; j++) {
					if (comb.contains("" + GA.guesses[i].charAt(j))) {
						clean = false;
						break;
					}
				}
				if (clean) possible.add(GA.guesses[i]);
				clean = true;
			}
		} else {
			boolean rem = false;
			for (int i=0; i<possible.size(); i++) {
				for (int j=0; j<5; j++) {
					String p = possible.get(i);
					if (comb.contains("" + p.charAt(j))) {
						rem = true;
						break;
					}
				}
				if (rem) possible.remove(i);
				rem = false;
			}
		}
		if (possible.size() == 0) return chrom.word;
		return possible.get(rand.nextInt(possible.size()));
	}
	
	/*public String focusYellow(Guess guess) {
		List<String> possibleFY;
		List<Character> yellowLetters;
		for (int i = 0; i<guess.yellow.length; i++) {
			for (int j = 0; j<5; j++)
			{
				yellowLetters.add(guess.yellow[i].charAt(j));
			}
		}
		for (int i=0; i<GA.guessesLen; i++) {
			for (int j = 0; j<yellowLetters.size(); j++) {
				if (GA.guesses[i].contains()) {
					for(int l = 0; l < guess.yellow.length; l++) {
						for(int z = 0; z < 5; z++)
						if (GA.guesses[i].charAt(z) == guess.yellow[l].charAt(z)){
							
						}
					}
				} 
			}
		}
		return "";
	}
	*/
}


