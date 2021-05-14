package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import sample.Vecc2f;

import java.util.Random;

public class Debris {


    Vecc2f position;
    Random random = new Random();
    ImageView debris;

    public Debris() {
        this.position = new Vecc2f((float) (135 + (random.nextInt(1574 ))), (float) (120 + (random.nextInt(764))));

        this.debris = new ImageView("file:src\\game\\background\\debris\\tile0" + random.nextInt(60) + ".png");

        //this.debris.setRotate(random.nextInt(4) * 90);
        this.debris.setOpacity(0.8 + (random.nextFloat() / 5));
    }

    public void load(Group group) {
        group.getChildren().add(this.debris);
        this.debris.setViewOrder(-3);
        this.debris.relocate(position.x, position.y);
    }

    public void unload(Group group) {
        group.getChildren().remove(this.debris);
    }

}
