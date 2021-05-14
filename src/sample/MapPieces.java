package sample;

import javafx.scene.image.ImageView;
import sample.Vecc2f;

public class MapPieces {

    ImageView mapPiece;
    int roomType;
    Vecc2f position;
    int posI;
    int posJ;


    public MapPieces(int i, int j, int room) {
        posI = i;
        posJ = j;
        this.position = new Vecc2f((64 * i), (64 * j));
        this.roomType = room;
        this.mapPiece = new ImageView("file:src\\game\\map Icons\\" + room + ".png");
    }

    public ImageView getMapPiece() {
        return mapPiece;
    }

    public void setMapPiece(ImageView mapPiece) {
        this.mapPiece = mapPiece;
    }

    public Vecc2f getPosition() {
        return position;
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

    public void setPosition(float x, float y) {
        this.position.set(x, y);
    }

}
