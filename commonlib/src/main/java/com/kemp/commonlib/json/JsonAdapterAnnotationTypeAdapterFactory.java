package com.kemp.commonlib.json;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.reflect.TypeToken;

/**
 * Given a type T, looks for the annotation {@link JsonAdapter} and uses an instance of the
 * specified class as the default type adapter.
 *
 * @since 2.3
 */
public final class JsonAdapterAnnotationTypeAdapterFactory implements TypeAdapterFactory {
    private final ConstructorConstructor constructorConstructor;

    public JsonAdapterAnnotationTypeAdapterFactory(ConstructorConstructor constructorConstructor) {
        this.constructorConstructor = constructorConstructor;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> targetType) {
        Class<? super T> rawType = targetType.getRawType();
        JsonAdapter annotation = rawType.getAnnotation(JsonAdapter.class);
        if (annotation == null) {
            return null;
        }
        return (TypeAdapter<T>) getTypeAdapter(constructorConstructor, gson, targetType, annotation);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" }) // Casts guarded by conditionals.
    TypeAdapter<?> getTypeAdapter(ConstructorConstructor constructorConstructor, Gson gson,
                                  TypeToken<?> type, JsonAdapter annotation) {
        Object instance = constructorConstructor.get(TypeToken.get(annotation.value())).construct();

        TypeAdapter<?> typeAdapter;
        if (instance instanceof TypeAdapter) {
            typeAdapter = (TypeAdapter<?>) instance;
        } else if (instance instanceof TypeAdapterFactory) {
            typeAdapter = ((TypeAdapterFactory) instance).create(gson, type);
        } else if (instance instanceof JsonSerializer || instance instanceof JsonDeserializer) {
            JsonSerializer<?> serializer = instance instanceof JsonSerializer
                    ? (JsonSerializer) instance
                    : null;
            JsonDeserializer<?> deserializer = instance instanceof JsonDeserializer
                    ? (JsonDeserializer) instance
                    : null;
            typeAdapter = new TreeTypeAdapter(serializer, deserializer, gson, type, null);
        } else {
            throw new IllegalArgumentException("Invalid attempt to bind an instance of "
                    + instance.getClass().getName() + " as a @JsonAdapter for " + type.toString()
                    + ". @JsonAdapter value must be a TypeAdapter, TypeAdapterFactory,"
                    + " JsonSerializer or JsonDeserializer.");
        }

        if (typeAdapter != null && annotation.nullSafe()) {
            typeAdapter = typeAdapter.nullSafe();
        }

        return typeAdapter;
    }
}

