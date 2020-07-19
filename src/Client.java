import java.io.*;
import java.net.*;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;


public class Client extends Application
{
    public static ServerMessager server;
	public static void main(String [] args)
	{
        Integer port;

        // Try to set all values to their corresponding args index. If something fails, terminate.
        try
        {
            port = Integer.parseInt(args[0]);
        }catch (Exception e)
        {
            System.out.println(e);
            return;
        }
        try
        {
            //Instead of localhost, we can instead use a specific IP address such as my home in Thessaloniki, in order to access the server (if the server is running obviously)
            Socket socket = new Socket("localhost", port);
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            server = new ServerMessager(socket, out);
            server.connectionEstablished(port);
        }catch(Exception e){System.out.println("error"+e); System.exit(-1);}

        

		launch(args);
	}
		

	@Override
    public void start(Stage primaryStage) 
    {
    	
        primaryStage.setTitle("Speedrun");

        final Menu projectMenu = new Menu("Project");


        MenuItem newProject = new MenuItem("New Project");

        newProject.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
               new ProjectEditor(server);
            }
        });

        projectMenu.getItems().add(newProject);
        //projectMenu.add("Open");
        //projectMenu.add("Save");
        //projectMenu.add("Edit");
        
        MenuBar menuBar = new MenuBar();

        menuBar.getMenus().add(projectMenu);
        

        VBox vBox = new VBox(menuBar);
        Scene scene = new Scene(vBox, 720, 480);

        primaryStage.setScene(scene);
        Logger logger = new Logger(server);
        primaryStage.show();
    }

}