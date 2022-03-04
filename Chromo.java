import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.Map;



//questions for vera
/*
 clone method on a treemap
 what data structure would be best to put the remaining valid words sin
 maybe have two varaible for length. total and running 
 */

public class Chromo  implements Comparable<Chromo>{
	public int chromoID;
	public double fitness;

	public String word;
	private List<String> remain;
	private int remaining = GA.guessesLen;
	//private int[] strat;
	private TreeMap<Integer,Integer> strat; //remake this as a treemap
	//public searchSpace;
	//two above work together to create policy
	public static int length=10;
	private static int strats=4; //including 0
	private int min = 3;
	private int max = 10;

	private static double mutationAmount = 0.5;
	private static int totalChromos=0;
	private static int trainingSessions = 50;

	//public static listLen = 261; //how to get this from game or main?

	public Chromo()  {
		this.fitness=0;
		this.chromoID = Chromo.totalChromos;
		Chromo.totalChromos++;
		remain = Arrays.asList(GA.guesses); //dont think this should be here
		Random rand = new Random();
		this.word = GA.guesses[rand.nextInt(GA.guessesLen)];
		//this.word = guesses[rand.nextInt(GA.guessesLen)];

		//System.out.println(this.word);
		if (this.strat==null)
			setupStrat();
	}

	public void setupStrat() {
		Random rand = new Random();
		//this.word = GA.guesses[rand.nextInt(GA.guessesLen)];
		int listlen = rand.nextInt(max - min) + min;
		this.strat = new TreeMap<Integer,Integer>();
		for (int i=0; i<listlen; i++) {
			int ss = rand.nextInt(GA.guessesLen);
			this.strat.put(ss, rand.nextInt(strats));
		}
	}

	public Chromo (TreeMap<Integer,Integer> strat) {
		this.fitness = 0;
		Random rand = new Random();
		//this.strat = strat.clone();
		if (this.strat==null)
			setupStrat();
		if (this.word==null)
			this.word = GA.guesses[rand.nextInt(GA.guessesLen)]; //this is probably wrong and needs to be passed the word as a param?
		for (Map.Entry<Integer,Integer> entry: strat.entrySet()) {
			this.strat.put(entry.getKey(),entry.getValue());
		}
		//System.out.println(Arrays.toString(this.policy));
		//this.fitness=-1;
		this.chromoID = Chromo.totalChromos;
		Chromo.totalChromos++;
	}

	public void mutate() {
		Random rand = new Random();
		int which = rand.nextInt(3);
		if (which == 0) {
			//mutate start word
			int newWord = rand.nextInt(GA.guessesLen);
			this.word = GA.guesses[newWord];
		} else if (which == 1) {
			//mutate strat
			which = rand.nextInt(strat.lastKey()+1);
			strat.replace(strat.ceilingKey(which), rand.nextInt(strats));
			//gets a random key and gives it a new strat
		} else {
			//mutate searchspace val
			//TODO change to be gaussian
			int stdev = rand.nextInt((GA.guessesLen)/10);
			int oldkey = strat.ceilingKey(rand.nextInt(GA.guessesLen)); //this might break. if so, go back to being in terms of last key+1
			int newkey = (int) Math.round(rand.nextGaussian() * stdev + oldkey);
			int oldval = strat.remove(oldkey);
			strat.put(newkey, oldval);
			//gets new searchspace val and old value by removing old chromo and adding new one to the map
		}
	}
	
	public static void crossover(Chromo c1, Chromo c2) {
		
		//implement crossover here
		return;
	}
	
	public TreeMap<Integer,Integer> getStrat() {
		return this.strat;
	}
	
	public double getFitness() {
		return this.fitness;
	}
	
	public void setFitness(double newFit) {
		this.fitness = newFit;
	}

	@Override
	public int compareTo(Chromo other) {
		if (this.fitness>other.fitness)
			return -1;
		else if (this.fitness<other.fitness)
			return 1;
		else return 0;
	}
}
