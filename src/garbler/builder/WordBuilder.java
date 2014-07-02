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
package garbler.builder;

import java.util.Random;
import java.util.Map;
import java.util.Map.Entry;
import garbler.library.CharMap;
import garbler.library.StatsLibrary;
import garbler.structure.BasicDecimalCharMap;

/**
 * Class which builds words using random chance and the statistics provided from
 * a StatsLibrary
 *
 * @author Rogue <Alice Q>
 */
public class WordBuilder {

    // CLASS WHICH HANDLES ALL THE FANCY NUMBER CRUNCHING
    private StatsCruncher libData;

    // STATS LIBRARY INSIDE THE CRUNCHER CLASS
    private StatsLibrary statLib;

    // RNG
    private Random random;

    /**
     * Basic constructor
     *
     * @param data a pre-initialized StatsCruncher object to use for generating
     * words for building words
     */
    public WordBuilder(StatsCruncher data) {
        libData = data;
        random = new Random();
        statLib = data.getStatsLibrary();
    }

    /**
     * Basic constructor
     *
     * @param data a pre-initialized StatsCruncher object to use for generating
     * words
     * @param seed the seed to use for the random number generator
     */
    public WordBuilder(StatsCruncher data, long seed) {
        libData = data;
        random = new Random(seed);
        statLib = data.getStatsLibrary();
    }

    /**
     * Method to generate a single word from the given data up to a certain
     * length, however the returned word is not guaranteed to be of said length
     *
     * @param maxLength the maximum length of a word
     * @return a generated word matching the data provided by the cruncher
     */
    public String generateWord(int maxLength, float threshold) {
        String s = "";

        s += pickFromDistribution(libData.getPrimaryCharacterDitribution());
        for (int i = 1; i < maxLength; i++) {
            // USE THE LAST UP-TO-6 CHARACTERS AS A SEED
            String seed = (s.length() < 6 ? s : s.substring(s.length() - 6, s.length()));

            // CHECK THE ODDS OF IT BEING AN ENDING
            if (random.nextFloat() < libData.getEOWFactor(seed)){
                return s;
            }
            
            // GER RECOMMENDATIONS FOR AND GENERATE THE NEXT CHARACTER
            CharMap<Float> recommendations = libData.generateAppendRecommendations(seed);
            if (threshold < 1.0f) {
                BasicDecimalCharMap.trimMap(recommendations, threshold);
            }
            BasicDecimalCharMap.rebalanceMap(recommendations);
            Character nextChar = pickFromDistribution(recommendations);

            // ADD IT TO THE WORD
            s += nextChar;
        }

        return s;
    }

    /**
     * Method to pick a random character from a PMF distribution of characters.
     * Please make sure that the sum of all the values in the distribution is
     * equal to 1.0f for best results.
     *
     * @param distribution a map of characters and their respective
     * probabilities
     * @return a randomly selected character from the distribution
     */
    public Character pickFromDistribution(Map<Character, Float> distribution) {
        // PICK A RANDOM NUMBER
        float pick = random.nextFloat();
        // ITERATE THROUGH THE VALUES UNTIL A MATCH IS MADE
        float sum = 0.0f;
        for (Entry<Character, Float> entry : distribution.entrySet()) {
            // ADD THE VALUE TO THE SUM
            sum += entry.getValue();

            // CHECK IF IT'S WITHIN RANGE
            if (pick <= sum) {
                return entry.getKey();
            }
        }

        // REACHED THE END, RETURN NULL
        return 0;
    }
}
