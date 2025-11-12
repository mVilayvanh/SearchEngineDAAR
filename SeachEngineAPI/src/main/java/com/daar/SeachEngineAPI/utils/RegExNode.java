package com.daar.SeachEngineAPI.utils;

public abstract class RegExNode {
    public abstract String toRegEx();

    /*
     * Class for parenthesis nodes (e.g., (a|b))
     */
    public static class ParenthesisNode extends RegExNode {
        RegExNode child;

        public ParenthesisNode(RegExNode child) {
            this.child = child;
        }

        public void setChild(RegExNode child) {
            this.child = child;
        }

        public RegExNode getChild() {
            return this.child;
        }

        @Override
        public String toRegEx() {
            return "(" + child.toRegEx() + ")";
        }   
    }

    /*
     * Class for alternation nodes (e.g., a|b|c)
     */
    public static class AlternationNode extends RegExNode {
        RegExNode childLeft;
        RegExNode childRight;

        public AlternationNode(RegExNode childLeft, RegExNode childRight) {
            this.childLeft = childLeft;
            this.childRight = childRight;
        }
        
        public void setChildLeft(RegExNode childLeft) {
            this.childLeft = childLeft;
        }

        public void setChildRight(RegExNode childRight) {
            this.childRight = childRight;
        }

        public RegExNode getChildLeft() {
            return this.childLeft;
        }

        public RegExNode getChildRight() {
            return this.childRight;
        }

        @Override
        public String toRegEx() {
            return childLeft.toRegEx() + "|" + childRight.toRegEx();
        }
    }

    /*
     * Class for concatenation nodes (e.g., abc)
     */
    public static class ConcatenationNode extends RegExNode {
        RegExNode childLeft;
        RegExNode childRight;

        public ConcatenationNode(RegExNode childLeft, RegExNode childRight) {
            this.childLeft = childLeft;
            this.childRight = childRight;
        }
        
        public void setChildLeft(RegExNode childLeft) {
            this.childLeft = childLeft;
        }

        public void setChildRight(RegExNode childRight) {
            this.childRight = childRight;
        }

        public RegExNode getChildLeft() {
            return this.childLeft;
        }

        public RegExNode getChildRight() {
            return this.childRight;
        }

        @Override
        public String toRegEx() {
            return childLeft.toRegEx() + "" + childRight.toRegEx();
        }
    }

    /*
     * Class for star nodes (e.g., a*)
     */
    public static class StarNode extends RegExNode {
        RegExNode child;

        public StarNode(RegExNode child) {
            this.child = child;
        }

        public void setChild(RegExNode child) {
            this.child = child;
        }

        public RegExNode getChild() {
            return this.child;
        }

       @Override
        public String toRegEx() {
            return child.toRegEx() + "*";
        }
    }

    /*
     * Class for dot nodes (e.g., .)
     */
    public static class DotNode extends RegExNode {
        @Override
        public String toRegEx() {
            return ".";
        }
    }

    /*
     * Class for ASCII character nodes (e.g., a, b, c, etc.)
     */
    public static class ASCIINode extends RegExNode {
        private final char character;

        public ASCIINode(char character) {
            this.character = character;
        }

        public char getChar(){
            return this.character;
        }

        @Override
        public String toRegEx() {
            return Character.toString(character);
        }
    }
}