package sample;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

public class Background {
    int roomX, roomY;
    Random rand = new Random();
    Image backgroundImage;

    public Background(int roomX, int roomY, int room) {
        //roomX and roomY are indicates for the 2D array
        //room will be 1,2 or 3
        switch (room) {
            case 1://normal room
                switch (rand.nextInt(7)) {
                    case 0:
                        this.backgroundImage = new Image("file:src\\game\\newFloors\\blood_floor.png");//room with blood    14%
                        break;
                    case 1:
                        this.backgroundImage = new Image("file:src\\game\\newFloors\\rope_floor.png");//room with rope on walls     14%
                        break;
                    case 2:
                        this.backgroundImage = new Image("file:src\\game\\newFloors\\scratch_floor.png");//room with wall carvings     14%
                        break;
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                        this.backgroundImage = new Image("file:src\\game\\newFloors\\default_floor.png");//basic room       58%
                        break;
                }
                break;
            case 2://shop
                this.backgroundImage = new Image("file:src\\game\\newFloors\\ShopFloor.png");//shop
                break;
            case 3://boss
                this.backgroundImage = new Image("file:src\\game\\newFloors\\BossFloor.png");//boss
                break;
        }
        this.roomX = roomX;
        this.roomY = roomY;
    }

    public int getRoomX() {
        return roomX;
    }

    public void setRoomX(int roomX) {
        this.roomX = roomX;
    }

    public int getRoomY() {
        return roomY;
    }

    public void setRoomY(int roomY) {
        this.roomY = roomY;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public Image getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(Image backgroundImage) {
        this.backgroundImage = backgroundImage;
    }
}
