package com.romnan.chillax.data.source

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Mix
import com.romnan.chillax.domain.model.Sound
import com.romnan.chillax.domain.model.UIText

class AppDataSource(
    private val appContext: Context,
) {

    val sounds: List<Sound> = SoundData.entries.map { soundData: SoundData ->
        Sound(
            id = soundData.id,
            readableName = soundData.readableName,
            iconResId = soundData.iconResId,
            audioResId = soundData.audioResId,
        )
    }

    val presetMixes: List<Mix> = MixData.entries.map { mixData: MixData ->
        val imageUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(appContext.packageName)
            .appendPath(mixData.imageResId.toString())
            .build()
            .toString()

        Mix(
            id = mixData.id,
            readableName = mixData.readableName,
            imageUri = imageUri,
            soundIdToVolume = mixData.soundToVolume
                .associate { (soundData: SoundData, volume: Float) ->
                    soundData.id to volume
                },
        )
    }

    val categories: List<Category> = CategoryData.entries.map { categoryData: CategoryData ->
        Category(
            id = categoryData.id,
            readableName = categoryData.readableName,
            description = categoryData.description,
            soundIds = categoryData.sounds.map { soundData: SoundData -> soundData.id },
        )
    }

    val mixImageUris: Set<String> = listOf(
        R.raw.mix_airplane_cabin,
        R.raw.mix_bedroom,
        R.raw.mix_camping,
        R.raw.mix_rainforest,
        R.raw.mix_jungle,
        R.raw.mix_riverside,
    ).map { resId: Int ->
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(appContext.packageName)
            .appendPath(resId.toString())
            .build()
            .toString()
    }.toSet()
}

private enum class MixData(
    val readableName: UIText,
    val imageResId: Int,
    val soundToVolume: List<Pair<SoundData, Float>>,
) {
    Rainforest(
        readableName = UIText.StringResource(R.string.mix_rainforest),
        imageResId = R.raw.mix_rainforest,
        soundToVolume = listOf(),
    ),
    Bedroom(
        readableName = UIText.StringResource(R.string.mix_bedroom),
        imageResId = R.raw.mix_bedroom,
        soundToVolume = listOf(),
    ),
    AirplaneCabin(
        readableName = UIText.StringResource(R.string.mix_airplane_cabin),
        imageResId = R.raw.mix_airplane_cabin,
        soundToVolume = listOf(),
    ),
    Camping(
        readableName = UIText.StringResource(R.string.mix_camping),
        imageResId = R.raw.mix_camping,
        soundToVolume = listOf(),
    ),
    Jungle(
        readableName = UIText.StringResource(R.string.mix_jungle),
        imageResId = R.raw.mix_jungle,
        soundToVolume = listOf(),
    ),
    Riverside(
        readableName = UIText.StringResource(R.string.mix_riverside),
        imageResId = R.raw.mix_riverside,
        soundToVolume = listOf(),
    ), ;

    val id: String
        get() = name
}

private enum class CategoryData(
    val readableName: UIText,
    val description: UIText,
    val sounds: List<SoundData>,
) {
    Rain(
        readableName = UIText.StringResource(R.string.cat_name_rain),
        description = UIText.StringResource(R.string.cat_desc_rain),
        sounds = listOf(
            SoundData.GentleRain,
            SoundData.HeavyRain1,
            SoundData.HeavyRain2,
            SoundData.HeavyRain3,
            SoundData.RainOnMetalRoof,
            SoundData.RainOnTent,
            SoundData.Thunderstorm,
        ),
    ),
    Nature(
        readableName = UIText.StringResource(R.string.cat_name_nature),
        description = UIText.StringResource(R.string.cat_desc_nature),
        sounds = listOf(
            SoundData.ForestWind,
            SoundData.Brook,
            SoundData.Creek,
            SoundData.River,
            SoundData.SeaWaves1,
            SoundData.SeaWaves2,
            SoundData.SeaWaves3,
        ),
    ),
    Animals(
        readableName = UIText.StringResource(R.string.cat_name_animals),
        description = UIText.StringResource(R.string.cat_desc_animals),
        sounds = listOf(
            SoundData.Birds1,
            SoundData.Birds2,
            SoundData.Birds3,
            SoundData.Frogs,
        ),
    ),
    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(
            SoundData.Fireplace,
            SoundData.AirConditioner,
        ),
    ),
    City(
        readableName = UIText.StringResource(R.string.cat_name_city),
        description = UIText.StringResource(R.string.cat_desc_city),
        sounds = listOf(
            SoundData.Train,
            SoundData.JetPlane,
        ),
    ),
    Other(
        readableName = UIText.StringResource(R.string.cat_name_other),
        description = UIText.StringResource(R.string.cat_desc_other),
        sounds = listOf(),
    ), ;

    val id: String
        get() = name
}

private enum class SoundData(
    val readableName: UIText,
    val iconResId: Int,
    val audioResId: Int,
) {
    AirConditioner(
        readableName = UIText.StringResource(R.string.sound_air_conditioner),
        iconResId = R.drawable.ic_sound_air_conditioner,
        audioResId = R.raw.sound_air_conditioner,
    ),
    Birds1(
        readableName = UIText.StringResource(R.string.sound_birds_1),
        iconResId = R.drawable.ic_sound_birds_1,
        audioResId = R.raw.sound_birds_1,
    ),
    Birds2(
        readableName = UIText.StringResource(R.string.sound_birds_2),
        iconResId = R.drawable.ic_sound_birds_2,
        audioResId = R.raw.sound_birds_2,
    ),
    Birds3(
        readableName = UIText.StringResource(R.string.sound_birds_3),
        iconResId = R.drawable.ic_sound_birds_3,
        audioResId = R.raw.sound_birds_3,
    ),
    Brook(
        readableName = UIText.StringResource(R.string.sound_brook),
        iconResId = R.drawable.ic_sound_brook,
        audioResId = R.raw.sound_brook,
    ),
    Creek(
        readableName = UIText.StringResource(R.string.sound_creek),
        iconResId = R.drawable.ic_sound_creek,
        audioResId = R.raw.sound_creek,
    ),
    Fireplace(
        readableName = UIText.StringResource(R.string.sound_fireplace),
        iconResId = R.drawable.ic_sound_fireplace,
        audioResId = R.raw.sound_fireplace,
    ),
    ForestWind(
        readableName = UIText.StringResource(R.string.sound_forest_wind),
        iconResId = R.drawable.ic_sound_forest_wind,
        audioResId = R.raw.sound_forest_wind,
    ),
    Frogs(
        readableName = UIText.StringResource(R.string.sound_frogs),
        iconResId = R.drawable.ic_sound_frogs,
        audioResId = R.raw.sound_frogs,
    ),
    GentleRain(
        readableName = UIText.StringResource(R.string.sound_gentle_rain),
        iconResId = R.drawable.ic_sound_gentle_rain,
        audioResId = R.raw.sound_gentle_rain,
    ),
    HeavyRain1(
        readableName = UIText.StringResource(R.string.sound_heavy_rain_1),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_heavy_rain_1,
    ),
    HeavyRain2(
        readableName = UIText.StringResource(R.string.sound_heavy_rain_2),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_heavy_rain_2,
    ),
    HeavyRain3(
        readableName = UIText.StringResource(R.string.sound_heavy_rain_3),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_heavy_rain_3,
    ),
    JetPlane(
        readableName = UIText.StringResource(R.string.sound_jet_plane),
        iconResId = R.drawable.ic_sound_jet_plane,
        audioResId = R.raw.sound_jet_plane,
    ),
    RainOnMetalRoof(
        readableName = UIText.StringResource(R.string.sound_rain_on_metal_roof),
        iconResId = R.drawable.ic_sound_rain_on_metal_roof,
        audioResId = R.raw.sound_rain_on_metal_roof,
    ),
    RainOnTent(
        readableName = UIText.StringResource(R.string.sound_rain_on_tent),
        iconResId = R.drawable.ic_sound_rain_on_tent,
        audioResId = R.raw.sound_rain_on_tent,
    ),
    SeaWaves1(
        readableName = UIText.StringResource(R.string.sound_sea_waves_1),
        iconResId = R.drawable.ic_sound_sea_waves,
        audioResId = R.raw.sound_sea_waves_1,
    ),
    SeaWaves2(
        readableName = UIText.StringResource(R.string.sound_sea_waves_2),
        iconResId = R.drawable.ic_sound_sea_waves,
        audioResId = R.raw.sound_sea_waves_2,
    ),
    SeaWaves3(
        readableName = UIText.StringResource(R.string.sound_sea_waves_3),
        iconResId = R.drawable.ic_sound_sea_waves,
        audioResId = R.raw.sound_sea_waves_3,
    ),
    River(
        readableName = UIText.StringResource(R.string.sound_river),
        iconResId = R.drawable.ic_sound_river,
        audioResId = R.raw.sound_river,
    ),
    Thunderstorm(
        readableName = UIText.StringResource(R.string.sound_thunderstorm),
        iconResId = R.drawable.ic_sound_thunderstorm,
        audioResId = R.raw.sound_thunderstorm,
    ),
    Train(
        readableName = UIText.StringResource(R.string.sound_train),
        iconResId = R.drawable.ic_sound_train,
        audioResId = R.raw.sound_train,
    ), ;

    val id: String
        get() = name
}