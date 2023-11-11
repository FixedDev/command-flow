/*
 * This file is part of commandflow, licensed under the MIT license
 *
 * Copyright (c) 2020-2023 Unnamed Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package team.unnamed.commandflow;

public interface Namespace {

    /**
     * Gets an injected object from the backing Map
     *
     * @param clazz The class type of the Object to get
     * @param name The name of the Object to get
     * @param <T> The type of the Object to get
     *
     * @return A nullable instance of T contained in the backing map with the specified name
     */
    <T> T getObject(Class<T> clazz, String name);

    /**
     * Sets an Object of Type T with a specified name into the backing Map
     * If an object with the same name and type is already on the map, it will be override
     *
     * @param clazz The class type of the Object to set
     * @param name The name of the object to set
     * @param object The Object to set into the backing map
     * @param <T> The Type of the object to set
     */
    <T> void setObject(Class<T> clazz, String name, T object);

    static Namespace create() {
        return new NamespaceImpl();
    }

}
