package com.romnan.chillax.data.serializer

import androidx.datastore.core.Serializer
import com.romnan.chillax.data.model.SleepTimerSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object SleepTimerSerializer : Serializer<SleepTimerSerializable> {
    override val defaultValue: SleepTimerSerializable
        get() = SleepTimerSerializable()

    override suspend fun readFrom(input: InputStream): SleepTimerSerializable {
        return try {
            Json.decodeFromString(
                deserializer = SleepTimerSerializable.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: SleepTimerSerializable, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = SleepTimerSerializable.serializer(),
                value = t,
            ).encodeToByteArray()
        )
    }
}