package model;

import Service.Field;
import javafx.scene.image.Image;

public class Grandpa extends Creature{
    private int x;
    private int y;
    private State_of_Creature state;
    private Field battleField;
    public Grandpa() {
    }

    public Grandpa(String image_route, int x, int y) {
        this.state= State_of_Creature.ALIVE;
        this.setImage(new Image(image_route));
        this.x = x;
        this.y = y;
        this.setId("yeye1");
        this.setFitHeight(58.0);
        this.state= State_of_Creature.ALIVE;

        this.setFitWidth(29.0);
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPickOnBounds(true);
        this.setPreserveRatio(true);
    }
}
