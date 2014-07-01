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

import garbler.structure.*;
import java.util.Map.Entry;

/**
 * Library for available characters and their respective statistics
 *
 * @author Rogue <Alice Q>
 */
public class StatsLibrary {

    // INTER-WORD CHARACTER STATS
    CharMap<CharStats> charSequenceStats;

    // FIRST-LETTER COUNTS
    BasicIntegerCharMap firstCharCounts;

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
        charSequenceStats = new CharMap<CharStats>() {
            @Override
            public CharStats mergeValues(CharStats oldValue, CharStats newValue) {
                return oldValue.addAll(newValue);
            }
        };
        firstCharCounts = new BasicIntegerCharMap(caseSensitive);
        this.setCaseSensitive(caseSensitive);
    }

    // STATISTICS PARSING
    // - parseCharacterSequence
    // - parseLineSimple (2)
    // - getWordLengths
    // - getCorrelationsMatching
    // - generateInfluenceMap (3)
    /**
     * Method which parses an entire word and adds all the characters within to
     * the internal statistics tracking structures, as well as general word
     * statistics
     *
     * @param charSequence the sequence of characters to parse and track. In
     * essence, a single word (but not always).
     */
    public void parseCharacterSequence(String charSequence) {
        // WORD STATISTICS
        wordLength.increment(charSequence.length() - 1);

        // CHARACTER STATISTICS - DO FOR EACH
        for (int i = 0; i < charSequence.length(); i++) {
            char atIndex = charSequence.charAt(i);
            CharStats cStat = charSequenceStats.get(atIndex);

            // MAKE SURE THAT IT EXISTS
            if (cStat == null) {
                cStat = new CharStats(atIndex);
                cStat.setCaseSensitive(charSequenceStats.isCaseSensitive());
                charSequenceStats.put(atIndex, cStat);
            }

            // ADD CHARACTER STATISTICS
            cStat.addWord(charSequence, i);
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
        if (!charSequenceStats.isCaseSensitive()) {
            line = line.toLowerCase(java.util.Locale.ROOT);
        }

        for (String s : line.split(regex)) {
            firstCharCounts.increment(s.charAt(0), 1);
            parseCharacterSequence(s);
        }
    }

    /**
     * Parses an entire line adding it to the internal statistics after
     * separating the sentence into words as specified by a regex string
     *
     * @param line a line of text
     * @param delim a string of delimiters to use in addition to whitespace
     */
    public void parseLineSimple(String line, String delim) {
        parseLine(line, "[" + delim + "\\s]+");
    }

    /**
     * Parses an entire line adding it to the internal statistics, separating
     * the String into words on any whitespace
     *
     * @param line a line of text
     */
    public void parseLineSimple(String line) {
        parseLine(line, "\\s+");
    }

    // STATISTICS DATA RETRIEVAL AND GENERATION
    // - getWordLengths
    // - generateInfluenceMap (2)
    // - getCharacterStats
    // - getAlphabet
    /**
     * Method for retrieval of data regarding all the word lengths encountered.
     * Please note that this list has indeces offset by 1. That is, the value at
     * index 0 corresponds to word length 1.
     *
     * @return An OccurrenceList displaying all the word lengths that have been
     * found
     */
    public OccurrenceList getWordLengths() {
        return wordLength;
    }

    /**
     * @return a map of the number of times each character has appeared as the
     * first character
     */
    public CharMap<Integer> getPrimaryCharacterCounts() {
        return firstCharCounts;
    }

    /**
     * Method to generate an Influence Map for a specified character sequence.
     * That is, a map of all the characters that have been calculated as
     * possible next characters with their influence levels
     *
     * @param charSequence the string of characters to interpret
     * @throws IllegalArgumentException when offset is less than 0
     * @return A character-sorted map of OccurrenceLists demonstrating the
     * amount of influence each relevant character has on the word based on the
     * distance from the end as an integer. If a character has no influence it
     * is not included in the return value.
     */
    public OccurrenceCharMap generateInfluenceMap(String charSequence) {
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
     * @return A character-sorted map of OccurrenceLists demonstrating the
     * amount of influence each relevant character has on the word based on the
     * distance from the end as an integer. If a character has no influence it
     * is not included in the return value.
     */
    public OccurrenceCharMap generateInfluenceMap(String charSequence, int offset) {
        int length = charSequence.length();
        int position;

        if (offset < 0) {
            throw new IllegalArgumentException("Negative offset passed");
        }

        OccurrenceCharMap results = new OccurrenceCharMap(charSequenceStats.isCaseSensitive());

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
            OccurrenceCharMap relevantLists = stats.getCorrelationsAtIndex(position);

            // AND ADD THAT DATA TO THE RESULTS
            for (Entry<Character, OccurrenceList> entry : relevantLists.entrySet()) {
                char key = entry.getKey();
                OccurrenceList value = entry.getValue();

                // MAKE A NEW LIST IF NEEDED
                OccurrenceList existingList = results.get(key);
                if (existingList == null) {
                    existingList = new OccurrenceList();
                    results.put(key, existingList);
                }
                existingList.increment(position, value.getCount(position));
            }
        }

        return results;
    }

    /**
     * Retrieves a set of statistics for a single character in a character
     * sequence
     *
     * @param character the character to retrieve for
     * @return a CharStats corresponding to the provided character
     */
    public CharStats getCharacterStats(Character character) {
        return charSequenceStats.get(character);
    }

    /**
     * @return A Collection of all the characters being currently used
     */
    public java.util.Collection<Character> getAlphabet() {
        return charSequenceStats.getAlphabet();
    }

    // MODIFIERS
    // - setCaseSensitive
    // = clear
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
}
