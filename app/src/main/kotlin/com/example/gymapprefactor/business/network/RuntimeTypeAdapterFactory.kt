package com.example.gymapprefactor.business.network

import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

import com.google.gson.*

// This is library java code that I adapted to kotlin
// basically you can store/pull interfaces, it will find the derived class and instantiate it
// it has to be setup beforehand with registerSubtype
class RuntimeTypeAdapterFactory<T> private constructor(
	private val baseType: Class<T>,
	private val typeFieldName: String,
	private val maintainType: Boolean
) : TypeAdapterFactory {
	private val labelToSubtype = mutableMapOf<String, Class<out T>>()
	private val subtypeToLabel = mutableMapOf<Class<out T>, String>()

	override fun <R : Any?> create(gson: Gson, type: TypeToken<R>): TypeAdapter<R>? {
		val rawType = type.rawType as Class<R>
		if (!baseType.isAssignableFrom(rawType)) {
			return null
		}

		val jsonElementAdapter = gson.getAdapter(JsonElement::class.java)
		val subtypeToDelegate = mutableMapOf<Class<out T>, TypeAdapter<*>>()
		for ((label, subtype) in labelToSubtype) {
			subtypeToDelegate[subtype] = gson.getDelegateAdapter(this, TypeToken.get(subtype))
		}

		return object : TypeAdapter<R>() {
			override fun write(out: JsonWriter, value: R) {
				val srcType = value!!::class.java as Class<out T>
				val label = subtypeToLabel[srcType]
					?: throw JsonParseException("Cannot serialize $srcType; not registered.")
				val delegate = subtypeToDelegate[srcType] as TypeAdapter<R>
				val jsonObject = delegate.toJsonTree(value).asJsonObject

				if (maintainType) {
					jsonElementAdapter.write(out, jsonObject)
					return
				}

				val clone = JsonObject()
				clone.add(typeFieldName, JsonPrimitive(label))
				for ((k, v) in jsonObject.entrySet()) {
					clone.add(k, v)
				}
				jsonElementAdapter.write(out, clone)
			}

			override fun read(reader: JsonReader): R {
				val jsonElement = jsonElementAdapter.read(reader)
				val jsonObject = jsonElement.asJsonObject
				val labelJson = jsonObject.remove(typeFieldName)
					?: throw JsonParseException("Cannot deserialize $baseType: missing $typeFieldName field.")
				val label = labelJson.asString
				val subtype = labelToSubtype[label]
					?: throw JsonParseException("Cannot deserialize $baseType: unknown label $label")
				val delegate = subtypeToDelegate[subtype] as TypeAdapter<R>
				return delegate.fromJsonTree(jsonObject)
			}
		}
	}

	fun registerSubtype(type: Class<out T>, label: String = type.simpleName): RuntimeTypeAdapterFactory<T> {
		require(!labelToSubtype.containsKey(label) && !subtypeToLabel.containsKey(type)) {
			"Types and labels must be unique."
		}
		labelToSubtype[label] = type
		subtypeToLabel[type] = label
		return this
	}

	companion object {
		fun <T> of(baseType: Class<T>, typeFieldName: String, maintainType: Boolean = false): RuntimeTypeAdapterFactory<T> {
			return RuntimeTypeAdapterFactory(baseType, typeFieldName, maintainType)
		}
	}
}
