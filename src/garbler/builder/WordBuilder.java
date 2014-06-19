/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q.>.
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
    private static final int SECONDARY_CACHE_SIZE = 32;
    private static final int PRIMARY_CACHE_SIZE = 32;

    // USED FOR CHARACTER RECOMMENDATIONS
    private float newCharacterPreference = 0.5f;

    //CONSTRUCTORS
    /**
     * Default constructor which makes a new empty StatsLibrary
     */
    public WordBuilder() {
        statLib = new StatsLibrary();
        primaryEndingsCache = new TreeMap();
        secondaryEndingsCache = new LinkedList();
    }

    /**
     * Basic constructor
     *
     * @param library the StatsLibrary to use
     */
    public WordBuilder(StatsLibrary library) {
        this.statLib = library;
    }

    // GET-SETS
    // - getStatsLibrary
    // - setNewCharacterPreferenceFactor
    /**
     * Method for accessing the statistics used by the building program
     *
     * @return a StatsLibrary of character and word statistics
     */
    public StatsLibrary getStatsLibrary() {
        return statLib;
    }

    /**
     * Method for setting the character aging factor when recommending new
     * characters. This factor changes how much additional influence characters
     * closer to the end of the word have versus those further away. A value of
     * 1.0 will heavily favor new characters whereas a value closer to 0 will
     * favor old characters much more heavily. The default value is 0.5f
     *
     * @param value a percentage value representing the shift, as a float
     * between 0.0 and 1.0 inclusive
     * @throws IllegalArgumentException when a float is used not within the
     * legal range
     */
    public void setNewCharacterPreferenceFactor(float value) {
        if (value < 0.0f || value > 1.0f) {
            throw new IllegalArgumentException("new value must be between 0 and 1");
        }
        newCharacterPreference = value;
    }

    // DATA STRUCTURE METHODS
    // - getPrimaryCacheContents
    // - getSecondaryCacheContents
    // - clearCacheContents
    // - getFromCache
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
        CharMap<Float> primary = primaryEndingsCache.get(key);
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
            primaryEndingsCache.put(key, secondary.getValue());
            secondaryEndingsCache.remove(secondary);
            return secondary.getValue();
        }
        // IT WAS IN NEITHER SO RETURN NULL
        return null;
    }

    // BUILDING METHODS
    // - getCharacterRecommendations
    public CharMap<Float> getCharacterRecommendations(String charSequence) {
        CharMap<Float> results = new CharMap<Float>() {
            @Override
            public Float merge(Float oldValue, Float newValue) {
                return oldValue + newValue;
            }
        };
        int length = charSequence.length();

        // GET THE ENDING IF IT EXISTS
        String ending;
        if (length >= 2) {
            ending = charSequence.substring(length - 2, length);
        }

        return results;
    }
}
