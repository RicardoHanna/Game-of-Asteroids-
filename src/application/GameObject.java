package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.Event;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GameObject{
private Hostile hostile;
    private Node view;
    public BooleanProperty gameOver,score;
    boolean mousePressed = false;
    
    

	private Point2D velocity = new Point2D(0, 0);
    KeyFrame keyFrame = new KeyFrame(Duration.millis(10),e->update());

    Timeline fanTimeline = new Timeline(keyFrame);

    private boolean alive = true;

    public GameObject(Node view) {
        this.view = view;
        this.fanTimeline=fanTimeline;
    this.gameOver = new SimpleBooleanProperty(false);
    this.score=new SimpleBooleanProperty(false);
        
    }
    
    


    public Timeline getFanTimeline() {
		return fanTimeline;
	}

	public void setFanTimeline(Timeline fanTimeline) {
		this.fanTimeline = fanTimeline;
	}

	public void update() {
		  
        view.setTranslateX(view.getTranslateX() + velocity.getX());
        view.setTranslateY(view.getTranslateY() + velocity.getY());
    
    }
	
	
   
	

	private void restart() {
		// TODO Auto-generated method stub
		
	}

	public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public Point2D getVelocity() {
        return velocity;
    }

    public Node getView() {
        return view;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDead() {
        return !alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public double getRotate() {
        return view.getRotate();
    }
//edit 90 to 5 !!!!
    public void rotateRight() {
        view.setRotate(view.getRotate() + 90);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
    }

    public void rotateLeft() {
        view.setRotate(view.getRotate() - 90);
        setVelocity(new Point2D(Math.cos(Math.toRadians(getRotate())), Math.sin(Math.toRadians(getRotate()))));
    }
    
public void increase() {
	fanTimeline.setRate(fanTimeline.getRate()+1);
}
public void decrease() {
	fanTimeline.setRate(fanTimeline.getRate()-1);
}
    public boolean isColliding(GameObject other) {
        return getView().getBoundsInParent().intersects(other.getView().getBoundsInParent());
    }


}