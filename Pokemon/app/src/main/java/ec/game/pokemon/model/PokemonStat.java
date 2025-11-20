package ec.game.pokemon.model;

import java.io.Serializable;

public class PokemonStat implements Serializable {
    private int base_stat;
    private StatItem stat;

    public int getBase_stat() {
        return base_stat;
    }

    public void setBase_stat(int base_stat) {
        this.base_stat = base_stat;
    }

    public StatItem getStat() {
        return stat;
    }

    public void setStat(StatItem stat) {
        this.stat = stat;
    }
}
