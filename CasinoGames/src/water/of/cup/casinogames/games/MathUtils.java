package water.of.cup.casinogames.games;

public class MathUtils {
	public static long factorial(int n) {
		if (n <= 2) {
	        return n;
	    }
	    return n * factorial(n - 1);
	}
}
