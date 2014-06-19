/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q.>.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, mergeValues, publish, distribute, sublicense, and/or sell
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

import garbler.library.*;

import java.util.Map.Entry;
import java.util.*;

/**
 * CLass which assists in building words by interpreting statistics and
 * maintaining a cache of repeated terms
 *
 * @author Rogue <Alice Q.>
 */
public class WordBuilder {

    // LIBRARY OF STATISTICS TO USE FOR WORD GENERATION
    private StatsLibrary statLib;

    // CACHES OF GENERATIONS FOR COMMON TERMS
    private TreeMap<String, CharMap<Float>> primaryEndingsCache;
    private LinkedList<Entry<String, CharMap<Float>>> secondaryEndingsCache;
    private int endingLength;
    private int secondaryCacheSize;
    private int primaryCacheSize;

    // USED FOR CHARACTER RECOMMENDATIONS
    private float closeCharacterPreference;
    private float sameCharacterWeightAdjust;

    //CONSTRUCTORS
    /**
     * Default constructor which makes a new empty StatsLibrary
     */
    public WordBuilder() {
        this(new StatsLibrary());
    }

    /**
     * Basic constructor
     *
     * @param library the StatsLibrary to use
     */
    public WordBuilder(StatsLibrary library) {
        statLib = library;
        primaryEndingsCache = new TreeMap();
        secondaryEndingsCache = new LinkedList();
        reset();
    }

    // GENERAL
    // - getStatsLibrary
    // - reset
    /**
     * Method for accessing the statistics used by the building program
     *
     * @return a StatsLibrary of character and word statistics
     */
    public StatsLibrary getStatsLibrary() {
        return statLib;
    }

    /**
     * Method to reset the configuration and factors back to their defaults.
     * Note that this does not clear the cache.
     */
    public final void reset() {
        endingLength = 2;
        secondaryCacheSize = 32;
        primaryCacheSize = 32;

        // USED FOR CHARACTER RECOMMENDATIONS
        closeCharacterPreference = 0.5f;
        sameCharacterWeightAdjust = 0.85f;
    }

    // CACHE METHODS
    // - getPrimaryCacheContents
    // - getSecondaryCacheContents
    // - clearCacheContents
    // - getFromCache
    // - setPrimaryCacheSize
    // - setSecondaryCacheSize
    // - setCachedWordSize
    /**
     * Method to fetch the contents of the primary cache, which is the first
     * cache searched and maintains data permanently until cleared
     *
     * @return a Collection of all the key entries in the primary cache
     */
    public Collection<String> getPrimaryCacheContents() {
        return primaryEndingsCache.keySet();
    }

    /**
     * Method to fetch the contents of the secondary cache, which is the second
     * cache searched and maintains data temporarily
     *
     * @return a Collection of all the key entries in the secondary cache
     */
    public Collection<String> getSecondaryCacheContents() {
        ArrayList<String> contents = new ArrayList(secondaryEndingsCache.size());

        for (Entry<String, CharMap<Float>> entry : secondaryEndingsCache) {
            contents.add(entry.getKey());
        }

        return contents;
    }

    /**
     * Clears both the primary and secondary caches
     *
     * @return the number of items cleared from the cache
     */
    public int clearCacheContents() {
        int size = primaryEndingsCache.size() + secondaryEndingsCache.size();
        primaryEndingsCache.clear();
        secondaryEndingsCache.clear();
        return size;

    }

    /**
     * Retrieves an item from the cache, moving it from the secondary to the
     * primary if needed
     *
     * @param key a sequence of characters to use as a key
     * @return the item held at the key value if it exists, otherwise null if it
     * doesn't
     */
    public CharMap<Float> getFromCache(String key) {
        // FIRST CHECK IF IT'S IN THE PRIMARY CACHE
        CharMap primary = primaryEndingsCache.get(key);
        if (primary != null) {
            return primary;
        }
        // IF IT WASN'T, CHECK THE SECONDARY
        Entry<String, CharMap<Float>> secondary = null;
        for (Entry<String, CharMap<Float>> entry : secondaryEndingsCache) {
            if (entry.getKey().equals(key)) {
                // IT WAS IN THE SECONDARY SO MOVE IT TO THE PRIMARY
                secondary = entry;
                break;
            }
        }
        if (secondary != null) {
            // IF IT WAS IN THE SECONDARY, MOVE IT TO THE PRIMARY THEN RETURN IT
            if (primaryEndingsCache.size() < primaryCacheSize) {
                primaryEndingsCache.put(key, secondary.getValue());
                secondaryEndingsCache.remove(secondary);
            }
            return secondary.getValue();
        }
        // IT WAS IN NEITHER SO RETURN NULL
        return null;
    }

    /**
     * Method for setting the size of the primary cache. Please note that unlike
     * the secondary cache, this method does not shrink the cache once it has
     * been filled
     *
     * @param size the new size. Note that a size of zero effectively disables
     * this
     * @throws IllegalArgumentException if size is less than zero
     */
    public void setPrimaryCacheSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size less than zero");
        }
        primaryCacheSize = size;
    }

    /**
     * Method for setting the size of the primary cache. Please note that unlike
     * the primary cache, this method will delete entries from the secondary
     * cache if you are shrinking it
     *
     * @param size the new size. Note that a size of zero effectively disables
     * this
     * @throws IllegalArgumentException if size is less than zero
     */
    public void setSecondaryCacheSize(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Size less than zero");
        }
        secondaryCacheSize = size;
        while (secondaryEndingsCache.size() >= secondaryCacheSize) {
            secondaryEndingsCache.removeFirst();
        }
    }

    /**
     * Method to set the cached word size. That is, the size of repeated endings
     * to move into the caching system. The default value is 2. A smaller value
     * will increase the number of cached entries but reduce their
     * effectiveness, while an increased value will decrease the number of
     * cached entries but increase their effectiveness.
     *
     * @param size the new size
     * @throws IllegalArgumentException if size is less than one
     */
    public void setCachedWordSize(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("Size less than zero");
        }
        endingLength = size;
    }

    // BUILDING METHODS 
    // - setCloseCharacterPreferenceFactor
    // - setSameCharacterAdjustFactor
    // - reduceInfluenceMap    
    // - reduceInfluenceMapAndCache
    // - generateAppendRecommendations
    /**
     * Method for setting the character aging factor when recommending new
     * characters. This factor changes how much additional influence characters
     * closer to the end of the word have versus those further away. A value of
     * 1.0 will heavily favor new characters whereas a value closer to 0 will
     * favor old characters much more heavily. The default value is 0.5f
     *
     * The influence of each term can be estimated (but isn't guaranteed to
     * exactly equal) value * (1-value)^n * I_n, where n indicates the distance
     * of the character
     *
     * @param value a percentage value representing the shift, as a float
     * between 0.0 and 1.0 inclusive
     * @throws IllegalArgumentException when a float is used not within the
     * legal range
     */
    public void setCloseCharacterPreferenceFactor(float value) {
        if (value < 0.0f || value > 1.0f) {
            throw new IllegalArgumentException("new value must be between 0 and 1");
        }
        closeCharacterPreference = value;
    }

    /**
     * Method to set the same character rejection factor. Use this method to
     * increase or reduce the number of same-character chains generated. This is
     * done by increasing or decreasing the influence of same-character chains
     * relative to the rest. A value of 1.0f will not treat them any differently
     * than other character chains whereas a greater value will increase their
     * occurrence and a smaller value will decrease it
     *
     * The default value for this is 0.85
     *
     * @param value the new value to use
     * @throws IllegalArgumentException if the new value is less than 0.0
     */
    public void setSameCharacterAdjustFactor(float value) {
        if (value < 0.0f) {
            throw new IllegalArgumentException("new value must be 0.0 or greater");
        }
        sameCharacterWeightAdjust = value;
    }

    /**
     * Method which reduces an integer-based character influence into a CharMap
     * of floats, each representing a weighted sum from the integers. Note that
     * this applies an aging algorithm to each entry which can be controlled
     * through the setCloseCharacterPreferenceFactor method and same-character
     * repetition can be reduced through the setSameCharacterAdjustFactor method
     *
     * @param map an OccurrenceMap of character influences and their distances
     * @return a crunched CharMap of floating point percentages between 0.0 and
     * 1.0
     */
    public CharMap<Float> reduceInfluenceMap(OccurrenceMap map) {
        CharMap<Float> results = new BasicDecimalCharMap(map.isCaseSensitive());

        // FOR THE AGING ALGORITHM
        float oldCharacterPreference = 1.0f - closeCharacterPreference;

        // NOW RUN THE ALGORITHM
        for (Entry<Character, OccurrenceList> entry : map.entrySet()) {
            char key = entry.getKey();
            OccurrenceList value = entry.getValue();

            // GET THE WEIGHTED SUM
            float weightedSum = 0.0f;
            for (int i = value.size() - 1; i >= 0; i--) {
                // WE MUST GO IN REVERSE TO APPLY AGING TO OLD VALUES
                weightedSum *= oldCharacterPreference;
                weightedSum += closeCharacterPreference * value.getCount(i);
            }

            // IF IT MATCHES THE KEY, REDUCE IT
            // ADD IT TO THE NEW MAP
            results.put(key, weightedSum);
        }

        return results;
    }

    /**
     * Main method for requesting a new character to append to a string which
     * applies the internal algorithms and values to the collected statistics
     *
     * @param charSequence a sequence of characters to interpret
     * @return a CharMap of percentages totaling 1.0f, each representing the
     * total influence of each character
     */
    public CharMap<Float> generateAppendRecommendations(String charSequence) {
        CharMap<Float> results = new CharMap<Float>() {
            @Override
            public Float mergeValues(Float oldValue, Float newValue) {
                // MERGING TWO LISTS OF PERCENTAGES REQUIRES A REBALANCING OF ALL THE PERCERNTAGES, SO IT CAN'T BE DONE IN A SINGLE PASS AND THUS IS ILLEGAL
                throw new ArithmeticException("Illegal percentages merge");
            }
        };

        // SEPARATE THE SEQUENCE INTO ENDING AND WORD
        int length = charSequence.length();
        String word, ending;
        if (length >= endingLength) {
            word = charSequence.substring(0, length - endingLength);
            ending = charSequence.substring(length - endingLength, length);
        } else {
            word = "";
            ending = charSequence;
        }

        // GET THE MAP FOR THE ENDING FROM CACHE If it EXISTS
        CharMap<Float> endingMap = getFromCache(ending);
        if (endingMap == null) {
            // IF IT DOESN'T EXIST, MAKE IT
            endingMap = reduceInfluenceMap(statLib.generateInfluenceMap(ending));
            // AND PUSH IT INTO THE SECONDARY CACHE
            secondaryEndingsCache.push(new AbstractMap.SimpleImmutableEntry(ending, endingMap));
            if (secondaryEndingsCache.size() >= secondaryCacheSize) {
                secondaryEndingsCache.pop();
            }
        }

        // GENERATE THE REST OF THE WORD
        CharMap<Float> wordMap = reduceInfluenceMap(statLib.generateInfluenceMap(charSequence, endingLength));

        // NOW MERGE THE TWO MAPS - FIRST THE ENDINGS
        for (Entry<Character, Float> entry : endingMap.entrySet()) {
            results.put(entry.getKey(), entry.getValue());
        }
        // THEN THE WORD
        for (Entry<Character, Float> entry : wordMap.entrySet()) {
            // SEE IF A VALUE ALREADY EXISTS
            Float existing = results.get(entry.getKey());
            // SINCE WE DID THE WEIGHING BEFOREHAND, WE JUST HAVE TO ADD THE RESULTS
            if (existing == null) {
                results.put(entry.getKey(), entry.getValue());
            } else {
                results.put(entry.getKey(), existing + entry.getValue());
            }
        }

        // APPLY REPEATING-CHARACTER CHAIN ADJUST
        char lastChar = charSequence.charAt(length - 1);
        Float repeatEntry = results.get(lastChar);
        if (repeatEntry != null) {
            results.put(lastChar, repeatEntry * sameCharacterWeightAdjust);
        }

        // THEN REBALANCE THME
        BasicDecimalCharMap.rebalanceMap(results);

        return results;
    }
}
