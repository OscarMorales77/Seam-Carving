
import java.util.Arrays;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Stack;

public class SeamCarverOld {

	// Picture pic;
	private double[][] energy;

	public SeamCarverOld(double[][] en) {
		// pic = picture;
		energy = en;

	}
	
	public void findMin() {
		int row = energy.length;
		int col = energy[0].length;
		// System.out.println("row and col : "+row+" "+col);
		double[] distTo = new double[row * col + 2];
		int[] prev = new int[row * col + 2];

		for (int i = 0; i < distTo.length; i++) {
			distTo[i] = Double.POSITIVE_INFINITY;
		}

		dijkstra(row * col, row * col + 1, distTo, prev);

		// System.out.println(getPath(prev, row * col + 1, row * col));

		System.out.println(Arrays.toString(getArray(prev, row * col + 1, row * col)));
	}
	
	
	
	
	



	private int map1D(int r, int c) {
		int col = energy[0].length;

		return col * r + c;

	}

	private boolean inBounds(int r, int c) {
		if (r < energy.length && c < energy[0].length && r >= 0 && c >= 0) {

			return true;
		} else {
			return false;
		}

	}

	private double edgeWeight(int n) {

		int row = energy.length;
		int col = energy[0].length;
		if (n == row * col || n == row * col + 1) {
			return 0;
		}

		int c = n % col;
		int r = (n - c) / col;

		return energy[r][c];
	}

	private Iterable<Integer> getNeighbors(int n) {

		Bag<Integer> neighbors = new Bag<>();
		int row = energy.length;
		int col = energy[0].length;

		if (n == row * col) {

			for (int i = 0; i < col; i++) {
				neighbors.add(i);
			}
			return neighbors;
		} else {
			// get the 2d representation of current N
			int c = n % col;
			int r = (n - c) / col;

			if (r == row - 1) {

				// System.out.println("called last one");
				neighbors.add(row * col + 1);
				return neighbors;
			}

			if (inBounds(r + 1, c - 1)) {
				neighbors.add(map1D(r + 1, c - 1));

			}
			if (inBounds(r + 1, c)) {
				neighbors.add(map1D(r + 1, c));
			}
			if (inBounds(r + 1, c + 1)) {
				neighbors.add(map1D(r + 1, c + 1));
			}

			return neighbors;
		}

	}



	public void dijkstra(int start, int end, double[] distTo, int[] edgeTo) {

		IndexMinPQ<Double> pq = new IndexMinPQ<>(energy.length * energy[0].length + 2);

		distTo[start] = 0.0;
		pq.insert(start, distTo[start]);

		while (!pq.isEmpty()) {
			int current = pq.delMin();
			if (current == end) {
				// System.out.println("found=====");

				break;
			}

			// System.out.print("current is " + current + " neighbor");
			for (Integer neighbor : getNeighbors(current)) {

				// System.out.print(", " + neighbor);

				// current cost of "current" plus the "cost/edge weight" to get to neighbor from
				// current
				double cost = edgeWeight(neighbor) + distTo[current];
				if (cost < distTo[neighbor]) {
					distTo[neighbor] = cost;
					edgeTo[neighbor] = current;

					if (pq.contains(neighbor)) {
						pq.changeKey(neighbor, distTo[neighbor]);
					} else {
						pq.insert(neighbor, distTo[neighbor]);
					}

				}

			}

			// System.out.println();
		}

	}

	public int[] getArray(int[] path, int end, int s) {
		int row = energy.length;
		int col = energy[0].length;
		int[] values = new int[row];

		for (int x = end; x != s; x = path[x]) {

			if (x != end) {
				values[row - 1] = x % col;
				row--;
			}

		}

		return values;
	}

	public String getPath(int[] path, int end, int s) {

		Stack<Integer> values = new Stack<>();
		String val = "";
		for (int x = end; x != s; x = path[x]) {

			if (x != end) {
				val += x + ",";
			}

		}
		return val;
	}

	
	
	
	
	
	
	
	
	
	
	public static void mainOld(String[] args) {
		// {1000,1000,1000,1000,1000,1000}
		/*
		 * double[][] test = { { 240.18, 225.59, 302.27, 159.43, 181.81, 192.99 }, {
		 * 1000, 237.35, 151.02, 234.09, 107.89, 1000 }, { 1000, 138.69, 228.10, 133.07,
		 * 211.51, 1000 }, { 1000, 153.88, 174.01, 284.01, 194.50, 1000 }, { 179.82,
		 * 175.49, 70.06, 270.80, 201.55, 191.20 } };
		 */
		double[][] test = { { 57685.0, 50893.0, 91370.0, 25418.0, 33055.0, 37246.0 },
				{ 5421.0, 56334.0, 22808.0, 54796.0, 11641.0, 25496.0 },
				{ 12344.0, 19236.0, 52030.0, 17708.0, 44735.0, 20663.0 },
				{ 17074.0, 23678.0, 30279.0, 80663.0, 37831.0, 45595.0 },
				{ 32337.0, 30796.0, 4909.0, 73334.0, 40613.0, 36556.0 } };

		SeamCarverOld my = new SeamCarverOld(test);

		my.findMin();
	}

	public void removeHorizontalSeam(int[] seam) {
	}

	public void removeVerticalSeam(int[] seam) {
	}

}
