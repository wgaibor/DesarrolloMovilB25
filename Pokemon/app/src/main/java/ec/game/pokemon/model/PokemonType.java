package ec.game.pokemon.model;

import java.io.Serializable;

public class PokemonType implements Serializable {
    private int slot;
    private Type type;

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
