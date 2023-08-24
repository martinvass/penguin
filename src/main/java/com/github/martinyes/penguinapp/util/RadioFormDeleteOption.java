package com.github.martinyes.penguinapp.util;

import lombok.Getter;
import lombok.Setter;

/**
 * A class representing a form for selecting a delete option in a radio form.
 */
@Getter
@Setter
public class RadioFormDeleteOption {

    /**
     * The selected delete option.
     */
    private DeleteOption deleteOption;
}