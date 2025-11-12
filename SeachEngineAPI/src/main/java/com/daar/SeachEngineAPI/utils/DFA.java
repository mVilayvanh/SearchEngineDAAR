package com.daar.SeachEngineAPI.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class DFA {

    public static class State {
        public int id;
        public Map<Character, State> transitions = new HashMap<>();
        public boolean isFinal;

        public State(int id, boolean isFinal) {
            this.id = id;
            this.isFinal = isFinal;
        }

        @Override
        public String toString() {
            return "q" + id + (isFinal ? " (final)" : "");
        }
    }

    public State start;
    public List<State> states = new ArrayList<>();

    private static int dfaStateCounter = 0;

    public static DFA nfaToDFA(NFA nfa) {
        DFA dfa = new DFA();
        Map<Set<NFA.State>, State> dfaStateMap = new HashMap<>();
        Queue<Set<NFA.State>> queue = new LinkedList<>();

        // Step 1: Compute ε-closure of the start state
        Set<NFA.State> startClosure = epsilonClosure(Set.of(nfa.getStart()));
        State dfaStartState = new State(dfaStateCounter++, startClosure.contains(nfa.getEnd()));
        dfa.states.add(dfaStartState);
        dfa.start = dfaStartState;
        dfaStateMap.put(startClosure, dfaStartState);
        queue.add(startClosure);

        while (!queue.isEmpty()) {
            Set<NFA.State> currentNFAStates = queue.poll();
            State currentDFAState = dfaStateMap.get(currentNFAStates);

            // Collect all possible transition symbols from current NFA states
            Set<Character> symbols = new HashSet<>();
            for (NFA.State nfaState : currentNFAStates) {
                symbols.addAll(nfaState.transitions.keySet());
            }

            for (char symbol : symbols) {
                Set<NFA.State> nextStates = new HashSet<>();
                for (NFA.State nfaState : currentNFAStates) {
                    List<NFA.State> targets = nfaState.transitions.get(symbol);
                    if (targets != null) {
                        nextStates.addAll(targets);
                    }
                }
                // Apply ε-closure
                Set<NFA.State> closure = epsilonClosure(nextStates);

                if (!dfaStateMap.containsKey(closure)) {
                    boolean isFinal = closure.contains(nfa.getEnd());
                    State newDFAState = new State(dfaStateCounter++, isFinal);
                    dfa.states.add(newDFAState);
                    dfaStateMap.put(closure, newDFAState);
                    queue.add(closure);
                }

                // Add the transition
                currentDFAState.transitions.put(symbol, dfaStateMap.get(closure));
            }
        }

        return dfa;
    }

    // Utility method: epsilon-closure of a set of NFA states
    private static Set<NFA.State> epsilonClosure(Set<NFA.State> states) {
        Set<NFA.State> closure = new HashSet<>(states);
        Stack<NFA.State> stack = new Stack<>();
        stack.addAll(states);

        while (!stack.isEmpty()) {
            NFA.State state = stack.pop();
            for (NFA.State next : state.epsilonTransitions) {
                if (!closure.contains(next)) {
                    closure.add(next);
                    stack.push(next);
                }
            }
        }

        return closure;
    }

    // ---------------------------------------- Code generate by ChatGPT ----------------------------------------
    public DFA minimize() {
        removeUnreachableStates();

        Set<Set<State>> partitions = new HashSet<>();
        Set<State> finals = new HashSet<>();
        Set<State> nonFinals = new HashSet<>();
        for (State s : states) {
            if (s.isFinal) finals.add(s);
            else nonFinals.add(s);
        }
        if (!finals.isEmpty()) partitions.add(finals);
        if (!nonFinals.isEmpty()) partitions.add(nonFinals);

        boolean updated;
        do {
            updated = false;
            Set<Set<State>> newPartitions = new HashSet<>();
            for (Set<State> group : partitions) {
                Map<Map<Character, Set<State>>, Set<State>> map = new HashMap<>();

                for (State s : group) {
                    Map<Character, Set<State>> signature = new HashMap<>();
                    for (char symbol : getAlphabet()) {
                        State target = s.transitions.get(symbol);
                        signature.put(symbol, getPartitionOf(target, partitions));
                    }
                    map.computeIfAbsent(signature, k -> new HashSet<>()).add(s);
                }

                if (map.size() > 1) updated = true;
                newPartitions.addAll(map.values());
            }
            partitions = newPartitions;
        } while (updated);

        Map<State, State> representative = new HashMap<>();
        DFA minimized = new DFA();
        for (Set<State> group : partitions) {
            State rep = group.iterator().next();
            State newState = new State(rep.id, rep.isFinal);
            minimized.states.add(newState);
            if (rep == start) minimized.start = newState;
            for (State s : group) representative.put(s, newState);
        }

        for (State old : states) {
            State newFrom = representative.get(old);
            if (newFrom == null) continue;
            for (Map.Entry<Character, State> entry : old.transitions.entrySet()) {
                State newTo = representative.get(entry.getValue());
                if (newTo != null) newFrom.transitions.put(entry.getKey(), newTo);
            }
        }
        return minimized;
    }

    private void removeUnreachableStates() {
        Set<State> reachable = new HashSet<>();
        Queue<State> queue = new LinkedList<>();
        queue.add(start);
        reachable.add(start);

        while (!queue.isEmpty()) {
            State s = queue.poll();
            for (State next : s.transitions.values()) {
                if (!reachable.contains(next)) {
                    reachable.add(next);
                    queue.add(next);
                }
            }
        }
        states.removeIf(s -> !reachable.contains(s));
    }   

    private Set<Character> getAlphabet() {
        Set<Character> alphabet = new HashSet<>();
        for (State s : states) {
            alphabet.addAll(s.transitions.keySet());
        }
        return alphabet;
    }

    private Set<State> getPartitionOf(State s, Set<Set<State>> partitions) {
        if (s == null) return null;
        for (Set<State> group : partitions) if (group.contains(s)) return group;
        return null;
    }

    //----------------------------------------------------------------------------------------------------

    public  Boolean acceptsPattern(String s){
        State current = this.start;
        Boolean accepts = false;

        int i = 0;

        while( i < s.length()) {
            if(!current.transitions.isEmpty()){
                if (current.transitions.containsKey(s.charAt(i))) {
                    current = current.transitions.get(s.charAt(i));
                    if(current.isFinal){
                        accepts = true;
                    }
                }
                else if(current.transitions.containsKey('.')){
                    current = current.transitions.get('.');
                }
                else{
                    if(!current.equals(this.start)){
                        i--;
                    }
                    current = this.start;             
                }
            }else{
                current = this.start;  
            }
            i++;
        }
    
        return accepts;
    }

    public HashMap<Integer,List<Integer>> acceptsPatternCol(String s){
        HashMap<Integer,List<Integer>> result = new HashMap<>();
        List<Integer> col = new ArrayList<>();
        State current = this.start;

        int i = 0;
        int j = 1;

        while( i < s.length()) {
            if(!current.transitions.isEmpty()){
                if (current.transitions.containsKey(s.charAt(i))) {
                    current = current.transitions.get(s.charAt(i));
                    col.add(i);
                    if(current.isFinal){
                        result.put(j,new ArrayList<>(col));
                        col.clear();
                        j++;
                    }
                }
                else if(current.transitions.containsKey('.')){
                    current = current.transitions.get('.');
                    col.add(i);
                }
                else{
                    if(!current.equals(this.start)){
                        i--;
                    }
                    current = this.start;    
                    col.clear();         
                }
            }else{
                current = this.start;  
                col.clear();
            }
            i++;
        }
    
        return result;
    }

}
