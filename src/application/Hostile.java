package application;

import javafx.animation.*;


import javafx.animation.AnimationTimer;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PathTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Labeled;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.sun.scenario.effect.InnerShadow;
import javafx.animation.*;
public class Hostile extends Application{
  private Pane pane;
  Label lblGameOver = new Label("GAME OVER :(");
  FadeTransition gameOverAnimation = new FadeTransition(Duration.millis(500));
  Text text = new Text(10, 20, "Points: 0");
  
  AtomicInteger points = new AtomicInteger();
  Label result=new Label();
    private List<GameObject> bullets = new ArrayList<>();
    private List<GameObject> enemies = new ArrayList<>();
	Button exit;
	Button restart;
	
    private GameObject player;
   
   VBox root=new VBox(10);
   
   
  
   
 
    private Parent createContent(Stage stage) {
        pane = new Pane();
  
       pane.getStylesheets().add(Hostile.class.getResource("application.css").toExternalForm());
       pane.setStyle("-fx-background-color:DarkBlue;");

      pane.setStyle("-fx-background-image: url('image/Space.jpg'); -fx-background-repeat: no-repeat; -fx-background-size: 1100 600; -fx-background-position: center center;");
        pane.setPrefSize(600, 600);
       
        pane.getChildren().add(text);
   
        restart = new Button("Restart");
        restart.setTranslateX(200);
        restart.setTranslateY(350);
        restart.setPrefSize(80,50);
        restart.setTextFill(Color.BLUE);
        restart.setOnAction( __ ->
        {
        	text.setText("Points: "+points.addAndGet(-1000));
          System.out.println( "Restarting app!" );
          stage.close();
          Platform.runLater( () -> {
			try {
				start( new Stage() );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} );
        } );
        
        exit = new Button("Exit");
        exit.setTranslateX(80);
        exit.setTranslateY(350);
        exit.setPrefSize(50,50);
        exit.setTextFill(Color.RED);
        exit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
       // pane.getChildren().addAll(start,exit);  
        player = new Player();
      
        player.setVelocity(new Point2D(1, 0));
        addGameObject(player, 300, 300);
      
        AnimationTimer timer = new AnimationTimer() {
            @Override
            
            public void handle(long now) {
                onUpdate();
               
            }
        };
        timer.start();

        return pane;
    }

    private void addBullet(GameObject bullet, double x, double y) {
        bullets.add(bullet);
        addGameObject(bullet, x, y);
    }

    private void addEnemy(GameObject enemy, double x, double y) {
        enemies.add(enemy);
        addGameObject(enemy, x, y);
    }

    private void addGameObject(GameObject object, double x, double y) {
        object.getView().setTranslateX(x);
        object.getView().setTranslateY(y);
        pane.getChildren().add(object.getView());
      }
   
    public void onUpdate() {
    
    	
        for (GameObject bullet : bullets) {
            for (GameObject enemy : enemies) {
                if (bullet.isColliding(enemy)) {
                    bullet.setAlive(false);
                    enemy.setAlive(false);

                    pane.getChildren().removeAll(bullet.getView(), enemy.getView());
                }
            }
            if(bullet.isDead()) {
            	 text.setText("Points: " + points.addAndGet(1000));
            }
        }
        
        for(GameObject enemy : enemies) {
        	if(player.isColliding(enemy)) {
                 	//lblGameOver.visibleProperty().bind(player.gameOver);
                 	player.gameOver.setValue(true);
                 	pane.getChildren().add(lblGameOver);
                 	result.setText("Your score: "+points.addAndGet(0));
                 	player.score.setValue(true);
                 
                 	pane.getChildren().add(result);
                 	pane.getChildren().addAll(exit);
                 	pane.getChildren().addAll(restart);
                 	
        	}
        	
        }
        
    
        	
        

        bullets.removeIf(GameObject::isDead);
        enemies.removeIf(GameObject::isDead);

        bullets.forEach(GameObject::update);
        enemies.forEach(GameObject::update);

        player.update();
       

        if (Math.random() < 0.01) {
            addEnemy(new Enemy(), Math.random() *pane.getPrefWidth(), Math.random() * pane.getPrefHeight());
        }

    }
    
    static    Image jetIcon = new Image("image/jet-plane.png", 40, 40, false, false);
    static   Image tankIcon = new Image("image/space-ship.png", 30, 30, false, false);
     //static  Image bulletIcon = new Image("image/sun.png", 15, 15, false, false);

  
    
    private static class Player extends GameObject {
        Player() {
           // super(new Rectangle(40, 20, Color.YELLOW));
            super(new ImageView(jetIcon));
        }
    }

    private static class Enemy extends GameObject {
        Enemy() {
            super(new ImageView(tankIcon));
        }
    }

    private static class Bullet extends GameObject {
        Bullet() {
            super(new Circle(5, 5, 5, Color.RED));
        }
    }
    
 /* void cleanup() {
      player.gameOver.setValue(false);
   player.score.setValue(false);
  // pane.getChildren().remove(exit);
   //pane.getChildren().remove(restart);
    }
    
    void startGame(Stage primaryStage) {
    	
    	 
       restart.setOnAction(e -> {
           restart(primaryStage);
        });
      
        primaryStage.show();
    }

    void restart(Stage primaryStage) {
        cleanup();
        startGame(primaryStage);
    }

    
    void displayApplication() {
        Platform.runLater(new Runnable() {
           @Override
           public void run() {
               try {
                   start(new Stage());
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       });
   }
    */
    @Override
    public void start(Stage stage) throws Exception {
    	 
    	   stage.setScene(new Scene(createContent(stage)));
       
    	 //  Platform.setImplicitExit(false);
        //startGame(stage);
    	  // scene.getStylesheets().add(Hostile.class.getResource("application.css").toExternalForm());
        text.setId("Points_Css");
        text.setX(10);
text.setY(30);
        lblGameOver.setTextFill(Color.RED);
        lblGameOver.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 60));
        lblGameOver.setLayoutX(50);
        lblGameOver.setLayoutY(230);
       lblGameOver.visibleProperty().bind(player.gameOver);
       
       result.setTextFill(Color.RED);
       result.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, FontPosture.ITALIC, 30));
       result.setLayoutX(50);
       result.setLayoutY(290);
      result.visibleProperty().bind(player.score);
       
    	  
        gameOverAnimation.setFromValue(0.003);
          gameOverAnimation.setToValue(0.1);
          gameOverAnimation.setCycleCount(-1);
          gameOverAnimation.setAutoReverse(true);
          gameOverAnimation.play();
          
       player.gameOver.addListener((vl, o, n) -> {
              if (n) {
                  pane.setOpacity(0.8);
              } else {
                  pane.setOpacity(1);
              }
          });
          
         /* button.setOnAction(e->{
             	restart(e);
          });*/
          
        
        stage.getScene().setOnKeyPressed(e -> {
        	
            if (e.getCode() == KeyCode.LEFT) {
                player.rotateLeft();
            } else if (e.getCode() == KeyCode.RIGHT) {
                player.rotateRight();
            } else if (e.getCode() == KeyCode.SPACE) {
                Bullet bullet = new Bullet();
                bullet.setVelocity(player.getVelocity().normalize().multiply(10));
                addBullet(bullet, player.getView().getTranslateX(), player.getView().getTranslateY());
                
            }
        });
    
        stage.show();
    }
    
 

    public static void main(String[] args) {
        launch(args);
    }
}
