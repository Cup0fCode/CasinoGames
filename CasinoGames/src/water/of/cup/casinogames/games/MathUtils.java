package water.of.cup.casinogames.games;

public class MathUtils {
	public static long factorial(int n) {
		if (n <= 2) {
	        return n;
	    }
	    return n * factorial(n - 1);
	}

	public static long combination(int x, int d) {
		if(x == d) return 1;
		return factorial(x) / (factorial(d) * factorial(x - d));
	}
}
