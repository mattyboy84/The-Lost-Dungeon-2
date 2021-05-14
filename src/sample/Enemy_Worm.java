package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.NodeOrientation;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Enemy_Worm extends Enemy_BaseClass {

    String state;
    int idleCounter;
    int idleCooldown = 240;
    int chargeCounter = 0;
    int chargeCooldown = 120;
    String directionStr;
    int directionInt;
    int imageDelta;


    public Enemy_Worm(ArrayList<Enemy_Worm> enemy_worms, Hero hero, String enemy, Enemy enemies) {
        this.arrayPos = enemy_worms.size();
        this.viewOrder = -7;
        this.name = enemy;
        //System.out.println(this.name);
        this.enemyHitbox = new Rectangle(69, 66);
        this.damage = 1;
        this.xdelta = 15;
        this.ydelta = 15;
        this.maxHealth = 11;
        this.health = this.maxHealth;
        this.hasGuts = true;
        this.bloodSize = "small";
        this.state = "idle";
        //
        this.enemyHitbox.setVisible(false);
        //
        enemyImagesSetup();
        //
        this.enemyImage = new ImageView(enemyImages[this.enemyImagesPos]);
        this.position = new Vecc2f(350 + (rand.nextInt(1220)), 325 + (rand.nextInt(430)));
        this.velocity = new Vecc2f();
        this.acceleration = new Vecc2f();
        //
        this.startPos = new Vecc2f(this.position);
        //
        //If an enemy can 'see'
        eye = new Eye((this.position.x + (this.enemyImage.getBoundsInParent().getWidth() / 2)), (this.position.y + (this.enemyImage.getBoundsInParent().getHeight() / 2)), 4, hero);
        //
        worm(hero);
    }

    private void worm(Hero hero) {
        pushBack = new Vecc2f();
        this.timeline = new Timeline((new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            switch (state) {
                case "idle":
                    idle();
                    break;
                case "charge":
                    charge();
                    break;
            }
            //Eye mover
            eye.relocate((int) (this.position.x + (this.enemyImage.getBoundsInParent().getWidth() / 2)), (int) (this.position.y + (this.enemyImage.getBoundsInParent().getHeight() / 2)), hero);
            //Hero collision
            collisionChecker(hero);
            //orientation decides left from right
            orientation(velocity);
            //
            bordorcheck();
            //
        })));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void idle() {
        idleCounter++;
        pushBack.random2D(25 + rand.nextInt(10));
        pushBack.limit((float) 0.5);
        //
        this.acceleration.add(pushBack);

        this.acceleration.limit((float) 1.5);
        //
        this.velocity.add(this.acceleration);
        this.acceleration.mult((float) 0.85);//'friction'
        //
        this.position.add(velocity);
        //
        this.velocity.limit(1);
        directionInt = (int) velocity.toAngle();
        directionStr = direction(directionInt);

        if (idleCounter % 90 == 0) {
            idleImageChanger();
        }
        //
        this.enemyImage.relocate(position.x, position.y);
        this.enemyHitbox.relocate(position.x + xdelta, position.y + ydelta);


        if (idleCounter > idleCooldown) {
            //System.out.println("hi1");
            for (int i = 0; i < eye.rays.size(); i++) {
                if (eye.rays.get(i).hitSomething) {
                    System.out.println("target found");
                    idleCounter = 0;
                    state = "charge";
                    acceleration.set(0, 0);
                    pushBack.set(0, 0);
                    velocity.set(eye.rays.get(i).direction);
                    velocity.mult((float) 4.5);
                    directionInt = (int) velocity.toAngle();
                    directionStr = direction(directionInt);
                    System.out.println(directionInt + "  " + directionStr);
                }
            }
        }
    }

    private void idleImageChanger() {
        imageDelta++;
        if (imageDelta >= 4) {//this is 4 because there are 4 images in each direction
            imageDelta = 0;//imageDelta is the ra
        }
        switch (directionStr) {
            case "up":
                enemyImage.setImage(enemyImages[4 + imageDelta]);
                break;
            case "down":
                enemyImage.setImage(enemyImages[8 + imageDelta]);
                break;
            case "left":
            case "right":
                enemyImage.setImage(enemyImages[imageDelta]);
                break;

        }
    }

    private String direction(int directionInt) {
        String dir = "";

        if (directionInt >= 315 || directionInt <= 45) {
            dir = "up";
        } else if (directionInt >= 45 && directionInt <= 135) {
            dir = "right";

        } else if (directionInt >= 135 && directionInt <= 225) {
            dir = "down";
        } else if (directionInt >= 225 && directionInt <= 315) {
            dir = "left";
        }
        return dir;
    }

    private void orientation(Vecc2f velocity) {
        if (velocity.x > 0) {
            this.enemyImage.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
        } else {
            this.enemyImage.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
        }
    }

    private void charge() {
        chargeCounter++;
        this.acceleration.add(pushBack);
        this.acceleration.limit((float) 1.5);
        //
        this.velocity.add(this.acceleration);
        this.acceleration.mult((float) 0.85);
        //
        this.position.add(velocity);
        //
        this.velocity.setMag((float) 4.5);
        //
        this.enemyImage.relocate(position.x, position.y);
        this.enemyHitbox.relocate(position.x + xdelta, position.y + ydelta);
        //
        directionInt = (int) velocity.toAngle();
        directionStr = direction(directionInt);
        chargeImageChanger();
        //
        if (chargeCounter > chargeCooldown) {
            chargeCounter = 0;
            state = "idle";
            velocity.set(0, 0);
            acceleration.set(0, 0);
            pushBack.set(0, 0);
            idleImageChanger();
        }
    }

    private void chargeImageChanger() {
        switch (directionStr) {
            case "up":
                enemyImage.setImage(enemyImages[14]);
                break;
            case "down":
                enemyImage.setImage(enemyImages[12]);
                break;
            case "left":
            case "right":
                enemyImage.setImage(enemyImages[13]);
                break;
        }
    }

    public void setHealth(int health, Group group, ArrayList<Enemy_Worm> enemy_worms, Vecc2f direction, ArrayList<Enemy_Explosion> enemy_explosions, Background_Items background_items, Room room) {
        this.health = health;
        if (this.health < 0) {
            this.health = 0;
        }
        if (this.health == 0) {
            group.getChildren().removeAll(this.enemyImage, this.enemyHitbox);
            enemy_worms.remove(enemy_worms.get(this.arrayPos));
            this.timeline.stop();

            for (Enemy_Worm enemy : enemy_worms) {
                if (enemy.getArrayPos() > this.arrayPos) {
                    enemy.setArrayPos(enemy.getArrayPos() - 1);
                }
            }
            enemy_explosions.add(new Enemy_Explosion(this.name, this.position, enemy_explosions, group));
            //background_items.addBlood(this.position, this.enemyImage, group,this.bloodSize,xdelta,ydelta);
            if (this.hasGuts) {
                background_items.addGuts(this.position, this.enemyImage, group);
            }
            if (room.getEnemies().size() == 0) {//room is now clear of enemies
                room.openDoors();
            }
        }
        if (state.equals("idle")) {
            applyKnockback(direction);//base class
        }
    }
}
