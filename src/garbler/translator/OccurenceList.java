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

/**
 * List which tracks the number of occurrences of an event at different times
 *
 * @author Rogue <Alice Q>
 */
public class OccurenceList implements java.util.RandomAccess {

    // THE INITIAL CONSTRUCTED SIZE OF THE INTERNAL ARRAY
    private static short INITIAL_SIZE = 10;

    // THE ACTUAL SIZE
    private short size;

    // AND THE INTERNAL MAP
    private short[] map;

    /**
     * Basic constructor
     */
    public OccurenceList() {
        size = INITIAL_SIZE;
        map = new short[INITIAL_SIZE];
    }

    /**
     * Returns the count at a certain position
     *
     * @param position The position
     * @return The number of times the event has happened
     */
    public short get(short position) {
        if (position < 0 || position >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (size != INITIAL_SIZE) {
            resize();
        }
        return map[position];
    }

    /**
     * Increments the count at a certain position by one
     *
     * @param position The position
     * @return The number of times the event has happened
     */
    public short increment(short position) {
        if (position < 0 || position >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (size != INITIAL_SIZE) {
            resize();
        }
        return ++map[position];
    }

    /**
     * Increments the count at a certain position
     *
     * @param position The position
     * @param amount The amount to increment by
     * @return The number of times the event has happened
     */
    public short increment(short position, short amount) {
        if (position < 0 || position >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (size != INITIAL_SIZE) {
            resize();
        }
        return (map[position] += amount);
    }

    /**
     * Resets the count at a certain position
     *
     * @param position The position to reset
     */
    public void reset(short position) {
        if (position < 0 || position >= size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        if (size != INITIAL_SIZE) {
            resize();
        }
        map[position] = 0;
    }

    /**
     * Resizes the internal data structure to the new size limit
     */
    public void resize() {
        if (size < INITIAL_SIZE) {
            short[] temp = new short[INITIAL_SIZE];
            System.arraycopy(map, 0, temp, 0, size);
            map = temp;
        } else {
            size = INITIAL_SIZE;
        }
    }

    /**
     * @return The size of the structure
     */
    public short size() {
        return size;
    }

    /**
     * Changes the global size of the data structure. Changes take place next
     * time the contents are modified or read
     *
     * @param size
     */
    public static void setMaxSize(short size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Size must be greater than 0");
        }
        INITIAL_SIZE = size;
    }
}
