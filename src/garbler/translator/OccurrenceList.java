/*
 * the MIT License
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
 * the above copyright notice and this permission notice shall be included in
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
     * @param size the initial size of the structure
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
    // - clear
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
     * @param index the index
     * @return the number of times the event has happened
     */
    public int increment(int index) {
        return increment(index, 1);
    }

    /**
     * Increments the count at a specified 0-indexed value, resizing the
     * structure as needed
     *
     * @param index the index
     * @param amount the amount to increment by
     * @return the number of times the event has happened
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
     * @param index the index to reset
     */
    public void reset(int index) {
        if (index < 0 || index >= map.length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        total -= map[index];
        map[index] = 0;
    }

    /**
     * Resets all the counts back to zero
     */
    public void clear() {
        for (int i = 0; i < map.length; i++) {
            map[i] = 0;
        }
    }

    /**
     * Resizes the internal data structure to the new size limit
     *
     * @param newSize the new size to resize the structure to
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
     * @param index the index of the value to retrieve
     * @return the number of times the event has happened
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
     * @param index the index of the value to retrieve
     * @return the number of times the event has happened. This returns 0 for
     * any index value outside the valid range.
     */
    public int getCount(int index) {
        if (index < 0 || index >= map.length) {
            return 0;
        }
        return map[index];
    }

    /**
     * @return the total number of event entries
     */
    public int getTotal() {
        return total;
    }

    /**
     * @return the size of the structure
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
     * @return a reference to itself
     */
    public OccurrenceList addAll(OccurrenceList list) {
        return addAll(list.values());
    }

    /**
     * Merges the values held internally with a new array of values by adding up
     * all their values, resizing itself if needed
     *
     * @param values An array of int values to addAll with
     * @return a reference to itself
     */
    public OccurrenceList addAll(int[] values) {
        // INCREASE THE SIZE OF THE ARRAY IF NEEDED
        if (values.length > map.length) {
            int[] temp = new int[values.length];
            System.arraycopy(map, 0, temp, 0, map.length);
            map = temp;
        }
        // AND ADD ALL THE VALUES
        for (int i = 0; i < values.length; i++) {
            map[i] += values[i];
        }

        return this;
    }

    // VALUE RETRIEVAL
    // - isEmpty
    // - find (2)
    // - findIndexOf (2)
    /**
     * Method to check if the OccurrenceList is empty
     *
     * @return true if and only if every value is 0
     */
    public boolean isEmpty() {
        return total == 0;
    }

    /**
     * Search function which uses user-provided guidelines to search through the
     * structure for a term within a provided range
     *
     * @param searchRule the rules to search by. Valid arguments are FIND_FIRST
     * and FIND_LAST
     * @param searchTerm the term to search for. Valid arguments are MAX_VALUE,
     * MIN_VALUE, MIN_NONZERO_VALUE, NONZERO_VALUE and ZERO_VALUE
     * @param index the index to start searching from
     * @param amount the number of terms to search
     * @return the matched item if it was found, otherwise -1
     * @throws IllegalArgumentException when an illegal search parameter is
     * passed or when the search range is invalid
     * @throws ArrayIndexOutOfBoundsException when the search range is out of
     * bounds
     */
    public int find(SearchPhrase searchRule, SearchPhrase searchTerm, int index, int amount) {
        int returnValue = findIndexOf(searchRule, searchTerm, index, amount);
        if (returnValue < 0) {
            return -1;
        } else {
            return map[returnValue];
        }
    }

    /**
     * Search function which uses user-provided guidelines to search through the
     * entire structure
     *
     * @param searchRule the rules to search by
     * @param searchTerm the term to search for.
     * @return the matched item if it was found, otherwise -1
     * @throws IllegalArgumentException when an illegal search parameter is
     * passed
     */
    public int find(SearchPhrase searchRule, SearchPhrase searchTerm) {
        int index = findIndexOf(searchRule, searchTerm);
        if (index < 0) {
            return -1;
        } else {
            return map[index];
        }
    }

    /**
     * Search function which searches for the provided search term, returning
     * the first matching value encountered
     *
     * @param searchRule the rules to search by
     * @return the matched item if it was found, otherwise -1
     * @throws IllegalArgumentException when an illegal search parameter is
     * passed
     */
    public int find(SearchPhrase searchRule) {
        return find(SearchPhrase.FIND_FIRST, searchRule);
    }

    /**
     * Search function which searches for the provided search term, returning
     * the last matching value encountered
     *
     * @param searchRule the rules to search by
     * @return the matched item if it was found, otherwise -1
     * @throws IllegalArgumentException when an illegal search parameter is
     * passed
     */
    public int findFromEnd(SearchPhrase searchRule) {
        return find(SearchPhrase.FIND_LAST, searchRule);
    }

    /**
     * Search function which uses user-provided guidelines to search through the
     * structure for a term within a provided range
     *
     * @param searchRule the rules to search by. Valid arguments are FIND_FIRST
     * and FIND_LAST
     * @param searchTerm the term to search for. Valid arguments are MAX_VALUE,
     * MIN_VALUE, MIN_NONZERO_VALUE, NONZERO_VALUE and ZERO_VALUE
     * @param index the index to start searching from
     * @param amount the number of terms to search
     * @return the index of the matched term, or -1 if none was found
     * @throws IllegalArgumentException when an illegal search parameter is
     * passed or when searching for less than one term
     * @throws ArrayIndexOutOfBoundsException when the search range is out of
     * bounds
     */
    public int findIndexOf(SearchPhrase searchRule, SearchPhrase searchTerm, int index, int amount) {
        int start;
        int step;
        int stop;

        // MAKE SURE THE BOUNDS ARE WITHIN RANGE
        if (index < 0 || index + amount > map.length) {
            throw new ArrayIndexOutOfBoundsException("Search range is out of bounds");
        }
        if (amount < 1) {
            throw new IllegalArgumentException("End index must be greater than start index");
        }

        // SET THE CONDITIONS FOR SEARCH ORDER
        switch (searchRule) {
            case FIND_FIRST:
                start = index;
                step = 1;
                stop = index + amount;
                break;
            case FIND_LAST:
                start = index + amount - 1;
                step = -1;
                stop = index - 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid rule parameter passed to search");
        }

        // START SEARCHING
        int match = -1;
        for (int i = start; i != stop; i += step) {
            switch (searchTerm) {
                case MAX_VALUE:
                    if (match < 0 || map[i] > map[match]) {
                        match = i;
                    }
                    break;
                case MIN_VALUE:
                    if (map[i] == 0) {
                        return i;
                    } else if (match < 0 || map[i] < map[match]) {
                        match = i;
                    }
                    break;
                case MIN_NONZERO_VALUE:
                    if (map[i] == 1) {
                        return i;
                    } else if (map[i] > 0 && (match < 0 || map[i] < map[match])) {
                        match = i;
                    }
                    break;
                case NONZERO_VALUE:
                    if (map[i] != 0) {
                        return i;
                    }
                    break;
                case ZERO_VALUE:
                    if (map[i] == 0) {
                        return i;
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid search parameter passed to search");
            }
        }

        return match;
    }

    /**
     * Search function which uses user-provided guidelines to search through the
     * entire structure
     *
     * @param searchRule the rules to search by
     * @param searchTerm the term to search for.
     * @return the index of the matched term, or -1 if none was found
     * @throws IllegalArgumentException when an illegal search parameter is
     * passed
     */
    public int findIndexOf(SearchPhrase searchRule, SearchPhrase searchTerm) {
        return findIndexOf(searchRule, searchTerm, 0, map.length);
    }

    // STATISTICS
    // - getMax
    // - getMin
    // - getNonzeroMin
    // - getNumberOf
    // - getAverage 
    // - getMedian
    // - getVariance
    // - getStandardDeviation
    // - getProbabilityMass
    /**
     * @return the highest value in the set
     */
    public int getMax() {
        int max = 0;
        for (int i : map) {
            if (i > max) {
                max = i;
            }
        }
        return max;
    }

    /**
     * @return the lowest value in the set
     */
    public int getMin() {
        int min = 0;
        for (int i : map) {
            if (i == 0) {
                return 0;
            } else if (i < min) {
                min = i;
            }
        }
        return min;
    }

    /**
     * @return the lowest non-zero value in the set
     */
    public int getNonzeroMin() {
        int min = 0;
        for (int i : map) {
            if (i == 1) {
                return 1;
            } else if (i > 0 && i < min) {
                min = i;
            }
        }
        return min;
    }

    /**
     * @param number the number to search for
     * @return the amount of indeces with the value specified by number
     */
    public int getNumberOf(int number) {
        int quantity = 0;
        for (int i : map) {
            if (i == number) {
                quantity++;
            }
        }
        return quantity;
    }

    /**
     * @return the average value in the set, also known as E(x)
     */
    public float getAverage() {
        return (float) total / map.length;
    }

    /**
     * @return the median value of the set being the average of two for even
     * numbered sets
     */
    public float getMedian() {
        int floorMid = (map.length - 1) / 2;
        int ceilMid = map.length / 2;

        return (float) (map[ceilMid] + map[floorMid]) / 2;
    }

    /**
     * @return the variance of the set
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

    /**
     * @return the standard deviation of the set
     */
    public float getStandardDeviation() {
        return (float) Math.sqrt(getVariance());
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
