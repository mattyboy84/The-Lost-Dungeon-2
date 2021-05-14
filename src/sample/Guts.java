package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import sample.Vecc2f;

import java.util.Random;

public class Guts {

    Vecc2f position;
    Vecc2f diff;
    Random random = new Random();
    ImageView gutImage;

    public Guts(Vecc2f position, ImageView enemyImage, Group group) {
        this.position=new Vecc2f(position.x,position.y);
        this.gutImage = new ImageView("file:src\\game\\background\\guts\\tile0" + random.nextInt(14) + ".png");

        this.position.add(random.nextInt((int) (enemyImage.getBoundsInParent().getWidth()-this.gutImage.getBoundsInParent().getWidth())),random.nextInt((int) (enemyImage.getBoundsInParent().getHeight()-this.gutImage.getBoundsInParent().getHeight())));

        group.getChildren().add(this.gutImage);
        this.gutImage.setRotate(random.nextInt(4) * 90);

        this.gutImage.setOpacity(0.4 + (random.nextFloat() / 5));

        this.gutImage.relocate(this.position.x, this.position.y);
    }


    public void load(Group group) {
        group.getChildren().add(this.gutImage);
        this.gutImage.setViewOrder(-2);
    }

    public void unload(Group group) {
        group.getChildren().remove(this.gutImage);
    }
}
