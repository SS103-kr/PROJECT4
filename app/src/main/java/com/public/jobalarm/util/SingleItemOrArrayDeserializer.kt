package com.jobalarm.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonTransformingSerializer

class SingleItemOrArrayDeserializer<T : Any>(
    elementSerializer: KSerializer<T>
) : JsonTransformingSerializer<List<T>>(ListSerializer(elementSerializer)) {
    override fun transformDeserialize(element: JsonElement): JsonElement {
        return when (element) {
            is JsonArray -> element
            is JsonObject -> JsonArray(listOf(element))
            else -> JsonArray(listOf(element))
        }
    }
}
