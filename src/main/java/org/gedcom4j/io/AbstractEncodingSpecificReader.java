/*
 * Copyright (c) 2009-2016 Matthew R. Harrah
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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.gedcom4j.io;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * A base class for the various kinds of readers needed based on the encoding used for the data
 * 
 * @author frizbog
 */
abstract class AbstractEncodingSpecificReader {
    /**
     * The stream of bytes to read
     */
    protected final InputStream byteStream;

    /**
     * The number of lines read from the input file
     */
    protected int linesRead = 0;

    /**
     * Whether the file has been completely read
     */
    protected boolean complete = false;

    /**
     * The list of observers
     */
    protected List<WeakReference<FileProgressListener>> observers = new CopyOnWriteArrayList<WeakReference<FileProgressListener>>();

    /**
     * Is the load process being cancelled
     */
    protected boolean cancelled;

    /**
     * Constructor.
     * 
     * @param byteStream
     *            the stream of bytes to read
     */
    protected AbstractEncodingSpecificReader(InputStream byteStream) {
        this.byteStream = byteStream;
    }

    /**
     * Indicate that file loading should be cancelled
     */
    public void cancel() {
        cancelled = true;
    }

    /**
     * Register a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void registerObserver(FileProgressListener observer) {
        observers.add(new WeakReference<FileProgressListener>(observer));
    }

    /**
     * Unregister a observer (listener) to be informed about progress and completion.
     * 
     * @param observer
     *            the observer you want notified
     */
    public void unregisterObserver(FileProgressListener observer) {
        int i = 0;
        while (i < observers.size()) {
            WeakReference<FileProgressListener> observerRef = observers.get(i);
            if (observerRef == null || observerRef.get() == observer) {
                observers.remove(observerRef);
            } else {
                i++;
            }
        }
        observers.add(new WeakReference<FileProgressListener>(observer));
    }

    /**
     * Read all the lines using the appropriate encoding
     * 
     * @return all the lines of the input stream
     * @throws IOException
     *             if there is a problem reading the bytes
     */
    protected abstract List<String> load() throws IOException;

    /**
     * Notify all listeners about the change
     * 
     * @param e
     *            the change event to tell the observers
     */
    protected void notifyObservers(FileProgressEvent e) {
        int i = 0;
        while (i < observers.size()) {
            WeakReference<FileProgressListener> observerRef = observers.get(i);
            if (observerRef == null) {
                observers.remove(observerRef);
            } else {
                observerRef.get().progressNotification(e);
            }
        }
    }
}
