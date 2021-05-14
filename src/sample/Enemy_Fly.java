package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Enemy_Fly extends Enemy_BaseClass {


    public Enemy_Fly(ArrayList<Enemy_Fly> enemy_flies, Hero hero, String enemy, Enemy enemies) {
        this.arrayPos = enemy_flies.size();
        this.viewOrder = -7;
        this.name = enemy;
        //System.out.println(this.name);
        this.enemyHitbox = new Rectangle(57, 45);
        this.damage = 1;
        this.xdelta = 19;
        this.ydelta = 25;
        this.maxHealth = 5;
        this.health = this.maxHealth;
        this.hasGuts = true;
        this.bloodSize = "small";
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

        fly(hero,enemies);
    }

    private void fly(Hero hero, Enemy enemies) {
        pushBack = new Vecc2f();
        this.timeline = new Timeline((new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            this.animationCounter++;
            //
            pushBack.random2D(20 + rand.nextInt(10));
            pushBack.limit((float) 0.5);
            //
            this.acceleration.add(pushBack);

            this.acceleration.limit((float) 1.5);
            //
            seperationSetter(enemies);
            //
            collisionChecker(hero);
            //
            this.velocity.add(this.acceleration);
            this.acceleration.mult((float) 0.85);
            //
            this.position.add(velocity);
            //
            this.velocity.limit(1);
            //
            this.enemyImage.relocate(position.x, position.y);
            this.enemyHitbox.relocate(position.x + xdelta, position.y + ydelta);
            if (animationCounter > 10 + rand.nextInt(5)) {
                switch (this.enemyImagesPos) {
                    case 0:
                        this.enemyImagesPos++;
                        enemyImage.setImage(enemyImages[this.enemyImagesPos]);
                        break;
                    case 1:
                        this.enemyImagesPos = 0;
                        enemyImage.setImage(enemyImages[this.enemyImagesPos]);
                        break;
                }
                animationCounter = 0;
            }
            bordorcheck();
        })));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void seperationSetter(Enemy enemies) {
        Vecc2f seperation = enemies.seperation(this.position,this.velocity);
        this.acceleration.add(seperation);
    }

    public void setHealth(int health, Group group, ArrayList<Enemy_Fly> enemy_flies, Vecc2f direction, ArrayList<Enemy_Explosion> enemy_explosions, Background_Items background_items, Room room) {
        this.health = health;
        if (this.health < 0) {
            this.health = 0;
        }
        if (this.health == 0) {

            group.getChildren().removeAll(this.enemyImage, this.enemyHitbox);
            enemy_flies.remove(enemy_flies.get(this.arrayPos));
            this.timeline.stop();

            for (Enemy_Fly enemy : enemy_flies) {
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
        //System.out.println(direction);
        applyKnockback(direction);
    }
}
