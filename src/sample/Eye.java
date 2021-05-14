package sample;

import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Eye {
    Circle circle;
    Vecc2f position;
    ArrayList<Ray> rays = new ArrayList<>();

    public Eye(double i2, double i1, int num, Hero hero) {

        this.position = new Vecc2f((float)i2, (float)i1);
        this.circle = new Circle(i2, i1, 1);
        this.circle.setVisible(false);
        //
        for (int i = 0; i < num; i++) {
            rays.add(new Ray(position, Math.toRadians((float) (360 / num) * i)));
        }
    }


    public void relocate(int x, int y,Hero hero) {
        circle.setCenterX(x);
        circle.setCenterY(y);
        for (Ray ray : rays) {
            ray.updatePos(x, y,hero);
        }
    }
}
