package com.romnan.chillax.data.source

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.romnan.chillax.R
import com.romnan.chillax.domain.model.Category
import com.romnan.chillax.domain.model.Mood
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

    val presetMoods: List<Mood> = MoodData.entries.map { moodData: MoodData ->
        val imageUri = Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(appContext.packageName)
            .appendPath(moodData.imageResId.toString())
            .build()
            .toString()

        Mood(
            id = moodData.id,
            readableName = moodData.readableName,
            imageUri = imageUri,
            soundIdToVolume = moodData.soundToVolume
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

    val moodImageUris: Set<String> = listOf(
        R.raw.mood_airplane_cabin,
        R.raw.mood_bedroom,
        R.raw.mood_camping,
        R.raw.mood_rainforest,
        R.raw.mood_jungle,
        R.raw.mood_riverside,
    ).map { resId: Int ->
        Uri.Builder()
            .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(appContext.packageName)
            .appendPath(resId.toString())
            .build()
            .toString()
    }.toSet()
}

private enum class MoodData(
    val readableName: UIText,
    val imageResId: Int,
    val soundToVolume: List<Pair<SoundData, Float>>,
) {
    Rainforest(
        readableName = UIText.StringResource(R.string.mood_rainforest),
        imageResId = R.raw.mood_rainforest,
        soundToVolume = listOf(),
    ),
    Bedroom(
        readableName = UIText.StringResource(R.string.mood_bedroom),
        imageResId = R.raw.mood_bedroom,
        soundToVolume = listOf(),
    ),
    AirplaneCabin(
        readableName = UIText.StringResource(R.string.mood_airplane_cabin),
        imageResId = R.raw.mood_airplane_cabin,
        soundToVolume = listOf(),
    ),
    Camping(
        readableName = UIText.StringResource(R.string.mood_camping),
        imageResId = R.raw.mood_camping,
        soundToVolume = listOf(),
    ),
    Jungle(
        readableName = UIText.StringResource(R.string.mood_jungle),
        imageResId = R.raw.mood_jungle,
        soundToVolume = listOf(),
    ),
    Riverside(
        readableName = UIText.StringResource(R.string.mood_riverside),
        imageResId = R.raw.mood_riverside,
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
            SoundData.HeavyRain1,
            SoundData.HeavyRain2,
            SoundData.HeavyRain3,
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
    Train(
        readableName = UIText.StringResource(R.string.sound_train),
        iconResId = R.drawable.ic_sound_train,
        audioResId = R.raw.sound_train,
    ), ;

    val id: String
        get() = name
}