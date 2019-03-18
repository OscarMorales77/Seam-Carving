
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
	private Picture picture;
	private double[][] energy;
	private int[][] colorPix;

	public SeamCarver(Picture picture) {
		if (picture == null) {
			throw new java.lang.IllegalArgumentException();
		}
		this.picture = new Picture(picture);
		computeEnergy();

	}

	/*
	 * public void showEnergy() { for (double[] row : energy) {
	 * System.out.println(Arrays.toString(row)); }
	 * 
	 * }
	 * 
	 * public void showRgb() { for (int[] row : colorPix) {
	 * System.out.println(Arrays.toString(row)); } }
	 */
	public Picture picture() {
		
		Picture val= new Picture(picture);
		return val;
	}

	public int width() {

		return picture.width();
	}

	public int height() {

		return picture.height();
	}

	public double energy(int x, int y) {

		if (x < 0 || y < 0 || x >= width() || y >= height()) {

			throw new IllegalArgumentException();
		}

		return energy[y][x];
	}

	public void removeHorizontalSeam(int[] seam) {
		if (seam == null || seam.length != width()||checkValidSeam(seam)||height()<=1) {
			throw new IllegalArgumentException();
		}
		
		for (int i = 0; i < seam.length; i++) {
			if (seam[i]<0|| seam[i]>=height()) {
				throw new IllegalArgumentException();
			}
		}

		
		colorPix = trasposeMatrix(colorPix);
		energy = trasposeMatrix(energy);

		removeHelp(seam);

	}

	private void removeHelp(int[] seam) {

		int[][] temp = new int[colorPix.length][colorPix[0].length - 1];

		for (int i = 0; i < temp.length; i++) {

			for (int j = 0; j < temp[0].length; j++) {

				if (seam[i] == j) {

					for (int j2 = j; j2 < temp[0].length; j2++) {
						temp[i][j2] = colorPix[i][j2 + 1];
					}

					j = temp[0].length;
				} else {
					temp[i][j] = colorPix[i][j];
				}

			}

		}

		temp = trasposeMatrix(temp);

		Picture other = new Picture(temp[0].length, temp.length);

		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {

				other.setRGB(j, i, temp[i][j]);
			}

		}

		picture = other;
		computeEnergy();

	}

	public void removeVerticalSeam(int[] seam) {
		
		for (int i = 0; i < seam.length; i++) {
			if (seam[i]<0|| seam[i]>=width()) {
				throw new IllegalArgumentException();
			}
		}

		if (seam == null || seam.length != height()||checkValidSeam(seam)||width()<=1) {
			throw new IllegalArgumentException();
		}

		int[][] temp = new int[colorPix.length][colorPix[0].length - 1];

		for (int i = 0; i < temp.length; i++) {

			for (int j = 0; j < temp[0].length; j++) {

				if (seam[i] == j) {

					for (int j2 = j; j2 < temp[0].length; j2++) {
						temp[i][j2] = colorPix[i][j2 + 1];
					}

					j = temp[0].length;
				} else {
					temp[i][j] = colorPix[i][j];
				}

			}

		}

		Picture other = new Picture(temp[0].length, temp.length);

		for (int i = 0; i < temp.length; i++) {
			for (int j = 0; j < temp[0].length; j++) {

				other.setRGB(j, i, temp[i][j]);
			}

		}

		picture = other;

		computeEnergy();

	}

	public int[] findHorizontalSeam() {

		energy = trasposeMatrix(energy);

		int[] rever = findVerticalSeam();
		energy = trasposeMatrix(energy);
		
		return rever;
	}

	public int[] findVerticalSeam() {
		int row = energy.length;
		int col = energy[0].length;

		double[] distTo = new double[row * col + 2];
		int[] prev = new int[row * col + 2];

		for (int i = 0; i < distTo.length; i++) {
			distTo[i] = Double.POSITIVE_INFINITY;
		}

		dijkstra(row * col, row * col + 1, distTo, prev);

		return getArray(prev, row * col + 1, row * col);
	}

	private void computeEnergy() {
		energy = new double[picture.height()][picture.width()];
		colorPix = new int[picture.height()][picture.width()];
		for (int i = 0; i < energy.length; i++) {

			for (int j = 0; j < energy[0].length; j++) {

				if (i == 0 || i == energy.length - 1 || j == 0 || j == energy[0].length - 1) {

					colorPix[i][j] = picture.getRGB(j, i);

					energy[i][j] = 1000;
				} else {

					// System.out.println("i and j is " + i + " " + j);
					// x: (i,j)-->(i-1,j) and (i+1,j)
					// y: (i,j)--->(i,j-1) and (i,j+1) flip them on pic

					// for x:
					int rgbXL = picture.getRGB(j, i - 1);
					colorPix[i - 1][j] = rgbXL;

					int rxL = (rgbXL >> 16) & 0xFF;
					int gxL = (rgbXL >> 8) & 0xFF;
					int bxL = (rgbXL >> 0) & 0xFF;

					int rgbXR = picture.getRGB(j, i + 1);
					colorPix[i + 1][j] = rgbXR;

					int rxR = (rgbXR >> 16) & 0xFF;
					int gxR = (rgbXR >> 8) & 0xFF;
					int bxR = (rgbXR >> 0) & 0xFF;

					double horizontal = Math.pow(rxL - rxR, 2) + Math.pow(gxL - gxR, 2) + Math.pow(bxL - bxR, 2);
					// System.out.println("horizontal: "+horizontal);

					// for y:
					int rgbYT = picture.getRGB(j - 1, i);
					colorPix[i][j - 1] = rgbYT;
					int rxT = (rgbYT >> 16) & 0xFF;
					int gxT = (rgbYT >> 8) & 0xFF;
					int bxT = (rgbYT >> 0) & 0xFF;

					int rgbYB = picture.getRGB(j + 1, i);
					colorPix[i][j + 1] = rgbYB;
					int rxB = (rgbYB >> 16) & 0xFF;
					int gxB = (rgbYB >> 8) & 0xFF;
					int bxB = (rgbYB >> 0) & 0xFF;

					double vertical = Math.pow(rxT - rxB, 2) + Math.pow(gxT - gxB, 2) + Math.pow(bxT - bxB, 2);
					// System.out.println("vertical: "+vertical);
					double gradient = Math.sqrt(horizontal + vertical);
					// double gradient= (horizontal+vertical);
					energy[i][j] = gradient;

				}

			}
		}

	}

	//////////////// --->Algorithms<------//////////////////////////////////////////////////////////

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

			int c = n % col;
			int r = (n - c) / col;

			if (r == row - 1) {

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

	private void dijkstra(int start, int end, double[] distTo, int[] edgeTo) {

		IndexMinPQ<Double> pq = new IndexMinPQ<>(energy.length * energy[0].length + 2);

		distTo[start] = 0.0;
		pq.insert(start, distTo[start]);

		while (!pq.isEmpty()) {
			int current = pq.delMin();
			if (current == end) {

				break;
			}

			for (Integer neighbor : getNeighbors(current)) {

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

		}

	}

	private int[] getArray(int[] path, int end, int s) {
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

	private int[][] trasposeMatrix(int[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;

		int[][] trasposedMatrix = new int[n][m];

		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				trasposedMatrix[x][y] = matrix[y][x];
			}
		}

		return trasposedMatrix;
	}

	private double[][] trasposeMatrix(double[][] matrix) {
		int m = matrix.length;
		int n = matrix[0].length;

		double[][] trasposedMatrix = new double[n][m];

		for (int x = 0; x < n; x++) {
			for (int y = 0; y < m; y++) {
				trasposedMatrix[x][y] = matrix[y][x];
			}
		}

		return trasposedMatrix;
	}
	
	private boolean checkValidSeam(int[] seam)
	{
		for (int i = 0; i < seam.length-1; i++) {
			if (Math.abs(seam[i+1] - seam[i]) > 1) {
				return true;
			}
		}
		return false;
	}
	
	

}
