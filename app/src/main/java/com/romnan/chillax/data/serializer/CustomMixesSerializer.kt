package com.romnan.chillax.data.serializer

import androidx.datastore.core.Serializer
import com.romnan.chillax.data.model.CustomMixList
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object CustomMixesSerializer : Serializer<CustomMixList> {

    override val defaultValue: CustomMixList
        get() = CustomMixList()

    override suspend fun readFrom(
        input: InputStream,
    ): CustomMixList {
        return try {
            Json.decodeFromString(
                deserializer = CustomMixList.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(
        t: CustomMixList,
        output: OutputStream,
    ) {
        output.write(
            Json.encodeToString(
                serializer = CustomMixList.serializer(),
                value = t,
            ).encodeToByteArray(),
        )
    }
}