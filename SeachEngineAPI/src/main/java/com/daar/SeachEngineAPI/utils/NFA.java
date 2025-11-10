package daar.projet1;

import java.util.*;

public class NFA {

    public static class State {
        public int id;
        public Map<Character, List<State>> transitions = new HashMap<>();
        public List<State> epsilonTransitions = new ArrayList<>();
        public State(int id) { this.id = id; }
    }

    private State start;
    private State end;
    private static HashSet<Character> alphabet = new HashSet<>();
    private static int stateCounter = 0;

    public NFA(State start, State end) {
        this.start = start;
        this.end = end;
    }

    public State getStart(){
        return this.start;
    }

    public State getEnd(){
        return this.end;
    }

    public HashSet<Character> getAlphabet(){
        return NFA.alphabet;
    }

    public static NFA treeToNFA(RegExNode tree) {
        if (tree == null) {
            return null;
        }
    
        if (tree instanceof RegExNode.ASCIINode) {
            // Single character node (a)
            RegExNode.ASCIINode asciiNode = (RegExNode.ASCIINode) tree;
            State start = new State(stateCounter++);
            State end = new State(stateCounter++);
            start.transitions.putIfAbsent(asciiNode.toRegEx().charAt(0), new ArrayList<>());
            start.transitions.get(asciiNode.getChar()).add(end);
            alphabet.add(asciiNode.getChar());
            return new NFA(start, end);
        } else if (tree instanceof RegExNode.ConcatenationNode) {
            // Concatenation node (AB)
            RegExNode.ConcatenationNode concatNode = (RegExNode.ConcatenationNode) tree;
            NFA leftNFA = treeToNFA(concatNode.getChildLeft());
            NFA rightNFA = treeToNFA(concatNode.getChildRight());
            leftNFA.end.epsilonTransitions.add(rightNFA.start);
            return new NFA(leftNFA.start, rightNFA.end);
        } else if (tree instanceof RegExNode.AlternationNode) {
            // Alternation node (|)
            RegExNode.AlternationNode alternationNode = (RegExNode.AlternationNode) tree;
            NFA leftNFA = treeToNFA(alternationNode.getChildLeft());
            NFA rightNFA = treeToNFA(alternationNode.getChildRight());
            State start = new State(stateCounter++);
            State end = new State(stateCounter++);
            start.epsilonTransitions.add(leftNFA.start);
            start.epsilonTransitions.add(rightNFA.start);
            leftNFA.end.epsilonTransitions.add(end);
            rightNFA.end.epsilonTransitions.add(end);
            return new NFA(start, end);
        } else if (tree instanceof RegExNode.StarNode) {
            // Star node (*)
            RegExNode.StarNode starNode = (RegExNode.StarNode) tree;
            NFA subNFA = treeToNFA(starNode.getChild());
            State start = new State(stateCounter++);
            State end = new State(stateCounter++);
            start.epsilonTransitions.add(subNFA.start);
            start.epsilonTransitions.add(end);
            subNFA.end.epsilonTransitions.add(subNFA.start);
            subNFA.end.epsilonTransitions.add(end);
            return new NFA(start, end);
        } else if (tree instanceof RegExNode.DotNode) {
            // Dot node (.)
            State start = new State(stateCounter++);
            State end = new State(stateCounter++);
            start.transitions.putIfAbsent('.', new ArrayList<>()); // Using '.' to represent "any character"
            start.transitions.get('.').add(end);
            return new NFA(start, end);
        }else if (tree instanceof RegExNode.ParenthesisNode) {
            // Parenthesis node
            RegExNode.ParenthesisNode parenNode = (RegExNode.ParenthesisNode) tree;
            return treeToNFA(parenNode.getChild());
        }
    
        throw new IllegalArgumentException("Unsupported RegExNode type");
    }

}