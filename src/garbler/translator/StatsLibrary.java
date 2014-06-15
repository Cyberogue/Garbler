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

/**
 * Library for available characters and their respective statistics
 *
 * @author Rogue <Alice Q>
 */
public class StatsLibrary {

    public static void main(String[] args) {
        java.util.Random rand = new java.util.Random();

        CharStats lc = new CharStats('a');
        CharStats uc = new CharStats('A');

        System.out.println(lc);
        System.out.println(uc);

        lc.setCaseSensitive(false);
        uc.setCaseSensitive(true);

        lc.addCharacterCorrelation('b', 1);
        lc.addCharacterCorrelation('c', 3);
        lc.addCharacterCorrelation('C', 2);
        uc.addCharacterCorrelation('C', 2);
        uc.addCharacterCorrelation('D', 3);

        lc.addInstance();
        lc.addInstance();
        uc.addInstance();
        uc.addInstance();

        System.out.println(lc.getAllCorrelations());
        System.out.println(uc.getAllCorrelations());

        lc.addAll(uc);

        System.out.println(lc.getAllCorrelations());
    }
}
