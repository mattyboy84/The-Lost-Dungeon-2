package sample;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class Enemy {

    ArrayList<Enemy_Fly> enemy_flies = new ArrayList<>();
    ArrayList<Enemy_AttackFly> enemy_attackFlies = new ArrayList<>();
    ArrayList<Enemy_Worm> enemy_worms = new ArrayList<>();
    String[] enemyName = {"fly", "attack fly", "worm"};
    String enemy;
    Random random = new Random();

    public void addNewEnemy(Hero hero, Enemy enemies) {
        int a = random.nextInt(enemyName.length);
        this.enemy = enemyName[a];
        switch (a) {
            case 0:
                enemy_flies.add(new Enemy_Fly(enemy_flies, hero, this.enemy, enemies));
                break;
            case 1:
                enemy_attackFlies.add(new Enemy_AttackFly(enemy_attackFlies, hero, this.enemy, enemies));
                break;
            case 2:
                enemy_worms.add(new Enemy_Worm(enemy_worms, hero, this.enemy, enemies));
                break;
        }
    }

    public boolean checkTearCollision(Circle tearHitbox, int size, Vecc2f direction, ArrayList<Enemy_Explosion> enemy_explosions, Background_Items background_items, Room room, Group group) {
        boolean hitEnemy = false;
        for (int i = 0; i < enemy_flies.size(); i++) {
            if (tearHitbox.getBoundsInParent().intersects(enemy_flies.get(i).getEnemyHitbox().getBoundsInParent())) {
                enemy_flies.get(i).setHealth(enemy_flies.get(i).getHealth() - size, group, enemy_flies, direction, enemy_explosions, background_items, room);
                hitEnemy = true;
            }
        }
        for (int i = 0; i < enemy_attackFlies.size(); i++) {
            if (tearHitbox.getBoundsInParent().intersects(enemy_attackFlies.get(i).getEnemyHitbox().getBoundsInParent())) {
                enemy_attackFlies.get(i).setHealth(enemy_attackFlies.get(i).getHealth() - size, group, enemy_attackFlies, direction, enemy_explosions, background_items, room);
                hitEnemy = true;
            }
        }
        for (int i = 0; i < enemy_worms.size(); i++) {
            if (tearHitbox.getBoundsInParent().intersects(enemy_worms.get(i).getEnemyHitbox().getBoundsInParent())) {
                enemy_worms.get(i).setHealth(enemy_worms.get(i).getHealth() - size, group, enemy_worms, direction, enemy_explosions, background_items, room);
                hitEnemy = true;
            }
        }
        return hitEnemy;
    }

    public Vecc2f seperation(Vecc2f position, Vecc2f velocity) {
        Vecc2f steering = new Vecc2f();
        int total = 0;
        //
        for (Enemy_Fly enemy_fly : enemy_flies) {
            total = getTotal(position, steering, total, enemy_fly.getPosition());
        }
        for (Enemy_AttackFly enemy_attackFly : enemy_attackFlies) {
            total = getTotal(position, steering, total, enemy_attackFly.getPosition());
        }
        for (Enemy_Worm enemy_worm : enemy_worms) {
            total = getTotal(position, steering, total, enemy_worm.getPosition());
        }
        if (total > 0) {
            steering.div(total);
            steering.setMag(12);
            steering.sub(velocity);
            steering.limit((float) 0.5);
        }
        steering.mult((float) 2);
        return steering;
    }

    private int getTotal(Vecc2f position, Vecc2f steering, int total, Vecc2f position2) {
        float d;
        d = (position.distance(position2));
        if ((d < 100) && position != position2) {
            Vecc2f difference = new Vecc2f().sub(position, position2);
            difference.div(d * d);
            steering.add(difference);
            total++;
        }
        return total;
    }

    public void load(Group group) {
        for (int i = 0; i < enemy_flies.size(); i++) {
            enemy_flies.get(i).load(group);
        }
        for (int i = 0; i < enemy_attackFlies.size(); i++) {
            enemy_attackFlies.get(i).load(group);
        }
        for (int i = 0; i < enemy_worms.size(); i++) {
            enemy_worms.get(i).load(group);
        }
    }

    public void unload(Group group) {
        for (int i = 0; i < enemy_flies.size(); i++) {
            enemy_flies.get(i).unload(group);
        }
        for (int i = 0; i < enemy_attackFlies.size(); i++) {
            enemy_attackFlies.get(i).unload(group);
        }
        for (int i = 0; i < enemy_worms.size(); i++) {
            enemy_worms.get(i).unload(group);
        }
    }

    public int size() {
        return enemy_flies.size() + enemy_attackFlies.size() + enemy_worms.size();
    }
}