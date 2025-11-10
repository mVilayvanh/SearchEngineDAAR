package daar.projet1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class KMPOptimized {
    private int[] carryOver;
    private char[] factor;

    public KMPOptimized(String pattern) {
        this.factor = pattern.toCharArray();
        this.carryOver = new int[factor.length + 1];
        buildCarryOver();
    }

    public int[] getCarryOver() {
        return carryOver;
    }

    public char[] getFactor() {
        return factor;
    }

    private void buildCarryOver() {
        this.carryOver[0] = -1;

        for (int i = 1; i <= this.factor.length; i++) {
            int k = this.carryOver[i - 1];

            while (k >= 0 && this.factor[k] != this.factor[i - 1]) {
                k = this.carryOver[k];
            }
            this.carryOver[i] = k + 1;
        }

        for (int i = 1; i < this.carryOver.length - 1; i++) {
            if (this.carryOver[this.carryOver[i]] == -1 && this.factor[i] == this.factor[this.carryOver[i]]) {
               this.carryOver[i] = -1;
            }
        }

        for(int i = 1; i < carryOver.length - 1; i++) {
            if (this.carryOver[i] != -1 &&  this.carryOver[i] != 0 && this.factor[i] == this.factor[this.carryOver[i]]) {
                this.carryOver[i] = this.carryOver[this.carryOver[i]];
            }
        }

    }

    public Boolean searchKMP(String text) {
        int i = 0;
        int j = 0;

        while (i < text.length()) {
            if (j == -1 || text.charAt(i) == factor[j]) {
                i++;
                j++;
            } else {
                j = carryOver[j];
            }

            if (j == factor.length) {
                return true;
            }
        }

        return false;
    }

    public HashMap<Integer,List<Integer>> searchKMPCol(String text) {
        HashMap<Integer,List<Integer>> result = new HashMap<>();
        List<Integer> col = new ArrayList<>();
        int i = 0;
        int j = 0;
        int k = 0;

        while (i < text.length()) {
            if (j == -1 || text.charAt(i) == factor[j]) {
                if(j != -1){
                    col.add(i);
                }
                i++;
                j++;
                
            } else {
                j = carryOver[j];
                col.clear();
            }

            if (j == factor.length) {
                result.put(k, new ArrayList<>(col));
                col.clear();
                k++;
                j=0;
            }
        }

        return result;
    }
}
