package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import sample.Vecc2f;

import java.util.Random;

public class Butterfly {
    Vecc2f startLocation;
    Vecc2f position;
    Vecc2f velocity = new Vecc2f();
    Vecc2f acceleration = new Vecc2f((float) 0, (float) 0);
    Timeline timeline;
    Random random = new Random();
    ImageView butterfly;
    int counter;
    int image;

    public Butterfly() {
        this.image = random.nextInt(2);
        this.butterfly = new ImageView("file:src\\game\\background\\butterfly\\butterfly" + image + ".png");
        this.startLocation = new Vecc2f((200 + random.nextInt(1920 - 400)), (200 + random.nextInt(1080 - 400)));
        this.position = new Vecc2f(this.startLocation.x, this.startLocation.y);
        this.counter = 0;
        int FPS = 60;
        this.timeline = new Timeline((new KeyFrame(Duration.seconds((float) 1 / FPS), event -> {

            this.butterfly.setImage(new Image("file:src\\game\\background\\butterfly\\butterfly" + image + ".png"));
            counter++;
            //
            this.acceleration.random2D(10);
            //
            this.velocity.add(acceleration);
            //
            this.position.add(velocity);
            //
            this.velocity.limit(1);
            //
            this.butterfly.relocate(position.x, position.y);
            //
            if (counter >= (45+(random.nextInt(16)))) {
                switch (image) {
                    case 0:
                        image = 1;
                        break;
                    case 1:
                        image = 0;
                        break;
                }
                counter = 0;
            }
            //boundary check
            if (this.position.y < (this.startLocation.y - 200) || (this.position.y > (this.startLocation.y + 200))) {
                this.velocity.y = (this.velocity.y * -1);
            }
            if (this.position.x < (this.startLocation.x - 200) || (this.position.x > (this.startLocation.x + 200))) {
                this.velocity.x = (this.velocity.x * -1);
            }
        })));
        this.timeline.setCycleCount(Timeline.INDEFINITE);

    }

    public void load(Group group) {
        //this.butterfly = new ImageView("file:E:\\Projects\\vectors\\src\\game\\butterfly" + image + ".png");
        group.getChildren().add(this.butterfly);
        this.butterfly.relocate(position.x, position.y);
        this.butterfly.setViewOrder(-2);
        this.timeline.play();
    }

    public void unload(Group group) {
        group.getChildren().remove(this.butterfly);
        this.timeline.pause();

    }
}