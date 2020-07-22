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
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.TextFormatter;
import org.controlsfx.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.text.TextAlignment;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Animation;
import javafx.util.Duration;
import java.util.regex.Matcher; 
import java.util.regex.Pattern;
import java.io.File;

public class Logger
{
  private GridPane grid;
	private Stage stage;
  private TextField usernameTF;
  private TextField passwordTF;
  private CheckBox keepLogged;
  private ServerMessager server;

	public Logger(ServerMessager server)
	{
    this.server = server;
		//Initialize
		stage = new Stage();
      
		stage.initModality(Modality.APPLICATION_MODAL);

		stage.setTitle("Log In");

		stage.setIconified(true);

    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
          public void handle(WindowEvent we) {
              System.exit(0);
          }
    });

		BorderPane bPane = new BorderPane();

		// Manage the central grid
   		grid = new GridPane();

   		fillCenterGrid(grid);

   		bPane.setCenter(grid);


   		// Manage the bottom grid

   		GridPane bGrid = new GridPane();

   		fillBottomGrid(bGrid);

    	bPane.setBottom(bGrid);

    	bPane.setAlignment(bGrid, Pos.BASELINE_RIGHT);

      //Show logo and text
      StackPane sPane = new StackPane();

      //🎮
      // ⚡
      Text title = new Text("\n  ⚡Speedrun🎮 ");
      Text subtitle = new Text("\n\n\n\n\n\nThe free scrum tool for game developers!");
      title.setFont(new Font("Helvetica", 32));
      title.setFill(Color.DARKCYAN);
      subtitle.setFont(new Font("Helvetica", 14));
      sPane.getChildren().add(subtitle);
      sPane.getChildren().add(title);
      
      //sPane.setAlignment(title, Pos.TOP_CENTER);
      bPane.setTop(sPane);
    	
    	// Finalize the scene
		      
		Scene scene = new Scene(bPane, 490, 300);		      
		
    //scene.getStylesheets().add("Speedrun.css");

		stage.setScene(scene);
		      
		stage.showAndWait();

	}

	public void fillBottomGrid(GridPane bGrid)
	{
   		bGrid.setHgap(10);
    	bGrid.setVgap(10);
    	bGrid.setPadding(new Insets(0, 10, 0, 10));
    	bGrid.setAlignment(Pos.BASELINE_RIGHT);

   		Button login = new Button("Log In!");


      Text txt0 = new Text("Forgot password?");
      txt0.setFont(Font.font ("Arial", 10));
      txt0.setUnderline(true);
      txt0.setFill(Color.BLUE);
      bGrid.add(txt0, 0, 0);
      txt0.setOnMouseClicked(
        new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                final Stage dialog = new Stage();
                dialog.initModality(Modality.APPLICATION_MODAL);
                dialog.initOwner(stage);
                dialog.setTitle("Change Password");
                BorderPane bGPane = new BorderPane();
                GridPane vgrid = new GridPane();
                bGPane.setCenter(vgrid);
                vgrid.setHgap(10);
                vgrid.setVgap(10);
                vgrid.setPadding(new Insets(0, 10, 0, 10));
                vgrid.setAlignment(Pos.CENTER);
                Text enterEmail = new Text("Enter your email: ✉");
                Text subEnter = new Text("We will get back to you soon with a unique 6 digit number!");
                enterEmail.setFill(Color.DARKCYAN);
                enterEmail.setFont(new Font("Helvetica", 26));
                subEnter.setFont(new Font("Helvetica", 14));
                vgrid.add(enterEmail, 1, 0);
                vgrid.add(subEnter, 1, 1);
                TextField email = new TextField();
                TextFormatter tf = (new TextFormatter<>(change -> {
                                    if (change.getText().equals(" ")) 
                                    {
                                        change.setText("");
                                    }
                                    else if(email.getText().length() > 40)
                                    {
                                        change.setText("");
                                    }
                                    return change;
                                }));
                email.setTextFormatter(tf);
                vgrid.add(email, 1, 2);
                Button submit = new Button("Submit!");
                submit.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                       String emailString = email.getText();
                       
                       if(!emailString.equals("") && server.checkEmailExists(emailString))
                       {
                          vgrid.getChildren().clear();
                          // old .graphic(new ImageView(new Image("email.png", 100, 100, false, false)))
                          Notifications.create()
                          .title("Email sent!")
                          .text("Check your email: " + emailString + " for a 6 digit code.")
                          .graphic(new ImageView(new Image(new File("../img/email.png").toURI().toString())))
                          .position(Pos.TOP_RIGHT)
                          .show(); 

                          

                          Text enterCode = new Text("Enter your code: 🕛");
                          String[] clocks = {"🕛","🕐","🕑", "🕒", "🕓", "🕔", "🕕", "🕖", "🕗", "🕘", "🕙", "🕚"};
                          
                          Text subCode = new Text("Quick! Your code will expire in: "+ server.getTimer(emailString) +"s ! ");
                          enterCode.setFill(Color.DARKCYAN);
                          enterCode.setFont(new Font("Helvetica", 26));
                          subCode.setFont(new Font("Helvetica", 14));
                          vgrid.add(subCode, 1, 1);
                                    Timeline timeline = new Timeline(new KeyFrame(Duration.millis(1000), a -> {

                                    String time;
                                    if(!(time = server.getTimer(emailString)).equals(""))
                                    {
                                      try
                                      {
                                        
                                        vgrid.getChildren().remove(subCode);
                                        subCode.setText("Quick! Your code will expire in: "+ time +"s !");
                                        vgrid.add(subCode, 1, 1);

                                        vgrid.getChildren().remove(enterCode);
                                        enterCode.setText("Enter your code: " + clocks[(clocks.length - Integer.parseInt(time) % clocks.length) % clocks.length]);
                                        vgrid.add(enterCode, 1, 0);

                                      }catch(NullPointerException npe){}
                                    }
                                  }
                                ));
                                timeline.setCycleCount(60);
                                timeline.setOnFinished(new EventHandler<ActionEvent>(){

                                  @Override
                                  public void handle(ActionEvent event)
                                  {
                                    dialog.close();
                                    Notifications.create()
                                    .title("Code expired!")
                                    .text("Your code is no longer valid.")
                                    .graphic(new ImageView(new Image(new File("../img/hourglassdone.png").toURI().toString())))
                                    .position(Pos.TOP_RIGHT)
                                    .show(); 
                                  }

                                });
                                timeline.play();
                          vgrid.add(enterCode, 1, 0);
                          
                         TextField code = new TextField();
                         TextFormatter tf = (new TextFormatter<>(change -> {
                                    if (change.getText().equals(" ")) 
                                    {
                                        change.setText("");
                                    }

                                    else if(code.getText().length() > 6)
                                    {
                                        change.setText("");
                                    }
                                    return change;
                                }));
                         code.setTextFormatter(tf);
                         vgrid.add(code, 1, 2);
                         Button check = new Button("Check!");
                         check.setOnAction(new EventHandler<ActionEvent>() {
                                 @Override
                            public void handle(ActionEvent event) {
                              //Check if code provided and code generated match
                              //server.checkCode(emailString, Integer.valueOf(code.getText())))
                              if(server.checkCode(emailString, Integer.valueOf(code.getText())))
                              {
                                timeline.stop();
                                vgrid.getChildren().clear();

                                Text pBoxLabel = new Text("Username:");
                                ChoiceBox pBox = new ChoiceBox();
                                pBox.setMinWidth(170);
                                ArrayList<String> usernameList = server.getAllUsernames(emailString);
                                for(String u : usernameList)
                                {
                                  pBox.getItems().add(u);
                                }

                                Text enterPass = new Text("Change your password: ");
                                Text subPass = new Text("\n \n \nRemember to keep it strong! ⚔");
                                StackPane temp = new StackPane();
                                enterPass.setFill(Color.DARKCYAN);
                                enterPass.setFont(new Font("Helvetica", 26));
                                subPass.setFont(new Font("Helvetica", 14));
                                temp.getChildren().add(enterPass);
                                temp.getChildren().add(subPass);
                                bGPane.setTop(temp);

                                vgrid.add(new Text("Choose user:"), 0, 2);
                                vgrid.add(pBox, 1, 2);

                                PasswordField passwordTF = new PasswordField();
                                PasswordField rPasswordTF = new PasswordField();
                                ProgressBar pb = new ProgressBar(0);
                                pb.setStyle("-fx-accent: red;");
                                Text percentage = new Text("\n\n\nWeak (" + pb.getProgress() * 100 +"%)");
                                percentage.setFill(Color.RED);
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
  
                                Text passwordLabel = new Text("New password:");

                                TextFormatter tf = (new TextFormatter<>(change -> {
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
                                vgrid.add(passwordLabel, 0, 3); 
                                vgrid.add(passwordTF, 1, 3);

                                Text rPasswordLabel = new Text("Repeat password:");
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
                                vgrid.add(rPasswordLabel, 0, 4); 
                                vgrid.add(rPasswordTF, 1, 4);

                                Button changePassword = new Button("Change!");
                                changePassword.setOnAction(new EventHandler<ActionEvent>() {
                                  @Override
                                  public void handle(ActionEvent event) {

                                    if(checkInput())
                                    {
                                      User usr = new User(pBox.getValue().toString(), rPasswordTF.getText());
                                      if(server.changePassword(usr))
                                      {
                                        dialog.close();
                                        Notifications.create()
                                        .title("Password changed succesfully!")
                                        .text("You're good to go.")
                                        .graphic(new ImageView(new Image(new File("../img/key1.png").toURI().toString())))
                                        .position(Pos.TOP_RIGHT)
                                        .show(); 
                                      }
                                      else
                                      {
                                        Notifications.create()
                                        .title("Password change unsuccesful.")
                                        .text("We have encountered an error on our end. Please try again.")
                                        .graphic(new ImageView(new Image(new File("../img/error.png").toURI().toString())))
                                        .position(Pos.TOP_RIGHT)
                                        .show();
                                      }
                                    }

                                  }

                                  public boolean checkInput()
                                  {
                                    pBox.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
                                    passwordTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
                                    rPasswordTF.setStyle("-fx-border-color: green ; -fx-border-width: 2px ;");
                                    boolean errorFound = false;
                                    if(pBox.getValue() == null)
                                    {
                                      Text t = new Text("You must choose a username!");
                                      t.setFont(Font.font ("Verdana", 8));
                                      t.setFill(Color.RED);
                                      for(Node node : vgrid.getChildren()) 
                                        {
                                            if(vgrid.getRowIndex(node) == 2 && vgrid.getColumnIndex(node) == 2) 
                                            {
                                                vgrid.getChildren().remove(node);
                                                break;
                                            }
                                        }
                                      vgrid.add(t, 2, 2);
                                      pBox.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                                      errorFound = true; 
                                    }
                                    else
                                     {
                                        for(Node node : vgrid.getChildren()) 
                                        {
                                            if(vgrid.getRowIndex(node) == 2 && vgrid.getColumnIndex(node) == 2) 
                                            {
                                                vgrid.getChildren().remove(node);
                                                break;
                                            }
                                        }
                                     }

                                    if(passwordTF.getText().equals(""))
                                    {

                                      Text t = new Text("Password cannot be empty!");
                                      t.setFont(Font.font ("Verdana", 8));
                                      t.setFill(Color.RED);
                                      for(Node node : vgrid.getChildren()) 
                                        {
                                            if(vgrid.getRowIndex(node) == 3 && vgrid.getColumnIndex(node) == 2) 
                                            {
                                                vgrid.getChildren().remove(node);
                                                break;
                                            }
                                        }
                                      vgrid.add(t, 2, 3);
                                      passwordTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                                      errorFound = true; 
                                    }
                                    else
                                     {
                                        for(Node node : vgrid.getChildren()) 
                                        {
                                            if(vgrid.getRowIndex(node) == 3 && vgrid.getColumnIndex(node) == 2) 
                                            {
                                                vgrid.getChildren().remove(node);
                                                break;
                                            }
                                        }
                                     }
                                     if(passwordTF.getText().length() < 4)
                                    {

                                      Text t = new Text("No weak passwords!");
                                      t.setFont(Font.font ("Verdana", 8));
                                      t.setFill(Color.RED);
                                      for(Node node : vgrid.getChildren()) 
                                        {
                                            if(vgrid.getRowIndex(node) == 3 && vgrid.getColumnIndex(node) == 2) 
                                            {
                                                vgrid.getChildren().remove(node);
                                                break;
                                            }
                                        }
                                      vgrid.add(t, 2, 3);
                                      passwordTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
                                      errorFound = true; 
                                    }
                                    else
                                     {
                                        for(Node node : vgrid.getChildren()) 
                                        {
                                            if(vgrid.getRowIndex(node) == 3 && vgrid.getColumnIndex(node) == 2) 
                                            {
                                                vgrid.getChildren().remove(node);
                                                break;
                                            }
                                        }
                                     }
                                    if(!passwordTF.getText().equals(rPasswordTF.getText()))
                                    {
                                      Text t = new Text("Passwords must match!");
                                      t.setFont(Font.font ("Verdana", 8));
                                      t.setFill(Color.RED);
                                      for(Node node : vgrid.getChildren()) 
                                        {
                                            if(vgrid.getRowIndex(node) == 4 && vgrid.getColumnIndex(node) == 2) 
                                            {
                                                vgrid.getChildren().remove(node);
                                                break;
                                            }
                                      }

                                      vgrid.add(t, 2, 4);
                                      rPasswordTF.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");     
                                      errorFound = true;
                                    }
                                    else
                                     {
                                        for(Node node : vgrid.getChildren()) 
                                        {
                                            if(vgrid.getRowIndex(node) == 4 && vgrid.getColumnIndex(node) == 2) 
                                            {
                                                vgrid.getChildren().remove(node);
                                                break;
                                            }
                                        }
                                     }

                                    if(errorFound)
                                    {
                                      return false;
                                    }

                                    return true;
                                  }
                                });
                                vgrid.add(changePassword, 2, 5);

                              }
                              else
                              {
                                for(Node node : vgrid.getChildren()) 
                                {
                                  if(vgrid.getRowIndex(node) == 3 && vgrid.getColumnIndex(node) == 1) 
                                  {
                                    vgrid.getChildren().remove(node);
                                    break;
                                  }
                                }
                                Text errorText = new Text("Codes don't match!");
                                errorText.setFont(Font.font ("Arial", 10));
                                errorText.setFill(Color.RED);
                                vgrid.add(errorText, 1, 3);
                              }
                              //If they do, show three fields that will allow the user to
                              // Select the username they want the change for
                              // Type a new password --> get this code from Registrator.java
                              // Retype that password (and all similar checks)
                              // Submit the answer once correct, thus going through the txt file and changing that information
                              // DECIDE WHAT TO DO WITH DUPLICATE EMAILS (either disallow them or make the users confirm email)
                              //If the code does not match:
                              //throw error
                            }

                         });

                         vgrid.add(check, 2, 2);
                       }
                      else
                      {
                        for(Node node : vgrid.getChildren()) 
                        {
                          if(vgrid.getRowIndex(node) == 3 && vgrid.getColumnIndex(node) == 1) 
                          {
                            vgrid.getChildren().remove(node);
                            break;
                          }
                        }
                        Text errorText = new Text("This email doesn't belong to a user");
                        errorText.setFont(Font.font ("Arial", 10));
                        errorText.setFill(Color.RED);
                        vgrid.add(errorText, 1, 3 );
                      }
                       
                    }
                });
                vgrid.add(submit, 2, 2);
                Scene dialogScene = new Scene(bGPane, 520, 240); 
                dialog.setScene(dialogScene);
                dialog.show();
            }
         });

      Text txt = new Text("Don't have an account?");  
      Button register = new Button("Register!");
      bGrid.add(txt, 1, 0);
   		bGrid.add(register, 2, 0);

      Text txt1 = new Text("Otherwise:");

      bGrid.add(txt1, 3, 0);
      bGrid.add(login, 4, 0);

      register.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
               Registrator register = new Registrator(server);
            }
        });
      login.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                User usr = new User(usernameTF.getText(),passwordTF.getText());
                if(server.requestedLogin(usr))
                {
                  Client.assignUser(usr);
                  if(keepLogged.isSelected())
                  {
                    usr.setLogged("../inp/logged.txt", true);
                  }
                  else
                  {
                    usr.setLogged("../inp/logged.txt", false);
                  }
                  stage.close();
                }
                else
                {
                  for(Node node : grid.getChildren()) 
                  {
                    if(grid.getRowIndex(node) == 4 && grid.getColumnIndex(node) == 1) 
                    {
                      grid.getChildren().remove(node);
                      break;
                    }
                  }
                  Text t = new Text("Username or password don't exist!");
                  t.setFont(Font.font ("Verdana", 8));
                  t.setFill(Color.RED);
                  grid.add(t,1, 4);
                }
                
            }
        });


	}

	public void fillCenterGrid(GridPane grid)
	{
		grid.setHgap(10);
    	grid.setVgap(10);
    	grid.setPadding(new Insets(0, 10, 0, 10));
    	grid.setAlignment(Pos.CENTER);

    	Text usernameLabel = new Text("Username:");
    	usernameTF = new TextField();

      TextFormatter tf = (new TextFormatter<>(change -> {
          if (change.getText().equals(" ")) 
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
    	

    	Text passwordLabel = new Text("Password:");
    	passwordTF = new PasswordField();
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
    	

      keepLogged = new CheckBox("Remember my credentials");
      


      if(!User.getLogged("../inp/logged.txt").equals(""))
      {
        usernameTF.setText(User.getLogged("../inp/logged.txt").split(" ")[0]);
        passwordTF.setText(User.getLogged("../inp/logged.txt").split(" ")[1]);
        keepLogged.setSelected(true);
      }

      grid.add(usernameTF, 1, 1);
      grid.add(passwordTF, 1, 2);
      grid.add(keepLogged, 1, 3);

      // Check if username and password are empty, if not -> logIn()
      //logIn(usernameTF.getText(), passwordTF.getText());

      /*

    	Text pBoxLabel = new Text("Project:");
    	ChoiceBox pBox = new ChoiceBox();
    	pBox.getItems().addAll("Game 1", "Game 2", "Unknown");
    	pBox.setValue("Unknown");

    	grid.add(pBoxLabel, 0, 5);
    	grid.add(pBox, 1, 5);		

      */
	}
}