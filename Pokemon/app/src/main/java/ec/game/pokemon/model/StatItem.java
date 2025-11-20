package ec.game.pokemon.model;

import java.io.Serializable;

public class StatItem implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
