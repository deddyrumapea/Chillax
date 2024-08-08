package com.romnan.chillax.data.serializer

import androidx.datastore.core.Serializer
import com.romnan.chillax.data.model.AppSettingsSerializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppSettingsSerializer : Serializer<AppSettingsSerializable> {
    override val defaultValue: AppSettingsSerializable
        get() = AppSettingsSerializable()

    override suspend fun readFrom(input: InputStream): AppSettingsSerializable {
        return try {
            Json.decodeFromString(
                deserializer = AppSettingsSerializable.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: AppSettingsSerializable, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppSettingsSerializable.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}