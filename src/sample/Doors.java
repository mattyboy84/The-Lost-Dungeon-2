package sample;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;

public class Doors {

    ImageView door;
    String direction;
    String doorType;
    Vecc2f location;
    Vecc2f doorEffectLocation;
    int roomType;
    String state;
    int arrayPos;
    String[] doorsList = {"door", "SpecialDoor", "BossDoor"};
    ImageView doorEffect;


    public Doors(String direction, int roomType, int currentRoomType, int roomX, int roomY, ArrayList<Doors> doors) {
        this.arrayPos = doors.size();
        if ((roomX == 9 && roomY == 9) || currentRoomType != 1) {
            this.state = "open";
        } else {
            this.state = "closed";
        }
        this.direction = direction;
        this.roomType = roomType;
        //
        this.doorType = doorsList[roomType - 1];
        //
        //if (this.doorType.equals("BossDoor")) {
        //    doorEffect = new ImageView("file:src\\game\\doors\\" + "BossDoor_" + this.direction + "_glow" + ".png");
        //    this.doorEffect.setVisible(false);
        //}
        //
        if (currentRoomType != 1) {
            this.doorType = doorsList[currentRoomType - 1];
        }
        //
        this.door = new ImageView("file:src\\game\\doors\\" + this.doorType + "_" + this.direction + "_" + this.state + ".png");
        switch (direction) {
            case "up":
                this.location = new Vecc2f((float) ((1920 / 2) - (this.door.getLayoutBounds().getWidth() / 2)), 0);
                //this.doorEffectLocation = new Vecc2f(this.location.x, (float) (this.location.y+(this.door.getBoundsInParent().getHeight()*0.9)));
                break;
            case "down":
                this.location = new Vecc2f((float) ((1920 / 2) - (this.door.getLayoutBounds().getWidth() / 2)), (float) (1080 - this.door.getLayoutBounds().getHeight()));
                //this.doorEffectLocation = new Vecc2f(this.location.x, (float) (this.location.y-(this.door.getBoundsInParent().getHeight()*0.9)));
                break;
            case "left":
                this.location = new Vecc2f(0, (float) ((1080 / 2) - (this.door.getLayoutBounds().getHeight() / 2)));
                //this.doorEffectLocation = new Vecc2f((float) ((float)this.location.x+(this.door.getBoundsInParent().getWidth())*0.9), this.location.y);
                break;
            case "right":
                this.location = new Vecc2f((float) (1920 - this.door.getLayoutBounds().getWidth()), (float) ((1080 / 2) - (this.door.getLayoutBounds().getHeight() / 2)));
                //this.doorEffectLocation = new Vecc2f((float) ((float)this.location.x-(this.door.getBoundsInParent().getWidth()*0.9)), this.location.y);
                break;
        }
    }

    public void open() {
        if (this.state.equals("closed")) {
            this.state = "open";
            this.door.setImage(new Image("file:src\\game\\doors\\" + this.doorType + "_" + this.direction + "_" + this.state + ".png"));
        }
    }

    public void close() {
        if (this.state.equals("open")) {
            this.state = "closed";
            this.door.setImage(new Image("file:src\\game\\doors\\" + this.doorType + "_" + this.direction + "_" + this.state + ".png"));
        }
    }

    public void unload(Group group) {
        group.getChildren().remove(this.door);
    }

    public void load(Group group, ArrayList<Doors> doors) {
        group.getChildren().add(this.door);
        this.door.relocate(this.location.x, this.location.y);
        this.door.setViewOrder(-2);
    }

    public ImageView getDoor() {
        return door;
    }

    public void setDoor(ImageView door) {
        this.door = door;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDoorType() {
        return doorType;
    }

    public void setDoorType(String doorType) {
        this.doorType = doorType;
    }

    public Vecc2f getLocation() {
        return location;
    }

    public void setLocation(Vecc2f location) {
        this.location = location;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

}
