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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;    

public class ProjectEditor
{
	private Stage stage = new Stage();

  private GridPane grid = new GridPane();
  private TextField nameTF;
  private TextArea description;
  private TextField searchBar;
  private ListView list;
  private DatePicker dates = new DatePicker(LocalDate.now().plusDays(1));
  private Team team = new Team();
  private Project proj = new Project();

  private User usr;

  private ServerMessager server;

	public ProjectEditor(ServerMessager server, User usr, Project proj)
	{
		//Initialize
		//Stage stage = new Stage();
    	this.server = server;

        this.usr = usr;

        this.proj = proj;

		stage.initModality(Modality.APPLICATION_MODAL);

		stage.setTitle("New Project");

		stage.setIconified(true);

		BorderPane bPane = new BorderPane();

		// Manage the central grid

   		fillCenterGrid();

   		bPane.setCenter(grid);

    	// Finalize the scene
   		StackPane sPane = new StackPane();
      	Text title = new Text("\n Project Editor:");
      	Text subtitle = new Text("\n \n \n \n \n Give us the building-blocks of your project! ✨");
     	title.setFont(new Font("Helvetica", 26));
      	title.setFill(Color.DARKCYAN);
      	subtitle.setFont(new Font("Helvetica", 14));
      	sPane.getChildren().add(subtitle);
      	sPane.getChildren().add(title);
      
      //sPane.setAlignment(title, Pos.TOP_CENTER);
    	bPane.setTop(sPane);
    	bPane.setAlignment(sPane, Pos.TOP_CENTER);

    	GridPane bGrid = new GridPane();

   		fillBottomGrid(bGrid);

    	bPane.setBottom(bGrid);

    	bPane.setAlignment(bGrid, Pos.BASELINE_RIGHT);
		      
		Scene scene = new Scene(bPane, 480, 450);		      
		
		stage.setScene(scene);
		      
		stage.showAndWait();

	}

	public void fillBottomGrid(GridPane bGrid)
	{
		bGrid.setHgap(10);
    	bGrid.setVgap(0);
    	bGrid.setPadding(new Insets(0, 10, 0, 10));
    	bGrid.setAlignment(Pos.BASELINE_RIGHT);

    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YYYY");
        LocalDate date = LocalDate.now();
        if(proj.getBeginDate() != null)
        {
            date = proj.getBeginDate();
        }

        dates.getEditor().setDisable(true);
        dates.setStyle("-fx-opacity: 1");
        dates.getEditor().setStyle("-fx-opacity: 1");

        if(proj.getEndDate() != null)
        {
            dates.setValue(proj.getEndDate());
        }
    	String strDate= formatter.format(date);  
    	strDate += " -";

    	Text currentTime = new Text(strDate); 

    	bGrid.add(currentTime, 0, 0);
    	bGrid.add(dates, 1, 0);

    	Button cancelButton = new Button("Cancel");
    	Button continueButton = new Button("Continue");

    	bGrid.add(cancelButton, 2, 0);
    	bGrid.add(continueButton, 3, 0);

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
               stage.close();
            }
        });
        
        continueButton.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
               if(checkInput())
               {
                    proj = new Project(nameTF.getText(), description.getText(), team, LocalDate.now(), dates.getValue());
                    Client.assignProject(proj);
                    stage.close();
               }
               
            }
        });
	}



	public void fillCenterGrid()
	{
		
		grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(0, 10, 0, 10));
    	grid.setAlignment(Pos.CENTER);


    	Text nameLabel = new Text("Project Name:");
    	nameTF = new TextField(proj.getName());
    	nameTF.setPromptText("Project0");
    	
    	TextFormatter tf = (new TextFormatter<>(change -> {
                                    if (change.getText().equals(" ") || change.getText().equals("[") || change.getText().equals("]")) 
                                    {
                                        change.setText("");
                                    }
                                    else if(nameTF.getText().length() > 20)
                                    {
                                        change.setText("");
                                    }
                                    return change;

                                }));
    	nameTF.setTextFormatter(tf);
    	grid.add(nameLabel, 0, 0);
    	grid.add(nameTF, 1, 0); 

    	Text descLabel = new Text("Description:");
    	description = new TextArea(proj.getDescription());
    	description.setPromptText("This is the description for my project!");
    	description.setPrefWidth(40);
    	description.setPrefHeight(80);

        tf = (new TextFormatter<>(change -> {
                                    if(description.getText().length() > 200)
                                    {
                                        change.setText("");
                                    }
                                    if(change.getText().equals("]") || change.getText().equals("["))
                                    {
                                        change.setText("");
                                    }
                                    return change;

                                }));
        description.setTextFormatter(tf);
    	grid.add(descLabel, 0, 1);
    	grid.add(description, 1, 1); 

    	Text searchLabel = new Text("Search Users:");
    	searchBar = new TextField();
    	searchBar.setPromptText("User0");
        tf = (new TextFormatter<>(change -> {
                                    if (change.getText().equals(" ")) 
                                    {
                                        change.setText("");
                                    }
                                    else if(searchBar.getText().length() > 20)
                                    {
                                        change.setText("");
                                    }
                                    return change;

                                }));
        searchBar.setTextFormatter(tf);
    	Button addMember = new Button("+");
        addMember.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                for(Node node : grid.getChildren()) 
                {
                    if(grid.getRowIndex(node) == 2 && grid.getColumnIndex(node) == 3) 
                    {
                        grid.getChildren().remove(node);
                        break;
                    }
                }
               if(!searchBar.getText().equals(""))
               {
                    Text doesntExist = new Text("User doesn't exist!");
                    doesntExist.setFont(Font.font ("Verdana", 8));
                    doesntExist.setFill(Color.RED);
                    Text alreadyInTeam = new Text("User is already invited!");
                    alreadyInTeam.setFont(Font.font ("Verdana", 8));
                    alreadyInTeam.setFill(Color.RED);
                    if(server.checkUserExists(new User(searchBar.getText(), "")))
                    {
                        
                        if(!list.getItems().contains(searchBar.getText()))
                        {
                            list.getItems().add(searchBar.getText());
                            
                            team.add(new User(searchBar.getText(), ""));
                        }
                        else
                        {
                            grid.add(alreadyInTeam, 3, 2);
                        }
                        
                    }
                    else
                    {
                        grid.add(doesntExist, 3, 2);
                    }
               }
            }
        });
    	list = new ListView();
        list.getItems().add(usr.getUsername());
        Button removeMember = new Button("-");
        removeMember.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                for(Node node : grid.getChildren()) 
                    {
                        if(grid.getRowIndex(node) == 3 && grid.getColumnIndex(node) == 3) 
                        {
                            grid.getChildren().remove(node);
                            break;
                        }
                    }
                if(list.getSelectionModel().getSelectedItem().equals(usr.getUsername()))
                {
                    Text cantRemove = new Text("Can't remove the owner!");
                    cantRemove.setFont(Font.font ("Verdana", 8));
                    cantRemove.setFill(Color.RED);
                    grid.add(cantRemove, 3, 3);
                    return;
                }
                list.getItems().remove(list.getSelectionModel().getSelectedItem());
            }
        });
        for(User u : proj.getTeam().getMembers())
        {
            if(!u.getUsername().equals(usr.getUsername()))
            {
                list.getItems().add(u.getUsername());
            }
            
        }
        team.add(new User(usr.getUsername(), ""));
    	Text teamLabel = new Text("Team composition:");

    	list.setPrefHeight(80);

    	grid.add(searchLabel, 0, 2);
    	grid.add(searchBar, 1, 2);
    	grid.add(addMember, 2, 2);
    	grid.add(list, 1, 3);
        grid.add(removeMember, 2, 3);
    	grid.add(teamLabel, 0, 3);


	}

    public boolean checkInput()
    {
        nameTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");          
        dates.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");

        Text emptyName = new Text("Your project must have a name!");
        
        boolean flag = true;
        if(nameTF.getText().equals(""))
        {
            for(Node node : grid.getChildren()) 
            {
                if(grid.getRowIndex(node) == 0 && grid.getColumnIndex(node) == 2) 
                {
                    grid.getChildren().remove(node);
                    break;
                }
            }
            grid.add(emptyName, 2, 0);
            emptyName.setFont(Font.font ("Verdana", 8));
            emptyName.setFill(Color.RED);
            nameTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            flag = false;
        }
        else
        {
            nameTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
            for(Node node : grid.getChildren()) 
            {
                if(grid.getRowIndex(node) == 0 && grid.getColumnIndex(node) == 2) 
                {
                    grid.getChildren().remove(node);
                    break;
                }
            }
        }
        
        Team temp = new Team();
        temp.add(usr);
        if(proj.getName().equals("") && server.checkProjectExists(new Project(nameTF.getText(), "", temp, null, null)))
        {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);

            DialogPane dialogPane = alert.getDialogPane();

            alert.setTitle("Duplicate project found!");
            alert.setHeaderText(String.format("Duplicate project found for project name: " + nameTF.getText() + "\n If you proceed, saving this project will overwrite the previous one."));
            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(ButtonType.OK);
            alert.getButtonTypes().addAll(ButtonType.CANCEL);
            Optional<ButtonType> result = alert.showAndWait();
            if(!result.isPresent()){flag = false;}
            else if(result.get() == ButtonType.OK){}
            else if(result.get() == ButtonType.CANCEL){flag = false;}
        }

        LocalDate endDate = dates.getValue();
        Text wrongDate = new Text("You can't travel back in time!");
        System.out.println(LocalDate.now().toString() + " is equal to " + endDate.toString() + " " + LocalDate.now().isEqual(endDate));
        if((LocalDate.now().isEqual(endDate)) || LocalDate.now().isAfter(endDate))
        {
            for(Node node : grid.getChildren()) 
            {
                if(grid.getRowIndex(node) == 4 && grid.getColumnIndex(node) == 1) 
                {
                    grid.getChildren().remove(node);
                    break;
                }
            }
            grid.add(wrongDate, 1, 4);
            wrongDate.setFont(Font.font ("Verdana", 8));
            wrongDate.setFill(Color.RED);
            dates.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            flag = false;
        }
        else
        {
            dates.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
            for(Node node : grid.getChildren()) 
            {
                if(grid.getRowIndex(node) == 4 && grid.getColumnIndex(node) == 1) 
                {
                    grid.getChildren().remove(node);
                    break;
                }
            }
        }

        return flag;
    }
}