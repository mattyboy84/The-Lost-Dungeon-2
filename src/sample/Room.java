package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class Room {

    Background background;
    Enemy enemies = new Enemy();
    Boss boss = new Boss();
    Background_Items background_items;
    //
    ArrayList<Items> items = new ArrayList<>();
    ArrayList<Doors> doors = new ArrayList<>();


    ImageView northDoor;
    ImageView eastDoor;
    ImageView southDoor;
    ImageView westDoor;
    int roomX;
    int roomY;
    int roomType;
    Random rand = new Random();

    public Room(int roomX, int roomY, int roomType, int down, int up, int right, int left, Hero hero, Group group) {
        this.background = new Background(roomX, roomY, roomType);
        this.background_items = new Background_Items();
        this.roomType = roomType;
        //
        switch (this.roomType) {
            case 1://normal room
                if (!(roomX == 9 && roomY == 9)) {
                    for (int i = 0; i < 4; i++) {
                        this.enemies.addNewEnemy(hero, enemies);
                    }
                }
                for (int i = 0; i < 1; i++) {
                    this.items.add(new Items(this.items));
                }
                background_items.addButterflys();
                background_items.addDebris();
                break;
            case 2://shop room
                //this.items.add(new Items(roomX, roomY, "staff", this.items));
                //this.items.add(new Items(roomX, roomY, "map", this.items));
                //this.items.add(new Items(roomX, roomY, "attack boost", this.items));
                break;
            case 3://boss room
                this.boss.addnewEnemy(hero, boss,background_items,group);
                break;
        }
        //
        if (down != 0) {//below door
            doors.add(new Doors("down", down, roomType, roomX, roomY, doors));
        }
        //
        if (up != 0) {//above door
            doors.add(new Doors("up", up, roomType, roomX, roomY, doors));
        }
        //
        if (left != 0) {//left door
            doors.add(new Doors("left", left, roomType, roomX, roomY, doors));
        }
        //
        if (right != 0) {//right
            doors.add(new Doors("right", right, roomType, roomX, roomY, doors));
        }
        //
        this.roomX = roomX;
        this.roomY = roomY;
    }

    public void checkItems(Hero hero, Inventory inventory, Group group) {
        for (int i = 0; i < items.size(); i++) {
            items.get(i).check(hero, inventory, this.items, group);
        }
    }

    public void openDoors() {
        for (Doors door : doors) {
            door.open();
        }
    }

    public void closeDoors() {
        for (Doors door : doors) {
            door.close();
        }
    }

    public void load(ImageView background, Group group) {
        //System.out.println("Entering " + this.roomType + " Boss size of " + this.boss.size());
        background.setImage(this.background.getBackgroundImage());
        background.setViewOrder(1);
        for (Items item : this.items) {
            item.load(group);
        }
        for (Doors door : this.doors) {
            door.load(group, doors);
        }
        boss.load(group);
        //System.out.println(this.boss.size());
        if (this.boss.size() > 0) {
            closeDoors();
        }
        //
        enemies.load(group);
        //
        background_items.load(group);
    }

    public void unload(Group group) {
        for (Items item : items) {
            item.unload(group);
        }
        for (Doors door : doors) {
            door.unload(group);
        }
        boss.unload(group);
        //
        enemies.unload(group);
        //
        background_items.unload(group);
    }

    public Background getBackground() {
        return background;
    }

    public ArrayList<Doors> getDoors() {
        return doors;
    }

    public void setDoors(ArrayList<Doors> doors) {
        this.doors = doors;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public void setBackground(Background background) {
        this.background = background;
    }

    public ArrayList<Items> getItems() {
        return items;
    }

    public void setItems(ArrayList<Items> items) {
        this.items = items;
    }

    public ImageView getNorthDoor() {
        return northDoor;
    }

    public void setNorthDoor(ImageView northDoor) {
        this.northDoor = northDoor;
    }

    public Background_Items getBackground_items() {
        return background_items;
    }

    public void setBackground_items(Background_Items background_items) {
        this.background_items = background_items;
    }

    public ImageView getEastDoor() {
        return eastDoor;
    }

    public void setEastDoor(ImageView eastDoor) {
        this.eastDoor = eastDoor;
    }

    public ImageView getSouthDoor() {
        return southDoor;
    }

    public void setSouthDoor(ImageView southDoor) {
        this.southDoor = southDoor;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public ImageView getWestDoor() {
        return westDoor;
    }

    public void setWestDoor(ImageView westDoor) {
        this.westDoor = westDoor;
    }

    public int getRoomX() {
        return roomX;
    }

    public Enemy getEnemies() {
        return enemies;
    }

    public void setEnemies(Enemy enemies) {
        this.enemies = enemies;
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
}
