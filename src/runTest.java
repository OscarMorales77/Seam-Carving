
import java.util.Arrays;

import edu.princeton.cs.algs4.Picture;

public class runTest {

	public static void mainGG(String[] args) {

		Picture picture = new Picture("test.png");

		SeamCarver test = new SeamCarver(picture);
	
		System.out.println("Height and Width : " +test.height()+" " +test.width());
		int[] seam=test.findHorizontalSeam();
		
		//System.out.println(Arrays.toString(seam));
		//test.removeHorizontalSeam(seam);
		System.out.println("Height and Width : " +test.height()+" " +test.width());
		test.picture().show();
	
	
	}

	public static void main(String[] args) {
	
			
	
		Picture inputImg = new Picture("test.png");
		int removeColumns = 0;
		int v=13;
		inputImg.show();

		SeamCarver sc = new SeamCarver(inputImg);

		StdOut.printf("original image size is %d columns by %d rows\n", sc.width(), sc.height());
		Stopwatch sw = new Stopwatch();

	
		for (int i = 0; i < removeColumns; i++) {
			int[] verticalSeam = sc.findVerticalSeam();
			//sc.removeVerticalSeam(verticalSeam);
		}
	
		
		
		for (int i = 0; i < v; i++) {
            int[] horizontalSeam = sc.findHorizontalSeam();
           // sc.removeHorizontalSeam(horizontalSeam);
        }
		
		for (int i = 0; i < 7; i++) {
			int[] verticalSeam = sc.findVerticalSeam();
			sc.removeVerticalSeam(verticalSeam);
		}
		Picture outputImg = sc.picture();

		StdOut.printf("new image size is %d columns by %d rows\n", sc.width(), sc.height());

		StdOut.println("Resizing time: " + sw.elapsedTime() + " seconds.");
		
		outputImg.show();
	}

}