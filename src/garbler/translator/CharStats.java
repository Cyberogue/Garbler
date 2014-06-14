/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, addAll, publish, distribute, sublicense, and/or sell
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
    public static int DEFAULT_LIST_SIZE = 5;

    // WHETHER OR NOT CASE MATTERS
    private boolean ignoreCase = true;

    // THE CHARACTER BEING REPRESENTED
    Character definition;

    /**
     * Basic constructor
     *
     * @param c The character that the entry is for
     */
    public CharStats(char entry) {
        definition = getKey(entry);
    }

    /**
     * @return the character being represented by the object
     */
    public Character getCharacter() {
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
        char _key = getKey(key);
        OccurrenceList list = super.get(_key);

        if (list == null) {
            super.put(_key, new OccurrenceList(DEFAULT_LIST_SIZE));
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the OccurrenceList at a specified key index, creating a new empty
     * OccurrenceList if one doesn't already exist
     *
     * @param key The character key matched to the OccurrenceList
     * @return An OccurrenceList corresponding to the character key
     */
    public OccurrenceList getSafe(char key) {
        char _key = getKey(key);
        OccurrenceList list = super.get(_key);

        if (list == null) {
            list = new OccurrenceList(DEFAULT_LIST_SIZE);
            super.put(_key, list);
        }
        return list;
    }

    /**
     * Gets a list of super between the represented character and the key
     * character in the same word
     *
     * @param key The character to get the key for
     * @return The OccurrenceList corresponding to a character key
     */
    public OccurrenceList getOccurrenceList(char key) {
        return super.get(getKey(key));
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
    public void add(char key, int index) {
        add(getKey(key), index, 1);
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
    public void add(char key, int index, int quantity) {
        key = getKey(key);
        OccurrenceList list = super.get(key);

        if (list == null) {
            list = new OccurrenceList(DEFAULT_LIST_SIZE);
            super.put(key, list);
        }

        list.increment(index, quantity);
    }

    /**
     * Method which retrieves a key representation of the input character,
     * ignoring case if required.
     *
     * @param key The character to use as a key
     * @return The same character with its case changed if required
     */
    public Character getKey(char key) {
        return (ignoreCase ? Character.toLowerCase(key) : key);
    }

    /**
     * Use this to ignore or take into account character case. This does not
     * change former values, however.
     *
     * @param active true in order to ignore case, false in order to take case
     * into account
     */
    public void setIgnoreCase(boolean active) {
        this.ignoreCase = active;
    }

    /**
     * Changes the default size to which new lists will be made
     *
     * @param size The new size
     */
    public static void setDefaultNewListSize(int size) {
        DEFAULT_LIST_SIZE = size;
    }

    /**
     * Merges all the values held internally with those in another CharStats
     * object by adding their values together.
     *
     * @param stats The CharStats to addAll from
     */
    public void addAll(CharStats stats) {
        for (Character key : stats.keySet()) {
            OccurrenceList list = super.get(key);
            if (list == null) {
                super.put(key, list);
            } else {
                list.addAll(super.get(key));
            }
        }
    }

    @Override
    public int compareTo(CharStats stats) {
        return Character.compare(this.definition, stats.definition);
    }

}
