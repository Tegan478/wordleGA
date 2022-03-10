import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
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
	private int wordInd;
	public String word;
	private List<String> remain;
	private int remaining = GA.guessesLen;
	//private int[] strat;
	TreeMap<Integer,Integer> strat; //remake this as a treemap
	//public searchSpace;
	//two above work together to create policy
	public static int length=10;
	private static int strats=2; //including 0
	private int min = 6;
	private int max = 6;

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
		wordInd = rand.nextInt(GA.guessesLen);
		this.word = GA.guesses[wordInd];
		//this.word = guesses[rand.nextInt(GA.guessesLen)];

		//System.out.println(this.word);
		if (this.strat==null)
			setupStrat();
	}

	public void setupStrat() {
		Random rand = new Random();
		//this.word = GA.guesses[rand.nextInt(GA.guessesLen)];
		int listlen = max; // - min) + min;
		this.strat = new TreeMap<Integer,Integer>();
		for (int i=0; i<listlen-1; i++) {
			int ss = rand.nextInt(GA.guessesLen);
			while (this.strat.containsKey(ss)) {
				ss = rand.nextInt(GA.guessesLen);
			}
			this.strat.put(ss, rand.nextInt(strats));
		}
		this.strat.put(GA.guessesLen, rand.nextInt(strats));
	}

	public Chromo (TreeMap<Integer,Integer> strat) {
		this.fitness = 0;
		Random rand = new Random();
		//this.strat = strat.clone();
		if (this.strat==null)
			setupStrat();
		if (this.word==null)
			this.word = GA.guesses[rand.nextInt(GA.guessesLen)]; //this is probably wrong and needs to be passed the word as a param?
		/*
		for (Map.Entry<Integer,Integer> entry: strat.entrySet()) {
			this.strat.put(entry.getKey(),entry.getValue());
		}
		*/
		//System.out.println(Arrays.toString(this.policy));
		//this.fitness=-1;
		this.chromoID = Chromo.totalChromos;
		Chromo.totalChromos++;
	}

	public void mutate() {
		Random rand = new Random();
		int which = rand.nextInt(4);
		int stdev = rand.nextInt((GA.guessesLen)/10);
		if (which == 0) {
			//mutate start word
			int newWord = (int) Math.round(rand.nextGaussian() * stdev + wordInd);
			if (newWord < 0 || newWord >= GA.guessesLen) newWord = rand.nextInt(GA.guessesLen);
			this.word = GA.guesses[newWord];
		} else if (which == 1) {
			//mutate strat
			which = rand.nextInt(strat.lastKey()+1);
			strat.replace(strat.ceilingKey(which), rand.nextInt(strats));
			//gets a random key and gives it a new strat
		} else {
			//mutate searchspace val
			//TODO change to be gaussian
			
			int oldkey = strat.ceilingKey(rand.nextInt(strat.lastKey()+1)); //this might break. if so, go back to being in terms of last key+1
			int newkey = (int) Math.round(rand.nextGaussian() * stdev + oldkey);
			int oldval = strat.remove(oldkey);
			strat.put(newkey, oldval);
			//gets new searchspace val and old value by removing old chromo and adding new one to the map
		}
	}
	
	public void mutateE() {
		Random rand = new Random();
		int which = rand.nextInt(7);
		int stdev = rand.nextInt((GA.guessesLen)/5);
		if (which == 0) {
			//mutate start word
			int newWord = (int) Math.round(rand.nextGaussian() * stdev + wordInd);
			if (newWord < 0 || newWord >= GA.guessesLen) newWord = rand.nextInt(GA.guessesLen);
			this.word = GA.guesses[newWord];
		} else if (which == 1 || which == 2) {
			//mutate strat
			which = rand.nextInt(strat.lastKey()+1);
			strat.replace(strat.ceilingKey(which), rand.nextInt(strats));
			//gets a random key and gives it a new strat
		} else {
			//mutate searchspace val
			//TODO change to be gaussian
			
			int oldkey = strat.ceilingKey(rand.nextInt(strat.lastKey()+1)); //this might break. if so, go back to being in terms of last key+1
			int newkey = (int) Math.round(rand.nextGaussian() * stdev + oldkey);
			int oldval = strat.remove(oldkey);
			strat.put(newkey, oldval);
			//gets new searchspace val and old value by removing old chromo and adding new one to the map
		}
	}
	
	public static void crossover(Chromo c1, Chromo c2) {
		Random rand = new Random();
		int which = rand.nextInt(3);
		if (which == 0) {
			//crossover for word
			List<String> possible = new LinkedList<String>();
			String comb = "";
			boolean clean = true;
			comb = c1.word + c2.word;
			for (int i=0; i<GA.guessesLen; i++) {
				for (int j=0; j<5; j++) {
					if (!comb.contains("" + GA.guesses[i].charAt(j))) {
						clean = false;
						break;
					}
				}
				if (clean) possible.add(GA.guesses[i]);
				clean = true;
			}
			c1.word = possible.get(rand.nextInt(possible.size()));
			c2.word = possible.get(rand.nextInt(possible.size()));
		} else if (which == 1) {
			//point crossover for strat
			int point =  c1.strat.ceilingKey(rand.nextInt(c1.strat.lastKey()));
			TreeMap<Integer,Integer> temp = new TreeMap<Integer,Integer>();
			Set<Integer> keys = c1.strat.keySet();
			//System.out.println(keys.size());
			List<Integer> c1k = new ArrayList<Integer>(keys);
			keys = c2.strat.keySet();
			List<Integer> c2k = new ArrayList<Integer>(keys);
			if (c1k.size() != c2k.size()) return;
			for (int i=0; i<c1k.size(); i++) {
				int ref = c1k.get(i);
				if (ref < point) {
					temp.put(ref, c1.strat.get(ref));
				} else {
					ref = c2k.get(i); //this is still occasionally causes out of bounds bc duplicate keys and set makes a smaller set
					temp.put(ref, c2.strat.get(ref));
				}
			}
			c1.strat.clear();
			c1.strat = temp;
		} else {
			TreeMap<Integer,Integer> temp = new TreeMap<Integer,Integer>();
			Set<Integer> keys = c1.strat.keySet();
			//System.out.println(keys.size());
			List<Integer> c1k = new ArrayList<Integer>(keys);
			keys = c2.strat.keySet();
			List<Integer> c2k = new ArrayList<Integer>(keys);
			//System.out.println(c1k.size());
			//System.out.println(c2k.size());
			if (c1k.size() != c2k.size()) return;
			
			for (int i=0; i<c1k.size(); i++) {
				boolean ref = rand.nextBoolean();
				if (ref) {
					temp.put(c1k.get(i), c1.strat.get(c1k.get(i)));
				} else {
					temp.put(c2k.get(i), c2.strat.get(c2k.get(i)));
				}
			}
		}
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
		if (this.fitness<other.fitness)
			return -1;
		else if (this.fitness>other.fitness)
			return 1;
		else return 0;
	}
}
