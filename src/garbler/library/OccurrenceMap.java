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
package garbler.library;

/**
 * Wrapper class for CharMaps of OccurrenceLists
 *
 * @author Rogue <Alice Q>
 */
public class OccurrenceMap extends CharMap<OccurrenceList> {
       // CONSTRUCTORS
    /**
     * Basic constructor for a case insensitive OccurrenceMap
     */
    public OccurrenceMap() {
        super();
    }

    /**
     * Basic constructor allowing a specified initial case sensitivity.
     *
     * @param caseSensitive true if case should be ignored, false otherwise
     */
    public OccurrenceMap(boolean caseSensitive) {
        super(caseSensitive);
    }

    @Override
    public OccurrenceList merge(OccurrenceList oldValue, OccurrenceList newValue) {
        return oldValue.addAll(newValue);
    }
}
