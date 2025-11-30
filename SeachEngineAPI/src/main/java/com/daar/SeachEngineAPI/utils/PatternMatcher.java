package com.daar.SeachEngineAPI.utils;

import java.util.Objects;

public class PatternMatcher {

    private final Automaton automaton;

    public PatternMatcher(String pattern) {
        Objects.requireNonNull(pattern);
        try {
            RegExParser rep = new RegExParser(pattern);
            RegExTree ret = rep.parse();
            RegExTreeParser rtp =  new RegExTreeParser(ret);
            this.automaton = rtp.parse().toDFA();
        } catch (Exception e) {
            throw new IllegalArgumentException("PatterMatcher: " + pattern + ": Wrong pattern given");
        }
    }

    public boolean matches(String text) {
        automaton.reset();
        for (char c : text.toCharArray()) {
            automaton.readCharacter(c);
        }
        return automaton.isAccepted();
    }

    @Override
    public String toString() {
        return automaton.toString();
    }
}
