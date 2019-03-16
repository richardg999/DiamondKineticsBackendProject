import java.util.ArrayList;
import java.util.Arrays;

public class SwingData_Improved {
	
	static class Tuple<X, Y> { 
		  public final X x; 
		  public final Y y; 
		  public Tuple(X x, Y y) { 
		    this.x = x; 
		    this.y = y; 
		  } 
		  public String toString() {
			  return "("+ x + "," + y + ")";
		  }
		} 
	
	public static int searchContiniuityAboveValue(double[] data, int indexBegin, int indexEnd, 
			double threshold, int winLength) {
		if (indexBegin < 0 || indexEnd >= data.length || indexBegin > indexEnd || winLength <= 0) 
			throw new IllegalArgumentException();
		for (int i = indexBegin; i <= indexEnd - winLength + 1; i++) {
			for (int j = 0; j <= winLength; j++) {
				// success condition
				if (j == winLength) {
					return i;
				}
				// failure condition
				if (data[i+j] <= threshold) {
					i += j;
					break;
				}
			}
		}
		return -1;
	}
	
	public static int backSearchContinuityWithinRange(double[] data, int indexBegin, int indexEnd,
			double thresholdLo, double thresholdHi, int winLength) {
		if (indexBegin < 0 || indexEnd >= data.length || indexBegin < indexEnd || winLength <= 0) 
			throw new IllegalArgumentException();
		for (int i = indexBegin; i >= indexEnd + winLength - 1; i--) {
			for (int j = 0; j <= winLength; j++) {
				// success condition
				if (j == winLength) {
					return i;
				}
				// failure condition
				if (data[i-j] >= thresholdHi || data[i-j] <= thresholdLo) {
					i -= j;
					break;
				}
			}
		}
		return -1;
	}
	
	public static int searchContinuityAboveValueTwoSignals(double[] data1, double[] data2, int indexBegin,
			int indexEnd, double threshold1, double threshold2, int winLength) {
		if (indexBegin < 0 || indexEnd >= data1.length || indexBegin > indexEnd || winLength <= 0
				|| data1.length != data2.length) 
			throw new IllegalArgumentException();
		for (int i = indexBegin; i <= indexEnd - winLength + 1; i++) {
			for (int j = 0; j <= winLength; j++) {
				// success condition
				if (j == winLength) {
					return i;
				}
				// failure condition
				if (data1[i+j] <= threshold1 || data2[i+j] <= threshold2) {
					i += j;
					break;
				}
			}
		}
		return -1;
	}
	
	public static ArrayList<Tuple<Integer, Integer>> searchMultiContinuityWithinRange(double[] data, int indexBegin,
			int indexEnd, double thresholdLo, double thresholdHi, int winLength) {
		if (indexBegin < 0 || indexEnd >= data.length || indexBegin > indexEnd || winLength <= 0) 
			throw new IllegalArgumentException();
		ArrayList<Tuple<Integer, Integer>> list = new ArrayList<>();
		for (int i = indexBegin; i <= indexEnd - winLength + 1; i++) {
			for (int j = 0; j <= winLength; j++) {
				// success condition
				if (j == winLength) {
					while (i + j <= indexEnd + 1) {
						// reached past the end
						if (i + j > indexEnd) {
							list.add(new Tuple<Integer, Integer>(i, i+j-1));
							i += j;
							break;
						}
						// reached secondary failure condition
						if (data[i+j] <= thresholdLo || data[i+j] >= thresholdHi) {
							list.add(new Tuple<Integer, Integer>(i, i+j-1));
							i += j;
							break;
						}
						j++;
					}
				}
				// failure condition
				else if (data[i+j] <= thresholdLo || data[i+j] >= thresholdHi) {
					i += j;
					break;
				}
			}
		}
		return list;
	}
	
	
	// testing code
	public static void main(String[] args) {
		double[] sample = {2, 1.5, 3, 4, 1};
		double[] sample2 = {1, 4, 9, 10, 14, 15, 6, 19};
		double[] sample3 = {1, 4, 9, 10, 14};
		System.out.println(searchContiniuityAboveValue(sample, 0, 4, 2.5, 2)); //2
		System.out.println(searchContiniuityAboveValue(sample2, 0, 7, 9.5, 4)); //-1
		System.out.println(searchContiniuityAboveValue(sample2, 3, 7, 9.5, 3)); //3
		System.out.println(searchContiniuityAboveValue(sample2, 4, 7, 9.5, 3)); //-1
		System.out.println(backSearchContinuityWithinRange(sample, 4, 0, 2.5, 4.5, 2)); //3
		System.out.println(searchContinuityAboveValueTwoSignals(sample, sample3, 0, 4, 2.5, 5.5, 2)); //2
		System.out.println(searchContinuityAboveValueTwoSignals(sample, sample3, 0, 4, 2.5, 10.5, 2)); //-1
		System.out.println(searchMultiContinuityWithinRange(sample, 0, 4, .5, 10, 1)); //[(0,4)]
		System.out.println(searchMultiContinuityWithinRange(sample2, 2, 6, 8.5, 16, 3)); //[(2,5)]
		System.out.println(searchMultiContinuityWithinRange(sample2, 0, 7, 8.5, 20, 1)); //[(2,5), (7,7)]
		System.out.println(searchMultiContinuityWithinRange(sample2, 0, 7, 5, 11, 2)); //[(2,3)]
	}

}
