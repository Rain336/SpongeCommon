/*
 * This file is part of Sponge, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
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
package co.aikar.util;

import gnu.trove.map.hash.TIntObjectHashMap;

import java.util.function.Function;

/**
 * Allows you to pass a Loader function that when a key is accessed that doesn't
 * exists, automatically loads the entry into the map by calling the loader
 * Function.
 *
 * .get() Will only return null if the Loader can return null.
 *
 * You may pass any backing Map to use.
 *
 * This class is not thread safe and should be wrapped with
 * Collections.synchronizedMap on the OUTSIDE of the LoadingMap if needed.
 *
 * Do not wrap the backing map with Collections.synchronizedMap.
 *
 * @param <V> Value
 */
public class LoadingIntMap<V> extends TIntObjectHashMap<V> {

    private static final long serialVersionUID = 4788110182547553543L;
    private final Function<Integer, V> loader;

    /**
     * Initializes an auto loading map using specified loader and backing map
     *
     * @param loader
     */
    public LoadingIntMap(Function<Integer, V> loader) {
        this.loader = loader;
    }

    @Override
    public V get(int key) {
        V res = super.get(key);
        if (res == null) {
            res = this.loader.apply(key);
            if (res != null) {
                put(key, res);
            }
        }
        return res;
    }

    /**
     * Due to java stuff, you will need to cast it to (Function) for some cases
     *
     * @param <T>
     */
    public abstract static class Feeder<T> implements Function<T, T> {

        @Override
        public T apply(Object input) {
            return apply();
        }

        public abstract T apply();
    }
}
