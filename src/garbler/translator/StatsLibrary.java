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

import java.util.TreeMap;

/**
 * Library for available characters and their respective statistics
 *
 * @author Rogue <Alice Q>
 */
public class StatsLibrary extends TreeMap<Character, CharStats> {
    /**
     * Adds a new field to the internal data structures
     *
     * @param key The character to add
     * @return true if a new field was added, or false if the field already
     * exists
     */
    public boolean addField(char key) {
        if (super.containsKey(key)) {
            return false;
        } else {
            super.put(key, new CharStats(key));
            return true;
        }
    }

    public static void main(String[] args) {
        java.util.Random rand = new java.util.Random();
        StatsLibrary lib = new StatsLibrary();
        CharStats cs = new CharStats('b');

        for (char c = 'a'; c <= 'f'; c++){
            lib.addField(c);
        }
        
        System.out.println(lib);
        
        System.out.println("End run");
    }
}
