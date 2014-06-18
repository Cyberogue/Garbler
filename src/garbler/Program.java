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
package garbler;

import garbler.library.StatsLibrary;
import garbler.builder.WordBuilder;

/**
 * Main test class simply used for running individual test cases
 *
 * @author Rogue <Alice Q.>
 */
public class Program {

    // MAIN TEST METHOD
    // THIS WILL BE TAKEN OUT OF THE FINAL VERSION AND IS JUST FOR TESTING TEST CASES
    public static void main(String[] args) {
        StatsLibrary lib = new StatsLibrary(false);
        WordBuilder builder = new WordBuilder(lib);

        lib.parseLine("abcd abcd bccx", ",.");
        long start = System.currentTimeMillis();
        for (char c = 'a'; c <= 'z'; c++) {
            lib.getInfluenceMapCached("abcabcabcabcab" + c);
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (char c = 'a'; c <= 'z'; c++) {
            lib.getInfluenceMapCached("abcabcabcabcab" + c);
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (char c = 'a'; c <= 'z'; c++) {
            lib.getInfluenceMapCached("abcabcabcabcab" + c);
        }
        System.out.println(System.currentTimeMillis() - start);

        start = System.currentTimeMillis();
        for (char c = 'a'; c <= 'z'; c++) {
            lib.getInfluenceMapCached("abcabcabcabcab" + c);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
