import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public final class ImageEditor extends JFrame implements ActionListener {
  private static final int SCREEN_WIDTH = 800;
  private static final int BUTTON_HEIGHT = 25;
  private static final int IMAGE_PANE_HEIGHT = 300;
  public static final int TOP_PANEL_FONT_SIZE = 20;
  private static final int SCREEN_HEIGHT = IMAGE_PANE_HEIGHT + 2 * BUTTON_HEIGHT + TOP_PANEL_FONT_SIZE;

  private static final String OPEN_IMAGE_TEXT = "Open Image";
  private static final String REMOVE_BLUEST_TEXT = "Remove Bluest Column";
  private static final String REMOVE_RANDOM_TEXT = "Remove Random Column";
  private static final String UNDO_TEXT = "Undo";
  private static final String QUIT_TEXT = "Quit";
  private static final List<String> BUTTON_TEXT_LIST = List.of(OPEN_IMAGE_TEXT, REMOVE_BLUEST_TEXT, REMOVE_RANDOM_TEXT, UNDO_TEXT, QUIT_TEXT);
  public static final int MIN_BUTTON_WIDTH = 110;
  public static final int IMAGE_PANEL_FONT_SIZE = 15;
  public static final int BUTTON_WIDTH_MULTIPLIER = 10;

  private JLabel originalImageLabel;
  private JLabel editedImageLabel;

  // This holds a reference to the grid for the loaded image.
  private Grid grid;

  public ImageEditor() {
    initUI();
    setTitle("Image Viewer");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(SCREEN_WIDTH, SCREEN_HEIGHT);
    setLocationRelativeTo(null);
    setOriginalImage(Utilities.generateImage());
  }

  private void initUI() {
    JPanel topPanel = createTopPanel();
    JPanel bottomPanel = createBottomPanel();
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(topPanel, BorderLayout.CENTER);
    getContentPane().add(bottomPanel, BorderLayout.SOUTH);
  }

  private JPanel createTopPanel() {
    JPanel topPanel = new JPanel(new GridLayout(1, 2));
    originalImageLabel = new JLabel();
    editedImageLabel = new JLabel();
    originalImageLabel.setFont(new Font("Arial", Font.PLAIN, TOP_PANEL_FONT_SIZE));
    editedImageLabel.setFont(new Font("Arial", Font.PLAIN, TOP_PANEL_FONT_SIZE));
    topPanel.add(createImagePanel("Original Image", originalImageLabel));
    topPanel.add(createImagePanel("Edited Image", editedImageLabel));
    return topPanel;
  }

  private JPanel createImagePanel(String title, JLabel imageLabel) {
    JPanel imagePanel = new JPanel(new BorderLayout());
    JLabel caption = new JLabel(title, SwingConstants.CENTER);
    caption.setFont(new Font("Arial", Font.PLAIN, IMAGE_PANEL_FONT_SIZE));
    imagePanel.add(caption, BorderLayout.NORTH);

    imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
    JPanel imageContainer = new JPanel(new BorderLayout());
    imageContainer.add(imageLabel, BorderLayout.CENTER);
    imagePanel.add(imageContainer, BorderLayout.CENTER);

    return imagePanel;
  }

  private JPanel createBottomPanel() {
    JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    BUTTON_TEXT_LIST.forEach(text -> {
      JButton button = new JButton(text);
      button.addActionListener(this);
      int width = Math.max(text.length() * BUTTON_WIDTH_MULTIPLIER, MIN_BUTTON_WIDTH);
      button.setPreferredSize(new Dimension(width, BUTTON_HEIGHT));
      bottomPanel.add(button);
    });
    return bottomPanel;
  }

  public void actionPerformed(ActionEvent event) {
    if (event.getSource() instanceof JButton source) {
      try {
        switch (source.getText()) {
          case OPEN_IMAGE_TEXT -> openImage();
          case REMOVE_BLUEST_TEXT -> removeBluestColumn();
          case REMOVE_RANDOM_TEXT -> removeRandomColumn();
          case UNDO_TEXT -> undo();
          case QUIT_TEXT -> System.exit(0);
        }
      } catch (RequestFailedException e) {
        show(e.getMessage());
      }
    }
  }

  private void show(String s) {
    JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
  }

  private void displayEditedImage() {
    editedImageLabel.setIcon(new ImageIcon(grid.convertToBufferedImage()));
  }

  private void removeBluestColumn() throws RequestFailedException {
    grid.removeBluestColumn();
    displayEditedImage();
  }

  private void removeRandomColumn() throws RequestFailedException {
    grid.removeRandomColumn();
    displayEditedImage();
  }

  private void undo() throws RequestFailedException {
    grid.undo();
    displayEditedImage();
  }

  private void openImage() {
    JFileChooser chooser = new JFileChooser();
    chooser.setAcceptAllFileFilterUsed(false);
    chooser.addChoosableFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "gif", "bmp"));
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      try {
        setOriginalImage(ImageIO.read(chooser.getSelectedFile()));
      } catch (IOException e) {
        show("Error loading image: " + e.getMessage());
      }
    }
  }

  private void setOriginalImage(BufferedImage image) {
    ImageIcon icon = new ImageIcon(image);
    originalImageLabel.setIcon(icon);
    grid = new Grid(image, System.currentTimeMillis());
    displayEditedImage();
  }

  /**
   * Launches the application.
   *
   * @param args ignored
   */
  public static void main(String[] args) {
    ImageEditor jframe = new ImageEditor();
    SwingUtilities.invokeLater(() -> {
      jframe.setVisible(true);
    });
  }
}
