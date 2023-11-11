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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

final class NamespaceImpl implements Namespace {

    private final Map<Class<?>, Map<String, Object>> backing;

    public NamespaceImpl() {
        backing = new ConcurrentHashMap<>();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getObject(Class<T> clazz, String name) {
        return (T) backing.getOrDefault(clazz, new ConcurrentHashMap<>()).get(name);
    }

    @Override
    public <T> void setObject(Class<T> clazz, String name, T object) {
        Map<String, Object> map = backing.computeIfAbsent(clazz, key -> new ConcurrentHashMap<>());

        map.put(name, object);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NamespaceImpl)) return false;
        NamespaceImpl namespace = (NamespaceImpl) o;
        return Objects.equals(backing, namespace.backing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(backing);
    }

}