import java.io.File;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Sample code showing how to load, manipulate and save a {@link BufferedImage}.
 */
public class SampleCode {
  private static final String INPUT_PATH = "src/main/resources/abby.png";
  private static final String OUTPUT_PATH = "tmp/abby-darkened.png";
  private static final String IMAGE_FORMAT = "png";

  public static void main(String[] args) {
    // Read in an image.
    File originalFile = new File(INPUT_PATH);
    BufferedImage oldImage;
    try {
      oldImage = ImageIO.read(originalFile);
    } catch (IOException e) {
      System.out.println("Unable to open " + originalFile);
      // If we can't read the file, the program can't continue.
      return;
    }

    // Create an empty image to hold the modified version.
    BufferedImage newImage = new BufferedImage(
            oldImage.getWidth(), oldImage.getHeight(), BufferedImage.TYPE_INT_RGB);

    // Iterate over the old image, point by point, setting values in the new image.
    for (int y = 0; y < oldImage.getHeight(); y++) {
      for (int x = 0; x < oldImage.getWidth(); x++) {
        // Get the int pixel value and Color from the current point.
        int pixel = oldImage.getRGB(x, y);
        Color originalColor = new Color(pixel, true);

        // Extract the red, green, and blue components.
        int red = originalColor.getRed();
        int green = originalColor.getGreen();
        int blue = originalColor.getBlue();

        // Make a color half as bright for the new image.
        Color newColor = new Color(red / 2, green / 2, blue / 2);
        newImage.setRGB(x, y, newColor.getRGB());
      }
    }

    // Save the new image.
    try {
      File newFile = new File(OUTPUT_PATH);
      ImageIO.write(newImage, IMAGE_FORMAT, newFile);
      System.out.println("Altered image stored to " + OUTPUT_PATH);
    } catch (IOException e) {
      System.out.println("Unable to save file.");
    }
  }
}
