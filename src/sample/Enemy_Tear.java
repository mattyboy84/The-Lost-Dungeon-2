package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Enemy_Tear {

    Vecc2f position;
    float velocity;
    Vecc2f direction;
    ImageView tear;
    Timeline timeline;
    Circle hitbox;
    int damage = 1;
    boolean removed;
    int arrayPos;

    public Enemy_Tear(Vecc2f start, Group group, Hero hero, Vecc2f dir, int tile, float velo, ArrayList<Enemy_Tear> tears, ArrayList<Tear_Explosion> tear_explosions) {
        this.velocity = velo;
        //"file:src\\game\\Bosses\\gurdyjr\\boss_021_gurdyjr.png"
        this.tear = new ImageView("file:src\\game\\tears\\tears\\tile0" + tile + ".png");
        this.position = new Vecc2f();
        this.position.set(start.x, start.y);
        this.position.sub((float) (this.tear.getBoundsInParent().getWidth() / 2), (float) (this.tear.getBoundsInParent().getHeight() / 2));
        this.removed = false;
        float r = 0;
        this.arrayPos = tears.size();
        if (tile >= 16) {
            r = (float) (9 + (2.5 * (tile - 16)));
        }
        this.hitbox = new Circle(r);
        this.hitbox.setVisible(false);
        //this.hitbox.setViewOrder(-3);
        this.hitbox.toFront();
        //
        this.direction = new Vecc2f(dir);
        group.getChildren().addAll(this.tear, this.hitbox);
        this.tear.relocate(this.position.x, this.position.y);
        //this.tear.setViewOrder(-3);
        //this.tear.toFront();

        timeline(group, tears, tear_explosions, hero);

    }

    // if ((this.tearHitbox.getCenterY() - this.tearHitbox.getRadius() <= 0) || (this.tearHitbox.getCenterY() + this.tearHitbox.getRadius() >= 1080) || (this.tearHitbox.getCenterX() - this.tearHitbox.getRadius() <= 0) || (this.tearHitbox.getCenterX() + this.tearHitbox.getRadius() >= 1920)) {
    //                        tearRemover(group, tears, tear_explosions);
    //                        this.removed = true;
    //                    }

    private void timeline(Group group, ArrayList<Enemy_Tear> tears, ArrayList<Tear_Explosion> tear_explosions, Hero hero) {
        this.timeline = new Timeline(new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            this.position.add((this.direction.x * velocity), (this.direction.y * velocity));
            this.tear.relocate(this.position.x, this.position.y);

            this.hitbox.setCenterX(position.x + (this.tear.getBoundsInParent().getWidth() / 2));
            this.hitbox.setCenterY(position.y + (this.tear.getBoundsInParent().getHeight() / 2));

            try {
                if (!removed) {//border collision
                    if ((this.hitbox.getCenterY() - this.hitbox.getRadius() <= 0) || (this.hitbox.getCenterY() + this.hitbox.getRadius() >= 1080) || (this.hitbox.getCenterX() - this.hitbox.getRadius() <= 0) || (this.hitbox.getCenterX() + this.hitbox.getRadius() >= 1920)) {
                        tearRemover(group, tears, tear_explosions);
                        this.removed = true;
                    }
                }
                if (!removed) {//hero collision
                    if ((this.hitbox.getBoundsInParent().intersects(hero.getHeroBodyHitbox().getBoundsInParent())) || (this.hitbox.getBoundsInParent().intersects(hero.getHeroHeadHitbox().getBoundsInParent()))) {
                        collisionChecker(hero);
                        tearRemover(group, tears, tear_explosions);
                        this.removed = true;
                    }
                }
            } catch (Exception e) {
                tearRemover(group, tears, tear_explosions);
            }
        }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        this.timeline.play();
    }

    private void tearRemover(Group group, ArrayList<Enemy_Tear> tears, ArrayList<Tear_Explosion> tear_explosions) {
        group.getChildren().removeAll(this.tear, this.hitbox);
        this.timeline.stop();
        try {
            tears.remove(tears.get(this.arrayPos));
        } catch (Exception e) {
            for (Enemy_Tear value : tears) {
                group.getChildren().removeAll(value.getTear(), value.getHitbox()/*, value.getTearShadow()*/);
                value.getTimeline().stop();
            }
            tears.clear();
        }
        for (Enemy_Tear value : tears) {
            if (value.getArrayPos() > this.arrayPos) {
                value.setArrayPos(value.getArrayPos() - 1);
            }
        }
        tear_explosions.add(new Tear_Explosion("red", this.position, group, tear_explosions));
    }

    public void collisionChecker(Hero hero) {
        //if ((hero.heroHeadHitbox.getBoundsInParent().intersects(this.hitbox.getBoundsInParent())) || (hero.heroBodyHitbox.getBoundsInParent().intersects(this.hitbox.getBoundsInParent()))) {
        if (hero.invulnerabilityCounter > hero.invulnerabilityCooldown) {
            //System.out.println("HIT OH NO STINKY");
            hero.hit(this.damage);
            hero.setInvulnerabilityCounter(0);
            Vecc2f steering = new Vecc2f();
            float d = hero.getLocation().distance(this.position);
            Vecc2f difference = new Vecc2f().sub(hero.getLocation(), this.position);
            //System.out.println(difference);
            hero.getVelocity().add(difference);
            // }
        }
    }


    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public ImageView getTear() {
        return tear;
    }

    public void setTear(ImageView tear) {
        this.tear = tear;
    }

    public Circle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Circle hitbox) {
        this.hitbox = hitbox;
    }

    public int getArrayPos() {
        return arrayPos;
    }

    public void setArrayPos(int arrayPos) {
        this.arrayPos = arrayPos;
    }
}
