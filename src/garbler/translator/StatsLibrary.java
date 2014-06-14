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
 * Library for available characters and their respective statistics
 *
 * @author Rogue <Alice Q>
 */
public class StatsLibrary extends TreeMap<Character, CharStats> {

    // WHETHER OR NOT CASE MATTERS
    private boolean ignoreCase = true;

    /**
     * Adds a new field to the internal data structures
     *
     * @param key The character to add
     * @return true if a new field was added, or false if the field already
     * exists
     */
    public boolean addField(char key) {
        char _key = getKey(key);
        if (containsKey(_key)) {
            return false;
        } else {
            put(_key, new CharStats(_key));
            return true;
        }
    }

    /**
     * The character alphabet being used by the library. That is - every single
     * character field that has been added.
     *
     * @return An array of all the characters being used
     */
    public Character[] getAlphabet() {
        return keySet().toArray(new Character[0]);
    }

    /**
     * Method to ignore case if invoked
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
        // SET THE NEW VALUE
        this.ignoreCase = active;
    }

    public static void main(String[] args) {
        java.util.Random rand = new java.util.Random();

        StatsLibrary lib = new StatsLibrary();
        System.out.println(lib);

        for (char c = 'a'; c <= 'd'; c++) {
            lib.addField(c);
        }
        System.out.println(lib);

        for (char c = 'A'; c <= 'F'; c++) {
            lib.addField(c);
        }
        System.out.println(lib);

        lib.setIgnoreCase(false);
        for (char c = 'A'; c <= 'F'; c++) {
            lib.addField(c);
        }
        lib.get('B').add('x', 5);
        System.out.println(lib);

        lib.setIgnoreCase(true);
        for (char c = 'A'; c <= 'F'; c++) {
            lib.addField(c);
        }
        System.out.println(lib);
    }
}
