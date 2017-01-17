package assignment.pkg3;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 *
 * @author Sam
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private Pane newNewPane;

    @FXML
    private Label label;

    @FXML
    private Pane newPane;

    @FXML
    private GridPane gridPane;

    @FXML
    private Rectangle rectangle;

    @FXML
    private Button browseButton;

    @FXML
    private Button readButton;

    @FXML
    private TextField fileName;

    @FXML
    private Text panSliceNumber;

    @FXML
    private Text waterVolumeNumber;

    @FXML
    private Button leftButton;

    @FXML
    private Button rightButton;

    @FXML
    private Button lastMapButton;

    @FXML
    private Button firstMapButton;

    // col need to put a way to make it the right length and not just 10
    int[][] data = new int[1000][20];
    int slice = 0;
    int numberOfRows = 0;
    int[] array = new int[20];

    @FXML
    void moveOneLeft(ActionEvent event) {
        if (slice > 0) {
            slice--;
        }
        uncolorCells();
        array = returnArrayFromSlice();
        int volume;
        volume = getWaterCells();
        colorInCells();

        printOutResultsToScreen();
        updatePanAndVolume(volume);
    }

    @FXML
    void moveOneRight(ActionEvent event) {
        if (slice < numberOfRows) {
            slice++;
        }
        uncolorCells();
        array = returnArrayFromSlice();
        int volume;
        volume = getWaterCells();
        colorInCells();

        printOutResultsToScreen();
        updatePanAndVolume(volume);
    }

    @FXML
    void moveToFirst(ActionEvent event) {
        slice = 0;
        uncolorCells();
        array = returnArrayFromSlice();
        int volume;
        volume = getWaterCells();
        colorInCells();

        printOutResultsToScreen();
        updatePanAndVolume(volume);
    }

    @FXML
    void moveToLast(ActionEvent event) {
        slice = numberOfRows;
        uncolorCells();
        array = returnArrayFromSlice();
        int volume;
        volume = getWaterCells();
        colorInCells();

        printOutResultsToScreen();
        updatePanAndVolume(volume);
    }

    @FXML
    void onBrowse(ActionEvent event) {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Opening Land Map File");
        File file = fileChooser.showOpenDialog(null);

        fileName.setText(file.toString());
    }

    @FXML
    void onRead(ActionEvent event) throws FileNotFoundException {
        String fileLocation = fileName.getText();

        File file = new File(fileLocation);
        Scanner fileIn = new Scanner(file);

        int i = 0;
        while (fileIn.hasNextLine()) {
            String line = fileIn.nextLine();
            String[] lineNumbers = line.split(",");
            for (int j = 0; j < 20; j++) {
                data[i][j] = Integer.parseInt(lineNumbers[j].trim());
                System.out.print(data[i][j] + " ");
            }
            System.out.println("");
            i++;
        }

        numberOfRows = i - 1;

        uncolorCells();
        array = returnArrayFromSlice();
        int volume = getWaterCells();
        colorInCells();

        updatePanAndVolume(volume);

    }

    public void colorInCells() {
        for (int col = 0; col < 20; col++) {
            for (int row = 9; row > (9 - array[col]); row--) {
                Node cell = getCellFromGridPane(gridPane, row, col);
                // here you set the property "background color" to a given color
                cell.setStyle("-fx-background-color: #ffc088");
            }
        }
    }

    public void uncolorCells() {
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row < 10; row++) {
                Node cell = getCellFromGridPane(gridPane, row, col);
                // here you set the property "background color" to a given color
                cell.setStyle("-fx-background-color: transparent");
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // this method is called everytime a GUI is created. This is where you
        // put commands that you want to do as an initialization process.
        // instead of creating manually a grid panel with 10 rows by 20 columns
        // in the SceneBuilder. I decided to create it here via 2 nested FOR
        // loops. I am also creating labels to be placed in the grid pane cells
        // the existing gridPane in the SceneBuilder is rewritten here, so it 
        // does not matter how many rows and columns you put in the scenebuilder
        // this is to make easier to create such a big grid pane.
        gridPane = new GridPane();
        for (int x = 0; x < 20; x++) {
            for (int y = 0; y < 10; y++) {
                // creating the labels
                Label label = new Label("  ");

                // binding the label size with the gridpane size, so that
                // when we resize the window the cells will be changed and the
                // labels also. Try to resize your application to see this happening
                label.prefWidthProperty().bind(gridPane.widthProperty());
                label.prefHeightProperty().bind(gridPane.heightProperty());

                // add label to the gridPane, but getting the gridPAne "children"
                // (cells) and adding one more child (cell) to the gridPane
                gridPane.getChildren().add(label);

                // this is needed to use the getCellFromGridPane() method below
                GridPane.setColumnIndex(label, x);
                GridPane.setRowIndex(label, y);
            }
        }
        gridPane.setMaxSize(525, 302);
        gridPane.setAlignment(Pos.CENTER);
        newNewPane.getChildren().add(gridPane);
        gridPane.setGridLinesVisible(true);
    }

    private Node getCellFromGridPane(GridPane gridPane, int row, int col) {
        // this method returns a cell ID for a given row and column in the gridpane
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == row) {
                return node;
            }
        }
        return null;
    }

    private int[] returnArrayFromSlice() {
        int[] sliceData = new int[20];

        sliceData = data[slice];

        return sliceData;
    }

    public void printOutResultsToScreen() {
        System.out.println(slice);
        for (int number : array) {
            System.out.print(number + " ");
        }
        System.out.println("");
    }

    public void updatePanAndVolume(int volume) {
        String panSlice = Integer.toString(slice);
        panSliceNumber.setText(panSlice);

        String newVolume = Integer.toString(volume);
        waterVolumeNumber.setText(newVolume);
    }

    public int getWaterCells() {
        int waterVolume = 0;
        for (int col = 0; col < 20; col++) {
            for (int row = 0; row <= 9 - array[col]; row++) {
                if (checkIfWallOnRight(row, col) && checkIfWallOnLeft(row, col)) {
                    colorInWater(col, row);
                    waterVolume++;
                }
            }
        }

        return waterVolume;
    }

    public void colorInWater(int col, int row) {
        //for (int newrow = 9; newrow > (9 - row); newrow--) {
        Node cell = getCellFromGridPane(gridPane, row, col);
        // here you set the property "background color" to a given color
        cell.setStyle("-fx-background-color: #1d61f4");
        //}
    }

    public boolean checkIfWallOnRight(int row, int col) {
        boolean check = false;
        for (int i = col; i < 20; i++) {
            if (10 - array[i] <= row) {
                check = true;
            }
        }

        return check;
    }

    public boolean checkIfWallOnLeft(int row, int col) {
        boolean check = false;
        for (int i = col; i >= 0; i--) {
            if (10 - array[i] <= row) {
                check = true;
            }
        }

        return check;
    }
}
