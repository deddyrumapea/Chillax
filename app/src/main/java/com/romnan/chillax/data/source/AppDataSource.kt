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
            SoundData.ForestWind1,
            SoundData.ForestWind2,
        ),
    ),
    Animals(
        readableName = UIText.StringResource(R.string.cat_name_animals),
        description = UIText.StringResource(R.string.cat_desc_animals),
        sounds = listOf(
            SoundData.Birds1,
            SoundData.Birds2,
            SoundData.Birds3,
            SoundData.Birds4,
        ),
    ),
    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(
            SoundData.Fireplace,
        ),
    ),
    City(
        readableName = UIText.StringResource(R.string.cat_name_city),
        description = UIText.StringResource(R.string.cat_desc_city),
        sounds = listOf(),
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
    Birds4(
        readableName = UIText.StringResource(R.string.sound_birds_4),
        iconResId = R.drawable.ic_sound_birds_4,
        audioResId = R.raw.sound_birds_4,
    ),
    Fireplace(
        readableName = UIText.StringResource(R.string.sound_fireplace),
        iconResId = R.drawable.ic_sound_fireplace,
        audioResId = R.raw.sound_fireplace,
    ),
    ForestWind1(
        readableName = UIText.StringResource(R.string.sound_forest_wind_1),
        iconResId = R.drawable.ic_sound_forest_wind,
        audioResId = R.raw.sound_forest_wind_1,
    ),
    ForestWind2(
        readableName = UIText.StringResource(R.string.sound_forest_wind_2),
        iconResId = R.drawable.ic_sound_forest_wind,
        audioResId = R.raw.sound_forest_wind_2,
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
    ), ;

    val id: String
        get() = name
}