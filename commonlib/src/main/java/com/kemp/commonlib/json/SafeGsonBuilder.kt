package com.kemp.commonlib.json

import com.google.gson.*
import com.google.gson.internal.ConstructorConstructor
import com.google.gson.internal.Excluder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Field
import java.lang.reflect.Type
import java.util.HashMap

class SafeGsonBuilder {
    companion object {
        val default by lazy { SafeGsonBuilder().setPrettyPrinting().safeCreate() }
        val insensitive by lazy {
            SafeGsonBuilder().setPrettyPrinting().setNamingStrategy(object : NamingStrategy {
                override fun translateFieldName(f: Field?): String? = f?.name?.toLowerCase()
                override fun translateJsonName(f: String?): String? = f?.toLowerCase()
            }).safeCreate()
        }

        var errorLog: ((Exception) -> Unit)? = { it.printStackTrace() }

        fun errorPrint(e: Exception) {
            errorLog?.invoke(e)
        }
    }

    private val mInstanceCreators by lazy { HashMap<Type, InstanceCreator<*>>() }
    private val mConstructorConstructor by lazy { ConstructorConstructor(mInstanceCreators) }
    private var mExcluder = Excluder.DEFAULT
    private val mGsonBuilder = GsonBuilder()
    private var mNameStrategy: NamingStrategy = object : NamingStrategy {
        override fun translateFieldName(f: Field?): String? = f?.name
        override fun translateJsonName(f: String?): String? = f
    }

    fun safeCreate(): Gson {
        mGsonBuilder.registerTypeAdapterFactory(BaseTypeAdapterFactory())
        val gson = create()

        val factoriesField = Gson::class.java.getDeclaredField("factories")
        factoriesField.isAccessible = true
        val factories = factoriesField.get(gson) as List<TypeAdapterFactory>
        val newFactories = arrayListOf<TypeAdapterFactory>()

        factories.forEach { typeAdapterFactory ->
            when (typeAdapterFactory) {
                is com.google.gson.internal.bind.CollectionTypeAdapterFactory -> newFactories.add(CollectionTypeAdapterFactory(mConstructorConstructor))
                is com.google.gson.internal.bind.ReflectiveTypeAdapterFactory -> newFactories.add(ReflectiveTypeAdapterFactory(mConstructorConstructor, mExcluder, JsonAdapterAnnotationTypeAdapterFactory(mConstructorConstructor), mNameStrategy))
                else -> newFactories.add(typeAdapterFactory)
            }
        }
        factoriesField.set(gson, newFactories)
        return gson
    }

    fun create(): Gson {
        return mGsonBuilder.create()
    }

    fun setPrettyPrinting(): SafeGsonBuilder {
        mGsonBuilder.setPrettyPrinting()
        return this
    }

    fun serializeNulls(): SafeGsonBuilder {
        mGsonBuilder.serializeNulls()
        return this
    }

    fun registerTypeAdapterFactory(factory: TypeAdapterFactory): SafeGsonBuilder {
        mGsonBuilder.registerTypeAdapterFactory(factory)
        return this
    }

    fun registerTypeAdapter(type: Type, typeAdapter: TypeAdapter<*>): SafeGsonBuilder {
        if (typeAdapter is InstanceCreator<*>) {
            mInstanceCreators[type] = typeAdapter as InstanceCreator<*>
        }
        mGsonBuilder.registerTypeAdapter(type, typeAdapter)
        return this
    }

    fun setNamingStrategy(namingStrategy: NamingStrategy): SafeGsonBuilder {
        mNameStrategy = namingStrategy
        return this
    }

    fun excludeFieldsWithoutExposeAnnotation(): SafeGsonBuilder {
        mGsonBuilder.excludeFieldsWithoutExposeAnnotation()
        return this
    }

    fun disableInnerClassSerialization(): SafeGsonBuilder {
        mGsonBuilder.disableInnerClassSerialization()
        return this
    }

    fun setExclusionStrategies(vararg strategies: ExclusionStrategy): SafeGsonBuilder {
        for (strategy in strategies) {
            mExcluder = mExcluder.withExclusionStrategy(strategy, true, true)
        }
        return this
    }

    fun setDateFormat(pattern: String): SafeGsonBuilder {
        mGsonBuilder.setDateFormat(pattern)
        return this
    }

    class BaseTypeAdapterFactory : TypeAdapterFactory {
        override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
            return when (type?.rawType) {
                Int::class.java -> NumberTypeAdapter.create<Int>(0)
                Int::class.javaObjectType -> NumberTypeAdapter.create<Int>(null)
                Double::class.java -> NumberTypeAdapter.create<Double>(0.0)
                Double::class.javaObjectType -> NumberTypeAdapter.create<Double>(null)
                Float::class.java -> NumberTypeAdapter.create<Float>(0f)
                Float::class.javaObjectType -> NumberTypeAdapter.create<Float>(null)
                Boolean::class.java -> BoolTypeAdapter()
                Boolean::class.javaObjectType -> BoolTypeAdapter(null)
                String::class.java -> StrTypeAdapter()
                else -> null
            } as? TypeAdapter<T>
        }
    }
}