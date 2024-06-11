import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.awt.image.BufferedImage;


import static org.junit.jupiter.api.Assertions.*;

public class AbstractGridTest {
  private static final long SEED = 1234L;

  public AbstractGrid makeGrid(BufferedImage bufferedImage) {
    return new Grid(bufferedImage, SEED, true);
  }

  BufferedImage imageTest1;

  long seed = 0;
  BufferedImage oneColumnImage;
  AbstractGrid oneColumnGrid;

  @BeforeEach
  public void setUp() {
    oneColumnImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    oneColumnGrid = new Grid(oneColumnImage, seed, true);
    imageTest1 = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
    imageTest1.setRGB(0, 0, new Color(0, 0, 255).getRGB());
  }

  private BufferedImage createBufferedImage(int width, int height, int[][] blueLevels) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        int blue = blueLevels[y][x];
        Color color = new Color(0, 0, blue);
        image.setRGB(x, y, color.getRGB());
      }
    }
    return image;
  }

  @Test
  public void convertToBufferedImageTest() {

    assertBufferedImagesEqual(oneColumnImage, oneColumnGrid.convertToBufferedImage());
  }

  private void assertBufferedImagesEqual(BufferedImage expected, BufferedImage actual) {
    assertEquals(expected.getWidth(), actual.getWidth());
    assertEquals(expected.getHeight(), actual.getHeight());

    for (int y = 0; y < expected.getHeight(); y++) {
      for (int x = 0; x < expected.getWidth(); x++) {
        assertEquals(expected.getRGB(x, y), actual.getRGB(x, y));
      }
    }
  }

  @Test
  void getBluestColumnIndexTest() throws RequestFailedException {
    int[][] blueLevels = {{100, 50, 75}, {25, 90, 120}, {80, 110, 60}};
    BufferedImage testImage = createBufferedImage(3, 3, blueLevels);

    AbstractGrid testGrid = new Grid(testImage, 0);

    assertEquals(2, testGrid.getBluestColumnIndex());
  }

  @Test
  public void removeBluestColumnTest() throws RequestFailedException {

    int[][] blueLevels = {{100, 50, 75}, {25, 90, 120}, {80, 110, 60}};
    BufferedImage testImage = createBufferedImage(3, 3, blueLevels);

    AbstractGrid testGrid = new Grid(testImage, 0, true);

    assertEquals(2, testGrid.getBluestColumnIndex());
    testGrid.removeBluestColumn();

    assertEquals(1, testGrid.getBluestColumnIndex());

    BufferedImage expectedImageAfterRemoval = createBufferedImage(2, 3, new int[][]{{50, 75}, {90, 120}, {110, 60}});
    System.out.println("expected width: " + expectedImageAfterRemoval.getWidth());
    System.out.println("actual width: " + testGrid.convertToBufferedImage().getWidth());

    testGrid.removeBluestColumn();
    assertThrows(RequestFailedException.class, () -> testGrid.removeBluestColumn());
  }

}
