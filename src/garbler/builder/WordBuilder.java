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

/**
 *
 * @author Rogue <Alice Q.>
 */
public class WordBuilder {

    // LIBRARY OF STATISTICS TO USE FOR WORD GENERATION
    private StatsLibrary statLib;

    // CONSTRUCTORS
    /**
     * Default constructor which makes a new empty StatsLibrary
     */
    public WordBuilder() {
        statLib = new StatsLibrary();
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
    /**
     * Method for accessing the statistics used by the building program
     *
     * @return a StatsLibrary of character and word statistics
     */
    public StatsLibrary getStatsLibrary() {
        return statLib;
    }
    
    // BUILDING METHODS
    // - getCharacterRecommendations
}
