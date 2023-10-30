package me.fixeddev.commandflow.input;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringSpaceTokenizer implements InputTokenizer {

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> tokenize(String line) {
        return new ArrayList<>(Arrays.asList(line.split(" ", -1)));
    }

}
