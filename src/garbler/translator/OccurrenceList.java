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

/**
 * List which tracks the number of occurrences of an event at different times
 *
 * @author Rogue <Alice Q>
 */
public class OccurrenceList implements java.lang.Cloneable, java.util.RandomAccess, java.lang.Comparable<OccurrenceList> {

    // AND THE INTERNAL MAP
    private int[] map;

    // TOTAL AMOUNT OF ENTRIES SINCE IT'S FREQUENTLY CALCULATED
    private int total = 0;

    // CONSTRUCTORS
    /**
     * Basic constructor
     */
    public OccurrenceList() {
        this(1);
    }

    /**
     * Basic constructor
     *
     * @param size The initial size of the structure
     */
    public OccurrenceList(int size) {
        map = new int[size];
    }

    /**
     * Method for constructing an OccurrenceList out of a predetermined array
     *
     * @param list An array of integer values
     * @throws IllegalArgumentException if any of the values inside list are
     * negative
     */
    public OccurrenceList(int[] list) {
        map = list.clone();
        for (int i : map) {
            if (i < 0) {
                throw new IllegalArgumentException("Negative values not allowed");
            }
            total += i;
        }
    }

    // DATA STRUCTURE
    // - increment (2)
    // - reset
    // - resize
    // - get
    // - getTotal
    // - size
    // - values
    // - addAll (2)
    /**
     * Increments the count by one at a specified 0-indexed value, resizing the
     * structure as needed
     *
     * @param index The index
     * @return The number of times the event has happened
     */
    public int increment(int index) {
        total++;
        return increment(index, 1);
    }

    /**
     * Increments the count at a specified 0-indexed value, resizing the
     * structure as needed
     *
     * @param index The index
     * @param amount The amount to increment by
     * @return The number of times the event has happened
     */
    public int increment(int index, int amount) {
        if (index < 0) {
            throw new ArrayIndexOutOfBoundsException();
        } else if (index >= map.length) {
            resize(index + 1);
        }
        total += amount;
        return (map[index] += amount);
    }

    /**
     * Resets the count at a specified 0-indexed value
     *
     * @param index The index to reset
     */
    public void reset(int index) {
        if (index < 0 || index >= map.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        total -= map[index];
        map[index] = 0;
    }

    /**
     * Resizes the internal data structure to the new size limit
     *
     * @param newSize The new size to resize the structure to
     */
    public void resize(int newSize) {
        if (newSize == map.length) {
            return;
        }

        int[] temp = new int[newSize];
        System.arraycopy(map, 0, temp, 0, Math.min(map.length, newSize));
        map = temp;

    }

    /**
     * Returns the count at a specified 0-indexed value
     *
     * @param index The index of the value to retrieve
     * @return The number of times the event has happened
     */
    public int get(int index) {
        if (index < 0 || index >= map.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return map[index];
    }

    /**
     * Similar to get(int), this method returns the count at a 0-indexed value,
     * returning 0 if the value is outside the internal data range
     *
     * @param index The index of the value to retrieve
     * @return The number of times the event has happened. This returns 0 for
     * any index value outside the valid range.
     */
    public int getCount(int index) {
        if (index < 0 || index >= map.length) {
            return 0;
        }
        return map[index];
    }

    /**
     * @return The total number of event entries
     */
    public int getTotal() {
        return total;
    }

    /**
     * @return The size of the structure
     */
    public int size() {
        return map.length;
    }

    /**
     * Gets an array of all the values in this object. Note that this array is
     * 'safe', meaning that modifying it does not modify the internal structure.
     *
     * @return An array of integers representing this object
     */
    public int[] values() {
        return map.clone();
    }

    /**
     * Merges the values held internally with the values held inside another
     * OccurrenceList by adding their values, resizing itself if needed
     *
     * @param list An OccurrenceList to addAll with
     */
    public void addAll(OccurrenceList list) {
        addAll(list.values());
    }

    /**
     * Merges the values held internally with a new array of values by adding up
     * all their values, resizing itself if needed
     *
     * @param values An array of int values to addAll with
     */
    public void addAll(int[] values) {
        // INCREASE THE SIZE OF THE ARRAY IF NEEDED
        if (values.length > map.length) {
            int[] temp = new int[values.length];
            System.arraycopy(map, 0, temp, 0, map.length);
            map = temp;
        }
        // AND ADD ALL THE VALUES
        for (int i = 0; i < map.length; i++) {
            map[i] += values[i];
        }
    }

    // VALUE RETRIEVAL
    // - getFirstNonzero
    // - getLastNonzero
    // - getIndexOfMax
    // - getIndexOfMin
    // - getIndexOfNonzeroMin
    // - getLastIndexOfMax
    // - getLastIndexOfMin
    // - getLastIndexOfNonzeroMin
    /**
     * @return The index of the lowest non-zero entry
     */
    public int getFirstNonzeroEntry() {
        for (int i = 0; i < map.length; i++) {
            if (map[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return The index of the highest non-zero entry
     */
    public int getLastNonzeroEntry() {
        for (int i = map.length - 1; i >= 0; i--) {
            if (map[i] != 0) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @return The first index of the highest value in the list
     */
    public int getIndexOfMax() {
        int index = 0;

        for (int i = 1; i < map.length; i++) {
            if (map[i] > map[index]) {
                index = i;
            }
        }

        return index;
    }

    /**
     * @return The first index of the lowest value in the list
     */
    public int getIndexOfMin() {
        int index = 0;

        for (int i = 1; i < map.length; i++) {
            if (map[i] == 0) {
                return i;
            } else if (map[i] < map[index]) {
                index = i;
            }
        }

        return index;
    }

    /**
     * @return The first index of the lowest non-zero value in the list
     */
    public int getIndexOfNonzeroMin() {
        int index = 0;

        for (int i = 1; i < map.length; i++) {
            if (map[i] == 1) {
                return i;
            } else if (map[i] < map[index] && map[i] != 0) {
                index = i;
            }
        }

        return index;
    }

    /**
     * @return The last index of the highest value in the list
     */
    public int getLastIndexOfMax() {
        int index = 0;

        for (int i = map.length - 1; i >= 0; i--) {
            if (map[i] > map[index]) {
                index = i;
            }
        }

        return index;
    }

    /**
     * @return The last index of the lowest value in the list
     */
    public int getLastIndexOfMin() {
        int index = 0;

        for (int i = map.length - 1; i >= 0; i--) {
            if (map[i] == 0) {
                return i;
            } else if (map[i] < map[index]) {
                index = i;
            }
        }

        return index;
    }

    /**
     * @return The last index of the lowest non-zero value in the list
     */
    public int getLastIndexOfNonzeroMin() {
        int index = 0;

        for (int i = map.length - 1; i >= 0; i--) {
            if (map[i] == 1) {
                return i;
            } else if (map[i] < map[index] && map[i] != 0) {
                index = i;
            }
        }

        return index;
    }
    // STATISTICS
    // - getAverage 
    // - getVariance
    // - getProbabilityMass
    /**
     * @return The average value in the set, defined as E(x)
     */
    public float getAverage() {
        return (float) total / map.length;
    }

    /**
     * @return The variance of the set
     */
    public float getVariance() {
        float sum_sq = 0;
        float sum_avg = 0;

        // FIND BOTH E(X) and E(X^2) IN ONE LOOP
        for (int i : map) {
            sum_avg += i;
            sum_sq += i * i;
        }

        // AVERAGE OF THE SQUARES
        float avg_of_sqr = sum_sq / map.length;

        // SQUARE OF THE AVERAGE
        float avg = sum_avg / map.length;
        float sqr_of_avg = avg * avg;

        // E(X^2) - E(X)^2
        return avg_of_sqr - sqr_of_avg;
    }

    public float getProbabilityMass(int index) {
        if (index < 0 || index >= map.length) {
            return 0.0f;
        } else if (total == 0) {
            return (float) 1 / map.length;
        }
        return (float) map[index] / total;
    }

    // OVERWRITTEN METHODS
    // - toString
    // - compareTo
    // - clone
    @Override
    public String toString() {
        String s = "[" + map[0];

        int index = 0;
        while (++index < map.length) {
            s += "," + map[index];
        }

        return s += ">";
    }

    @Override
    public int compareTo(OccurrenceList list) {
        return Integer.compare(this.map.length, list.map.length);
    }

    @Override
    public OccurrenceList clone() throws CloneNotSupportedException {
        return new OccurrenceList(map);
    }
}
