package com.daar.SeachEngineAPI.utils;

import java.util.Objects;

public class RegExTreeParser {

    private final RegExTree regExTree;

    public RegExTreeParser(RegExTree ret) {
        Objects.requireNonNull(ret);
        this.regExTree = ret;
    }

    public Automaton parse() {
        return Automaton.transform(regExTree);
    }
}
