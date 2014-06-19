/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q.>.
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
package garbler.builder;

import garbler.library.CharMap;
import java.util.LinkedList;
import java.util.Map.Entry;

/**
 * Basic wrapper class for a CharMap<Float> providing the basic skeleton code
 * and methods required
 *
 * @author Rogue <Alice Q.>
 */
public class BasicDecimalCharMap extends CharMap<Float> {

    /**
     * Wrapper for CharMap constructor which specifies the default case
     * sensitivity
     *
     * @param caseSensitivity true if case should be ignored, false otherwise
     */
    public BasicDecimalCharMap(boolean caseSensitivity) {
        super(caseSensitivity);
    }

    /**
     * Method to get the sum of all the internally held values
     *
     * @return a float representation of the sum of all internal values
     */
    public Float getSum() {
        float sum = 0.0f;
        for (float f : values()) {
            sum += f;
        }
        return sum;
    }

    @Override
    public Float mergeValues(Float oldValue, Float newValue) {
        return oldValue + newValue;
    }

    // STATIC METHODS
    // - rebalanceMap
    // - trimMap
    // - trimAndRebalanceMap
    /**
     * Method to equalize or re-balance a decimal map so that the sum of its
     * values equals 1.0f while still keeping in proportion to each other.
     *
     * @param map a map of decimal point values to equalize
     */
    public static void rebalanceMap(CharMap<Float> map) {
        // TAKE THE TOTAL SUM
        float sum = 0;
        for (Float f : map.values()) {
            sum += f;
        }
        // DIVIDE BY SUM TO EQUALIZE
        for (Entry<Character, Float> entry : map.entrySet()) {
            map.put(entry.getKey(), entry.getValue() / sum);
        }
    }

    /**
     * Method to trim any low-valued items inside a character-keyed decimal map
     *
     * @param map the map to trim
     * @param threshold the lower threshold of acceptable values
     * @return the number of deleted items
     * @throws IllegalArgumentException when the threshold is less than or equal
     * to 0
     */
    public static int trimMap(CharMap<Float> map, float threshold) {
        if (threshold <= 0.0f) {
            throw new IllegalArgumentException("Threshold must be between 0.0 and 1.0");
        }
        LinkedList<Character> trash = new LinkedList();
        // SEEK LOW-VALUED ENTRIES
        for (Entry<Character, Float> entry : map.entrySet()) {
            if (entry.getValue() <= threshold) {
                trash.push(entry.getKey());
            }
        }
        // EMPTY THE TRASH
        int removed = trash.size();
        for (Character c : trash) {
            map.remove(c);
        }
        return removed;
    }

    /**
     * Method which trims and re-equalizes a decimal map by removing any values
     * lower than the threshold and equalizing the results so their sum equals
     * 1.0f
     *
     * @param map the map to trim and equalize
     * @param threshold the lower threshold of acceptable values
     * @return the number of deleted items
     * @throws IllegalArgumentException when the threshold is less than or equal
     * to 0
     */
    public static int trimAndRebalanceMap(CharMap<Float> map, float threshold) {
        if (threshold <= 0.0f) {
            throw new IllegalArgumentException("Threshold must be between 0.0 and 1.0");
        }
        LinkedList<Entry<Character, Float>> trash = new LinkedList();
        // SEEK LOW-VALUED ENTRIES AND GATHER THE SUM TO BEGIN WITH
        float sum = 0;
        for (Entry<Character, Float> entry : map.entrySet()) {
            if (entry.getValue() <= threshold) {
                trash.push(entry);
            } else {
                sum += entry.getValue();
            }
        }
        // EMPTY THE TRASH
        int removed = trash.size();
        for (Entry<Character, Float> entry : trash) {
            map.remove(entry.getKey());
        }
        // AND REBALANCE WHAT'S LEFT
        for (Entry<Character, Float> entry : map.entrySet()) {
            map.put(entry.getKey(), entry.getValue() / sum);
        }
        return removed;
    }
}
