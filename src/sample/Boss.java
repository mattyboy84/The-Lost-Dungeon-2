package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Random;

public class Boss {

    ArrayList<Boss_Fistula> boss_fistula = new ArrayList<>();
    ArrayList<Boss_Gurdyjr> boss_gurdyjr = new ArrayList<>();
    //String[] bossName = {"fistula"};
    //String[] bossName = {"fistula", "Gurdyjr"};
    String[] bossName = {"Gurdyjr"};

    String boss;
    Random random = new Random();


    public void addnewEnemy(Hero hero, Boss boss, Background_Items background_items, Group group) {
        int a = random.nextInt(bossName.length);
        this.boss = bossName[a];
        switch (a) {
            case 0:
                //boss_fistula.add(new Boss_Fistula(boss_fistula, 4, (1920 / 2), (1080 / 2), 1, boss, hero));
                boss_gurdyjr.add(new Boss_Gurdyjr(boss_gurdyjr, hero, boss, background_items, group));
                break;
            case 1:
                break;
        }
    }

    public boolean checkTearCollision(Circle tearHitbox, int tearDamage, Vecc2f direction, Room room, Group group, Hero hero) {
        boolean hitBoss = false;
        for (int i = 0; i < boss_fistula.size(); i++) {
            if (tearHitbox.getBoundsInParent().intersects(boss_fistula.get(i).getHitbox().getBoundsInParent())) {
                boss_fistula.get(i).setHealth(boss_fistula.get(i).getHealth() - tearDamage, direction, boss_fistula, group, room, hero);
                hitBoss = true;
            }
        }
        for (int i = 0; i < boss_gurdyjr.size(); i++) {
            if (tearHitbox.getBoundsInParent().intersects(boss_gurdyjr.get(i).getHitbox().getBoundsInParent())) {
                boss_gurdyjr.get(i).setHealth(boss_gurdyjr.get(i).getHealth() - tearDamage, room, group, boss_gurdyjr);
                hitBoss=true;
            }
        }

        return hitBoss;
    }

    public Vecc2f seperation(Vecc2f position, Vecc2f velocity, ImageView bossImage) {
        Vecc2f steering = new Vecc2f(0, 0);
        int total = 0;
        for (Boss_Fistula bossFistula : boss_fistula) {
            total = getTotal(position, steering, total, bossFistula.getPosition(), bossImage);
        }
        for (Boss_Gurdyjr bossGurdyjr : boss_gurdyjr) {
            total = getTotal(position, steering, total, bossGurdyjr.getPosition(), bossImage);
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

    private int getTotal(Vecc2f position, Vecc2f steering, int total, Vecc2f position2, ImageView bossImage) {
        float d;
        d = position.distance(position2);
        if ((d < (bossImage.getBoundsInParent().getWidth()) * 0.8) && position != position2) {
            Vecc2f difference = new Vecc2f().sub(position, position2);
            //System.out.println(difference);
            difference.div((d * d));
            steering.add(difference);
            total++;
        }
        return total;
    }

    public void load(Group group) {
        for (int i = 0; i < boss_fistula.size(); i++) {
            boss_fistula.get(i).load(group);
        }
        for (int i = 0; i < boss_gurdyjr.size(); i++) {
            boss_gurdyjr.get(i).load(group);
        }
    }

    public void unload(Group group) {
        for (int i = 0; i < boss_fistula.size(); i++) {
            boss_fistula.get(i).unload(group);
        }
        for (int i = 0; i < boss_gurdyjr.size(); i++) {
            boss_gurdyjr.get(i).unload(group);
        }
    }

    public int size() {
        return boss_fistula.size() + boss_gurdyjr.size();
    }

}
