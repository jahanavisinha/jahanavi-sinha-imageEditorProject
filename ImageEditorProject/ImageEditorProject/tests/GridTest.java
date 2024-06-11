import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {
  private static final String INPUT_PATH = "src/main/resources/abby.png";

  BufferedImage imageTest1;

  long seed = 0;
  BufferedImage oneColumnImage;
  Grid oneColumnGrid;
  Grid multipleColumnGrid;

  @BeforeEach
  public void setUp() {
    oneColumnImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    oneColumnGrid = new Grid(oneColumnImage, seed, true);
    imageTest1 = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
    imageTest1.setRGB(0, 0, new Color(0, 0, 225).getRGB());
    imageTest1.setRGB(0, 1, new Color(255, 0, 0).getRGB());
    imageTest1.setRGB(0, 2, new Color(0, 0, 225).getRGB());
    imageTest1.setRGB(1, 0, new Color(0, 0, 225).getRGB());
    imageTest1.setRGB(1, 1, new Color(0, 0, 225).getRGB());
    imageTest1.setRGB(1, 2, new Color(0, 0, 225).getRGB());
    imageTest1.setRGB(2, 0, new Color(0, 0, 225).getRGB());
    imageTest1.setRGB(2, 1, new Color(0, 0, 225).getRGB());
    imageTest1.setRGB(2, 2, new Color(0, 0, 225).getRGB());
    multipleColumnGrid = new Grid(imageTest1, seed, true);


  }

  @Test
  public void removeRandomTest() throws RequestFailedException {
    assertThrows(RequestFailedException.class, () -> oneColumnGrid.removeRandomColumn());
  }

  @Test
  public void removeMultipleRandomTest() throws RequestFailedException {
    multipleColumnGrid.removeRandomColumn();
    BufferedImage b = multipleColumnGrid.convertToBufferedImage();
    assertEquals(2, b.getWidth());
  }


  @Test
  public void undoTest() {
    try {
      multipleColumnGrid.removeBluestColumn();
      multipleColumnGrid.undo();
    } catch (RequestFailedException e) {
      throw new RuntimeException(e);
    }

    BufferedImage undo = multipleColumnGrid.convertToBufferedImage();
    System.out.println(undo.toString());
    assertEquals(new Color(0, 0, 225).getRGB(), undo.getRGB(0, 0));
    assertEquals(new Color(255, 0, 0).getRGB(), undo.getRGB(0, 1));
    assertEquals(new Color(0, 0, 225).getRGB(), undo.getRGB(1, 0));
  }
}