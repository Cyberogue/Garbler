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

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Class intended to hold statistics for a single character
 *
 * @author Rogue <Alice Q>
 */
public class CharStats {

    // THE NAME OF THE CHARACTER BEING REPRESENTED BY THESE STATISTICS
    private Character name;

    // THE NUMBER OF TIMES THIS CHARACTER HAS BEEN ENCOUNTERED
    private int encounters;

    // THE DISTANCE FROM THE START AND END OF A WORD
    private OccurrenceList startPosition;
    private OccurrenceList endPosition;

    // MAP OF ALL THE TIMES THIS CHARACTER OCCURS IN THE SAME WORD AS OTHER CHARACTERS, AND THEIR DISTANCES
    private CharMap<OccurrenceList> correlations;

    /**
     * Usage of this constructor is not recommended, please use CharStats(char).
     * This constructor makes a new CharStats for the null character
     */
    public CharStats() {
        this('\0');
    }

    /**
     * Basic constructor for a specific character
     *
     * @param c The character this entry is for
     */
    public CharStats(char c) {
        name = c;

        startPosition = new OccurrenceList();
        endPosition = new OccurrenceList();

        correlations = new CharMap<OccurrenceList>() {
            // WHEN A MERGE IS REQUIRED SIMPLY ADD ALL THE NEW VALUES TO THE OLD VALUES
            @Override
            public void merge(OccurrenceList oldValue, OccurrenceList newValue) {
                oldValue.addAll(newValue);
            }
        };
    }

    public void collapse() {

    }

}
