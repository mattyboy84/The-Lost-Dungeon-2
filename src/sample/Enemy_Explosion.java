package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.util.ArrayList;

public class Enemy_Explosion {

    ImageView enemy_Explosion;
    Vecc2f position;
    String name;
    int temp;
    Timeline timeline;
    int timelineCounter;
    int arraypos;

    public Enemy_Explosion(String name, Vecc2f position, ArrayList<Enemy_Explosion> enemy_explosions, Group group) {
        this.arraypos = enemy_explosions.size();
        this.temp = 0;
        this.position = position;
        this.name = name;
        this.enemy_Explosion = new ImageView("file:src\\game\\enemies\\" + this.name + "\\death\\tile0" + this.temp + ".png");

        this.enemy_Explosion.relocate(this.position.x, this.position.y);
        group.getChildren().add(this.enemy_Explosion);

        this.timeline = new Timeline((new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            timelineCounter++;
            if (timelineCounter % 2 == 0) {
                this.temp++;
                this.enemy_Explosion.setImage(new Image("file:src\\game\\enemies\\" + this.name + "\\death\\tile0" + this.temp + ".png"));
            }
            if (this.temp == 12) {
                group.getChildren().remove(this.enemy_Explosion);
                enemy_explosions.remove(this.arraypos);
                for (Enemy_Explosion enemy_explosion : enemy_explosions) {
                    if (enemy_explosion.getArraypos() > this.arraypos) {
                        enemy_explosion.setArraypos(enemy_explosion.getArraypos() - 1);
                    }
                }
                this.timeline.stop();
            }
        })));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    public ImageView getEnemy_Explosion() {
        return enemy_Explosion;
    }

    public void setEnemy_Explosion(ImageView enemy_Explosion) {
        this.enemy_Explosion = enemy_Explosion;
    }

    public Vecc2f getPosition() {
        return position;
    }

    public void setPosition(Vecc2f position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public int getTimelineCounter() {
        return timelineCounter;
    }

    public void setTimelineCounter(int timelineCounter) {
        this.timelineCounter = timelineCounter;
    }

    public int getArraypos() {
        return arraypos;
    }

    public void setArraypos(int arraypos) {
        this.arraypos = arraypos;
    }
}
