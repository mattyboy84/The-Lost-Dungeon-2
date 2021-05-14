package sample;

import javafx.scene.Group;
import javafx.scene.image.ImageView;

import java.io.*;
import java.util.ArrayList;
public class MapOverlay {

    ArrayList<MapPieces> mapPieces = new ArrayList<>();
    ArrayList<BordorPieces> borderPieces = new ArrayList<>();
    String[][] lookUpTable = new String[256][3];


    boolean loaded = false;
    ImageView indicator = new ImageView("file:src\\game\\map Icons\\Indicator.png");
    Vecc2f indicatorPos = new Vecc2f();
    int[][] newMap = new int[19][19];
    //"src\\game\\map Icons\\a.txt"

    public MapOverlay() {
    }

    public void makeMap(int[][] map, int numberOfRooms, Group group, ArrayList<Room_Neighbours> room_neighbours) {
        //
        String line;
        try {
            FileReader fr = new FileReader("src\\game\\map Icons\\a.txt");
            BufferedReader tr = new BufferedReader(fr);
            for (int i = 0; i < lookUpTable.length - 1; i++) {
                //
                lookUpTable[i] = ((tr.readLine().split(",")));
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
        for (int i = 0; i < 19; i++) {
            for (int j = 0; j < 19; j++) {
                if (map[i][j] >= 1) {
                    this.newMap[i][j] = 1;
                } else {
                    this.newMap[i][j] = 0;
                }
            }
        }
        //
        int localRooms = 0;
        int l = 0;
        int topI = 0;
        int rightJ = 0;
        for (int i = 1; i < 18; i++) {
            for (int j = 1; j < 18; j++) {
                mapPieces.add(new MapPieces(j, i, map[i][j]));//sends 19^2 to the MapPieces class
                borderPieces.add(new BordorPieces(j, i, this.newMap[i][j], this.newMap,lookUpTable));
            }
        }
        for (int i = 0; i < 19; i++) {//this gets the top and right most room into order to move the map to the top right corner
            for (int j = 0; j < 19; j++) {
                if (!(map[i][j] == 0) && l == 0) {
                    topI = i;//highest room
                    l = 1;
                }
                if (!(map[j][i] == 0) /*&&this.newMap[j][i]<10*/) {
                    localRooms++;
                }
                if (localRooms == numberOfRooms) {
                    localRooms++;
                    rightJ = i;//right most room
                }
            }
        }
        float right = mapPieces.get(rightJ).getPosition().x;
        for (MapPieces mapPiece : mapPieces) {
            mapPiece.setPosition((mapPiece.getPosition().x) + (1920 - right) - 15, (mapPiece.getPosition().y) - (64 * topI) + 15);
            if (mapPiece.getPosI() == 9 && mapPiece.getPosJ() == 9) {
                indicatorPos.set(mapPiece.getPosition().x, mapPiece.getPosition().y);
            }
        }
        for (int i = 0; i < borderPieces.size(); i++) {
            borderPieces.get(i).setPosition(mapPieces.get(i).getPosition().x, mapPieces.get(i).getPosition().y);
        }
    }

    public void moveIndicator(String direction) {
        switch (direction) {
            case "up":
                indicatorPos.set(indicatorPos.x, indicatorPos.y - 64);
                break;
            case "down":
                indicatorPos.set(indicatorPos.x, indicatorPos.y + 64);
                break;
            case "left":
                indicatorPos.set(indicatorPos.x - 64, indicatorPos.y);
                break;
            case "right":
                indicatorPos.set(indicatorPos.x + 64, indicatorPos.y);
                break;
        }
        indicator.relocate(indicatorPos.x, indicatorPos.y);
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded, Group group) {
        this.loaded = loaded;
        if (this.loaded) {
            load(group);
        } else {
            unload(group);
        }
    }

    public void load(Group group) {
        System.out.println("map loaded");
        for (MapPieces mapPiece : mapPieces) {
            if (mapPiece.getMapPiece() != null) {
                group.getChildren().add(mapPiece.getMapPiece());
                mapPiece.getMapPiece().relocate(mapPiece.getPosition().x, mapPiece.getPosition().y);
                mapPiece.getMapPiece().setViewOrder(-9);
                mapPiece.getMapPiece().setOpacity(0.6);
            }
        }
        group.getChildren().add(indicator);
        indicator.relocate(indicatorPos.x, indicatorPos.y);
        indicator.setViewOrder(-10);
        indicator.setOpacity(0.6);
        //
        for (BordorPieces mapPiece : borderPieces) {
            if (mapPiece.getMapPiece() != null) {
                group.getChildren().add(mapPiece.getMapPiece());
                mapPiece.getMapPiece().relocate(mapPiece.getPosition().x, mapPiece.getPosition().y);
                mapPiece.getMapPiece().setViewOrder(-9);
                mapPiece.getMapPiece().setOpacity(0.6);
            }
        }
    }

    public void unload(Group group) {
        System.out.println("map unloaded");
        for (MapPieces mapPiece : mapPieces) {
            group.getChildren().remove(mapPiece.getMapPiece());
        }
        for (BordorPieces mapPiece : borderPieces) {
            group.getChildren().remove(mapPiece.getMapPiece());
        }
        group.getChildren().remove(indicator);
    }

    public ImageView getIndicator() {
        return indicator;
    }


    public Vecc2f getIndicatorPos() {
        return indicatorPos;
    }

    public void setIndicatorPos(float x, float y) {
        this.indicatorPos.set(x, y);
    }

    public void clear() {
        mapPieces.clear();
    }
}