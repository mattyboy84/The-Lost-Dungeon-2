package sample;


import javafx.scene.image.ImageView;
import sample.Vecc2f;

public class BordorPieces {

    ImageView mapPiece;
    int roomType;
    Vecc2f position;
    int posI;
    int posJ;
    int[][] states = new int[19][19];
    int bordorH=64;
    int bordorW = 64;

    public BordorPieces(int j, int i, int currentRoom, int[][] newMap, String[][] lookUpTable) {
        this.posI = i;
        this.posJ = j;
        this.position = new Vecc2f((bordorW * j), (bordorH * i));

        if (currentRoom == 0) {
            states[this.posI][this.posJ] = stateGetter(newMap[this.posI + 1][this.posJ - 1], newMap[this.posI + 1][this.posJ], newMap[this.posI + 1][this.posJ + 1], newMap[this.posI][this.posJ + 1], newMap[this.posI - 1][this.posJ + 1], newMap[this.posI - 1][this.posJ], newMap[this.posI - 1][this.posJ - 1], newMap[this.posI][this.posJ - 1]);
            if (states[this.posI][this.posJ] != 0) {
                this.mapPiece = new ImageView("file:src\\game\\map Icons\\uniquer bordor\\" + lookUpTable[states[this.posI][this.posJ]-1][1] + ".png");
                this.mapPiece.rotateProperty().set(Double.parseDouble(lookUpTable[states[this.posI][this.posJ]-1][2]));
            }
        }
    }

    private int stateGetter(int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
        return ((i) + (i1 * 2) + (i2 * 4) + (i3 * 8) + (i4 * 16) + (i5 * 32) + (i6 * 64) + (i7 * 128));
    }

    public ImageView getMapPiece() {
        return mapPiece;
    }

    public void setMapPiece(ImageView mapPiece) {
        this.mapPiece = mapPiece;
    }

    public int getRoomType() {
        return roomType;
    }

    public void setRoomType(int roomType) {
        this.roomType = roomType;
    }

    public Vecc2f getPosition() {
        return position;
    }

    public void setPosition(Vecc2f position) {
        this.position = position;
    }

    public int getPosI() {
        return posI;
    }

    public void setPosI(int posI) {
        this.posI = posI;
    }

    public int getPosJ() {
        return posJ;
    }

    public void setPosJ(int posJ) {
        this.posJ = posJ;
    }

    public void setPosition(float v, float v1) {
        this.position.set(v, v1);
    }
}