/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, mergeValues, publish, distribute, sublicense, and/or sell
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
package garbler.library;

import java.util.Map.Entry;

/**
 * Library for available characters and their respective statistics
 *
 * @author Rogue <Alice Q>
 */
public class StatsLibrary {

    // INTER-WORD CHARACTER STATS
    CharMap<CharStats> charSequenceStats;

    // 
    // THE LENGTH OF A WORD
    private CounterList wordLength;

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
        wordLength = new CounterList();
        charSequenceStats = new CharMap<CharStats>() {
            @Override
            public CharStats mergeValues(CharStats oldValue, CharStats newValue) {
                return oldValue.addAll(newValue);
            }
        };
        this.setCaseSensitive(caseSensitive);
    }

    // STATISTICS
    // - parseWord
    // - parseLine (2)
    // - getWordLengths
    // - getCorrelationsMatching
    // - generateInfluenceMap (3)
    /**
     * Method which parses an entire word and adds all the characters within to
     * the internal statistics tracking structures, as well as general word
     * statistics
     *
     * @param word
     */
    public void parseWord(String word) {
        // CASE SENSITIVITY
        if (!charSequenceStats.isCaseSensitive()) {
            word = word.toLowerCase(java.util.Locale.ROOT);
        }

        // WORD STATISTICS
        wordLength.increment(word.length() - 1);

        // CHARACTER STATISTICS - DO FOR EACH
        for (int i = 0; i < word.length(); i++) {
            char atIndex = word.charAt(i);
            CharStats cStat = charSequenceStats.get(atIndex);

            // MAKE SURE THAT IT EXISTS
            if (cStat == null) {
                cStat = new CharStats(atIndex);
                cStat.setCaseSensitive(charSequenceStats.isCaseSensitive());
                charSequenceStats.put(atIndex, cStat);
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
    public void parseLineRegex(String line, String regex) {
        for (String s : line.split(regex)) {
            parseWord(s);
        }
    }

    /**
     * Parses an entire line adding it to the internal statistics after
     * separating the sentence into words as specified by a regex string
     *
     * @param line a line of text
     * @param delim a string of delimiters to use in addition to whitespace
     */
    public void parseLine(String line, String delim) {
        parseLineRegex(line, "[" + delim.replace("]", "\\]").replace("[", "\\[").replace("%", "\\%") + "\\s]+");
    }

    /**
     * Parses an entire line adding it to the internal statistics, separating
     * the String into words on any whitespace
     *
     * @param line a line of text
     */
    public void parseLine(String line) {
        parseLineRegex(line, "\\s+");
    }

    /**
     * Method for retrieval of data regarding all the word lengths encountered.
     * Please note that this list has indeces offset by 1. That is, the value at
     * index 0 corresponds to word length 1.
     *
     * @return An CounterList displaying all the word lengths that have been
 found
     */
    public CounterList getWordLengths() {
        return wordLength;
    }

    /**
     * Method to generate an Influence Map for a specified character sequence.
     * That is, a map of all the characters that have been calculated as
     * possible next characters with their influence levels
     *
     * @param charSequence the string of characters to interpret
     * @throws IllegalArgumentException when offset is less than 0
     * @return A character-sorted map of CounterLists demonstrating the
 amount of influence each relevant character has on the word based on the
 distance from the end as an integer. If a character has no influence it
 is not included in the return value.
     */
    public CounterCharMap generateInfluenceMap(String charSequence) {
        return generateInfluenceMap(charSequence, 0);
    }

    /**
     * Method to generate an Influence Map for a specified character sequence.
     * That is, a map of all the characters that have been calculated as
     * possible next characters with their influence levels
     *
     * @param charSequence the string of characters to interpret
     * @param offset the offset with which to treat the word. That is, an offset
     * greater than 0 will treat the word as if there were ghost characters in
     * front of it
     * @throws IllegalArgumentException when offset is less than 0
     * @return A character-sorted map of CounterLists demonstrating the
 amount of influence each relevant character has on the word based on the
 distance from the end as an integer. If a character has no influence it
 is not included in the return value.
     */
    public CounterCharMap generateInfluenceMap(String charSequence, int offset) {
        int length = charSequence.length();
        int position;

        if (offset < 0) {
            throw new IllegalArgumentException("Negative offset passed");
        }

        CounterCharMap results = new CounterCharMap(charSequenceStats.isCaseSensitive());

        // GO THROUGH EACH CHARACTER IN THE SEQQUENCES
        for (int i = length - 1 - offset; i >= 0; i--) {
            char charAt = charSequence.charAt(i);
            position = length - i - 1;

            // FETCH THE ASSOCIATED CHARSTAT
            CharStats stats = charSequenceStats.get(charAt);
            if (stats == null) {
                continue;   // THERE ARE NO STATS SO IT DOESN'T MATTER
            }

            // GET ALL THE LISTS WITH IMPORTANT DATA
            CounterCharMap relevantLists = stats.getCorrelationsAtIndex(position);

            // AND ADD THAT DATA TO THE RESULTS
            for (Entry<Character, CounterList> entry : relevantLists.entrySet()) {
                char key = entry.getKey();
                CounterList value = entry.getValue();

                // MAKE A NEW LIST IF NEEDED
                CounterList existingList = results.get(key);
                if (existingList == null) {
                    existingList = new CounterList();
                    results.put(key, existingList);
                }
                existingList.increment(position, value.getCount(position));
            }
        }

        return results;
    }

    /**
     * Method to set the case sensitivity of the internal character-sorted
     * structures
     *
     * @param active false in order to ignore case sensitivity when accessing
     * data, false otherwise
     */
    public final void setCaseSensitive(boolean active) {
        charSequenceStats.setCaseSensitive(active);
        for (CharStats stats : charSequenceStats.values()) {
            stats.setCaseSensitive(active);
        }
    }

    /**
     * Clears the internal data structures
     */
    public void clear() {
        charSequenceStats.clear();
        wordLength.clear();
    }

    /**
     * @return A Collection of all the characters being currently used
     */
    public java.util.Collection<Character> getAlphabet() {
        return charSequenceStats.getAlphabet();
    }
}
