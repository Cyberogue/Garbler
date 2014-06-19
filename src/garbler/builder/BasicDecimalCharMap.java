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

import garbler.library.CharMap;

/**
 * Basic wrapper class for a CharMap<Float> providing the basic skeleton code
 * and methods required
 *
 * @author Rogue <Alice Q.>
 */
public class BasicDecimalCharMap extends CharMap<Float> {

    /**
     * Wrapper for CharMap constructor which specifies the default case
     * sensitivity
     *
     * @param caseSensitivity true if case should be ignored, false otherwise
     */
    public BasicDecimalCharMap(boolean caseSensitivity) {
        super(caseSensitivity);
    }

    @Override
    public Float mergeValues(Float oldValue, Float newValue) {
        return oldValue + newValue;
    }
}
