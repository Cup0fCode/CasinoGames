package water.of.cup.casinogames.games.gameutils;

public class MathUtils {
	public static long factorial(int n) {
		if (n <= 2) {
	        return n;
	    }
	    return n * factorial(n - 1);
	}

	public static long binomial(int N, int K) {
		long ret = 1;
		for(int k = 0; k < K; k++) {
			ret = ret * (N-k);
			ret = ret / (k + 1);
		}
		return ret;
	}
}
