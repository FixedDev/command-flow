package me.fixeddev.commandflow.annotated.part;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Objects;

public class Key {
    private Type type;
    private Annotation annotation;

    public Key(@NotNull Type type, @Nullable Annotation annotation) {
        this.type = type;
        this.annotation = annotation;
    }

    public Key(@NotNull Type type) {
        this.type = type;
    }

    @NotNull
    public Type getType() {
        return type;
    }

    @Nullable
    public Annotation getAnnotation() {
        return annotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Key)) return false;
        Key key = (Key) o;
        return type.equals(key.type) &&
                Objects.equals(annotation, key.annotation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, annotation);
    }
}
