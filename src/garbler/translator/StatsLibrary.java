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
public class StatsLibrary extends CharMap<CharStats> {

    // THE LENGTH OF A WORD
    private OccurrenceList wordLength;

    /**
     * Default constructor for a case sensitive StatsLibrary
     */
    public StatsLibrary() {
        this(true);
    }

    /**
     * Basic constructor which allows the user to specify the case sensitivity
     * from initialization
     *
     * @param caseSensitive false in order to ignore case sensitivity when
     * accessing data, false otherwise
     */
    public StatsLibrary(boolean caseSensitive) {
        wordLength = new OccurrenceList();
        this.setCaseSensitive(caseSensitive);
    }

    // STATISTICS
    // - parseWord
    // - parseLine (2)
    // - getWordLengths
    /**
     * Method which parseLineLines an entire word and adds all the characters
     * within to the internal statistics tracking structures, as well as general
     * word statistics
     *
     * @param word
     */
    public void parseWord(String word) {
        // CASE SENSITIVITY
        if (!isCaseSensitive()) {
            word = word.toLowerCase();
        }

        // WORD STATISTICS
        wordLength.increment(word.length());

        // CHARACTER STATISTICS - DO FOR EACH
        for (int i = 0; i < word.length(); i++) {
            char atIndex = word.charAt(i);
            CharStats cStat = get(atIndex);

            // MAKE SURE THAT IT EXISTS
            if (cStat == null) {
                cStat = new CharStats(atIndex);
                put(atIndex, cStat);
            }

            // ADD CHARACTER STATISTICS
            cStat.addWord(word, i);
        }
    }

    /**
     * Parses an entire line adding it to the internal statistics after
     * separating the sentence into words as specified by a regex string
     *
     * @param line A line of text
     * @param regex The regular expressions string specifying where to break
     * apart the line of text
     */
    public void parseLine(String line, String regex) {
        for (String s : line.split(regex)) {
            parseWord(s);
        }
    }

    /**
     * Parses an entire line adding it to the internal statistics, separating
     * the String into words on any whitespace
     *
     * @param line A line of text
     */
    public void parseLine(String line) {
        parseLine(line, "\\s+");
    }

    /**
     * Method for retrieval of data regarding all the word lengths encountered
     *
     * @return An OccurrenceList displaying all the word lengths that have been
     * found
     */
    public OccurrenceList getWordLengths() {
        return wordLength;
    }

    // OVERWRITTEN METHODS
    // - merge
    // - setCaseSensitive
    @Override
    public final void merge(CharStats oldValue, CharStats newValue) {
        oldValue.addAll(newValue);
    }

    @Override
    public final void setCaseSensitive(boolean active) {
        super.setCaseSensitive(active);
        for (CharStats stats : values()) {
            stats.setCaseSensitive(active);
        }
    }

    @Override
    public final void clear() {
        super.clear();
        wordLength.clear();
    }

    // MAIN TEST METHOD
    // THIS WILL BE TAKEN OUT OF THE FINAL VERSION AND IS JUST FOR TESTING TEST CASES
    public static void main(String[] args) {
        java.util.Random rand = new java.util.Random();

        StatsLibrary lib = new StatsLibrary(false);

        System.out.println("parse 1 sentence of Lorem Ipsum");
        lib.clear();
        lib.parseLine("Lorem ipsum dolor sit amet");

        System.out.println("\tWordlength: " + lib.getWordLengths());
        for (CharStats stat : lib.values()) {
            System.out.println("\t" + stat.getCharValue() + "\t" + stat.getAllCorrelations());
        }

        System.out.println("parse 1 paragraph of Lorem Ipsum");
        lib.clear();
        lib.parseLine("Lorem ipsum dolor sit amet, his option reformidans te, tation gubergren ei cum. Sea atomorum quaerendum ei, pri vivendo oportere ex. No debet scaevola ius. Dolore soluta electram sit id, an nibh ridens sea. Errem noster intellegam eum ei, te eum audire bonorum veritus. Vix recusabo argumentum ne, alii oratio vix ne. Vel sensibus voluptaria ne.");

        System.out.println("\tWordlength: " + lib.getWordLengths());
        for (CharStats stat : lib.values()) {
            System.out.println("\t" + stat.getCharValue() + "\t" + stat.getAllCorrelations());
        }
    }
}
