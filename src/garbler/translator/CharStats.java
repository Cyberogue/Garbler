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

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Class containing the statistics for a single character
 *
 * @author Rogue <Alice Q>
 */
public class CharStats extends TreeMap<Character, OccurrenceList> implements java.lang.Comparable<CharStats> {

    // DEFAULT SIZE TO MAKE NEW LISTS
    public static final int DEFAULT_LIST_SIZE = 5;

    // THE CHARACTER BEING REPRESENTED
    char definition;

    /**
     * Basic constructor
     *
     * @param c The character that the entry is for
     */
    public CharStats(char entry) {
        definition = entry;
    }

    /**
     * @return the character being represented by the object
     */
    public char getCharacter() {
        return definition;
    }

    /**
     * Adds a new field to the internal data structures
     *
     * @param key The character to add
     * @return true if a new field was added, or false if the field already
     * exists
     */
    public boolean addField(char key) {
        OccurrenceList list = super.get(key);

        if (list == null) {
            super.put(key, new OccurrenceList(DEFAULT_LIST_SIZE));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets a list of super between the represented character and the key
     * character in the same word
     *
     * @param key The character to get the key for
     * @return The OccurrenceList corresponding to a character key
     */
    public OccurrenceList getOccurrenceList(char key) {
        return super.get(key);
    }

    /**
     * Adds a single occurrence between the represented character and the key
     * character at a specified index
     *
     * @param key The character to add to
     * @param index The index to add to within the character
     * @throws java.util.NoSuchElementException when the specified key doesn't
     * exist
     */
    public void addOccurrence(char key, int index) {
        addOccurrence(key, index, 1);
    }

    /**
     * Adds a number of super between the represented character and the key
     * character at a specified index. If there is no such data structure at the
     * specified key, it is created and initialized to zero.
     *
     * @param key The character to add to
     * @param index The index to add to within the character
     * @param quantity The amount to add
     * @throws java.util.NoSuchElementException when the specified key doesn't
     * exist
     */
    public void addOccurrence(char key, int index, int quantity) {
        OccurrenceList list = super.get(key);

        if (list == null) {
            list = new OccurrenceList(DEFAULT_LIST_SIZE);
            super.put(key, list);
        }

        list.increment(index, quantity);
    }

    @Override
    public int compareTo(CharStats stats) {
        return Character.compare(this.definition, stats.definition);
    }

}
