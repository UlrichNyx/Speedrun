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
import javafx.stage.Stage;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.layout.VBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tab;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import org.controlsfx.control.*;
import javafx.scene.control.*;


public class Client extends Application
{
    private static User user;
    private static Project project;
    public static ServerMessager server;
    public static TabPane tabPane = new TabPane();
    public static VBox vBox = new VBox();
    public static MenuItem editProject;
    public static MenuItem saveProject;
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

    public static void assignUser(User usr)
    {
        user = usr;
    }

    public static void assignProject(Project proj)
    {
        project = proj;
        tabPane.getTabs().clear();
        Tab pTab = new Tab("Project: " + proj.getName() + " - Dashboard");

        tabPane.getTabs().add(pTab);
        Dashboard db = new Dashboard(server, pTab);
        saveProject.setDisable(false);
        editProject.setDisable(false);
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
               if(project != null){server.saveProject(project);}
               new ProjectEditor(server, user, new Project());
               
            }
        });

        projectMenu.getItems().add(newProject);



        editProject = new MenuItem("Edit Project");

        editProject.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
               new ProjectEditor(server, user, project);
            }
        });

        projectMenu.getItems().add(editProject);

       saveProject = new MenuItem("Save Project");

        saveProject.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                if(server.saveProject(project))
                {
                    Notifications.create()
                          .title("Save complete!")
                          .text("You're good to go.")
                          .graphic(new ImageView(new Image(new File("../img/save.png").toURI().toString())))
                          .position(Pos.TOP_RIGHT)
                          .show();
                }
                else
                {
                    Notifications.create()
                          .title("Save failed!")
                          .text("Something went wrong while trying to save your project")
                          .graphic(new ImageView(new Image(new File("../img/error.png").toURI().toString())))
                          .position(Pos.TOP_RIGHT)
                          .show();
                }
            }
        });

        projectMenu.getItems().add(saveProject);

        editProject.setDisable(true);
        saveProject.setDisable(true);
        
        
        MenuItem loadProject = new MenuItem("Load Project");

        loadProject.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                if(!server.getAllProjects(user.getUsername()).isEmpty())
                {
                    if(project != null){server.saveProject(project);}
                    List<String> choices = new ArrayList<>(server.getAllProjects(user.getUsername()));
                    

                    ChoiceDialog<String> dialog = new ChoiceDialog<String>("",choices);
                    dialog.setTitle("Load Project");
                    dialog.setHeaderText("We found some projects!");
                    dialog.setContentText("Choose your project:");

                    // Traditional way to get the response value.
                    Optional<String> result = dialog.showAndWait();
                    if (result.isPresent()){
                        System.out.println("Your choice: " + result.get());
                        assignProject(server.loadProject(result.get()));
                    }
                }
                else
                {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    alert.setTitle("No projects found!");
                    alert.setHeaderText(String.format("We couldn't find a project under your username!"));
                    alert.getButtonTypes().clear();
                    alert.getButtonTypes().addAll(ButtonType.OK);
                    alert.showAndWait();
                }
            }
        });

        projectMenu.getItems().add(loadProject);

        final Menu teamMenu = new Menu("Team");

        MenuItem manageTeam = new MenuItem("Manage Team");
        MenuItem editRoles = new MenuItem("Edit Roles");
        MenuItem quitTeam = new MenuItem("Quit team");

        teamMenu.getItems().addAll(manageTeam, editRoles, quitTeam);
        
        final Menu userMenu = new Menu("User");

        MenuItem editProfile= new MenuItem("Edit Profile");
        MenuItem logOut = new MenuItem("Log out");

        logOut.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                primaryStage.hide();
               Logger logger = new Logger(server);
               primaryStage.show();
            }
        });

        userMenu.getItems().addAll(editProfile, logOut);


        final Menu taskMenu = new Menu("Task");

        MenuItem manageTasks = new MenuItem("Manage Tasks");
        MenuItem addTask = new MenuItem("Add Task");
        MenuItem editCategories = new MenuItem("Edit Categories");

        taskMenu.getItems().addAll(manageTasks, addTask, editCategories);

        final Menu sprintMenu = new Menu("Sprint");

        MenuItem manageSprints = new MenuItem("Manage Sprints");
        MenuItem addSprint = new MenuItem("Add Sprint");
        MenuItem produceAnalytics = new MenuItem("Produce Analytics");

        sprintMenu.getItems().addAll(manageSprints, addSprint, produceAnalytics);
        final Menu achievementsMenu = new Menu("Achievements");
        MenuItem newAchievement = new MenuItem("New Achievement");
        MenuItem myAchievements = new MenuItem("My Achievements");

        achievementsMenu.getItems().addAll(newAchievement, myAchievements);

        final Menu settingsMenu = new Menu("Settings");

        MenuItem darkMode = new MenuItem("Dark Mode");
        MenuItem setTextures = new MenuItem("Set new Textures");

        settingsMenu.getItems().addAll(darkMode, setTextures);

        //final Menu userInfo = new Menu("Logged in as: " + user.getUsername());

        //userInfo.setDisable(true);

        MenuBar menuBar = new MenuBar();

        ToolBar toolBar = new ToolBar();

        Button saveButton = new Button();
        saveButton.setDisable(true);
        ImageView iv = new ImageView(new Image(new File("../img/save.png").toURI().toString()));
        iv.setFitWidth(18);
        iv.setFitHeight(18);
        saveButton.setGraphic(iv);
        toolBar.getItems().add(saveButton);

        menuBar.getMenus().add(projectMenu);
        menuBar.getMenus().add(teamMenu);
        menuBar.getMenus().add(userMenu);
        menuBar.getMenus().add(taskMenu);
        menuBar.getMenus().add(sprintMenu);
        menuBar.getMenus().add(achievementsMenu);
        menuBar.getMenus().add(settingsMenu);
        //menuBar.getMenus().add(userInfo);
        vBox.getChildren().add(menuBar);
        vBox.getChildren().add(toolBar);
        vBox.getChildren().add(tabPane);


        
        Scene scene = new Scene(vBox, 720, 480);

        primaryStage.setScene(scene);
        Logger logger = new Logger(server);
        primaryStage.show();
    }

}