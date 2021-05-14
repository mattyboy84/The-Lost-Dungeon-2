package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import sample.Vecc2f;

import java.util.Random;

public class Blood {

    Vecc2f position;
    Vecc2f diff;
    Random random = new Random();
    ImageView bloodImage;
    String bloodSize;
    int smallint = 11;
    int mediumint = 5;
    int largeint = 5;

    public Blood(Vecc2f position, ImageView enemyImage, Group group, String bloodSize) {
        this.bloodSize = bloodSize;
        int use = 0;
        switch (bloodSize) {
            case "small":
                use = smallint;
                break;
            case "medium":
                use = mediumint;
                break;
            case "large":
                use = largeint;
                break;
        }
        this.bloodImage = new ImageView("file:src\\game\\background\\blood\\" + this.bloodSize + "\\tile0" + random.nextInt(use) + ".png");
        this.position=new Vecc2f(position.x,position.y);

        this.position.add(random.nextInt((int) (enemyImage.getBoundsInParent().getWidth()-this.bloodImage.getBoundsInParent().getWidth())),random.nextInt((int) (enemyImage.getBoundsInParent().getHeight()-this.bloodImage.getBoundsInParent().getHeight())));

        group.getChildren().add(this.bloodImage);
        this.bloodImage.relocate(this.position.x,this.position.y);
        this.bloodImage.setRotate(random.nextInt(4)*90);
        this.bloodImage.setOpacity(0.4 + (random.nextFloat() / 5));

    }

    public Blood(Vecc2f position, ImageView enemyImage, Group group, String bloodSize,String c) {
        this.bloodSize = bloodSize;
        int use = 0;
        switch (bloodSize) {
            case "small":
                use = smallint;
                break;
            case "medium":
                use = mediumint;
                break;
            case "large":
                use = largeint;
                break;
        }
        this.bloodImage = new ImageView("file:src\\game\\background\\blood\\" + this.bloodSize + "\\tile0" + random.nextInt(use) + ".png");
        this.position=new Vecc2f((float) (position.x+(enemyImage.getBoundsInParent().getWidth()/2)),(float) (position.y+(enemyImage.getBoundsInParent().getHeight()/2)));

        //this.position.add(random.nextInt((int) (enemyImage.getBoundsInParent().getWidth()-this.bloodImage.getBoundsInParent().getWidth())),random.nextInt((int) (enemyImage.getBoundsInParent().getHeight()-this.bloodImage.getBoundsInParent().getHeight())));

        group.getChildren().add(this.bloodImage);
        this.bloodImage.relocate(this.position.x,this.position.y);
        this.bloodImage.setRotate(random.nextInt(4)*90);
        this.bloodImage.setOpacity(0.2 + (random.nextFloat() / 5));

    }

    public void load(Group group) {
        group.getChildren().add(this.bloodImage);
        this.bloodImage.setViewOrder(-2);
    }

    public void unload(Group group) {
        group.getChildren().remove(this.bloodImage);
    }
}
