package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Boss_Fistula extends  Boss_BaseClass{
    //"file:src\\game\\Bosses\\gurdyjr\\boss_021_gurdyjr.png"

    public Boss_Fistula(ArrayList<Boss_Fistula> boss_fistula, int size, int i, int j, int depth, Boss bossClass, Hero hero) {
        if (size > 0) {
            this.damage = 1;
            this.arrayPos = boss_fistula.size();
            this.rotate = random.nextFloat();
            this.size = size;
            this.depth = depth;
            healthBarSetUp();
            this.maxHealth = Math.round(120 / depth);
            this.health = maxHealth;
            this.bossImage = new ImageView("file:src\\game\\Bosses\\fistula\\fistula_" + size + ".png");
            if (size == 4) {
                this.position = new Vecc2f((float) (i - (this.bossImage.getBoundsInParent().getWidth() / 2)), (float) (j - (this.bossImage.getBoundsInParent().getHeight() / 2)));
            } else {
                this.position = new Vecc2f(i, j);
            }
            this.velocity = new Vecc2f(((7 + (3 * random.nextFloat())) * ((random.nextInt(2) * 2) - 1)), ((7 + (3 * random.nextFloat())) * ((random.nextInt(2) * 2) - 1)));
            this.magnitude = this.velocity.magnitude();
            this.acceleration = new Vecc2f(0, 0);
            this.bossImage.setViewOrder(-7);
            this.bossImage.relocate(position.x, position.y);
            //
            this.r = (int) (((this.bossImage.getBoundsInParent().getWidth() / 2) + (this.bossImage.getBoundsInParent().getHeight() / 2)) / 2);
            this.hitbox = new Circle(this.r);
            this.hitbox.setVisible(false);
            //
            this.timeline = new Timeline(new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
                this.bossImage.setRotate(this.bossImage.getRotate() + this.rotate);
                //System.out.println(this.velocity);
                this.velocity.setMag(this.magnitude);
                this.velocity.add(this.acceleration);
                this.acceleration.mult((float) 0.85);
                this.velocity.limit(4);
                //
                if (this.acceleration.magnitude() < 0.1) {
                    this.acceleration.set(0, 0);
                }
                //

                //
                collisionChecker(hero);
                //
                this.position.add(this.velocity);
                //
                this.bossImage.relocate(this.position.x, this.position.y);
                this.hitbox.setCenterX(this.position.x + this.r);
                this.hitbox.setCenterY(this.position.y + this.r);
                //
                bordorChecker();
                //
                seperationSetter(bossClass);

            }));
            this.timeline.setCycleCount(Timeline.INDEFINITE);
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health, Vecc2f direction, ArrayList<Boss_Fistula> boss_fistula, Group group, Room room, Hero hero) {
        this.health = health;
        if (this.health < 0) {
            this.health = 0;
        }
        //updates the health bar
        for (Boss_Fistula bossFistula : boss_fistula) {
            try {
                group.getChildren().remove(bossFistula.getHealthBarRec());
            } catch (Exception e) {
            }
            try {
                group.getChildren().remove(bossFistula.getHealthBarImg());
            } catch (Exception e) {
            }
        }
        group.getChildren().addAll(this.healthBarImg, this.healthBarRec);
        this.healthBarImg.relocate(this.healthBarImgPosition.x, this.healthBarImgPosition.y);
        this.healthBarRec.relocate(this.healthBarRecPosition.x, this.healthBarRecPosition.y);
        //
        this.healthBarRec.setViewOrder(-8);
        this.healthBarImg.setViewOrder(-8);
        //
        this.healthBarRec.setWidth((int) (this.rectWidth * ((float) this.health / this.maxHealth)));
        //System.out.println(this.size);
        if (this.health == 0) {
            //
            for (int i = 0; i < this.size + random.nextInt(2); i++) {//blood and guts background items
                room.background_items.addBlood(this.position, this.bossImage, group, "medium");
                room.background_items.addGuts(this.position, this.bossImage, group);
            }
            //
            this.timeline.stop();
            group.getChildren().removeAll(this.bossImage, this.hitbox, this.healthBarImg, this.healthBarRec);
            boss_fistula.remove(boss_fistula.get(this.arrayPos));
            for (Boss_Fistula boss_fistula1 : boss_fistula) {
                if (boss_fistula1.getArrayPos() > this.arrayPos) {
                    boss_fistula1.setArrayPos(boss_fistula1.getArrayPos() - 1);
                }
            }
            int children;
            if (this.size == 4) {
                children = 4;
            } else {
                children = 2;
            }
            if (this.size > 2) {
                for (int i = 0; i < children; i++) {
                    //System.out.println("asd");
                    boss_fistula.add(new Boss_Fistula(boss_fistula, this.size - 1, (int) position.x, (int) position.y, this.depth + 1, room.boss, hero));
                    boss_fistula.get(boss_fistula.size() - 1).getTimeline().play();
                    group.getChildren().addAll(boss_fistula.get(boss_fistula.size() - 1).getFistula(), boss_fistula.get(boss_fistula.size() - 1).getHitbox());
                }
            }
        }
        //System.out.println(boss_fistula.size());
        //TODO when all bosses are defeated clear the boss class in rooms.
        //
        if (room.boss.size() == 0) {
            //
            System.out.println("Victory");
            room.openDoors();
            System.out.println("Doors open");
        }
        //
        applyKnockback(direction);
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public ImageView getFistula() {
        return bossImage;
    }

    public void setFistula(ImageView fistula) {
        this.bossImage = fistula;
    }

    public int getArrayPos() {
        return arrayPos;
    }

    public void setArrayPos(int arrayPos) {
        this.arrayPos = arrayPos;
    }

    public ImageView getHealthBarImg() {
        return healthBarImg;
    }

    public void setHealthBarImg(ImageView healthBarImg) {
        this.healthBarImg = healthBarImg;
    }

    public Rectangle getHealthBarRec() {
        return healthBarRec;
    }

    public void setHealthBarRec(Rectangle healthBarRec) {
        this.healthBarRec = healthBarRec;
    }

    public Vecc2f getHealthBarImgPosition() {
        return healthBarImgPosition;
    }

    public void setHealthBarImgPosition(Vecc2f healthBarImgPosition) {
        this.healthBarImgPosition = healthBarImgPosition;
    }

    public Vecc2f getHealthBarRecPosition() {
        return healthBarRecPosition;
    }

    public void setHealthBarRecPosition(Vecc2f healthBarRecPosition) {
        this.healthBarRecPosition = healthBarRecPosition;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public int getParentPos() {
        return parentPos;
    }

    public void setParentPos(int parentPos) {
        this.parentPos = parentPos;
    }

    public float getRotate() {
        return rotate;
    }

    public void setRotate(float rotate) {
        this.rotate = rotate;
    }

    public int getRectWidth() {
        return rectWidth;
    }

    public void setRectWidth(int rectWidth) {
        this.rectWidth = rectWidth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
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

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public Random getRandom() {
        return random;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    public Circle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Circle hitbox) {
        this.hitbox = hitbox;
    }

    public int getR() {
        return r;
    }

    public void setR(int r) {
        this.r = r;
    }
}
