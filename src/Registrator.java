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
import java.io.File;
import org.controlsfx.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.TextFormatter;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;

public class Registrator
{
	private Stage stage = new Stage();
    private TextField emailTF;
    private TextField usernameTF;
    private PasswordField passwordTF;
    private PasswordField rPasswordTF;
    private static String userFileName = "../inp/users.txt";
    private GridPane grid = new GridPane();
    private ServerMessager server;

	public Registrator(ServerMessager server)
	{
		//Initialize
		//Stage stage = new Stage();
    this.server = server;

		stage.initModality(Modality.APPLICATION_MODAL);

		stage.setTitle("Registration");

		stage.setIconified(true);

		BorderPane bPane = new BorderPane();

		// Manage the central grid

   		fillCenterGrid(grid);

   		bPane.setCenter(grid);


   		// Manage the bottom grid

   		GridPane bGrid = new GridPane();

   		fillBottomGrid(bGrid);

    	bPane.setBottom(bGrid);

    	bPane.setAlignment(bGrid, Pos.BASELINE_RIGHT);


      StackPane sPane = new StackPane();
      Text title = new Text("\n Enter your info:");
      Text subtitle = new Text("\n \n \n \n \nPlease refrain from weak passwords. ⚔");
      title.setFont(new Font("Helvetica", 26));
      title.setFill(Color.DARKCYAN);
      subtitle.setFont(new Font("Helvetica", 14));
      sPane.getChildren().add(subtitle);
      sPane.getChildren().add(title);
      
      //sPane.setAlignment(title, Pos.TOP_CENTER);
      bPane.setTop(sPane);

    	
    	// Finalize the scene
		      
		Scene scene = new Scene(bPane, 450, 300);		      
		
		stage.setScene(scene);
		      
		stage.showAndWait();

	}

	public void fillBottomGrid(GridPane bGrid)
	{
   		bGrid.setHgap(10);
    	bGrid.setVgap(10);
    	bGrid.setPadding(new Insets(0, 10, 0, 10));
    	bGrid.setAlignment(Pos.BASELINE_RIGHT);

   		Button register = new Button("Register!");

   		bGrid.add(register, 0, 0);

   		register.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                if(registerUser())
                {
                    stage.close();
                    Notifications.create()
                          .title("Registration complete!")
                          .text("You're good to go.")
                          .graphic(new ImageView(new Image(new File("../img/scroll.png").toURI().toString())))
                          .position(Pos.TOP_RIGHT)
                          .show(); 
                }
                else
                {
                  if(checkInput())
                  {
                    Notifications.create()
                          .title("Registration unsuccesful!")
                          .text("We have encountered an error on our end. Please try again.")
                          .graphic(new ImageView(new Image(new File("../img/error.png").toURI().toString())))
                          .position(Pos.TOP_RIGHT)
                          .show(); 
                  }
                  
                }
            }
        });


	}

	public void fillCenterGrid(GridPane grid)
	{

    TextFormatter tf = (new TextFormatter<>(change -> {
                                    if (change.getText().equals(" ")) 
                                    {
                                        change.setText("");
                                    }
                                    else if(emailTF.getText().length() > 40)
                                    {
                                        change.setText("");
                                    }
                                    return change;

                                }));

		  grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(0, 10, 0, 10));
    	grid.setAlignment(Pos.CENTER);

      String emailString = new String("Email address: ");
    	Text emailLabel = new Text(emailString);
    	emailTF = new TextField();
      emailTF.setPromptText("example@domain.com");
      emailTF.setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
             emailTF.setStyle(null);              
              }
          });
      emailTF.setTextFormatter(tf);
    	grid.add(emailLabel, 0, 0); 
    	grid.add(emailTF, 1, 0);

    	Text usernameLabel = new Text("Username:");
    	usernameTF = new TextField();
      usernameTF.setPromptText("user0");
      usernameTF.setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
             usernameTF.setStyle(null);              
              }
          });
      tf = (new TextFormatter<>(change -> {
          if (change.getText().equals(" ") || change.getText().equals("[") || change.getText().equals("]")) 
          {
              change.setText("");
          }
          else if(usernameTF.getText().length() > 20)
          {
              change.setText("");
          }
          return change;
      }));
      usernameTF.setTextFormatter(tf);
    	grid.add(usernameLabel, 0, 1); 
    	grid.add(usernameTF, 1, 1);
      ProgressBar pb = new ProgressBar(0);
      pb.setStyle("-fx-accent: red;");
      Text percentage = new Text("\n\n\nWeak (" + pb.getProgress() * 100 +"%)");
      percentage.setFill(Color.RED);
    	Text passwordLabel = new Text("Password:");
    	passwordTF = new PasswordField();
      Tooltip passTool = new Tooltip("- Add alphabetics 'a-z' & 'A-Z'\n- Add numerals '0-9'\n- Add special characters '!', '?' ...");
      //passTool.setStyle("-fx-background-color: ghostwhite;");
      StackPane toolStack = new StackPane();
      
      toolStack.getChildren().addAll(pb, percentage);
      passTool.setGraphic(toolStack);
      Tooltip.install(passwordTF, passTool);
      passwordTF.setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
             passwordTF.setStyle(null); 
             if(!passwordTF.getText().equals(""))
             {
                rPasswordTF.setDisable(false);
                double diversity = 0;
                double progress = 0;
                String temp = passwordTF.getText();
                progress = Math.min(7, temp.length());
                Pattern letter = Pattern.compile("[a-zA-z]");
                Pattern digit = Pattern.compile("[0-9]");
                Pattern special = Pattern.compile ("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
                Matcher hasLetter = letter.matcher(temp);
                Matcher hasDigit = digit.matcher(temp);
                Matcher hasSpecial = special.matcher(temp);
                if(hasLetter.find())
                {
                  progress++;
                }
                if(hasDigit.find())
                {
                  progress++;
                }
                if(hasSpecial.find())
                {
                  progress++;
                }
                progress += diversity;
                progress = progress / 10;
                pb.setProgress(progress);

                percentage.setText("\n\n\nWeak (" + pb.getProgress() * 100 +"%)");
                
                if(progress <= 0.3)
                {
                  pb.setStyle("-fx-accent: red;");
                  percentage.setFill(Color.RED);
                }
                else if(progress > 0.3 && progress < 0.7)
                {
                  pb.setStyle("-fx-accent: yellow;");
                  percentage.setFill(Color.YELLOW);
                  percentage.setText("\n\n\nMedium (" + pb.getProgress() * 100 +"%)");

                }
                else
                {
                  pb.setStyle("-fx-accent: green;");
                  percentage.setFill(Color.GREEN);
                  percentage.setText("\n\n\nStrong (" + pb.getProgress() * 100 +"%)");
                }
                Tooltip.uninstall(passwordTF, passTool);
                toolStack.getChildren().remove(pb);
                toolStack.getChildren().remove(percentage);
                
                toolStack.getChildren().addAll(pb, percentage);
                passTool.setGraphic(toolStack);
                Tooltip.install(passwordTF, passTool);
                // Check for diversity of characters 
                // update progress bar, change color according to:
                // Red: Weak (<= 3%)
                // Orange-Yellow: ( 3 < p < 7)
                // Green: (>= 7)
             }
             else
             {
                pb.setProgress(0);
                percentage.setText("\n\n\nWeak (" + pb.getProgress() * 100 +"%)");
                Tooltip.uninstall(passwordTF, passTool);
                toolStack.getChildren().remove(pb);
                toolStack.getChildren().remove(percentage);
                
                toolStack.getChildren().addAll(pb, percentage);
                passTool.setGraphic(toolStack);
                Tooltip.install(passwordTF, passTool);
                rPasswordTF.setDisable(true);
             }            
              }
          });
      tf = (new TextFormatter<>(change -> {
          if (change.getText().equals(" ")) 
          {
              change.setText("");
          }
          else if(passwordTF.getText().length() > 20)
          {
              change.setText("");
          }
          return change;
      }));
      passwordTF.setTextFormatter(tf);
      
      


    	grid.add(passwordLabel, 0, 2); 
    	grid.add(passwordTF, 1, 2);

      

    	Text rPasswordLabel = new Text("Repeat password:");
    	rPasswordTF = new PasswordField();
      rPasswordTF.setDisable(true);
      rPasswordTF.setOnKeyPressed(new EventHandler<KeyEvent>() {
          @Override
          public void handle(KeyEvent event) {
             rPasswordTF.setStyle(null);              
              }
          });
      tf = (new TextFormatter<>(change -> {
          if (change.getText().equals(" ")) 
          {
              change.setText("");
          }
          else if(rPasswordTF.getText().length() > 20)
          {
              change.setText("");
          }
          return change;
      }));
      rPasswordTF.setTextFormatter(tf);
    	grid.add(rPasswordLabel, 0, 3); 
    	grid.add(rPasswordTF, 1, 3);

        /*

    	Text cBoxLabel = new Text("Role:");
    	ChoiceBox cBox = new ChoiceBox();
    	cBox.getItems().addAll("Master", "Apprentice", "NPC");
    	cBox.setValue("NPC");

    	grid.add(cBoxLabel, 0, 4);
    	grid.add(cBox, 1, 4);

    	Text pBoxLabel = new Text("Adventure:");
    	ChoiceBox pBox = new ChoiceBox();
    	pBox.getItems().addAll("Game 1", "Game 2", "Unknown");
    	pBox.setValue("Unknown");

    	grid.add(pBoxLabel, 0, 5);
    	grid.add(pBox, 1, 5);		
        */
	}  

    public boolean checkInput()
    {
      emailTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");          
      usernameTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
      passwordTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
      rPasswordTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");

      boolean errorFound = false;
      if(emailTF.getText().equals(""))
      {
        Text t = new Text("Email cannot be empty or invalid!");
        t.setFont(Font.font ("Verdana", 8));
        t.setFill(Color.RED);
        emailTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 0 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }

        grid.add(t, 2, 0);
        errorFound = true;
      }
      else
      {
        emailTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
        for(Node node : grid.getChildren()) {
            if(grid.getRowIndex(node) == 0 && grid.getColumnIndex(node) == 2) {
                grid.getChildren().remove(node);
                break;
            }
  }
      }
      if(usernameTF.getText().equals(""))
      {
        Text t = new Text("User name cannot be empty!");
        t.setFont(Font.font ("Verdana", 8));
        t.setFill(Color.RED);
        for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 1 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }
        grid.add(t, 2, 1);
        usernameTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        errorFound = true;  
       }
       else
       {
          for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 1 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }
       }
      if(passwordTF.getText().equals(""))
      {

        Text t = new Text("Password cannot be empty!");
        t.setFont(Font.font ("Verdana", 8));
        t.setFill(Color.RED);
        for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 2 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }
        grid.add(t, 2, 2);
        passwordTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        errorFound = true; 
      }
      else
       {
          for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 2 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }
       }
      if(!passwordTF.getText().equals(rPasswordTF.getText()))
      {
        Text t = new Text("Passwords must match!");
        t.setFont(Font.font ("Verdana", 8));
        t.setFill(Color.RED);
        for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 3 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
        }

        grid.add(t, 2, 3);
        rPasswordTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");     
        errorFound = true;
      }
      else
       {
          for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 3 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }
       }
      if(passwordTF.getText().length() < 4)
      {
        Text t = new Text("No weak passwords!");
        t.setFont(Font.font ("Verdana", 8));
        t.setFill(Color.RED);
        for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 2 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
        }

        grid.add(t, 2, 2);
        passwordTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");     
        errorFound = true;
      }
      else
       {
          for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 2 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }
       }

      if(!isValidEmail(emailTF.getText()))
      {
        Text t = new Text("Email must be valid!");
        t.setFont(Font.font ("Verdana", 8));
        t.setFill(Color.RED);
        //Remove the previous message
        for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 0 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }
        grid.add(t, 2, 0);
        emailTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        errorFound = true;
      }

      else
       {
          emailTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
          for(Node node : grid.getChildren()) 
          {
              if(grid.getRowIndex(node) == 0 && grid.getColumnIndex(node) == 2) 
              {
                  grid.getChildren().remove(node);
                  break;
              }
          }
       }

      User user = new User(emailTF.getText(), usernameTF.getText(), passwordTF.getText());
      if(!usernameTF.getText().equals("") && server.checkUserExists(user))
      {
        Text t = new Text("This user already exists!");
        t.setFont(Font.font ("Verdana", 8));
        t.setFill(Color.RED);
        for(Node node : grid.getChildren()) 
          {
            if(grid.getRowIndex(node) == 1 && grid.getColumnIndex(node) == 2) 
            {
              grid.getChildren().remove(node);
              break;
            }
          }
        grid.add(t, 2, 1);
        usernameTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
        errorFound = true;
      }
      else
       {
        if(!usernameTF.getText().equals(""))
        {
          for(Node node : grid.getChildren()) 
          {
            if(grid.getRowIndex(node) == 1 && grid.getColumnIndex(node) == 2) 
            {
              grid.getChildren().remove(node);
              break;
            }
          }
        }
          
       }

      if(errorFound)
      {
        return false;
      }



      return true;

    }

    public static boolean isValidEmail(String email) 
        { 
            String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+ 
                                "[a-zA-Z0-9_+&*-]+)*@" + 
                                "(?:[a-zA-Z0-9-]+\\.)+[a-z" + 
                                "A-Z]{2,7}$"; 
                                  
            Pattern pat = Pattern.compile(emailRegex); 
            if (email == null) 
                return false; 
            return pat.matcher(email).matches(); 
        } 


    public boolean registerUser()
    {
            if(!checkInput())
            {
              return false;
            }
            else
            {
              User usr = new User(emailTF.getText(), usernameTF.getText(), passwordTF.getText());
              if(server.requestedRegister(usr))
              {
                return true;
              }

              return false;
            }
    }
}