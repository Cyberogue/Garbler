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
import java.util.Collections;
import java.util.TreeMap;

/**
 * Class containing the statistics for a single character
 *
 * @author Rogue <Alice Q>
 */
public class CharStats {

    // INITIAL LENGTH FOR OCCURRENCELISTS
    public static final int DEFAULT_LIST_SIZE = 5;

    // THE CHARACTER BEING REPRESENTED
    char definition;

    // A MAP OF THE NUMBER OF TIMES A CHARACTER OCCURS AFTER THIS LETTER
    TreeMap<Character, OccurrenceList> occurrences;

    // A SORTED LIST OF ALL THE KEYS IN USE
    ArrayList<Character> keys;

    /**
     * Basic constructor
     *
     * @param c The character that the entry is for
     */
    public CharStats(char entry) {
        definition = entry;

        occurrences = new TreeMap();
        keys = new ArrayList();
    }

    /**
     * @return The character the entry is for
     */
    public char getReferenceCharacter() {
        return definition;
    }

    /**
     * Generates a key and adds a field to the internal data structure, if it
     * doesn't already exist
     *
     * @param key The character to make a key for
     * @return True if the key already exists
     */
    public boolean addField(char c) {
        if (findKey(c) != null) {
            return true;
        }
        // MAKE A NEW KEY
        Character key = new Character(c);

        // ADD IT TO THE KEYS AND RESORT
        keys.add(key);
        Collections.sort(keys);

        // AND MAKE ITS RESPECTIVE STRUCTURES
        occurrences.put(key, new OccurrenceList(DEFAULT_LIST_SIZE));

        return false;
    }

    /**
     * Performs a binary search through the keys in order to find the key with
     * the specified character
     *
     * @param c The character to find a corresponding key for
     * @return A key corresponding to the input character, or null if none was
     * found
     */
    public Character findKey(char c) {
        int i_min = 0, i_max = keys.size(), i_mid;

        // BINARY SEARCH
        while (i_min < i_max) {
            // FIND THE MIDPOINT
            i_mid = (i_min + i_max) / 2;
            // CHECK THE CHARACTER AT THE MIDPOINT
            Character c_mid = keys.get(i_mid);

            if (c_mid.charValue() == c) {
                // FOUND THE KEY!
                return c_mid;
            } else if (c_mid.charValue() < c) {
                // KEY IS LARGER, CHANGE THE LOWER LIMIT
                i_min = i_mid + 1;
            } else {
                // KEY IS SMALLER, CHANGE THE UPPER LIMIT
                i_max = i_mid;
            }
        }

        return null;
    }

    /**
     * Gets the OccurrenceList for a character reference to this character
     *
     * @param c The character to get occurrences for
     * @return A list representing all the times the two characters were found
     * in the same word, and the distance between each other
     */
    public OccurrenceList getOccurrenceList(char c) {
        Character key = findKey(c);

        if (key == null) {
            return null;
        } else {
            return occurrences.get(key);
        }
    }

    /**
     * Adds a single occurrence to the corresponding character reference
     *
     * @param c The character to add a reference to
     * @param index The index to add to
     * @return The OccurrenceList corresponding to the specified character
     */
    public OccurrenceList addOccurrence(char c, int index) {
        return addOccurrence(c, index, 1);
    }

    /**
     * Adds an amount of occurrences to the corresponding character reference
     *
     * @param c The character to add a reference to
     * @param index The index to add to
     * @param amount The amount to add
     * @return The OccurrenceList corresponding to the specified character
     */
    public OccurrenceList addOccurrence(char c, int index, int amount) {
        Character key = findKey(c);

        if (key == null) {
            return null;
        } else {
            OccurrenceList list = occurrences.get(key);
            list.increment(index, amount);
            return list;
        }
    }

    @Override
    public String toString() {
        return definition + occurrences.toString();
    }
}
