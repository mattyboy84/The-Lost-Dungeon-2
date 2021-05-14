package sample;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.NodeOrientation;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.ArrayList;

public class Hero {
    int posX = 9;
    int posY = 9;
    Room room;
    //
    ImageView heroBody;
    ImageView heroHead;
    //
    Rectangle heroBodyHitbox;
    Circle heroHeadHitbox;
    //
    Vecc2f location;
    Vecc2f direction;
    Vecc2f velocity;
    Vecc2f acceleration;
    Vecc2f friction;
    Vecc2f center;
    //
    boolean north;
    boolean south;
    boolean east;
    boolean west;
    //
    boolean shootingNorth;
    boolean shootingSouth;
    boolean shootingEast;
    boolean shootingWest;
    //
    int invulnerabilityCooldown = 100;
    int invulnerabilityCounter = 0;
    float speedComponent = (float) 0.1;
    float veloLimit = 7;//default is 7
    float timePassedDoorCheck = 0;
    float timePassedAnimator = 0;
    float attackCoolDown = 0;
    String movingDirection;
    int YAnimateCounter = 6;
    int XAnimateCounter = 16;
    float attackSpeed = 40;//default is 40
    Image[] tiles = new Image[25];
    boolean attacking = false;
    int ydelta = 32;
    int xdelta = 0;
    String finalAttackingDir = "";
    ArrayList<Tears> tears = new ArrayList<>();
    ArrayList<Tear_Explosion> tear_explosions = new ArrayList<>();
    ArrayList<Enemy_Explosion> enemy_explosions = new ArrayList<>();
    //
    int attackDamage = 4;//default is 4
    //TODO
    String attackColour = "blue";
    int viewOrder;
    Timeline hit;
    int hitCounter = 0;
    Health health;
    Inventory inventory;

    public Hero() {

    }

    public void newHero(Group group, ArrayList<Room> rooms, ImageView background, MapOverlay mapOverlay, Hero hero) {
        //Inventroy
        inventory = new Inventory();
        //Health
        health = new Health();
        health.setHealth(3, group);
        //View order
        this.viewOrder = -6;
        //sprites
        for (int i = 0; i < tiles.length; i++) {//reads in the  player sprites
            tiles[i] = new Image("file:src\\game\\Isaac Sprites\\tile0" + i + ".png");
        }
        //
        this.heroBody = new ImageView(tiles[8]);//starting body image
        this.heroHead = new ImageView(tiles[0]);//starting head image
        //
        //this.heroBody.setVisible(false);
        //this.heroHead.setVisible(false);
        //hitboxes
        this.heroHeadHitbox = new Circle();
        this.heroHeadHitbox.setRadius((int) ((this.heroHead.getBoundsInParent().getWidth() / 2) * 0.8));
        this.heroHeadHitbox.setVisible(false);
        this.heroBodyHitbox = new Rectangle((this.heroBody.getBoundsInParent().getWidth() * 0.5), (this.heroBody.getBoundsInParent().getHeight() * 0.5));
        this.heroBodyHitbox.setVisible(false);
        //
        //this.heroHeadHitbox.toFront();
        //this.heroBodyHitbox.toFront();
        //
        //caching
        this.heroBody.setCache(true);
        this.heroBody.setCacheHint(CacheHint.SPEED);
        this.heroHead.setCache(true);
        this.heroHead.setCacheHint(CacheHint.SPEED);
        this.heroBodyHitbox.setCache(true);
        this.heroBodyHitbox.setCacheHint(CacheHint.SPEED);
        this.heroHeadHitbox.setCache(true);
        this.heroHeadHitbox.setCacheHint(CacheHint.SPEED);
        //vector setup
        this.movingDirection = "down";
        group.getChildren().addAll(this.heroBody, this.heroHead, this.heroBodyHitbox, this.heroHeadHitbox);
        this.location = new Vecc2f((float) (1920 / 2 - (heroBody.getBoundsInParent().getWidth() / 2)), (float) (1080 / 2 - (heroBody.getBoundsInParent().getHeight() / 2)));
        this.velocity = new Vecc2f(0, 0);
        this.acceleration = new Vecc2f(0, 0);
        this.friction = new Vecc2f(0, 0);
        this.center = new Vecc2f();
        this.direction = new Vecc2f();
        //Body positing and view order
        this.heroBody.relocate(location.x, location.y);
        this.heroBodyHitbox.relocate(location.x, location.y);
        this.heroBody.setViewOrder(viewOrder);
        this.heroBodyHitbox.setViewOrder(viewOrder);
        //Head positing and view order
        this.heroHead.relocate(location.x + xdelta, location.y - ydelta);
        this.heroHead.setViewOrder(viewOrder);
        this.heroHeadHitbox.setViewOrder(viewOrder);
        //
        heroHitboxRelocator();
        //
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            //
            //System.out.println(tears.size());
            //


            timePassedDoorCheck++;
            timePassedAnimator++;
            attackCoolDown++;
            invulnerabilityCounter++;
            //
            this.direction.set(velocity.x, velocity.y);
            this.direction.limit(1);
            //
            if (north) {//W
                this.acceleration.y = this.acceleration.y - speedComponent;
            }
            if (south) {//S
                this.acceleration.y = this.acceleration.y + speedComponent;
            }
            if (east) {//D
                this.acceleration.x = this.acceleration.x + speedComponent;
            }
            if (west) {//A
                this.acceleration.x = this.acceleration.x - speedComponent;
            }
            if (west && east) {
                this.acceleration.x = 0;
                this.velocity.x = 0;
            }
            if (north && south) {
                this.acceleration.y = 0;
                this.velocity.y = 0;
            }
            this.acceleration.limit(2);
            //
            //this.friction.set(velocity.x, velocity.y);
            //this.friction.normalise();
            //this.friction.mult(-1);
            //this.friction.setMag((float) 0.01);
            //System.out.println(this.friction);
            //
            this.velocity.add(this.acceleration);

            if (!north && !east && !west && !south) {
                this.acceleration.set(0, 0);
            }
            this.velocity.mult((float) 0.95);
            if (this.velocity.magnitude() < 0.2 /*&& this.velocity.magnitude() != 0*/) {
                this.velocity.set(0, 0);
                //System.out.println("hi");
                this.heroBody.setImage(tiles[8]);
                if (!attacking) {
                    this.heroHead.setImage(tiles[0]);
                }
            }
            //this.velocity.add(this.friction);
            this.velocity.limit(this.veloLimit);
            //
            this.location.add(this.velocity);
            heroHitboxRelocator();
            //
            attacking = shootingEast || shootingNorth || shootingSouth || shootingWest;
            //attackingDecider();
            if (attacking) {
                attackingDecider();
                if (attackCoolDown > attackSpeed) {
                    shoot(group, tear_explosions, enemy_explosions, hero);
                    attackCoolDown = 0;
                }
            } else {
                finalAttackingDir = "";
            }
            //
            if (!((heroBody.getLayoutX() == Math.round(this.location.x) && heroBody.getLayoutY() == Math.round(location.y)))) {//only runs this if the hero is moving
                this.heroBody.relocate(Math.round(this.location.x), Math.round(this.location.y));
                this.heroHead.relocate(Math.round(this.location.x + xdelta), Math.round(this.location.y - ydelta));
                if (timePassedDoorCheck >= 6) {
                    timePassedDoorCheck = 0;
                    doorChecker(rooms, group, background, mapOverlay);
                }
                if (timePassedAnimator >= 6) {
                    timePassedAnimator = 0;
                    heroAnimator();
                }
            }
            /*
            System.out.println(movingDirection);
            if (!north && !east && !west && !south) {
                this.movingDirection = "null";
            }
            */
            //System.out.println("left " + shootingWest + " right " + shootingEast + " north " + shootingNorth + " south " + shootingSouth);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public void hit(int damage) {
        hit = new Timeline(new KeyFrame(Duration.seconds((float) 1 / 60), event -> {
            hitCounter++;
            switch (hitCounter) {
                case 1:
                    this.heroHead.setEffect(new Glow(2));
                    this.heroBody.setEffect(new Glow(2));
                    for (int i = 0; i < damage; i++) {
                        health.decreaseHealth(1);
                    }
                    break;
                case 20:
                    this.heroHead.setEffect(new Glow(-1));
                    this.heroBody.setEffect(new Glow(-1));
                    break;
                case 40:
                    this.heroHead.setEffect(new Glow(2));
                    this.heroBody.setEffect(new Glow(2));
                    break;
                case 60:
                    this.heroHead.setEffect(new Glow(-1));
                    this.heroBody.setEffect(new Glow(-1));
                    hitCounter = 0;

                    break;
            }
        }));
        hit.setCycleCount(60);
        hit.play();
    }

    private void heroHitboxRelocator() {
        this.heroHeadHitbox.setCenterX(location.x + (this.heroHead.getBoundsInParent().getWidth() / 2));
        this.heroHeadHitbox.setCenterY(location.y + (this.heroHead.getBoundsInParent().getHeight() / 2) - ydelta);
        this.center.set(heroHeadHitbox.getCenterX(), heroHeadHitbox.getCenterY());
        //
        this.heroBodyHitbox.relocate(this.location.x + (this.heroBodyHitbox.getBoundsInParent().getWidth() / 2), this.location.y + (this.heroBodyHitbox.getBoundsInParent().getHeight() / 2));
    }

    private void attackingDecider() {
        if (shootingEast && shootingWest) {
            shootingEast = false;
            shootingWest = false;
        }
        if (shootingNorth && shootingSouth) {
            shootingSouth = false;
            shootingNorth = false;
        }
        /*
        if ((shootingNorth || shootingSouth) && (shootingWest || shootingEast)) {
            shootingEast = false;
            shootingWest = false;
        }
        */
        if (shootingEast) {
            finalAttackingDir = "east";
        }
        if (shootingWest) {
            finalAttackingDir = "west";
        }
        if (shootingNorth) {
            finalAttackingDir = "north";
        }
        if (shootingSouth) {
            finalAttackingDir = "south";
        }

        //System.out.println(finalAttackingDir);
        //System.out.println("left " + shootingWest + " right " + shootingEast + " north " + shootingNorth + " south " + shootingSouth);
    }

    public void shoot(Group group, ArrayList<Tear_Explosion> tear_explosions, ArrayList<Enemy_Explosion> enemy_explosions, Hero hero) {
        attacking = true;
        //System.out.println(finalAttackingDir);
        switch (finalAttackingDir) {
            case "north":
                heroHead.setImage(tiles[5]);
                break;
            case "south":
                heroHead.setImage(tiles[1]);
                break;
            case "west":
                heroHead.setImage(tiles[3]);
                heroHead.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                break;
            case "east":
                heroHead.setImage(tiles[3]);
                heroHead.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
                break;
        }
        tears.add(new Tears(finalAttackingDir, attackDamage, attackColour, group, location.x, location.y - ydelta, velocity, direction, this.veloLimit, tears, this.room, tear_explosions, enemy_explosions, room.getBackground_items(), this.movingDirection, hero));
    }

    private void heroAnimator() {

        float angle = this.direction.toAngle();
        if (angle > 45 && angle < 135) {//right
            this.movingDirection = "right";
            heroBody.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            if (!attacking) {
                heroHead.setNodeOrientation(NodeOrientation.LEFT_TO_RIGHT);
            }
            subHeroAnimterX();
        } else if (angle > 135 && angle < 225) {//down
            this.movingDirection = "down";
            subHeroAnimterY();
            if (!attacking) {
                heroHead.setImage(tiles[0]);
            }
        } else if (angle > 225 && angle < 315) {//left
            this.movingDirection = "left";
            heroBody.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            if (!attacking) {
                heroHead.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            }
            subHeroAnimterX();
        } else if (angle > 315 || angle < 45) {//up
            this.movingDirection = "up";
            subHeroAnimterY();
            if (!attacking) {
                heroHead.setImage(tiles[4]);
            }
        }
    }

    private void subHeroAnimterY() {
        YAnimateCounter++;
        if (YAnimateCounter > 15) {
            YAnimateCounter = 6;
        }
        heroBody.setImage(tiles[YAnimateCounter]);
    }

    private void subHeroAnimterX() {
        XAnimateCounter++;
        if (XAnimateCounter > 24) {
            XAnimateCounter = 16;
        }
        heroBody.setImage(tiles[XAnimateCounter]);
        if (!attacking) {
            heroHead.setImage(tiles[2]);
        }
    }

    private void doorChecker(ArrayList<Room> rooms, Group group, ImageView background, MapOverlay mapOverlay) {
        for (int j = 0; j < this.room.getDoors().size(); j++) {
            if (heroBodyHitbox.getBoundsInParent().intersects(this.room.getDoors().get(j).getDoor().getBoundsInParent()) && (this.movingDirection.equals(this.room.getDoors().get(j).getDirection())) && (this.room.getDoors().get(j).getState().equals("open"))) {
                acceleration.set(0, 0);
                velocity.set(0, 0);
                this.room.unload(group);
                switch (this.room.getDoors().get(j).getDirection()) {
                    case "up":
                        this.posX = this.posX - 1;
                        location.x = (float) ((1920 / 2) - (heroBody.getLayoutBounds().getWidth() / 2));
                        location.y = (float) (1080 - heroBody.getLayoutBounds().getHeight() - 150 - 80);
                        mapOverlay.moveIndicator("up");
                        break;
                    case "left":
                        this.posY = this.posY - 1;
                        location.x = (float) (1920 - 150 - heroBody.getLayoutBounds().getWidth() - 80);
                        location.y = (float) ((1080 / 2) - (heroBody.getLayoutBounds().getHeight() / 2));
                        mapOverlay.moveIndicator("left");
                        break;
                    case "right":
                        this.posY = this.posY + 1;
                        location.x = (float) (heroBody.getLayoutBounds().getWidth() + 80);
                        location.y = (float) ((1080 / 2) - (heroBody.getLayoutBounds().getHeight() / 2));
                        mapOverlay.moveIndicator("right");
                        break;
                    case "down":
                        this.posX = this.posX + 1;
                        location.x = (float) ((1920 / 2) - (heroBody.getLayoutBounds().getWidth() / 2));
                        location.y = (float) 150 + 80;
                        mapOverlay.moveIndicator("down");
                        break;
                }
                removeTears(group, tears, tear_explosions);
                roomFinder(rooms);
                heroBody.relocate(location.x, location.y);
                heroHead.relocate(Math.round(this.location.x + xdelta), Math.round(this.location.y - ydelta));
                roomLoader(rooms, group, background);
                break;
            }
        }
    }

    private void removeTears(Group group, ArrayList<Tears> tears, ArrayList<Tear_Explosion> tear_explosions) {
        for (Tears tear : this.tears) {
            tear.getTimeline().stop();
            group.getChildren().removeAll(tear.getTear(), tear.getTearHitbox());
        }
        this.tears.clear();
    }

    private void roomFinder(ArrayList<Room> rooms) {
        for (Room value : rooms) {
            if (this.posX == value.getRoomX() && this.posY == value.getRoomY()) {
                this.room = value;
            }
        }
    }

    private void roomLoader(ArrayList<Room> rooms, Group group, ImageView background) {
        this.room.load(background, group);
    }

    public void increaseHealth(int i) {
        health.increaseHealth(i);
    }

    public void decreaseHealth(int i) {
        health.decreaseHealth(i);
    }

    public void addHeart(Group group) {
        health.addHeart(group);
    }


    public ImageView getHeroHead() {
        return heroHead;
    }

    public void setHeroHead(ImageView heroHead) {
        this.heroHead = heroHead;
    }

    public boolean isShootingNorth() {
        return shootingNorth;
    }

    public void setShootingNorth(boolean shootingNorth) {
        this.shootingNorth = shootingNorth;
    }

    public boolean isShootingSouth() {
        return shootingSouth;
    }

    public void setShootingSouth(boolean shootingSouth) {
        this.shootingSouth = shootingSouth;
    }

    public boolean isShootingEast() {
        return shootingEast;
    }

    public void setShootingEast(boolean shootingEast) {
        this.shootingEast = shootingEast;
    }

    public boolean isShootingWest() {
        return shootingWest;
    }

    public void setShootingWest(boolean shootingWest) {
        this.shootingWest = shootingWest;
    }

    public Rectangle getHeroBodyHitbox() {
        return heroBodyHitbox;
    }

    public void setHeroBodyHitbox(Rectangle heroBodyHitbox) {
        this.heroBodyHitbox = heroBodyHitbox;
    }

    public Circle getHeroHeadHitbox() {
        return heroHeadHitbox;
    }

    public void setHeroHeadHitbox(Circle heroHeadHitbox) {
        this.heroHeadHitbox = heroHeadHitbox;
    }

    public Vecc2f getDirection() {
        return direction;
    }

    public void setDirection(Vecc2f direction) {
        this.direction = direction;
    }

    public Vecc2f getFriction() {
        return friction;
    }

    public void setFriction(Vecc2f friction) {
        this.friction = friction;
    }

    public float getSpeedComponent() {
        return speedComponent;
    }

    public void setSpeedComponent(float speedComponent) {
        this.speedComponent = speedComponent;
    }

    public float getVeloLimit() {
        return veloLimit;
    }

    public void setVeloLimit(float veloLimit) {
        this.veloLimit = veloLimit;
    }

    public float getTimePassedDoorCheck() {
        return timePassedDoorCheck;
    }

    public void setTimePassedDoorCheck(float timePassedDoorCheck) {
        this.timePassedDoorCheck = timePassedDoorCheck;
    }

    public float getTimePassedAnimator() {
        return timePassedAnimator;
    }

    public void setTimePassedAnimator(float timePassedAnimator) {
        this.timePassedAnimator = timePassedAnimator;
    }

    public float getAttackCoolDown() {
        return attackCoolDown;
    }

    public void setAttackCoolDown(float attackCoolDown) {
        this.attackCoolDown = attackCoolDown;
    }

    public String getMovingDirection() {
        return movingDirection;
    }

    public void setMovingDirection(String movingDirection) {
        this.movingDirection = movingDirection;
    }

    public int getYAnimateCounter() {
        return YAnimateCounter;
    }

    public void setYAnimateCounter(int YAnimateCounter) {
        this.YAnimateCounter = YAnimateCounter;
    }

    public int getXAnimateCounter() {
        return XAnimateCounter;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public void setXAnimateCounter(int XAnimateCounter) {
        this.XAnimateCounter = XAnimateCounter;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public Image[] getTiles() {
        return tiles;
    }

    public void setTiles(Image[] tiles) {
        this.tiles = tiles;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public int getYdelta() {
        return ydelta;
    }

    public void setYdelta(int ydelta) {
        this.ydelta = ydelta;
    }

    public int getXdelta() {
        return xdelta;
    }

    public void setXdelta(int xdelta) {
        this.xdelta = xdelta;
    }

    public String getFinalAttackingDir() {
        return finalAttackingDir;
    }

    public void setFinalAttackingDir(String finalAttackingDir) {
        this.finalAttackingDir = finalAttackingDir;
    }

    public ArrayList<Tears> getTears() {
        return tears;
    }

    public void setTears(ArrayList<Tears> tears) {
        this.tears = tears;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public String getAttackColour() {
        return attackColour;
    }

    public void setAttackColour(String attackColour) {
        this.attackColour = attackColour;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public ImageView getHeroBody() {
        return heroBody;
    }

    public void setHeroBody(ImageView heroBody) {
        this.heroBody = heroBody;
    }

    public Vecc2f getLocation() {
        return location;
    }

    public void setLocation(Vecc2f location) {
        this.location = location;
    }

    public Vecc2f getVelocity() {
        return velocity;
    }

    public void setVelocity(Vecc2f velocity) {
        this.velocity = velocity;
    }

    public int getInvulnerabilityCooldown() {
        return invulnerabilityCooldown;
    }

    public void setInvulnerabilityCooldown(int invulnerabilityCooldown) {
        this.invulnerabilityCooldown = invulnerabilityCooldown;
    }

    public int getInvulnerabilityCounter() {
        return invulnerabilityCounter;
    }

    public void setInvulnerabilityCounter(int invulnerabilityCounter) {
        this.invulnerabilityCounter = invulnerabilityCounter;
    }

    public Vecc2f getAcceleration() {
        return acceleration;
    }

    public void setAcceleration(Vecc2f acceleration) {
        this.acceleration = acceleration;
    }

    public boolean isNorth() {
        return north;
    }

    public void setNorth(boolean north) {
        this.north = north;
    }

    public boolean isSouth() {
        return south;
    }

    public void setSouth(boolean south) {
        this.south = south;
    }

    public boolean isEast() {
        return east;
    }

    public void setEast(boolean east) {
        this.east = east;
    }

    public boolean isWest() {
        return west;
    }

    public void setWest(boolean west) {
        this.west = west;
    }

    public Vecc2f getCenter() {
        return center;
    }

    public void setCenter(Vecc2f center) {
        this.center = center;
    }
}
