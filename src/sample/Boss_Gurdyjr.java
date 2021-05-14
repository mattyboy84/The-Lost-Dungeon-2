package sample;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Random;

public class Boss_Gurdyjr extends Boss_BaseClass {
    //"file:src\\game\\Bosses\\gurdyjr\\boss_021_gurdyjr.png"

    //ImageView[][] bossFaces = new ImageView[5][4];

    ArrayList<BossFaces> bossFaces = new ArrayList<>();
    ArrayList<Enemy_Tear> tears = new ArrayList<>();
    ArrayList<Tear_Explosion> tear_explosions = new ArrayList<>();

    ImageView currentFace = new ImageView();
    int FDeltaX = 69;
    int FDeltaY = 42;
    //
    int HDeltaX;
    int HDeltaY;
    //
    State state;

    enum State {
        Idle,
        Jumping,
        Sliding
    }

    int screenHeight = 1080;

    //
    boolean tearBurst = false;

    //Timers
    int idleTimer;
    int slidingTimer;
    int slidingImageTimer;
    int jumpingTimer;
    int jumpingImageTimer;
    Vecc2f center = new Vecc2f();
    boolean skip;
    int imageDelta = 0;
    boolean borderOveride;
    float jumpingHeight;
    int jumpNo = 0;


    private class BossFaces {

        Image face;

        public BossFaces(Image Image) {
            this.face = Image;
        }

        public Image getFace() {
            return face;
        }

        public void setFace(Image face) {
            this.face = face;
        }
    }

    public Boss_Gurdyjr(ArrayList<Boss_Gurdyjr> boss_gurdyjr, Hero hero, Boss boss, Background_Items background_items, Group group) {
        this.size = 4;
        this.damage = 1;

        //new Pulse().play();//animatefx effect
        this.arrayPos = boss_gurdyjr.size();
        this.state = State.Idle;
        healthBarSetUp();
        this.maxHealth = 250;
        this.health = maxHealth;
        this.bossImage = new ImageView(new WritableImage(new Image("file:src\\game\\Bosses\\gurdyjr\\boss_021_gurdyjr.png").getPixelReader(), 0, 0, 288, 240));
        //
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 5; j++) {
                bossFaces.add(new BossFaces(new ImageView(new WritableImage(new Image("file:src\\game\\Bosses\\gurdyjr\\boss_021_gurdyjr.png").getPixelReader(), (j * 144), 96 + (i * 144), 144, 144)).getImage()));
            }
        }
        {
            bossFaces.remove(19);
            bossFaces.remove(18);
            bossFaces.remove(1);
            bossFaces.remove(0);
        }

        this.currentFace.setImage(bossFaces.get(3).getFace());

        this.velocity = new Vecc2f(0, 0);
        this.acceleration = new Vecc2f(0, 0);
        //
        this.bossImage.setCache(true);
        this.bossImage.setCacheHint(CacheHint.SPEED);
        this.currentFace.setCache(true);
        this.currentFace.setCacheHint(CacheHint.SPEED);
        //
        this.position = new Vecc2f((float) (1920 / 2 - (this.bossImage.getBoundsInParent().getWidth() / 2)), (float) (1080 / 2 - (this.bossImage.getBoundsInParent().getHeight() / 2)));
        //
        this.HDeltaX = (int) (this.bossImage.getBoundsInParent().getWidth() / 2);
        this.HDeltaY = (int) (this.bossImage.getBoundsInParent().getHeight() / 2) + 35;

        this.r = (int) ((this.bossImage.getBoundsInParent().getWidth() / 2) * 0.65);
        this.hitbox = new Circle(this.r);
        this.hitbox.setVisible(false);
        //this.hitbox.setOpacity(0.4);
        //
        this.idleTimer = -70;
        timeline(hero, boss, background_items, group);
    }

    private void timeline(Hero hero, Boss boss, Background_Items background_items, Group group) {
        this.timeline = new Timeline(new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            switch (state) {
                case Idle:
                    idle(hero);
                    break;
                case Jumping:
                    jumping(group, hero);
                    break;
                case Sliding:
                    sliding(group, background_items);
                    break;
            }

            this.hitbox.setCenterX((position.x + HDeltaX));
            this.hitbox.setCenterY((position.y + HDeltaY));
            this.center.set(hitbox.getCenterX(), hitbox.getCenterY());
            this.bossImage.relocate(this.position.x, this.position.y);
            this.currentFace.relocate(this.position.x + FDeltaX, this.position.y + FDeltaY);

            //
            idleTimer++;
            slidingTimer++;
            jumpingTimer++;

            //System.out.println(state);

            bordorChecker();
            collisionChecker(hero);
            seperationSetter(boss);
        }));
        this.timeline.setCycleCount(Timeline.INDEFINITE);
    }

    private void sliding(Group group, Background_Items background_items) {
        //swap between animation 8/9
        slidingImageTimer++;

        //this.velocity.add(this.acceleration);
        //this.acceleration.mult((float) 0.70);

        this.position.add(this.velocity);
        if (random.nextInt(9) == 4) {
            background_items.addBlood(this.position, bossImage, group, "medium", "Center");
        }

        slidingImageChanger();

        if (slidingTimer > 170 + random.nextInt(20)) {
            state = State.Idle;
            idleTimer = -70;
            //System.out.println("changing");
            currentFace.setImage((bossFaces.get(3).getFace()));

            //this.position.set((int) position.x, (int) position.y);

        }
    }

    private void slidingImageChanger() {
        if (slidingImageTimer > 8) {
            slidingImageTimer = 0;

            if (imageDelta == 2) {
                imageDelta = 0;
            }

            currentFace.setImage((bossFaces.get(8 + imageDelta).getFace()));
            imageDelta++;
        }
    }


    private void idle(Hero hero) {
        currentFace.setImage((bossFaces.get(3).getFace()));
        if (idleTimer > 10) {
            idleTimer = 0;

            switch (random.nextInt(2)) {
                case 0:
                    if (!(this.hitbox.getBoundsInParent().getMaxY() > (screenHeight * 0.9))) {
                        state = State.Jumping;
                        jumpNo = 0;
                        startJumping();
                    }
                    //jumpingTimer = 0;
                    break;
                case 1:
                    state = State.Sliding;
                    startSliding(hero);
                    slidingTimer = 0;
                    break;
            }
        }
    }

    Vecc2f test = new Vecc2f(0, 0);

    private void startJumping() {
        jumpingTimer = 0;
        this.tearBurst = false;
        borderOveride = true;
        velocity.set(0, 0);
        acceleration.set(0, -2.5);//-2 is good
        this.jumpingHeight = this.position.y;
        this.position.add(0, -1);
    }

    private void jumping(Group group, Hero hero) {

        this.acceleration.add((float) 0.00001, (float) 0.225);//0.15 is good
        this.velocity.add(this.acceleration);
        //
        //test.set(acceleration.x,acceleration.y);
        //test.setMag(1);
        //System.out.println(acceleration.y);
        //0,1,7
        if (acceleration.y < 0) {
            currentFace.setImage(bossFaces.get(1).getFace());
        } else if ((acceleration.y > 0 && acceleration.y < 2.5)) {
            currentFace.setImage(bossFaces.get(2).getFace());
        } else if (acceleration.y > 2.5) {
            currentFace.setImage(bossFaces.get(7).getFace());
        }

        //
        if (this.position.y >= this.jumpingHeight) {//touched the ground
            currentFace.setImage(bossFaces.get(0).getFace());
            //
            //System.out.println("ground");
            if (!tearBurst) {
                tearBurst(group, hero, this.tears, this.tear_explosions);
                tearBurst = true;
            }
            //
            this.velocity.set(0, 0);

            this.acceleration.set(0, 0);
            //System.out.println(jumpingTimer);
            if (jumpingTimer > 80) {//interval between jumps, 1 jump is about 30
                jumpNo++;
                if (jumpNo < 2) {//jumps twice
                    startJumping();
                } else {
                    state = State.Idle;
                    idleTimer = -30;
                    //System.out.println("changing");
                    currentFace.setImage((bossFaces.get(3).getFace()));
                    borderOveride = false;
                }
            }
        }
        this.position.add(this.velocity);


    }
    private void tearBurst(Group group, Hero hero, ArrayList<Enemy_Tear> tears, ArrayList<Tear_Explosion> tear_explosions) {
        for (int i = 0; i < 8; i++) {
            /*
            Vecc2f push = new Vecc2f();
            push.set(hero.getCenter());
            push.sub(this.center);
            push.setMag(1);
            */
            Vecc2f dir = new Vecc2f((float) (Math.sin(Math.toRadians(45) * (i + 1))), (float) (Math.cos(Math.toRadians(45) * (i + 1))));
            //
            this.tears.add(new Enemy_Tear(new Vecc2f(this.center.x,this.center.y+30), group, hero, dir, 22,11, tears, tear_explosions));

        }
    }

    private void startSliding(Hero hero) {
        Vecc2f push = new Vecc2f();
        push.set(hero.getCenter());
        push.sub(this.center);
        push.setMag(10);
        this.velocity.set(push);

        currentFace.setImage((bossFaces.get(9).getFace()));

    }

    public void load(Group group) {
        group.getChildren().addAll(this.bossImage, this.currentFace, this.hitbox, this.healthBarImg, this.healthBarRec);
        this.bossImage.relocate(position.x, position.y);
        this.currentFace.relocate(position.x + FDeltaX, position.y + FDeltaY);
        this.healthBarImg.relocate(healthBarImgPosition.x, healthBarImgPosition.y);
        this.healthBarRec.relocate(healthBarRecPosition.x, healthBarRecPosition.y);
        this.bossImage.setViewOrder(-7);
        this.currentFace.setViewOrder(-7);
        this.hitbox.setViewOrder(-7);
        this.timeline.play();
    }

    public void unload(Group group) {
        group.getChildren().removeAll(this.bossImage, this.currentFace, this.hitbox, this.healthBarRec, this.healthBarImg);
        this.timeline.pause();
    }

    public void bordorChecker() {//
        if (!borderOveride) {
            if (((this.hitbox.getBoundsInParent().getMinY() * 0.98) < 0 || (this.hitbox.getBoundsInParent().getMaxY() * 1.02) > 1080)) {
                this.velocity.y = (this.velocity.y * -1);
            }
            if ((this.hitbox.getBoundsInParent().getMinX() * 0.98) < 0 || (this.hitbox.getBoundsInParent().getMaxX() * 1.02) > 1920) {
                this.velocity.x = (this.velocity.x * -1);
            }
        }
    }

    public void setHealth(int health, Room room, Group group, ArrayList<Boss_Gurdyjr> boss_gurdyjr) {
        this.health = health;
        //System.out.println(health);
        if (this.health < 0) {
            this.health = 0;
        }
        this.healthBarRec.setWidth((int) (this.rectWidth * ((float) this.health / this.maxHealth)));
        if (this.health == 0) {

            for (int i = 0; i < this.size + random.nextInt(2); i++) {//blood and guts background items
                room.background_items.addBlood(this.position, this.bossImage, group, "large");
                room.background_items.addGuts(this.position, this.bossImage, group);
            }
            //
            this.timeline.stop();

            group.getChildren().removeAll(this.bossImage, this.currentFace, this.hitbox, this.healthBarImg, this.healthBarRec);

            boss_gurdyjr.remove(boss_gurdyjr.get(this.arrayPos));
            for (Boss_Gurdyjr bossGurdyjr : boss_gurdyjr) {
                if (bossGurdyjr.getArrayPos() > this.arrayPos) {
                    bossGurdyjr.setArrayPos(bossGurdyjr.getArrayPos() - 1);
                }
            }
        }
    }

    public int getHealth() {
        return health;
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

    public ArrayList<BossFaces> getBossFaces() {
        return bossFaces;
    }

    public void setBossFaces(ArrayList<BossFaces> bossFaces) {
        this.bossFaces = bossFaces;
    }

    public ImageView getCurrentFace() {
        return currentFace;
    }

    public void setCurrentFace(ImageView currentFace) {
        this.currentFace = currentFace;
    }

    public int getFDeltaX() {
        return FDeltaX;
    }

    public void setFDeltaX(int FDeltaX) {
        this.FDeltaX = FDeltaX;
    }

    public int getFDeltaY() {
        return FDeltaY;
    }

    public void setFDeltaY(int FDeltaY) {
        this.FDeltaY = FDeltaY;
    }

    public int getHDeltaX() {
        return HDeltaX;
    }

    public void setHDeltaX(int HDeltaX) {
        this.HDeltaX = HDeltaX;
    }

    public int getHDeltaY() {
        return HDeltaY;
    }

    public void setHDeltaY(int HDeltaY) {
        this.HDeltaY = HDeltaY;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getIdleTimer() {
        return idleTimer;
    }

    public void setIdleTimer(int idleTimer) {
        this.idleTimer = idleTimer;
    }

    public int getSlidingTimer() {
        return slidingTimer;
    }

    public void setSlidingTimer(int slidingTimer) {
        this.slidingTimer = slidingTimer;
    }

    public int getSlidingImageTimer() {
        return slidingImageTimer;
    }

    public void setSlidingImageTimer(int slidingImageTimer) {
        this.slidingImageTimer = slidingImageTimer;
    }

    public Vecc2f getCenter() {
        return center;
    }

    public void setCenter(Vecc2f center) {
        this.center = center;
    }

    public boolean isSkip() {
        return skip;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public int getImageDelta() {
        return imageDelta;
    }

    public void setImageDelta(int imageDelta) {
        this.imageDelta = imageDelta;
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
