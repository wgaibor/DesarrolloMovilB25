package ec.game.pokemon.model;

import java.io.Serializable;

public class PokemonAbilities implements Serializable {
    private AbilityItems ability;

    public AbilityItems getAbility() {
        return ability;
    }

    public void setAbility(AbilityItems ability) {
        this.ability = ability;
    }
}
