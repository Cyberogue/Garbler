/*
 * The MIT License
 *
 * Copyright 2014 Rogue <Alice Q>.
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
package garbler.library;

/**
 * Interface for creating container objects that allow for data compaction with
 * either themselves or other objects. The compact() method is used in order to
 * shrink the internally held data, whereas the addAll method is used to add the
 * data from a second object as a way of merging the two. The mergeValues method
 * is to be used in case of mergeValues conflicts between the two, and may be
 * left blank if not required or overwritten if desired.
 *
 * @author Rogue <Alice Q>
 * @param <E> The type of object held internally
 * @param <M> The type of object this can be mergeValuesd with
 */
public interface Compactable<M, E> {

    /**
     * Method used in order to compact the internal data structure by merging
     * internal data members
     */
    public void compact();

    /**
     * Method which allows all the internal values from a second object to be
     * added to this one. In case of a conflict the mergeValues(). This method
     * should not be destructive to the source object and all destruction should
     * be done outside the method.
     *
     * @param source the object to add from #return this should return the new
     * value to use, most often a reference to itself
     * @return the resultant data structure or object after adding all the data
     * from the source object. This can be either a self-reference or a new
     * structure with the mergeValuesd data.
     */
    public M addAll(M source);

    /**
     * Method used in order to merge two values into one, frequently called
     * during collision conflicts
     *
     * @param oldValue the old value. This is the value that gets mergeValuesd
     * TO
     * @param newValue the new value. This is the value that gets mergeValuesd
     * FROM
     * @return the object to replace the oldValue with. This can be either a
     * simple reference of oldValue if it is immutable or an entirely new object
     * if it is not.
     */
    public E mergeValues(E oldValue, E newValue);
}
