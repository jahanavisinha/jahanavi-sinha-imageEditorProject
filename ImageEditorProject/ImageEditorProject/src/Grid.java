import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Stack;

public class Grid extends AbstractGrid {
  private Stack<Color[][]> undoStack = new Stack<>();
  private Color[][] image;
  private int tempFileCounter = 0;
  private static final String IMAGE_FORMAT = "png";
  private int width;
  private int height;
  private int count;
  private boolean testMode;


  Grid(BufferedImage image, long seed) {
    super(image, seed, false);
    initializeArray(image);
  }

  Grid(BufferedImage image, long seed, boolean testMode) {
    super(image, seed, testMode);
    initializeArray(image);
  }

  private void initializeArray(BufferedImage image) {
    width = image.getWidth();
    height = image.getHeight();
    // if (width <= 0) throw new RequestFailedException("Invalid image");
    this.image = new Color[height][width];

    for (int x = 0; x < height; x++) {
      for (int y = 0; y < width; y++) {
        int rgb = image.getRGB(y, x);
        this.image[x][y] = new Color(rgb);
      }
    }
  }

  @Override
  protected BufferedImage convertToBufferedImage() {
    BufferedImage newImage = new BufferedImage(image[0].length, image.length, BufferedImage.TYPE_INT_RGB);

    for (int y = 0; y < image.length; y++) {
      for (int x = 0; x < image[0].length; x++) {
        newImage.setRGB(x, y, image[y][x].getRGB());
      }
    }
    return newImage;
  }

  @Override
  protected int getBluestColumnIndex() throws RequestFailedException {
    long max = 0;
    int index = 0;
    for (int col = 0; col < image[0].length; col++) {
      long sum = calculateBluestColumn(col);
      System.out.println("index = " + col + ", " + sum);
      if (sum > max) {
        max = sum;
        index = col;
      }
    }
    System.out.println(max);
    System.out.println(index);
    return index;
  }

  private long calculateBluestColumn(int x) {
    long sum = 0;
    for (int y = 0; y < image.length; y++) {
      sum += image[y][x].getBlue();
    }
    return sum;
  }

  private void removeColum(int columnIndex) throws RequestFailedException {
    if (image[0].length <= 1) {
      throw new RequestFailedException("The grid has one or fewer columns.");
    }
    if (columnIndex < 0 || columnIndex >= image[0].length) {
      throw new RequestFailedException("Invalid column index.");
    }

    Color[][] newImage = getCopy(columnIndex);
    image = newImage;
  }

  private Color[][] getCopy(int columnIndex) {
    Color[][] newImage = new Color[image.length][image[0].length - 1];
    int newCol = 0;
    for (int srcColumn = 0; srcColumn < image[0].length; srcColumn++) {
      if (srcColumn != columnIndex) {
        for (int row = 0; row < image.length; row++) {
          newImage[row][newCol] = image[row][srcColumn];
        }
        newCol++;
      }
    }
    return newImage;
  }


  @Override
  protected void removeBluestColumn() throws RequestFailedException {
    int bluestColumnIndex = getBluestColumnIndex();
    undoStack.push(getCopy(bluestColumnIndex));
    saveImageHelper();
    for (int x = 0; x < height; x++) {
      image[x][bluestColumnIndex] = Color.BLUE;
    }
    removeColum(bluestColumnIndex);
    saveImageHelper();
  }

  protected void removeRandomColumn() throws RequestFailedException {
    int randomColumn = random.nextInt(image[0].length);
    undoStack.push(getCopy(randomColumn));
    removeColum(randomColumn);
    saveImageHelper();
  }

  public void undo() throws RequestFailedException {
    if (undoStack.isEmpty()) {
      throw new RequestFailedException("No changes made so far");
    }
    image = undoStack.pop();
  }

  void saveImageHelper() {
    if (testMode) {
      return;
    }
    try {
      // Ensure the tmp directory exists
      File dir = new File("tmp");
      if (!dir.exists()) {
        dir.mkdirs();
      }

      // Save the file in the tmp directory
      File newFile = new File(dir, "temp-file1.png" + count + ".png");
      ImageIO.write(convertToBufferedImage(), IMAGE_FORMAT, newFile);
      System.out.println("Altered image stored to " + newFile.getPath());
      count++;
    } catch (IOException e) {
      System.out.println("Unable to save file.");
    }
  }
}