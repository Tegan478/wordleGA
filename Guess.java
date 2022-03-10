  
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

/*
TODO:
make a guess class that holds the words so far
an array of greens, a 2d array of yellows, and a list of grays
could also have searchspace
 */
public class Guess{
	public List<String> tries;
	public String[] green;
	public String[] yellow;
	public List<String> gray; 
	public int searchSpace;
	public  Map<String, int[]> freq;
	public List<String> remGuess;
	public boolean correct = false;


	public Guess () {
		tries = new ArrayList<String>();
		green = new String[5];
		yellow = new String[5];
		for (int i=0; i<5; i++) {
			yellow[i] = "";
		}
		gray = new ArrayList<String>();
		searchSpace = GA.guessesLen;
		copyMapandList();
	}

	public void copyMapandList() {
		/*for (Entry<String, int[]> entry: Game.freq.entrySet()) {
		this.freq.put(entry.getKey(),entry.getValue().clone()); //is this done right? w/ the clone part 
	}*/
		remGuess = new ArrayList<String>();
		for (int i=0; i<GA.guessesLen; i++) {
			remGuess.add(GA.guesses[i]);
		}
	}

	public boolean newGuess(String guess) {
	    //System.out.println("TRIES AND COMP " + guess + " " + Game.answer);
		if (guess.equals(Game.answer)) {
			//System.out.println("YAAYYYAYYAYAY");
			correct = true;
			return correct;
		}
		tries.add(guess);
		for (int i=0; i<5; i++) {
			String c = "" + guess.charAt(i);
			//check if is answer
			int ind = Game.answer.indexOf(c); //this will break for more than 1 letter
			if (ind == -1) { //this might be bugged since its checking for a string
				//int[] cleared = {0,0,0,0,0};
				//this.freq.replace(c, cleared);
				//replaces with blank array since that char is not in the answer
				this.gray.add(c);
			} else if (Game.answer.charAt(i) == guess.charAt(i)) {
				this.green[i] = c;
			} else {
				this.yellow[i] += c;
				//this.freq.get(c)[i] = 0; //sets yellow index to 0
			}		  
		}
		//System.out.println("green: " + Arrays.deepToString(this.green));
		//System.out.println("yellow: " + Arrays.deepToString(this.yellow));
		//System.out.println("gray: " + this.gray);
		calcSearchSpace();
		return false;
	}

	public void calcSearchSpace() {
		//make sure all types are same and work well together
		//for (String w: this.remGuess) {
		List<String> temp = new ArrayList<String>();  
		this.remGuess.forEach((w) -> {
			boolean valid = true;
			for (int i=0; i<5; i++) {
				String c = "" + w.charAt(i);
				if (this.green[i] != null) {
					if (!this.green[i].equals(c)) { //might have a cast issue here
						//this.remGuess.remove(w);
						if (w.equals(Game.answer)) System.out.println("gree");
						valid = false;
						break; //hope exits for loop
					}
				}
			}
			for (int i=0; i<5; i++) {
				String c = "" + w.charAt(i);
				if (this.gray.contains(c) || (this.yellow[i] != null && this.yellow[i].contains(c))) { //double check yellow 
					//this.remGuess.remove(w);
					if (w.equals(Game.answer)) System.out.println("ISSUE with Gray/yellow: char  = " + c);
					valid = false;
					break; //hope this just exits inner for
				}
			}
			if (valid) {
				temp.add(w);
			} else {
				this.searchSpace--;
			}
		});
		remGuess = temp;
		/*
		for (int i=0; i<searchSpace; i++) {
			System.out.print(remGuess.get(i) + " ");
		}
		System.out.print("\n");
		*/
		//return this.searchSpace;
	}
}
