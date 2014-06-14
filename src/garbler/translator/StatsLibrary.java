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

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.Map.Entry;

/**
 * Library for available characters and their respective statistics
 *
 * @author Rogue <Alice Q>
 */
public class StatsLibrary extends CharMap<CharStats>{

    public void merge(CharStats lowerEntry, CharStats upperEntry) {

    }

    public static void main(String[] args) {
        java.util.Random rand = new java.util.Random();

        CharMap<OccurrenceList> map = new CharMap<OccurrenceList>() {
            @Override
            public void merge(Entry<Character, OccurrenceList> oldValue, Entry<Character, OccurrenceList> newValue) {
                oldValue.getValue().addAll(newValue.getValue());
            }
        };

        map.setCaseSensitive(true);
        System.out.println(map);

        map.put('a', new OccurrenceList());
        map.put('b', new OccurrenceList());
        map.put('c', new OccurrenceList());
        map.put('A', new OccurrenceList());
        map.put('B', new OccurrenceList());
        map.put('C', new OccurrenceList());
        System.out.println(map);

        map.get('a').increment(2);
        map.get('b').increment(3);
        map.get('A').increment(4);
        map.get('A').increment(2);
        System.out.println(map);

        map.collapse();
        System.out.println(map);
    }
}
