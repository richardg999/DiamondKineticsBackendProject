// Bonus answer: Impact happens at around the 1,100,000 timestamp

import java.util.Arrays;

public class SwingData_Old {
	
	private int dataLength;
	private double[] axMinTree;
	private double[] axMaxTree;
	private double[] ayMinTree;
	private double[] ayMaxTree;
	private double[] azMinTree;
	private double[] azMaxTree;
	private double[] wxMinTree;
	private double[] wxMaxTree;
	private double[] wyMinTree;
	private double[] wyMaxTree;
	private double[] wzMinTree;
	private double[] wzMaxTree;
	
	public SwingData_Old(double[] ax, double[] ay, double[] az, double[] wx, double[] wy, double[] wz) {
		if (ax == null || ay == null || az == null || wx == null || wy == null || wz == null)
			throw new IllegalArgumentException();
		dataLength = ax.length;
		if (dataLength != ay.length || dataLength != az.length || dataLength != wx.length || 
			dataLength != wy.length || dataLength != wz.length) throw new IllegalArgumentException();
		axMinTree = buildMinTree(ax);
		axMaxTree = buildMaxTree(ax);
		ayMinTree = buildMinTree(ay);
		ayMaxTree = buildMaxTree(ay);
		azMinTree = buildMinTree(az);
		azMaxTree = buildMaxTree(az);
		wxMinTree = buildMinTree(wx);
		wxMaxTree = buildMaxTree(wx);
		wyMinTree = buildMinTree(wy);
		wyMaxTree = buildMaxTree(wy);
		wzMinTree = buildMinTree(wz);
		wzMaxTree = buildMaxTree(wz);
		System.out.println(Arrays.toString(axMinTree));
		System.out.println(findMinOfInterval(axMinTree, 1, 3));
		System.out.println(findMinOfInterval(axMinTree, 0, 5));
		System.out.println(findMaxOfInterval(axMaxTree, 1, 3));
	}
	
	// Note: data is a string and must be equal to one of {ax, ay, az, wx, wy, wz}
	public int searchContinuityAboveValue(String data, int indexBegin, int indexEnd, double threshold,
			int winLength) {
		if (indexBegin < 0 || indexEnd >= dataLength || indexBegin > indexEnd || winLength <= 0) 
			throw new IllegalArgumentException();
		double[] minTree = findMinTreeByName(data);
		for (int i = indexBegin; i <= indexEnd - winLength + 1; i++) {
			if (findMinOfInterval(minTree, i, i + winLength - 1) > threshold)
				return i;
		}
		return -1;
	}
	
	public int backSearchContinuityWithinRange(String data, int indexBegin, int indexEnd, 
			double thresholdLo, double thresholdHi, int winLength) {
		if (indexBegin < 0 || indexEnd >= dataLength || indexBegin < indexEnd || winLength <= 0) 
			throw new IllegalArgumentException();
		double[] minTree = findMinTreeByName(data);
		double[] maxTree = findMaxTreeByName(data);
		for (int i = indexBegin; i >= indexEnd + winLength - 1; i--) {
			if (findMinOfInterval(minTree, i - winLength + 1, i) > thresholdLo 
					&& findMaxOfInterval(maxTree, i - winLength + 1, i) < thresholdHi)
				return i;
		}
		return -1;
	}
	
	public int searchContinuityAboveValueTwoSignals(String data1, String data2, int indexBegin,
			int indexEnd, double threshold1, double threshold2, int winLength) {
		if (indexBegin < 0 || indexEnd >= dataLength || indexBegin > indexEnd || winLength <= 0) 
			throw new IllegalArgumentException();
		double[] minTree1 = findMinTreeByName(data1);
		double[] minTree2 = findMinTreeByName(data2);
		for (int i = indexBegin; i <= indexEnd - winLength + 1; i++) {
			if (findMinOfInterval(minTree1, i, i + winLength - 1) > threshold1
					&& findMinOfInterval(minTree2, i, i + winLength - 1) > threshold2)
				return i;
		}
		return -1;
	}
	
	
	
	
	
	
	// ------------- Implementation Details --------------
	
	private double[] findMinTreeByName(String name) {
		if (name == null) throw new IllegalArgumentException();
		if (name.equals("ax")) return axMinTree;
		if (name.equals("ay")) return ayMinTree;
		if (name.equals("az")) return azMinTree;
		if (name.equals("wx")) return wxMinTree;
		if (name.equals("wy")) return wyMinTree;
		if (name.equals("wz")) return wzMinTree;
		throw new IllegalArgumentException();
	}
	
	private double[] findMaxTreeByName(String name) {
		if (name == null) throw new IllegalArgumentException();
		if (name.equals("ax")) return axMaxTree;
		if (name.equals("ay")) return ayMaxTree;
		if (name.equals("az")) return azMaxTree;
		if (name.equals("wx")) return wxMaxTree;
		if (name.equals("wy")) return wyMaxTree;
		if (name.equals("wz")) return wzMaxTree;
		throw new IllegalArgumentException();
	}
	
	
	// --------------   Implementation of Min Segment Tree -------------------------
	
	private void buildMinTreeHelper(double[] tree, double[] data, int node, int start, int end) {
		if (start == end) {
			tree[node] = data[start];
		}
		else {
			int mid = (start + end) / 2;
			buildMinTreeHelper(tree, data, 2*node, start, mid);
			buildMinTreeHelper(tree, data, 2*node + 1, mid + 1, end);
			tree[node] = Double.min(tree[2*node], tree[2*node + 1]);
		}
	}
	
	private double[] buildMinTree(double[] data) {
		double[] tree = new double[4*data.length];
		buildMinTreeHelper(tree, data, 1, 0, data.length - 1);
		return tree;
	}
	
	private double findMinOfIntervalHelper(double[] tree, int node, int start, int end, int l, int r) {
		if (r < start || end < l) {
			return Double.MAX_VALUE;
		}
		else if (l <= start && end <= r) {
			return tree[node];
		}
		int mid = (start + end) / 2;
		double v1 = findMinOfIntervalHelper(tree, 2*node, start, mid, l, r);
		double v2 = findMinOfIntervalHelper(tree, 2*node + 1, mid+1, end, l, r);
		return Double.min(v1, v2);
	}
	
	private double findMinOfInterval(double[] tree, int l, int r) {
		if (l < 0 || r >= dataLength || l > r) {
			return Double.MAX_VALUE;
		}
		return findMinOfIntervalHelper(tree, 1, 0, dataLength - 1, l, r);
	}
	
	
	// --------------   Implementation of Max Segment Tree -------------------------
	
	private void buildMaxTreeHelper(double[] tree, double[] data, int node, int start, int end) {
		if (start == end) {
			tree[node] = data[start];
		}
		else {
			int mid = (start + end) / 2;
			buildMaxTreeHelper(tree, data, 2*node, start, mid);
			buildMaxTreeHelper(tree, data, 2*node + 1, mid + 1, end);
			tree[node] = Double.max(tree[2*node], tree[2*node + 1]);
		}
	}
	
	private double[] buildMaxTree(double[] data) {
		double[] tree = new double[4*data.length];
		buildMaxTreeHelper(tree, data, 1, 0, data.length - 1);
		return tree;
	}
	
	private double findMaxOfIntervalHelper(double[] tree, int node, int start, int end, int l, int r) {
		if (r < start || end < l) {
			return -Double.MAX_VALUE;
		}
		else if (l <= start && end <= r) {
			return tree[node];
		}
		int mid = (start + end) / 2;
		double v1 = findMaxOfIntervalHelper(tree, 2*node, start, mid, l, r);
		double v2 = findMaxOfIntervalHelper(tree, 2*node + 1, mid+1, end, l, r);
		return Double.max(v1, v2);
	}
	
	private double findMaxOfInterval(double[] tree, int l, int r) {
		if (l < 0 || r >= dataLength || l > r) {
			return -Double.MAX_VALUE;
		}
		return findMaxOfIntervalHelper(tree, 1, 0, dataLength - 1, l, r);
	}
	
}
