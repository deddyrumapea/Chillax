package com.romnan.chillax.core.data.util

import androidx.datastore.core.Serializer
import com.romnan.chillax.core.data.model.PlayerStateSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object PlayerStateSerializer : Serializer<PlayerStateSerializable> {
    override val defaultValue: PlayerStateSerializable
        get() = PlayerStateSerializable.defaultValue

    override suspend fun readFrom(input: InputStream): PlayerStateSerializable {
        return try {
            Json.decodeFromString(
                deserializer = PlayerStateSerializable.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: PlayerStateSerializable, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = PlayerStateSerializable.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}