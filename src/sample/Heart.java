package sample;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Heart {

    ImageView heart;
    Vecc2f location;
    String status;
    int viewOrder;
    int health;

    public Heart(Group group, ArrayList<Heart> hearts) {//original 3 health
        this.viewOrder = -20;
        this.health = 2;
        this.status = "full";
        this.heart = new ImageView("file:src\\game\\hearts\\" + status + " heart.png");

        heartPosition(hearts);

        group.getChildren().add(this.heart);
        this.heart.relocate(this.location.x, this.location.y);
        this.heart.setViewOrder(this.viewOrder);
        this.heart.setOpacity(0.7);
    }

    public Heart(Group group, ArrayList<Heart> hearts, int remainingHealth) {//additional health
        this.viewOrder = -20;
        this.health = remainingHealth;
        switch (remainingHealth) {
            case 0:
                this.status = "empty";
                break;
            case 1:
                this.status = "half";
                break;
            case 2:
                this.status = "full";
                break;
        }
        this.heart = new ImageView("file:src\\game\\hearts\\" + status + " heart.png");
        heartPosition(hearts);
        group.getChildren().add(this.heart);
        this.heart.relocate(this.location.x, this.location.y);
        this.heart.setViewOrder(this.viewOrder);
        this.heart.setOpacity(0.7);
    }


    private void heartPosition(ArrayList<Heart> hearts) {
        float x,y;
        try {
            x = (float) (hearts.get(hearts.size() - 1).location.x + this.heart.getBoundsInParent().getWidth());
            y = hearts.get(hearts.size() - 1).location.y;
        } catch (Exception e) {
            x = (float) (hearts.size() * this.heart.getBoundsInParent().getWidth());
            y=0;
        }
        while (x >= 1920) {
            x = x - 1920;
            y = (float) (y + this.heart.getBoundsInParent().getHeight());
        }
        this.location = new Vecc2f(x, y);
    }


    public void setHealth(int i) {
        this.health = i;
        switch (this.health) {
            case 2:
                this.status = "full";
                this.heart.setImage(new Image("file:src\\game\\hearts\\" + status + " heart.png"));
                break;
            case 1:
                this.status = "half";
                this.heart.setImage(new Image("file:src\\game\\hearts\\" + status + " heart.png"));
                break;
            case 0:
                this.status = "empty";
                this.heart.setImage(new Image("file:src\\game\\hearts\\" + status + " heart.png"));
                break;
        }
    }

    public int getHealth() {
        return health;
    }

    public ImageView getHeart() {
        return heart;
    }

    public void setHeart(ImageView heart) {
        this.heart = heart;
    }
}
