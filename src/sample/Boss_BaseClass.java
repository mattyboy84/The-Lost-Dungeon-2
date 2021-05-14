package sample;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Boss_BaseClass {
    //
    int size;
    ImageView bossImage;
    Vecc2f position;
    Vecc2f velocity;
    Vecc2f acceleration;
    Timeline timeline;
    Random random = new Random();
    Circle hitbox;
    int health;
    int maxHealth;
    int r;
    int depth;
    //
    ImageView healthBarImg;
    Rectangle healthBarRec;
    Vecc2f healthBarImgPosition;
    Vecc2f healthBarRecPosition;
    int rectWidth = 658;
    int arrayPos;
    float magnitude;
    int parentPos;
    int damage;
    float rotate;
    //

    public void collisionChecker(Hero hero) {
        if ((hero.heroHeadHitbox.getBoundsInParent().intersects(this.hitbox.getBoundsInParent())) || (hero.heroBodyHitbox.getBoundsInParent().intersects(this.hitbox.getBoundsInParent()))) {
            if (hero.invulnerabilityCounter > hero.invulnerabilityCooldown) {
                //System.out.println("HIT OH NO STINKY");
                hero.hit(this.damage);
                hero.setInvulnerabilityCounter(0);
                Vecc2f steering = new Vecc2f();
                float d = hero.getLocation().distance(this.position);
                Vecc2f difference = new Vecc2f().sub(hero.getLocation(), this.position);
                //System.out.println(difference);
                hero.getVelocity().add(difference);
            }
        }
    }

    public void load(Group group) {
        group.getChildren().addAll(this.bossImage, this.hitbox, this.healthBarImg, this.healthBarRec);
        this.bossImage.relocate(position.x, position.y);
        this.healthBarImg.relocate(healthBarImgPosition.x, healthBarImgPosition.y);
        this.healthBarRec.relocate(healthBarRecPosition.x, healthBarRecPosition.y);
        this.bossImage.setViewOrder(-7);
        this.timeline.play();
    }

    public void unload(Group group) {
        group.getChildren().removeAll(this.bossImage, this.hitbox, this.healthBarRec, this.healthBarImg);
        this.timeline.pause();
    }

    public void applyKnockback(Vecc2f direction) {
        direction.limit((float) 0.15);
        this.acceleration.set(direction);
    }

    public void healthBarSetUp() {
        this.healthBarImg = new ImageView("file:src\\game\\Bosses\\healthBarImage.png");
        this.healthBarImgPosition = new Vecc2f((float) ((1920 / 2) - (this.healthBarImg.getBoundsInParent().getWidth() / 2)), (100));
        this.healthBarImg.setOpacity(0.5);
        this.healthBarImg.setViewOrder(-8);
        //
        this.healthBarRec = new Rectangle(healthBarImgPosition.x + 106, healthBarImgPosition.y + 37, rectWidth, 48);
        this.healthBarRecPosition = new Vecc2f((float) this.healthBarRec.getX(), (float) this.healthBarRec.getY());
        this.healthBarRec.setOpacity(0.6);
        this.healthBarRec.setFill(Color.RED);
        this.healthBarRec.setViewOrder(-8);
    }

    public void seperationSetter(Boss boss) {//
        Vecc2f seperation = boss.seperation(this.position, this.velocity,this.bossImage);
        //System.out.println("seperation " + seperation);

        this.acceleration.add(seperation);

        //System.out.println("new Acc is " + this.acceleration);
        //System.out.println("---------------------------------------");
    }

    public void bordorChecker() {//
        if ((position.y < 0 || (position.y + (hitbox.getBoundsInParent().getHeight())) > 1080)) {
            this.velocity.y = (this.velocity.y * -1);
        }
        if ((position.x) < 0 || (position.x + (hitbox.getBoundsInParent().getWidth())) > 1920) {
            this.velocity.x = (this.velocity.x * -1);
        }
        if ((position.y) < 0) {
            this.acceleration.add(0, (float) 1);
        }
        if ((position.y + (hitbox.getRadius() * 2)) > 1080) {
            this.acceleration.add(0, (float) -1);
        }
        if ((position.x + (hitbox.getRadius() * 2)) > 1920) {
            this.acceleration.add((float) -1, 0);
        }
        if ((position.x + (hitbox.getRadius() * 2)) < 0) {
            this.acceleration.add((float) 1, 0);
        }
    }

}
