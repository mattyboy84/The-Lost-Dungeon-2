package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Tears {

    ImageView tear;
    String colour;
    int size;
    Circle tearHitbox;
    int pos;
    int arrayPos;
    //
    Vecc2f tearLocation;
    Vecc2f tearVelocity = new Vecc2f(0, 0);
    //
    Vecc2f shadowLocation;
    Vecc2f shadowVelocity;
    Vecc2f direction;
    Timeline timeline;
    boolean removed;
    float flyTime;
    float heightDelta;
    //
    Vecc2f tearYAcc;
    Vecc2f tearYVecc;
    float startY;
    float startX;

    //
    ImageView tearShadow;

    public Tears(String finalAttackingDir, int attackDamage, String attackColour, Group group, float x, float y, Vecc2f velocity, Vecc2f direction, float veloLimit, ArrayList<Tears> tears, Room room, ArrayList<Tear_Explosion> tear_explosions, ArrayList<Enemy_Explosion> enemy_explosions, Background_Items background_items, String movingDirection, Hero hero) {
        //System.out.println("new tear");
        this.removed = false;
        //
        tearYAcc = new Vecc2f(0, (float) 0.0015);
        tearYVecc = new Vecc2f(0, 0);
        //
        this.heightDelta = 0;
        this.flyTime = 0;
        this.size = attackDamage;
        this.colour = attackColour;
        this.tearLocation = new Vecc2f(x, y);
        //
        this.startX = x;
        this.startY = y;
        this.arrayPos = tears.size();
        switch (attackColour) {
            case "blue":
                pos = 0;
                break;
            case "red":
                pos = 16;
                break;
        }
        pos = pos + attackDamage;
        this.tear = new ImageView("file:src\\game\\tears\\tears\\tile0" + pos + ".png");
        this.tearHitbox = new Circle(9 + (2.5 * attackDamage));//9+ (2.5*attackDamage)
        this.tearHitbox.setVisible(false);
        //
        this.tearShadow = new ImageView("file:src\\game\\tears\\shadow.png");
        //
        this.tear.setViewOrder(-3);
        this.tearHitbox.setViewOrder(-3);
        this.tearShadow.setViewOrder(-3);
        group.getChildren().addAll(this.tearHitbox, this.tearShadow, this.tear);
        //
        this.tear.relocate(tearLocation.x, tearLocation.y);
        //
        this.tearShadow.relocate(this.tearLocation.x + ((tear.getBoundsInParent().getWidth() - (tearHitbox.getRadius() * 2)) / 2), startY + (tear.getBoundsInParent().getHeight()));
        //
        float temp = (float) (tearShadow.getBoundsInParent().getHeight() / (tearHitbox.getRadius() * 2));
        //System.out.println((temp));
        this.tearShadow.setFitWidth((this.tearHitbox.getRadius() * 2));
        this.tearShadow.setFitHeight((((this.tearHitbox.getRadius()) / temp)));
        this.tearShadow.setOpacity(0.4);

        this.tearVelocity.add(velocity.x / 2, velocity.y / 2);
        //System.out.println(this.velocity);

        //

        switch (finalAttackingDir) {
            case "north":
                this.tearVelocity.add(0, (float) (-veloLimit * 1.5));
                break;
            case "south":
                this.tearVelocity.add(0, (float) (veloLimit * 1.5));
                break;
            case "west":
                this.tearVelocity.add((float) (-veloLimit * 1.5), 0);
                break;
            case "east":
                this.tearVelocity.add((float) (veloLimit * 1.5), 0);
                break;
        }
        //
        this.shadowLocation = new Vecc2f(this.tearLocation.x, this.tearLocation.y);
        this.shadowVelocity = new Vecc2f(this.tearVelocity.x, this.tearVelocity.y);
        //
        this.direction = new Vecc2f(this.tearVelocity.x, this.tearVelocity.y);
        this.direction.limit(1);


        this.timeline = new Timeline((new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            //
            this.tearLocation.add(this.tearVelocity);
            this.shadowLocation.add(this.shadowVelocity);
            //
            this.tear.relocate(this.tearLocation.x, (this.tearLocation.y));
            //
            this.tearShadow.relocate(this.shadowLocation.x + ((tear.getBoundsInParent().getWidth() - (tearHitbox.getRadius() * 2)) / 2), this.shadowLocation.y + (tear.getBoundsInParent().getHeight()));
            //
            this.tearHitbox.setCenterX(tearLocation.x + (this.tear.getBoundsInParent().getWidth() / 2));
            this.tearHitbox.setCenterY(tearLocation.y + (this.tear.getBoundsInParent().getHeight() / 2));
            //tear gravity
            flyTime = (flyTime + (1));
            //
            tearYVecc.add(tearYAcc);
            tearYVecc.limit((float) 0.6);
            heightDelta = (float) (heightDelta + tearYVecc.y);
            tearLocation.add(0, heightDelta);
            //
            if (flyTime==60){
                tearRemover(group, tears, tear_explosions);
            }
            //
            try {
                if (!this.removed) {//edge of screen collision
                    if ((this.tearHitbox.getCenterY() - this.tearHitbox.getRadius() <= 0) || (this.tearHitbox.getCenterY() + this.tearHitbox.getRadius() >= 1080) || (this.tearHitbox.getCenterX() - this.tearHitbox.getRadius() <= 0) || (this.tearHitbox.getCenterX() + this.tearHitbox.getRadius() >= 1920)) {
                        tearRemover(group, tears, tear_explosions);
                        this.removed = true;
                    }
                }
                //
                if (!this.removed) {//enemy collision
                    if (room.enemies.checkTearCollision(this.tearHitbox, this.size, this.direction, enemy_explosions, background_items, room, group)) {
                        tearRemover(group, tears, tear_explosions);
                        this.removed = true;
                    }
                }
                //
                if (!this.removed) {//boss collision
                    if (room.boss.checkTearCollision(this.tearHitbox, size, this.direction, room, group, hero)) {
                        tearRemover(group, tears, tear_explosions);
                        this.removed = true;
                    }

                }
            } catch (Exception e) {
                tearRemover(group, tears, tear_explosions);
            }
        })));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    public void tearRemover(Group group, ArrayList<Tears> tears, ArrayList<Tear_Explosion> tear_explosions) {
        group.getChildren().removeAll(this.tear, this.tearHitbox, this.tearShadow);
        this.timeline.stop();
        try {
            tears.remove(tears.get(this.arrayPos));
        } catch (Exception e) {
            for (Tears value : tears) {
                group.getChildren().removeAll(value.getTear(), value.getTearHitbox(), value.getTearShadow());
                value.getTimeline().stop();
            }
            tears.clear();
        }
        for (Tears value : tears) {
            if (value.getArrayPos() > this.arrayPos) {
                value.setArrayPos(value.getArrayPos() - 1);
            }
        }
        tear_explosions.add(new Tear_Explosion(this.colour, this.tearLocation, group, tear_explosions));
    }

    public ImageView getTear() {
        return tear;
    }

    public void setTear(ImageView tear) {
        this.tear = tear;
    }

    public Circle getTearHitbox() {
        return tearHitbox;
    }

    public void setTearHitbox(Circle tearHitbox) {
        this.tearHitbox = tearHitbox;
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public int getArrayPos() {
        return arrayPos;
    }

    public void setArrayPos(int arrayPos) {
        this.arrayPos = arrayPos;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public ImageView getTearShadow() {
        return tearShadow;
    }

    public void setTearShadow(ImageView tearShadow) {
        this.tearShadow = tearShadow;
    }
}
