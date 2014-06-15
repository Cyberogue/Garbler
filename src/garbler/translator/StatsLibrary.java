/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package garbler.translator;

/**
 * Library for available characters and their respective statistics
 *
 * @author Rogue <Alice Q>
 */
public class StatsLibrary implements Compactable<StatsLibrary, CharMap> {

    // MAP OF ALL THE INDIVIDUAL CHARACTER STATS BEING TRACKED
    private CharMap<CharStats> characterStats;

    // THE LENGTH OF A WORD
    private OccurrenceList wordLength;

    public StatsLibrary() {
        wordLength = new OccurrenceList();
        characterStats = new CharMap<CharStats>() {
            @Override
            public void merge(CharStats oldValue, CharStats newValue) {
                oldValue.addAll(newValue);
            }
        };
    }

    @Override
    public void compact() {

    }

    @Override
    public void addAll(StatsLibrary lib) {

    }

    @Override
    public void merge(CharMap oldValue, CharMap newValue) {
        oldValue.addAll(newValue);
    }

    public static void main(String[] args) {
        java.util.Random rand = new java.util.Random();

        CharStats stats = new CharStats('a');

        System.out.println(stats.addWord("aABb"));
        System.out.println("1:\t" + stats);
        System.out.println("\t" + stats.getAllCorrelations());
        System.out.println("\t" + stats.getDistancesFromEnd() + ", " + stats.getDistancesFromStart());

        stats.addWord("aAbBc");
        System.out.println("2:\t" + stats);
        System.out.println("\t" + stats.getAllCorrelations());
        System.out.println("\t" + stats.getDistancesFromEnd() + ", " + stats.getDistancesFromStart());
    }
}
