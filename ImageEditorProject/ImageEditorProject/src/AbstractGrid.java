import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * A grid representing a mutable image with removable columns.
 */
public abstract class AbstractGrid {
  protected Random random;
  protected boolean testMode = false;

  /**
   * Constructs a grid from the provided image and the seed, which is used
   * for random number generation.
   *
   * @param image the image
   * @param seed  the seed
   * @throws IllegalArgumentException if the image is invalid, such as having 0
   *                                  rows or columns
   */
  public AbstractGrid(BufferedImage image, long seed) {
    random = new Random(seed);
  }

  /**
   * Constructs a grid from the provided image and the seed, optionally
   * specifying whether to run in test mode, which suppresses external
   * operations, such as temporary file creation.
   *
   * @param image    the image
   * @param seed     the seed, which is used for random number generation
   * @param testMode whether to run in test mode
   * @throws IllegalArgumentException if the image is invalid, such as having 0
   *                                  rows or columns
   */
  AbstractGrid(BufferedImage image, long seed, boolean testMode) {
    this(image, seed); // call the other constructor
    this.testMode = testMode;
  }

  /**
   * Creates a buffered image representation of the image in this grid.
   *
   * @return the buffered image representation
   */
  abstract BufferedImage convertToBufferedImage();

  /**
   * Gets the index of the bluest column. The blueness of a column is the
   * sum of the blue components of each of its pixels. Counterintuitively,
   * an all white column would be considered completely blue, since the
   * red, green, and blue components of all of its pixels would have the maximum
   * values.
   * <p>
   * The concrete subclass should not permit grids with no columns to be
   * created. The check for zero columns in this method is an example of
   * defensive programming.
   *
   * @return the index of the bluest column
   * @throws RequestFailedException if the grid has no columns
   */
  abstract int getBluestColumnIndex() throws RequestFailedException;

  /**
   * Mutates this grid by removing the bluest column.
   *
   * @throws RequestFailedException if the grid has one or fewer columns
   */
  abstract void removeBluestColumn() throws RequestFailedException;
}
