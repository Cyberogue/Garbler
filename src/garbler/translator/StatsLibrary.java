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
import java.util.Map.Entry;

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
            CharStats stats = new CharStats(_key);
            stats.ignoreCases(ignoreCase);
            put(_key, stats);
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
    public void ignoreCases(boolean active) {
        // SET THE NEW VALUE
        this.ignoreCase = active;

        // AND PASS THIS ON TO THE CHILDREN
        for (CharStats stats : values()) {
            stats.ignoreCases(active);
        }
    }

    /**
     * Collapses the internal data structure by making all the keys lowercase,
     * merging with necessary. Please not that this method is destructive and
     * there is no way to undo this.
     */
    public void collapseCases() {
        // LIST OF CRAP TO REMOVE AND REPLACE
        ArrayList<Character> trashbin = new ArrayList(size());
        ArrayList<Entry<Character, CharStats>> uppercase = new ArrayList(size());

        // ITERATE THROUGH IT ALL AND FIND THE UPPERCASE ENTRIES
        for (Entry<Character, CharStats> e : entrySet()) {
            if (Character.isUpperCase(e.getKey())) {
                uppercase.add(e);
            }
        }

        // NOW SWAP THE NECESSARY KEYS
        for (Entry<Character, CharStats> e : uppercase) {
            Character keyUpper = e.getKey();
            CharStats entryUpper = e.getValue();
            Character keyLower = Character.toLowerCase(keyUpper);
            CharStats entryLower = get(keyLower);

            if (entryLower == null) {
                put(keyLower, entryUpper);
            } else {
                entryLower.addAll(entryUpper);
            }
            trashbin.add(keyUpper);
        }

        // DELETE THE UPPERCASE KEYS
        for (Character key : trashbin) {
            remove(key);
        }

        // AND COMPACT THE RESULTS
        for (CharStats stats : values()) {
            stats.collapseCases();
        }
    }

    public static void main(String[] args) {
        java.util.Random rand = new java.util.Random();

        StatsLibrary lib = new StatsLibrary();
        lib.ignoreCases(false);

        System.out.println("--------- TEST#" + 1 + " ---------");

        for (char c = 'a'; c <= 'd'; c++) {
            lib.addField(c);
        }

        for (char c = 'A'; c <= 'F'; c++) {
            lib.addField(c);
        }

        for (char c = 'A'; c <= 'F'; c++) {
            lib.addField(c);
        }
        lib.get('b').add('x', 6);
        lib.get('B').add('x', 1);
        lib.get('B').add('X', 2);
        lib.get('B').add('y', 3);
        System.out.println(lib);

        lib.collapseCases();
        System.out.println(lib);

        for (int y = 1; y <= 5; y++) {
            System.out.println("--------- TEST#" + y + " ---------");
            lib.clear();
            lib.ignoreCases(false);
            char c = 'a';
            for (int i = 0; i <= 2 + rand.nextInt(4); i++) {
                if (Character.isLowerCase(c) && rand.nextFloat() > .3f) {
                    c = Character.toUpperCase(c);
                } else {
                    c = (char) ('a' + rand.nextInt(26));
                }
                lib.addField(c);
                CharStats cstat = lib.get(c);

                for (int x = 0; x <= 2 + rand.nextInt(4); x++) {
                    if (rand.nextFloat() > .5f) {
                        cstat.add((char) ('a' + rand.nextInt(26)), rand.nextInt(6), 1 + rand.nextInt(5));
                    } else {
                        cstat.add((char) ('A' + rand.nextInt(26)), rand.nextInt(6), 2);
                    }
                }
            }
            System.out.println(lib);
            System.out.print("Alphabet: ");
            for (Character abc : lib.getAlphabet()) {
                System.out.print(abc);
            }
            System.out.println();

            lib.collapseCases();
            System.out.println(lib);
            System.out.print("Alphabet: ");
            for (Character abc : lib.getAlphabet()) {
                System.out.print(abc);
            }
            System.out.println();
        }
    }
}
