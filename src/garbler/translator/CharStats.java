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
import java.util.Map.Entry;

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
        OccurrenceList list = get(_key);

        if (list == null) {
            put(_key, new OccurrenceList(DEFAULT_LIST_SIZE));
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
        OccurrenceList list = get(_key);

        if (list == null) {
            list = new OccurrenceList(DEFAULT_LIST_SIZE);
            put(_key, list);
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
        return get(getKey(key));
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
        add(key, index, 1);
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
        char _key = getKey(key);
        OccurrenceList list = get(_key);

        if (list == null) {
            list = new OccurrenceList(DEFAULT_LIST_SIZE);
            put(_key, list);
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
    public void ignoreCases(boolean active) {
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
        for (Entry<Character, OccurrenceList> e : stats.entrySet()) {
            Character key = e.getKey();
            OccurrenceList entry = e.getValue();
            OccurrenceList entryLocal = get(key);
            if (entryLocal == null) {
                put(key, entry);
            } else {
                entryLocal.addAll(entry);
            }
        }
    }

    /**
     * Collapses the internal data structure by making all the keys lowercase,
     * merging with necessary. Please not that this method is destructive and
     * there is no way to undo this.
     */
    public void collapseCases() {
        // LIST OF CRAP TO REMOVE
        ArrayList<Character> trashbin = new ArrayList(size());

        // NOTE: SINCE OCCURRENCELISTS DON'T HAVE CONCURRENCY ISSUES, THIS CAN BE DONE MUCH FASTER THAN STATSLIBRARY
        // ITERATE THROUGH IT ALL
        for (Entry<Character, OccurrenceList> e : super.entrySet()) {
            if (Character.isUpperCase(e.getKey())) {
                // GET RELEVANT FIELDS
                Character keyUpper = e.getKey();
                OccurrenceList entryUpper = e.getValue();

                Character keyLower = Character.toLowerCase(keyUpper);
                OccurrenceList entryLower = get(keyLower);

                //  MERGE
                if (entryLower == null) {
                    put(keyLower, entryUpper);
                } else {
                    entryLower.addAll(entryUpper);
                }

                // AND MARK THE KEY FOR DELETION
                trashbin.add(keyUpper);
            }
        }
        // NOW DELETE THE DEPRECATED KEYS
        for (Character key : trashbin) {
            remove(key);
        }
    }

    @Override
    public int compareTo(CharStats stats) {
        return Character.compare(this.definition, stats.definition);
    }

}
