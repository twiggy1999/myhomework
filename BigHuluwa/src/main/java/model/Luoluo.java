package model;

import Service.Field;
import javafx.scene.image.Image;

public class Luoluo extends Creature{
    private int x;
    private int y;
    private State_of_Creature state;
    private Field battleField;
    public Luoluo() {
    }

    public Luoluo(String image_route, int x, int y) {
        this.state= State_of_Creature.ALIVE;
        this.setImage(new Image(image_route));
        this.x = x;
        this.y = y;
        this.setId("yeye1");
        this.setFitHeight(58.0);

        this.setFitWidth(29.0);
        this.setLayoutX(x);
        this.setLayoutY(y);
        this.setPickOnBounds(true);
        this.setPreserveRatio(true);
        this.state= State_of_Creature.ALIVE;
    }
}
