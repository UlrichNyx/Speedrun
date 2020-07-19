import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.geometry.Insets;
import java.util.*;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.event.EventHandler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import org.controlsfx.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.TextFormatter;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class ProjectEditor
{
	private Stage stage = new Stage();


  private static String projectFileName = "projects.txt";
  private GridPane grid = new GridPane();


  private ServerMessager server;

	public ProjectEditor(ServerMessager server)
	{
		//Initialize
		//Stage stage = new Stage();
    this.server = server;

		stage.initModality(Modality.APPLICATION_MODAL);

		stage.setTitle("New Project");

		stage.setIconified(true);

		BorderPane bPane = new BorderPane();

		// Manage the central grid

   		//fillCenterGrid(grid);

   		bPane.setCenter(grid);

    	
    	// Finalize the scene
		      
		Scene scene = new Scene(bPane, 450, 300);		      
		
		stage.setScene(scene);
		      
		stage.showAndWait();

	}
}