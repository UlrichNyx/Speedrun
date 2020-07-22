import java.io.*;
import java.net.*;
import java.util.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import org.controlsfx.control.*;
import javafx.scene.control.*;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

	

public class Dashboard
{
	private Tab tp;
    private GridPane grid = new GridPane();
    private ServerMessager server;
	public Dashboard(ServerMessager server, Tab tp)
	{
		this.server = server;
		this.tp = tp;

		tp.setContent(grid);

		StackPane sPane = new StackPane();
      	Text title = new Text("\n " + tp.getText().split(" ")[1]);
     	title.setFont(new Font("Helvetica", 26));
      	title.setFill(Color.DARKCYAN);
      	sPane.getChildren().add(title);
      	grid.add(title, 0,0);	
	}
}