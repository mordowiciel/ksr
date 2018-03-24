package metrics;/*
 * The MIT License
 *
 * Copyright 2016 Thibault Debatty.
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

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.LinkedList;

/**
 *
 * @author Thibault Debatty
 * @param <T> The type of items in this dataset
 */
public abstract class Dataset<T> implements Iterable<T>, Serializable {

    /**
     * Return all items of the dataset (except if it's an infinite dataset).
     * @return
     */
    public LinkedList<T> getAll() {
        LinkedList<T> list = new LinkedList<T>();
        for (T item : this) {
            list.add(item);
        }
        return list;
    }

    /**
     * Get the first items of the dataset.
     * @param count
     * @return
     */
    public final LinkedList<T> get(int count) {
        LinkedList<T> list = new LinkedList<T>();
        for (T item : this) {
            list.add(item);
            if (list.size() == count) {
                return list;
            }
        }
        return list;
    }


    /**
     * Serialize and save this dataset to an output stream (might be a file).
     * The java metrics.Dataset class gets serialized and saved, not the actual
     * elements of the dataset.
     * @param out
     * @throws IOException if output file does not exist
     */
    public final void save(final OutputStream out) throws IOException {
        ObjectOutputStream object_out = new ObjectOutputStream(out);
        object_out.writeObject(this);
    }

    /**
     * Load a saved dataset.
     *
     * @param input
     * @return
     * @throws IOException if file does not exist
     * @throws ClassNotFoundException if class is not recognized
     */
    public static Dataset load(final InputStream input)
            throws IOException, ClassNotFoundException {
        ObjectInputStream object_input = new ObjectInputStream(input);
        Dataset dataset = (Dataset) object_input.readObject();
        return dataset;
    }
}
