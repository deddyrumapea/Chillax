package com.romnan.chillax.data.serializer

import androidx.datastore.core.Serializer
import com.romnan.chillax.data.model.CustomMoodList
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object CustomMoodsSerializer : Serializer<CustomMoodList> {

    override val defaultValue: CustomMoodList
        get() = CustomMoodList()

    override suspend fun readFrom(
        input: InputStream,
    ): CustomMoodList {
        return try {
            Json.decodeFromString(
                deserializer = CustomMoodList.serializer(),
                string = input.readBytes().decodeToString(),
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(
        t: CustomMoodList,
        output: OutputStream,
    ) {
        output.write(
            Json.encodeToString(
                serializer = CustomMoodList.serializer(),
                value = t,
            ).encodeToByteArray(),
        )
    }
}