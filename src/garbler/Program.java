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
package garbler;

import garbler.library.*;
import garbler.builder.*;
import java.util.Collections;

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

        String testSeed = "orem ipsum dolor sit amet, te sit erat adipisci necessitatibus. Illud exerci animal ne per. Sea cibo scribentur eu. Ea ceteros insolens instructior cum. "
                + "Vix sint dicit fabulas ut, ullum nostrud nec no. Eu debitis omittam per, quo labores offendit placerat at. Dicta solet dissentiet eu vel.";
        String[] testRequests = {"val", "abc", "mal", "malosu", "dubi", "lica", "labi"};

        System.out.println("SEED: " + testSeed);

        for (String req : testRequests) {
            builder.reset();

            System.out.println("REQUEST: " + req);
            lib.parseLine(testSeed, ",.");

            CharMap<Float> result = builder.generateAppendRecommendations(req);
            System.out.println("\tRaw: " + result);

            builder.setCloseCharacterPreferenceFactor(0.9f);
            result = builder.generateAppendRecommendations(req);
            BasicDecimalCharMap.trimAndRebalanceMap(result, 0.07f);
            System.out.println("\tMod 1: " + result);

            builder.setSameCharacterAdjustFactor(0.5f);
            result = builder.generateAppendRecommendations(req);
            BasicDecimalCharMap.trimAndRebalanceMap(result, 0.07f);
            System.out.println("\tMod 2: " + result);

            System.out.print("\tSuggestions: ");
            for (char c : result.keySet()) {
                System.out.print(req + c + ", ");
            }

            System.out.println("\n\tSecondary Cache: " + builder.getSecondaryCacheContents() + "\tPrimary Cache: " + builder.getPrimaryCacheContents());
            System.out.println();
        }
    }
}
