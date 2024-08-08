package com.romnan.chillax.data.serializer

import androidx.datastore.core.Serializer
import com.romnan.chillax.data.model.PlayerSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PlayerSerializer : Serializer<PlayerSerializable> {
    override val defaultValue: PlayerSerializable
        get() = PlayerSerializable()

    override suspend fun readFrom(input: InputStream): PlayerSerializable {
        return try {
            Json.decodeFromString(
                deserializer = PlayerSerializable.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: PlayerSerializable, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = PlayerSerializable.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}