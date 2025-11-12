package com.daar.SeachEngineAPI.utils;

import java.util.ArrayList;
import java.util.List;

public class RegExParsing {

    public static RegExNode parse(String regex) throws Exception {
        // Shunting-yard algorithm for operator precedence
        List<RegExNode> nodes = new ArrayList<>();
        int i = 0;

        while (i < regex.length()) {
            char c = regex.charAt(i);
            switch (c) {
                case '(' -> {
                    if (!nodes.isEmpty() && !(nodes.get(nodes.size() - 1) instanceof RegExNode.AlternationNode)) {
                        nodes.add(new RegExNode.ConcatenationNode(null, null)); // Left operand to be filled later
                    }

                    int nbParenthesis = 1;
                    int j = i + 1;
                    while (j < regex.length() && nbParenthesis > 0) {
                        if (regex.charAt(j) == '(') nbParenthesis++;
                        else if (regex.charAt(j) == ')') nbParenthesis--;
                        j++;
                    }
                    if (nbParenthesis != 0) throw new Exception("Mismatched parenthesis");
                    String subExpr = regex.substring(i + 1, j - 1);
                    RegExNode subNode = parse(subExpr);
                    nodes.add(new RegExNode.ParenthesisNode(subNode));
                    i = j - 1;

                }
                case '*' -> nodes.add(new RegExNode.StarNode(null));
                case '|' -> nodes.add(new RegExNode.AlternationNode(null, null)); // Right operand to be filled later
                default -> {
                    if (c >= 32 && c <= 126) {
                        if (!nodes.isEmpty() && !(nodes.get(nodes.size() - 1) instanceof RegExNode.AlternationNode)) {
                            nodes.add(new RegExNode.ConcatenationNode(null, null)); // Left operand to be filled later
                        }
                        // ASCII or dot as any char
                        RegExNode newNode;
                        if (c == '.') {
                            newNode = new RegExNode.DotNode();
                        } else {
                            newNode = new RegExNode.ASCIINode(c);
                        }
                        nodes.add(newNode);
                    } else {
                        throw new IllegalArgumentException("Unsupported character: " + c);
                    }
                }
            }
            i++;
        }

        precedenceOperator(nodes);

        if (nodes.size() != 1) throw new Exception("Invalid regex");
        return nodes.get(0);
    }

    private static void precedenceOperator(List<RegExNode> nodes) throws Exception {
        List<Integer> starNodes = containsStarNode(nodes);

        if(!starNodes.isEmpty()){
            for (int index = starNodes.size() - 1; index >= 0; index--) {
                int i = starNodes.get(index);
                if (nodes.get(i)instanceof RegExNode.StarNode && ((RegExNode.StarNode) nodes.get(i)).getChild() == null) {
                    if (i == 0) throw new Exception("Star operator without operand");
                    RegExNode operand = nodes.get(i - 1);
                    nodes.set(i, new RegExNode.StarNode(operand));
                    nodes.remove(i - 1);
                }
            }
        }
        List<Integer> concatenationNodes = containsConcatenationNode(nodes);

        if(!concatenationNodes.isEmpty()) {
            for (int index = concatenationNodes.size() - 1; index >= 0; index--) {
                int i = concatenationNodes.get(index);
                if (nodes.get(i) instanceof RegExNode.ConcatenationNode && ((RegExNode.ConcatenationNode) nodes.get(i)).getChildLeft() == null && ((RegExNode.ConcatenationNode) nodes.get(i)).getChildRight() == null) {
                    if (i == 0 || i == nodes.size() - 1)
                        throw new Exception("Concatenation operator without operand");
                    RegExNode left = nodes.get(i - 1);
                    RegExNode right = nodes.get(i + 1);
                    nodes.set(i, new RegExNode.ConcatenationNode(left, right));
                    nodes.remove(i + 1);
                    nodes.remove(i - 1);
                }
            }
        }

        List<Integer> alternationNodes = containsAlternationNode(nodes);

        if(!alternationNodes.isEmpty()) {
            for (int index = alternationNodes.size() - 1; index >= 0; index--) {
                int i = alternationNodes.get(index);
                if (nodes.get(i) instanceof RegExNode.AlternationNode && ((RegExNode.AlternationNode) nodes.get(i)).getChildLeft() == null && ((RegExNode.AlternationNode) nodes.get(i)).getChildRight() == null) {
                    if (i == 0 || i == nodes.size() - 1)
                        throw new Exception("Alternation operator without operand");
                    RegExNode left = nodes.get(i - 1);
                    RegExNode right = nodes.get(i + 1);
                    nodes.set(i, new RegExNode.AlternationNode(left, right));
                    nodes.remove(i + 1);
                    nodes.remove(i - 1);
                }
            }
        }
    }

    private static List<Integer> containsStarNode(List<RegExNode> nodes) {
        List<Integer> starNodes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) instanceof RegExNode.StarNode && ((RegExNode.StarNode) nodes.get(i)).getChild() == null) {
                starNodes.add(i);
            }
        }
        return starNodes;
    }

    private static List<Integer> containsConcatenationNode(List<RegExNode> nodes) {
        List<Integer> concatenationNodes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) instanceof RegExNode.ConcatenationNode && ((RegExNode.ConcatenationNode) nodes.get(i)).getChildLeft() == null && ((RegExNode.ConcatenationNode) nodes.get(i)).getChildRight() == null) {
                concatenationNodes.add(i);
            }
        }
        return concatenationNodes;
    }

    private static List<Integer> containsAlternationNode(List<RegExNode> nodes) {
        List<Integer> alternationNodes = new ArrayList<>();
        for (int i = 0; i < nodes.size(); i++) {
            if (nodes.get(i) instanceof RegExNode.AlternationNode && ((RegExNode.AlternationNode) nodes.get(i)).getChildLeft() == null && ((RegExNode.AlternationNode) nodes.get(i)).getChildRight() == null) {
                alternationNodes.add(i);
            }
        }
        return alternationNodes;
    }
}
