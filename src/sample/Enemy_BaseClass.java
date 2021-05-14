package sample;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.util.Random;

public class Enemy_BaseClass {
    int health;
    int maxHealth;
    int xdelta;
    int ydelta;
    int animationCounter;
    int viewOrder;
    int damage;
    int arrayPos;
    //
    ImageView enemyImage;
    //
    String name;
    //
    Vecc2f startPos;
    Vecc2f position;
    Vecc2f velocity;
    Vecc2f acceleration;
    Vecc2f pushBack;
    //
    Random rand = new Random();
    int enemyNamePos;
    Image[] enemyImages;
    int enemyImagesPos;
    Timeline timeline;
    Rectangle enemyHitbox;
    //
    boolean hasGuts;
    String bloodSize;
    //
    Eye eye;

    public void collisionChecker(Hero hero) {
        if ((hero.heroHeadHitbox.getBoundsInParent().intersects(this.enemyHitbox.getBoundsInParent())) || (hero.heroBodyHitbox.getBoundsInParent().intersects(this.enemyHitbox.getBoundsInParent()))) {
            if (hero.invulnerabilityCounter > hero.invulnerabilityCooldown) {
                //System.out.println("HIT OH NO STINKY");
                hero.hit(this.damage);
                hero.setInvulnerabilityCounter(0);
                Vecc2f steering = new Vecc2f();
                float d = hero.getLocation().distance(this.position);
                Vecc2f difference = new Vecc2f().sub(hero.getLocation(), this.position);
                System.out.println(difference);
                hero.getVelocity().add(difference);
            }
        }
    }


    public void load(Group group) {
        group.getChildren().addAll(this.enemyImage, this.enemyHitbox);
        this.enemyImage.relocate(this.position.x, this.position.y);
        this.enemyImage.setViewOrder(viewOrder);

        try {
            group.getChildren().add(this.eye.circle);
            for (int i = 0; i < this.eye.rays.size(); i++) {
                group.getChildren().add(this.eye.rays.get(i).line);
            }
        } catch (Exception e) {

        }
        this.timeline.play();
    }

    public void unload(Group group) {
        group.getChildren().removeAll(this.enemyImage, this.enemyHitbox);

        try {
            group.getChildren().remove(this.eye.circle);
            for (int i = 0; i < this.eye.rays.size(); i++) {
                group.getChildren().remove(this.eye.rays.get(i).line);
            }
        } catch (Exception e) {

        }

        this.timeline.pause();
    }

    public void applyKnockback(Vecc2f direction) {
        direction.mult(20);
        this.acceleration.add(direction);
    }

    public void enemyImagesSetup() {
        File directPath = new File("src\\game\\enemies\\" + this.name + "\\" + this.name);
        String[] contents = directPath.list();
        enemyImages = new Image[contents.length];
        for (int i = 0; i < contents.length; i++) {
            enemyImages[i] = new Image("file:src\\game\\enemies\\" + this.name + "\\" + this.name + "\\" + "tile0" + i + ".png");
        }
        this.enemyNamePos = 0;
        //
    }

    /*
    tile00.png
tile01.png
tile010.png
tile011.png
tile012.png
tile013.png
tile014.png
tile02.png
tile03.png
tile04.png
tile05.png
tile06.png
tile07.png
tile08.png
tile09.png

     */

    public void bordorcheck() {
        if ((position.y + ydelta) < 0 || (position.y + ydelta + enemyHitbox.getWidth()) > 1080) {
            this.velocity.y = (this.velocity.y * -1);
        }
        if ((position.x + ydelta) < 0 || (position.x + xdelta + enemyHitbox.getHeight()) > 1920) {
            this.velocity.x = (this.velocity.x * -1);
        }
        if ((position.y + ydelta) < 0) {
            this.acceleration.add(0, (float) 1);
        }
        if ((position.y + ydelta + enemyHitbox.getWidth()) > 1080) {
            this.acceleration.add(0, (float) -1);
        }
        if ((position.x + xdelta + enemyHitbox.getHeight()) > 1920) {
            this.acceleration.add((float) -1, 0);
        }
        if ((position.x + ydelta) < 0) {
            this.acceleration.add((float) 1, 0);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getXdelta() {
        return xdelta;
    }

    public void setXdelta(int xdelta) {
        this.xdelta = xdelta;
    }

    public int getYdelta() {
        return ydelta;
    }

    public void setYdelta(int ydelta) {
        this.ydelta = ydelta;
    }

    public int getAnimationCounter() {
        return animationCounter;
    }

    public void setAnimationCounter(int animationCounter) {
        this.animationCounter = animationCounter;
    }

    public int getViewOrder() {
        return viewOrder;
    }

    public void setViewOrder(int viewOrder) {
        this.viewOrder = viewOrder;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getArrayPos() {
        return arrayPos;
    }

    public void setArrayPos(int arrayPos) {
        this.arrayPos = arrayPos;
    }

    public ImageView getEnemyImage() {
        return enemyImage;
    }

    public void setEnemyImage(ImageView enemyImage) {
        this.enemyImage = enemyImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Vecc2f getStartPos() {
        return startPos;
    }

    public void setStartPos(Vecc2f startPos) {
        this.startPos = startPos;
    }

    public Vecc2f getPosition() {
        return position;
    }

    public void setPosition(Vecc2f position) {
        this.position = position;
    }

    public Vecc2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vecc2f velocity) {
        this.velocity = velocity;
    }

    public Vecc2f getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vecc2f acceleration) {
        this.acceleration = acceleration;
    }

    public Vecc2f getPushBack() {
        return pushBack;
    }

    public void setPushBack(Vecc2f pushBack) {
        this.pushBack = pushBack;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public int getEnemyNamePos() {
        return enemyNamePos;
    }

    public void setEnemyNamePos(int enemyNamePos) {
        this.enemyNamePos = enemyNamePos;
    }

    public Image[] getEnemyImages() {
        return enemyImages;
    }

    public void setEnemyImages(Image[] enemyImages) {
        this.enemyImages = enemyImages;
    }

    public int getEnemyImagesPos() {
        return enemyImagesPos;
    }

    public void setEnemyImagesPos(int enemyImagesPos) {
        this.enemyImagesPos = enemyImagesPos;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public Rectangle getEnemyHitbox() {
        return enemyHitbox;
    }

    public void setEnemyHitbox(Rectangle enemyHitbox) {
        this.enemyHitbox = enemyHitbox;
    }

    public boolean isHasGuts() {
        return hasGuts;
    }

    public void setHasGuts(boolean hasGuts) {
        this.hasGuts = hasGuts;
    }

    public String getBloodSize() {
        return bloodSize;
    }

    public void setBloodSize(String bloodSize) {
        this.bloodSize = bloodSize;
    }
}
