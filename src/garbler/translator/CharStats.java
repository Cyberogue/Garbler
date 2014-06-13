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
 * Class containing the statistics for a single character
 *
 * @author Rogue <Alice Q>
 */
public class CharStats {

    // THE CHARACTER BEING REPRESENTED
    char definition;

    // A MAP OF THE NUMBER OF TIMES A CHARACTER OCCURS AFTER THIS LETTER
    TreeMap<Character, OccurrenceList> numHits;

    /**
     * Basic constructor
     *
     * @param c The character that the entry is for
     */
    public CharStats(char entry) {
        definition = entry;

        numHits = new TreeMap();
    }

    /**
     * @return The character the entry is for
     */
    public char getDefinitionCharacter() {
        return definition;
    }

    @Override
    public String toString() {
        return definition + numHits.toString();
    }
}
