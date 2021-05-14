package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

public class Tear_Explosion {

    ImageView tear_Explosion;
    Vecc2f position;
    String colour;
    int temp;
    Timeline timeline;
    int timelineCounter = 0;
    int arrayPos;

    public Tear_Explosion(String colour, Vecc2f location, Group group, ArrayList<Tear_Explosion> tear_explosions) {
        this.arrayPos = tear_explosions.size();
        this.temp = 0;
        this.position = location;
        this.colour = colour;
        this.tear_Explosion = new ImageView("file:src\\game\\tears\\" + this.colour + " tear explosion\\tile0" + this.temp + ".png");

        this.tear_Explosion.relocate(this.position.x, this.position.y);
        group.getChildren().add(this.tear_Explosion);
        this.tear_Explosion.setViewOrder(-2);

        this.timeline = new Timeline((new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            timelineCounter++;
            if (timelineCounter % 2 == 0) {
                this.temp++;
                this.tear_Explosion.setImage(new Image("file:src\\game\\tears\\" + this.colour + " tear explosion\\tile0" + this.temp + ".png"));
            }
            if (this.temp == 15) {
                group.getChildren().remove(this.tear_Explosion);
                tear_explosions.remove(this.arrayPos);
                for (Tear_Explosion tear_explosion : tear_explosions) {
                    if (tear_explosion.getArrayPos() > this.arrayPos) {
                        tear_explosion.setArrayPos(tear_explosion.getArrayPos() - 1);
                    }
                }
                this.timeline.stop();
            }
        })));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    public Vecc2f getPosition() {
        return position;
    }

    public void setPosition(Vecc2f position) {
        this.position = position;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public int getArrayPos() {
        return arrayPos;
    }

    public void setArrayPos(int arrayPos) {
        this.arrayPos = arrayPos;
    }
}
