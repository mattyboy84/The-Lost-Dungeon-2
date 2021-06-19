package sample;

import animatefx.animation.*;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


import java.util.ArrayList;
import java.util.Random;

//||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
public class Main extends Application {
    int WIDTH = 1920;
    int HEIGHT = 1080;
    Group group = new Group();
    Scene scene = new Scene(group, (int) (WIDTH * 1), (int) (HEIGHT * 1));
    int[][] map = new int[19][19];
    Random rand = new Random();
    ArrayList<Room_Neighbours> room_neighbours = new ArrayList<>();
    ArrayList<Room> rooms = new ArrayList<>();
    ImageView background = new ImageView("file:src\\game\\floors\\floor2.png");
    int positionX = 0;
    int positionY = 0;
    MapOverlay mapOverlay = new MapOverlay();
    Hero hero = new Hero();

//io.github.typhon0:AnimateFX:1.2.1


    @Override
    public void start(Stage stage) throws InterruptedException {
        /*
        System.out.println("start");
        Thread.sleep(10000);
        System.out.println("end");
*/


        hero.newHero(group, rooms, background, mapOverlay, hero);
        group.getChildren().add(background);
        background.toBack();
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W://move up
                    if (!hero.inventory.loaded) {
                        hero.setNorth(true);
                    } else {
                        hero.inventory.moveSelect("up");
                    }
                    break;
                case A://move left
                    if (!hero.inventory.loaded) {
                        hero.setWest(true);
                    } else {
                        hero.inventory.moveSelect("left");
                    }
                    break;
                case S://move down
                    if (!hero.inventory.loaded) {
                        hero.setSouth(true);
                    } else {
                        hero.inventory.moveSelect("down");
                    }
                    break;
                case D://move right
                    if (!hero.inventory.loaded) {
                        hero.setEast(true);
                    } else {
                        hero.inventory.moveSelect("right");
                    }
                    break;
                case UP://shoot up
                    //hero.shoot();
                    hero.setShootingNorth(true);
                    break;
                case DOWN://shoot down
                    //hero.shoot();
                    hero.setShootingSouth(true);
                    break;
                case LEFT://shoot left
                    //hero.shoot();
                    hero.setShootingWest(true);
                    break;
                case RIGHT://shoot right
                    //hero.shoot();
                    hero.setShootingEast(true);
                    break;
                case N:
                    System.out.println("Size:" + room_neighbours.size());
                    for (Room_Neighbours room_neighbour : room_neighbours) {
                        System.out.println("neighbour at " + room_neighbour.getJ() + " " + room_neighbour.getI());
                    }
                    break;
                case K:
                    System.out.println("O  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 COLUMN/ J");//outputs the map
                    for (int[] ints : map) {
                        for (int j = 0; j < map.length; j++) {
                            System.out.print(ints[j] + "  ");
                        }
                        System.out.println("");
                    }
                    for (Room room : rooms) {
                        System.out.println("X: " + room.getRoomX() + " Y: " + room.getRoomY() + " Room type " + room.getRoomType() + " " + room.enemies.size() + " enemies ");
                    }
                    System.out.println("there are " + rooms.size() + " rooms");
                    System.out.println(hero.getPosX() + " " + hero.getPosY());
                    break;
                case F:
                    stage.setFullScreen(!stage.isFullScreen());
                    break;
                case TAB:
                    hero.inventory.setLoaded(!hero.inventory.isLoaded(), group);
                    break;
                case E:
                    if (hero.inventory.isLoaded()) {
                        //using an item in your inventory
                        hero.inventory.useItem(group, hero);
                    } else if (!hero.inventory.isLoaded()){
                        hero.room.checkItems(hero, hero.inventory, group);
                    }
                    break;
                case M:
                    mapOverlay.setLoaded(!mapOverlay.isLoaded(), group);
                    break;
                case O:
                    //increase health
                    hero.increaseHealth(1);
                    break;
                case L:
                    //decrease health
                    hero.decreaseHealth(1);
                    break;
                case P:
                    hero.addHeart(group);
                    break;
                case G:
                    //shakes left to right a few times
                    //new Shake(hero.room.boss.boss_gurdyjr.get(0).getCurrentFace()).play();
                    //new Shake(hero.room.boss.boss_gurdyjr.get(0).bossImage).play();
                    //
                    new RubberBand(hero.room.boss.boss_gurdyjr.get(0).getCurrentFace()).play();
                    new RubberBand(hero.room.boss.boss_gurdyjr.get(0).bossImage).play();
                    //System.out.println("");

                    break;
                case H:
                    //System.out.println(hero.health.health);
                    break;
                case ESCAPE:
                    break;
                case J:
                    hero.room.openDoors();
                    break;
                case U:
                    hero.room.closeDoors();
                    break;
            }
        });
        scene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case A:
                    hero.setWest(false);
                    hero.acceleration.set(0, 0);
                    break;
                case D:
                    hero.setEast(false);
                    hero.acceleration.set(0, 0);
                    break;
                case W:
                    hero.setNorth(false);
                    hero.acceleration.set(0, 0);
                    break;
                case S:
                    hero.setSouth(false);
                    hero.acceleration.set(0, 0);
                    break;
                case UP://shoot up
                    hero.setShootingNorth(false);
                    break;
                case DOWN://shoot down
                    hero.setShootingSouth(false);
                    break;
                case LEFT://shoot left
                    hero.setShootingWest(false);
                    break;
                case RIGHT://shoot right
                    hero.setShootingEast(false);
                    break;
            }
        });
        int localRooms;
        do {
            for (int i = 0; i < map.length; i++) {
                for (int j = 0; j < map.length; j++) {
                    map[i][j] = 0;
                }
            }
            room_neighbours.clear();
            localRooms = mapMaker(map);
            System.out.println(localRooms);
        } while (localRooms < 20);
        finalDungeonCreator();
        mapOverlay.makeMap(map, localRooms, group, room_neighbours);
        //
        stage.setScene(scene);
        stage.setFullScreenExitHint("");
        stage.setFullScreen(true);
        stage.setTitle("The Lost Dungeon");



        stage.show();
    }

    private void finalDungeonCreator() {
        System.out.println("O  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 COLUMN/ J");//outputs the map
        for (int[] ints : map) {
            for (int j = 0; j < map.length; j++) {
                System.out.print(ints[j] + "  ");
            }
            System.out.println("");
        }
        for (int i = 1; i < map.length - 1; i++) {
            for (int j = 1; j < map.length - 1; j++) {
                if (map[i][j] != 0) {
                    rooms.add(new Room(i, j, map[i][j], map[i + 1][j], map[i - 1][j], map[i][j + 1], map[i][j - 1], hero,group));
                }
            }
        }
        try {
            roomLoader();
            //hero.heroHitboxDetector();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void roomLoader() throws InterruptedException {
        for (Room room : rooms) {
            if (room.getRoomX() == positionX && room.getRoomY() == positionY) {
                room.load(background, group);
                hero.setRoom(room);
            }
        }
    }

    private int mapMaker(int[][] map) {
        //System.out.println(map.length);
        int localRooms = 0;
        int distanceFromNodeX = 0;
        int distanceFromNodeY = 0;
        map[(int) (map.length / 2)][(int) (map.length / 2)] = 1;//starting room
        System.out.println(map.length / 2);
        positionX = map.length / 2;
        positionY = map.length / 2;
        //
        switch (rand.nextInt(2)) {
            case 0:
                distanceFromNodeX = (rand.nextInt(2) * 2) - 1;//left or right
                distanceFromNodeY = 0;
                break;
            case 1:
                distanceFromNodeX = 0;
                distanceFromNodeY = (rand.nextInt(2) * 2) - 1;//up or down
                break;
        }
        int NoOfRooms = 1;
        mapGenerator(map, distanceFromNodeX, distanceFromNodeY, rand, NoOfRooms);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 1) {
                    neighbourAdder(i, j);
                }
            }
        }
        //choses an empty neighbour to be the boss room
        int randBossRoom = rand.nextInt(room_neighbours.size() - 1);
        map[room_neighbours.get(randBossRoom).getI()][room_neighbours.get(randBossRoom).getJ()] = 3;
        neighbourAdder(room_neighbours.get(randBossRoom).getI(), room_neighbours.get(randBossRoom).getJ());
        room_neighbours.remove(randBossRoom);
        localRooms++;
        //chooses an empty neighbour to be the shop room
        int randShopRoom = rand.nextInt(room_neighbours.size() - 1);
        map[room_neighbours.get(randShopRoom).getI()][room_neighbours.get(randShopRoom).getJ()] = 2;
        neighbourAdder(room_neighbours.get(randShopRoom).getI(), room_neighbours.get(randShopRoom).getJ());
        room_neighbours.remove(randShopRoom);
        localRooms++;


        //System.out.println("O  1  2  3  4  5  6  7  8  9  10 11 12 13 14 15 16 17 18 COLUMN/ J");//outputs the map
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map.length; j++) {
                if (map[i][j] == 5) {
                    map[i][j] = 0;
                }
                if (map[i][j] == 1) {
                    localRooms++;
                }
                //System.out.print(map[i][j] + "  ");
            }
            //System.out.println("");
        }
        return localRooms;
    }

    private void neighbourAdder(int i, int j) {
        try {
            if (map[i + 1][j] == 0) {
                room_neighbours.add(new Room_Neighbours(i + 1, j));
                map[i + 1][j] = 5;
            }
            if (map[i - 1][j] == 0) {
                room_neighbours.add(new Room_Neighbours(i - 1, j));
                map[i - 1][j] = 5;
            }
            if (map[i][j + 1] == 0) {
                room_neighbours.add(new Room_Neighbours(i, j + 1));
                map[i][j + 1] = 5;
            }
            if (map[i][j - 1] == 0) {
                room_neighbours.add(new Room_Neighbours(i, j - 1));
                map[i][j - 1] = 5;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e);
        }
    }

    private static void mapGenerator(int[][] map, int distanceFromNodeX, int distanceFromNodeY, Random rand, int NoOfRooms) {//creates the map
        if (NoOfRooms < 30) {//when this is false the recursion ends therefore the map is generated.
            if (9 + distanceFromNodeX == 3 || 9 + distanceFromNodeX == map.length - 3) {
                distanceFromNodeX = 0;                distanceFromNodeY = 0;

            }
            if (9 + distanceFromNodeY == 3 || 9 + distanceFromNodeY == map.length - 3) {
                distanceFromNodeY = 0;  distanceFromNodeX = 0;
            }
            try {
                int direction = rand.nextInt(2);
                map[9 + distanceFromNodeX][9 + distanceFromNodeY] = 1;
                NoOfRooms++;
                switch (direction) {//randomly chooses a direction
                    case 0:
                        distanceFromNodeX = distanceFromNodeX + (rand.nextInt(2) * 2) - 1;
                        break;
                    case 1:
                        distanceFromNodeY = distanceFromNodeY + (rand.nextInt(2) * 2) - 1;
                        break;
                }
                mapGenerator(map, distanceFromNodeX, distanceFromNodeY, rand, NoOfRooms);
            } catch (Exception e) {
                distanceFromNodeX = 0;
                distanceFromNodeY = 0;
            }
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}