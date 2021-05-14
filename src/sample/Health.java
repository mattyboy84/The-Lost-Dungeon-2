package sample;


import javafx.scene.Group;

import java.util.ArrayList;

public class Health {

    int health;
    int maxHealth;

    int heartHealth;
    ArrayList<Heart> hearts = new ArrayList<>();

    public Health() {
    }


    public void setHealth(int health, Group group) {
        this.health = health;
        for (int i = 0; i < this.health; i++) {
            hearts.add(new Heart(group, hearts));
        }
        this.health = this.health * 2;
        this.maxHealth = this.health;
    }

    public void increaseHealth(int i) {
        this.health = this.health + i;
        if (this.health > this.maxHealth) {
            this.health = this.maxHealth;
        }
        if (this.health % 2 == 0) {//3 going up to 4
            ifSubPro();
            ifsubPro1();
        } else {//4 going up to 5
            elseSubPro();
        }
    }

    public void decreaseHealth(int i) {
        this.health = this.health - i;
        if (this.health < 0) {
            this.health = 0;
        }
        if (this.health % 2 == 0) {//5 going down to 4
            ifSubPro();
            hearts.get(heartHealth).setHealth(0);
            ifsubPro1();
        } else {//6 going down to 5
            elseSubPro();
        }
    }

    private void ifSubPro() {
        heartHealth = this.health / 2;
        for (int j = 0; j < heartHealth; j++) {
            hearts.get(j).setHealth(2);
        }
    }

    private void ifsubPro1() {
        for (int j = heartHealth; j < hearts.size(); j++) {
            hearts.get(j).setHealth(0);
        }
    }


    private void elseSubPro() {
        heartHealth = (this.health - 1) / 2;
        for (int j = 0; j < heartHealth; j++) {
            hearts.get(j).setHealth(2);
        }
        hearts.get(heartHealth).setHealth(1);
        for (int j = heartHealth + 1; j < hearts.size(); j++) {
            hearts.get(j).setHealth(0);
        }
    }

    public void addHeart(Group group) {
        int remainingHealth = 2;
        for (Heart heart : hearts) {
            switch (heart.getHealth()) {
                case 0:
                    remainingHealth = addHeartSub(remainingHealth, heart, 2);
                    remainingHealth = addHeartSub(remainingHealth, heart, 1);
                    break;
                case 1:
                    remainingHealth = addHeartSub(remainingHealth, heart, 1);
                    break;
                case 2:
                    break;
            }
        }
        this.maxHealth = this.maxHealth + 2;
        this.health = this.health + 2;
        hearts.add(new Heart(group, hearts, remainingHealth));
    }

    private int addHeartSub(int remainingHealth, Heart heart, int i) {
        if (remainingHealth >= i) {
            heart.setHealth(heart.getHealth() + i);
            remainingHealth = remainingHealth - i;
        }
        return remainingHealth;
    }


}
