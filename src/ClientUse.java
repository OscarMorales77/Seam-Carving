import edu.princeton.cs.algs4.Picture;

public class ClientUse {

	public static void main(String[] args) {
		String picSelection="test.png";
		Picture inputImg = new Picture(picSelection);

		inputImg.show();

		SeamCarver currentInstance = new SeamCarver(inputImg);

		StdOut.printf("original image size is %d columns by %d rows\n", currentInstance.width(), currentInstance.height());
		Stopwatch sw = new Stopwatch();
		int numOfSeam=150;
		for (int i = 0; i < numOfSeam; i++) {
			int[] verticalSeam = currentInstance.findVerticalSeam();
			currentInstance.removeVerticalSeam(verticalSeam);
		}
		Picture outputImg = currentInstance.picture();

		StdOut.printf("new image size is %d columns by %d rows\n", currentInstance.width(), currentInstance.height());

		StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");

		outputImg.show();
	}
}
