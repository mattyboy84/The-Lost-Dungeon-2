package sample;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class Ray {
    Vecc2f position;
    Vecc2f direction;
    Line line;
    boolean hitSomething;

    public Ray(Vecc2f position, double v) {
        this.position = new Vecc2f(position.x, position.y);
        this.direction = new Vecc2f();
        this.direction.fromAngle(v);
        this.line = new Line(position.x, position.y, position.x + (direction.x * 10), position.y + (direction.y) * 10);
        this.line.setVisible(false);
        this.line.setFill(Color.WHITE);
        this.line.setStroke(Color.WHITE);
    }

    private Vecc2f cast(Hero hero) {
        int smallestDistance = 500;
        float smallPX = 0;
        float smallPY = 0;
        Vecc2f pt = new Vecc2f();
        //for (Boundry boundry : boundries) {
        float x1 = (float) hero.heroHeadHitbox.getBoundsInParent().getMinX();
        float y1 = (float) hero.heroHeadHitbox.getBoundsInParent().getMinY();
        float x2 = (float) hero.heroBodyHitbox.getBoundsInParent().getMaxX();
        float y2 = (float) hero.heroBodyHitbox.getBoundsInParent().getMaxY();
        //
        float x3 = this.position.x;
        float y3 = this.position.y;
        float x4 = this.position.x + this.direction.x;
        float y4 = this.position.y + this.direction.y;
        //
        float den = ((x1 - x2) * (y3 - y4)) - ((y1 - y2) * (x3 - x4));
        float t = (((x1 - x3) * (y3 - y4)) - ((y1 - y3) * (x3 - x4))) / den;
        float u = -(((x1 - x2) * (y1 - y3)) - ((y1 - y2) * (x1 - x3))) / den;
        if (t > 0 && t < 1 && u > 0) {
            pt.x = x1 + (t * (x2 - x1));
            pt.y = y1 + (t * (y2 - y1));
            int distance = (int) Vecc2f.distance(pt.x, pt.y, position.x, position.y);
            if (distance < smallestDistance) {//finds the closest boundary
                smallestDistance = distance;
                smallPX = pt.x;
                smallPY = pt.y;
            }
        }
        //}
        pt.x = smallPX;
        pt.y = smallPY;

        return pt;


    }

    public void updatePos(int x, int y, Hero hero) {
        position.x = x;
        position.y = y;
        this.line.setStartX(x);
        this.line.setStartY(y);

        Vecc2f point = cast(hero);
        if (point.magnitude() == 0) {
            this.line.setEndX(x);
            this.line.setEndY(y);
            this.hitSomething = false;
        } else {
            this.line.setEndX(point.x);
            this.line.setEndY(point.y);
            this.hitSomething = true;
        }

    }

}
