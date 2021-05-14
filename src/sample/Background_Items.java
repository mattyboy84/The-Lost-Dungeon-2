package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class Background_Items {

    ArrayList<Butterfly> butterflies = new ArrayList<>();
    ArrayList<Guts> guts = new ArrayList<>();
    ArrayList<Blood> blood = new ArrayList<>();
    ArrayList<Debris> debris = new ArrayList<>();
    Random random = new Random();

    public Background_Items() {
    }

    public void load(Group group) {
        for (Blood value : this.blood) {
            value.load(group);
        }
        for (Guts gut : this.guts) {
            gut.load(group);
        }
        for (Debris value : debris) {
            value.load(group);
        }
        for (Butterfly butterfly : this.butterflies) {
            butterfly.load(group);
        }
    }

    public void unload(Group group) {
        for (Butterfly butterfly : this.butterflies) {
            butterfly.unload(group);
        }
        for (Guts gut : this.guts) {
            gut.unload(group);
        }
        for (Blood value : this.blood) {
            value.unload(group);
        }
        for (Debris value : debris) {
            value.unload(group);
        }
    }

    public void addGuts(Vecc2f position, ImageView enemyImage, Group group) {
        // Math.round(health / 4);
        //System.out.println(health);
        //float diff = (health/4);
        //System.out.println(diff);
        //System.out.println(Math.ceil(diff));
        for (int i = 0; i < 1/* + (Math.ceil(diff))*/; i++) {
            guts.add(new Guts(position, enemyImage, group));
        }
    }

    public void addBlood(Vecc2f position, ImageView enemyImage, Group group, String bloodSize) {
        blood.add(new Blood(position, enemyImage, group, bloodSize));
    }

    public void addBlood(Vecc2f position, ImageView enemyImage, Group group, String bloodSize, String c) {
        blood.add(new Blood(position, enemyImage, group, bloodSize, c));
    }

    public void addButterflys() {
        for (int i = 0; i < 2 + random.nextInt(4); i++) {
            butterflies.add(new Butterfly());
        }
    }

    public void addDebris() {
        for (int i = 0; i < 7 + random.nextInt(7); i++) {
            debris.add(new Debris());
        }
    }
}
