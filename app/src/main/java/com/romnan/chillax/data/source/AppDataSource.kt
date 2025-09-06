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
        R.raw.mix_airplane_journey,
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
        soundToVolume = listOf(
            SoundData.GentleRain to 0.8f,
            SoundData.Rain to 0.5f,
            SoundData.ForestWind to 0.6f,
            SoundData.Crickets to 0.1f,
            SoundData.Cicadas to 0.1f,
        ),
    ),
    Bedroom(
        readableName = UIText.StringResource(R.string.mix_bedroom),
        imageResId = R.raw.mix_bedroom,
        soundToVolume = listOf(
            SoundData.AirConditioner to 0.7f,
            SoundData.Rain to 0.6f,
            SoundData.RainOnRoof to 0.3f,
            SoundData.Crickets to 0.05f,
        ),
    ),
    AirplaneJourney(
        readableName = UIText.StringResource(R.string.mix_airplane_journey),
        imageResId = R.raw.mix_airplane_journey,
        soundToVolume = listOf(
            SoundData.PlaneCabin to 0.8f,
            SoundData.AirConditioner to 0.2f,
        ),
    ),
    Camping(
        readableName = UIText.StringResource(R.string.mix_camping),
        imageResId = R.raw.mix_camping,
        soundToVolume = listOf(
            SoundData.Fireplace to 0.8f,
            SoundData.RainOnTent to 0.2f,
            SoundData.ForestWind to 0.2f,
            SoundData.Cicadas to 0.1f,
        ),
    ),
    Jungle(
        readableName = UIText.StringResource(R.string.mix_jungle),
        imageResId = R.raw.mix_jungle,
        soundToVolume = listOf(
            SoundData.Brook to 0.6f,
            SoundData.ForestWind to 0.4f,
            SoundData.Birds1 to 0.6f,
            SoundData.Birds2 to 0.6f,
            SoundData.Cicadas to 0.3f,
            SoundData.Frogs to 0.1f,
            SoundData.Crickets to 0.1f,
        ),
    ),
    Riverside(
        readableName = UIText.StringResource(R.string.mix_riverside),
        imageResId = R.raw.mix_riverside,
        soundToVolume = listOf(
            SoundData.River to 0.7f,
            SoundData.Creek to 0.6f,
            SoundData.Brook to 0.5f,
        ),
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
            SoundData.Rain,
            SoundData.RainOnRoof,
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
            SoundData.Crickets,
            SoundData.Cicadas,
        ),
    ),
    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(
            SoundData.Fireplace,
            SoundData.AirConditioner,
            SoundData.TrainCabin,
            SoundData.PlaneCabin,
        ),
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
    Cicadas(
        readableName = UIText.StringResource(R.string.sound_cicadas),
        iconResId = R.drawable.ic_sound_cicadas,
        audioResId = R.raw.sound_cicadas,
    ),
    Creek(
        readableName = UIText.StringResource(R.string.sound_creek),
        iconResId = R.drawable.ic_sound_creek,
        audioResId = R.raw.sound_creek,
    ),
    Crickets(
        readableName = UIText.StringResource(R.string.sound_crickets),
        iconResId = R.drawable.ic_sound_crickets,
        audioResId = R.raw.sound_crickets,
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
    PlaneCabin(
        readableName = UIText.StringResource(R.string.sound_plane_cabin),
        iconResId = R.drawable.ic_sound_plane_cabin,
        audioResId = R.raw.sound_plane_cabin,
    ),
    Rain(
        readableName = UIText.StringResource(R.string.sound_rain),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_rain,
    ),
    RainOnRoof(
        readableName = UIText.StringResource(R.string.sound_rain_on_roof),
        iconResId = R.drawable.ic_sound_rain_on_roof,
        audioResId = R.raw.sound_rain_on_roof,
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
    TrainCabin(
        readableName = UIText.StringResource(R.string.sound_train_cabin),
        iconResId = R.drawable.ic_sound_train_cabin,
        audioResId = R.raw.sound_train_cabin,
    ), ;

    val id: String
        get() = name
}