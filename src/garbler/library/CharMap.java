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

import garbler.structure.Compactable;
import java.util.Map.Entry;

/**
 * Class which serves as a character-keyed extension of TreeMap
 *
 * @author Rogue <Alice Q>
 * @param <E> The type of Object contained within the TreeMap
 */
public abstract class CharMap<E> extends java.util.TreeMap<Character, E> implements Compactable<CharMap<E>, E> {

    // WHETHER OR NOT TO IGNORE CASES
    private boolean caseSensitive;

    // CONSTRUCTORS
    /**
     * Basic constructor for a case insensitive CharMap
     */
    public CharMap() {
        this.caseSensitive = false;
    }

    /**
     * Basic constructor allowing a specified initial case sensitivity.
     *
     * @param caseSensitive true if case should be ignored, false otherwise
     */
    public CharMap(boolean caseSensitive) {
        this.caseSensitive = caseSensitive;
    }

    // ADDITIONAL METHODS 
    // - setCaseSensitive
    // - isCaseSensitive
    // - getKey
    // - getAlphabet
    // - compact
    /**
     * Method for making a CharMap case sensitive or insensitive. Please note
     * that this does nothing to the internal data structure
     *
     * @param active false in order to ignore case sensitivity when accessing
     * data, false otherwise
     */
    public void setCaseSensitive(boolean active) {
        this.caseSensitive = active;
    }

    /**
     * Method for retrieving the case sensitivity of the structure. That is,
     * That is, 'a' yields the same results as 'A' when false.
     *
     * @return true if case is taken into consideration when accessing data,
     * false if case is ignored.
     */
    public boolean isCaseSensitive() {
        return this.caseSensitive;
    }

    /**
     * Gets a key for the map associated with the input character, taking case
     * sensitivity into account
     *
     * @param c The character to retrieve a key for
     * @return A constant Character representation of c, taking case sensitivity
     * into account
     */
    public Character getKey(Character c) {
        return (caseSensitive ? (Character) c : Character.toLowerCase(c));
    }

    /**
     * @return A Collection of all the characters being currently used
     */
    public java.util.Collection<Character> getAlphabet() {
        return keySet();
    }

    /**
     * This method is used to compact the internal data structure by reassigning
     * uppercase keys to their lowercase variants where possible. Please note
 that when two data members already exist the mergeValues() method is invoked on
 the two. Please use this to specify the procedure to use in case of a
 collision, otherwise the uppercase data member will simply be destroyed.
     */
    @Override
    public final void compact() {
        // FIRST FIND THE UPPERCASE MEMBERS
        java.util.LinkedList<Entry> upperSet = new java.util.LinkedList();

        for (Entry<Character, E> entry : entrySet()) {
            if (Character.isUpperCase(entry.getKey())) {
                upperSet.add(entry);
            }
        }

        // THEN GO THROUGH THE SET AND MODIFY AS NEEDED
        for (Entry<Character, E> entry : upperSet) {
            // FIND THE RELEVANT ENTRIES
            Character cHigh = entry.getKey();
            Character cLow = Character.toLowerCase(cHigh);
            E vHigh = entry.getValue();
            E vLow = super.get(cLow);

            // CHECK FOR REPLACEMENT
            if (vLow == null) {
                // SAFE TO REPLACE
                super.put(cLow, vHigh);
            } else if (vLow != vHigh) {
                // ASK THE USER WHAT TO DO
                super.put(cLow, mergeValues(vLow, vHigh));
            }
            // IF THE SAME OBJECT IS HELD IN BOTH, DO NOTHING

            // REMOVE THE UPPERCASE ENTRY
            super.remove(cHigh);
        }
    }

    @Override
    public CharMap<E> addAll(CharMap<E> obj) {
        for (Entry<Character, E> entry : obj.entrySet()) {
            E existing = get(entry.getKey());
            if (existing == null) {
                // WE'RE GOOD, JUST ADD THE NEW ONE
                super.put(entry.getKey(), entry.getValue());
            } else if (existing != entry.getValue()) {  // ONLY NEED TO MERGE IF THEY'RE NOT THE SAME THING
                // UHOH, CONFLICT. ASK THE USER WHAT TO DO
                mergeValues(existing, entry.getValue());
            }
        }
        return this;
    }

    // WRAPPER METHODS
    // - ceilingEntry
    // - ceilingKey
    // - containsKey
    // - floorEntry
    // - floorKey
    // - get
    // - headMap (2)
    // - higherEntry
    // - higherKey
    // - lowerEntry
    // - lowerKey
    // - put
    // - remove
    // - subMap (2)
    // - tailMap (2)
    @Override
    public Entry<Character, E> ceilingEntry(Character key) {
        return super.ceilingEntry(getKey(key));
    }

    @Override
    public Character ceilingKey(Character key) {
        return super.ceilingKey(getKey(key));
    }

    public boolean containsKey(Character key) {
        return super.containsKey(getKey(key));
    }

    @Override
    public Entry floorEntry(Character key) {
        return super.floorEntry(getKey(key));
    }

    @Override
    public Character floorKey(Character key) {
        return super.floorKey(getKey(key));
    }

    public E get(Character key) {
        return super.get(getKey(key));
    }

    @Override
    public java.util.NavigableMap<Character, E> headMap(Character toKey, boolean inclusive) {
        return super.headMap(getKey(toKey), inclusive);
    }

    @Override
    public java.util.SortedMap<Character, E> headMap(Character toKey) {
        return super.headMap(getKey(toKey));
    }

    @Override
    public Entry higherEntry(Character key) {
        return super.higherEntry(getKey(key));
    }

    @Override
    public Character higherKey(Character key) {
        return super.higherKey(getKey(key));
    }

    @Override
    public Entry lowerEntry(Character key) {
        return super.lowerEntry(getKey(key));
    }

    @Override
    public Character lowerKey(Character key) {
        return super.lowerKey(getKey(key));
    }

    @Override
    public E put(Character key, E value) {
        return super.put(getKey(key), value);
    }

    public E remove(Character key) {
        return super.remove(getKey(key));
    }

    @Override
    public java.util.NavigableMap<Character, E> subMap(Character fromKey, boolean fromInclusive, Character toKey, boolean toInclusive) {
        return super.subMap(getKey(fromKey), fromInclusive, getKey(fromKey), toInclusive);
    }

    @Override
    public java.util.SortedMap<Character, E> subMap(Character fromKey, Character toKey) {
        return super.subMap(getKey(fromKey), getKey(fromKey));
    }

    @Override
    public java.util.NavigableMap tailMap(Character fromKey, boolean inclusive) {
        return super.tailMap(getKey(fromKey), inclusive);
    }

    @Override
    public java.util.SortedMap tailMap(Character fromKey) {
        return super.tailMap(getKey(fromKey));
    }
}
