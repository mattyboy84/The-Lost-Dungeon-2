package sample;

import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Texts {
    Text text;
    Vecc2f position = new Vecc2f();
    //name 252 x 47
    public Texts(String input,float x, float y, Color fontColor, int fontSize) {
        this.text = new Text(input);
        this.text.setFont(javafx.scene.text.Font.font("Upheaval", FontWeight.BOLD, fontSize));//font and such
        this.text.setFill(fontColor);
        this.position.set(x, y);
        this.text.setViewOrder(-10);
    }


    public void setText(String input) {
        this.text.setText(input);
    }


    public Text getText() {
        return text;
    }

    public void setPosition(float v, float v1) {
        this.position.set(v,v1);
        this.text.relocate(v,v1);
    }
}
