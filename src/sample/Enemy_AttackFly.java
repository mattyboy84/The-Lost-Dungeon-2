package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Enemy_AttackFly extends Enemy_BaseClass {


    public Enemy_AttackFly(ArrayList<Enemy_AttackFly> enemy_attackFlies, Hero hero, String name, Enemy enemies) {
        this.arrayPos = enemy_attackFlies.size();
        this.viewOrder = -7;
        this.name = name;
        //
        this.enemyHitbox = new Rectangle(57, 45);
        this.damage = 1;
        this.xdelta = 19;
        this.ydelta = 25;
        this.maxHealth = 9;
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
        //
        attackFly(hero, enemies);

    }

    private void attackFly(Hero hero, Enemy enemies) {
        pushBack = new Vecc2f();
        this.timeline = new Timeline((new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            this.animationCounter++;
            //
            pushBack.set(hero.getLocation());
            pushBack.sub(this.position);
            pushBack.limit((float) 0.1);
            this.acceleration.add(pushBack);
            this.acceleration.limit((float) 1.5);
            //
            seperationSetter(enemies);
            //
            collisionChecker(hero);
            //
            this.velocity.add(acceleration);
            //
            this.position.add(velocity);
            //
            this.velocity.limit((float) 1.5);
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
        Vecc2f seperation = enemies.seperation(this.position, this.velocity);
        this.acceleration.add(seperation);
    }

    public void setHealth(int health, Group group, ArrayList<Enemy_AttackFly> enemy_attackFlies, Vecc2f direction, ArrayList<Enemy_Explosion> enemy_explosions, Background_Items background_items, Room room) {
        this.health = health;
        if (this.health < 0) {
            this.health = 0;
        }
        if (this.health == 0) {

            group.getChildren().removeAll(this.enemyImage, this.enemyHitbox);
            enemy_attackFlies.remove(enemy_attackFlies.get(this.arrayPos));
            this.timeline.stop();

            for (Enemy_AttackFly enemy : enemy_attackFlies) {
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
