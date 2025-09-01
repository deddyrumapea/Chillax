package com.romnan.chillax.data.source

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import com.romnan.chillax.R
import com.romnan.chillax.domain.constant.PlayerConstants
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
            soundIdToVolume = moodData.sounds.associate { soundData: SoundData ->
                soundData.id to PlayerConstants.DEFAULT_SOUND_VOLUME
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
    val sounds: List<SoundData>,
) {
    Rainforest(
        readableName = UIText.StringResource(R.string.mood_rainforest),
        imageResId = R.raw.mood_rainforest,
        sounds = listOf(
            SoundData.GentleRain,
            SoundData.Rain,
            SoundData.WindInTrees,
            SoundData.River,
            SoundData.Birds2,
        ),
    ),
    Bedroom(
        readableName = UIText.StringResource(R.string.mood_bedroom),
        imageResId = R.raw.mood_bedroom,
        sounds = listOf(
            SoundData.AirConditioner,
            SoundData.Crickets3,
        ),
    ),
    AirplaneCabin(
        readableName = UIText.StringResource(R.string.mood_airplane_cabin),
        imageResId = R.raw.mood_airplane_cabin,
        sounds = listOf(
            SoundData.AirConditioner,
            SoundData.JetPlane,
        ),
    ),
    Camping(
        readableName = UIText.StringResource(R.string.mood_camping),
        imageResId = R.raw.mood_camping,
        sounds = listOf(
            SoundData.FirePlace1,
            SoundData.FirePlace2,
            SoundData.WindInTrees,
        ),
    ),
    Jungle(
        readableName = UIText.StringResource(R.string.mood_jungle),
        imageResId = R.raw.mood_jungle,
        sounds = listOf(
            SoundData.Birds1,
            SoundData.Birds2,
            SoundData.Birds3,
            SoundData.Brook,
            SoundData.WindInTrees,
        ),
    ),
    Riverside(
        readableName = UIText.StringResource(R.string.mood_riverside),
        imageResId = R.raw.mood_riverside,
        sounds = listOf(
            SoundData.Brook,
            SoundData.Creek,
            SoundData.River,
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
    IndoorRains(
        readableName = UIText.DynamicString("Indoor Rains"),
        description = UIText.DynamicString("Indoor rain sounds"),
        sounds = listOf(
            SoundData.IndoorRain1,
            SoundData.IndoorRain2,
        ),
    ),
    ForestRains(
        readableName = UIText.DynamicString("Forest Rains"),
        description = UIText.DynamicString("Forest rain sounds"),
        sounds = listOf(
            SoundData.ForestRain1,
            SoundData.ForestRain2,
            SoundData.ForestRain3,
            SoundData.ForestRain4,
            SoundData.ForestRain5,
            SoundData.ForestRain6,
            SoundData.ForestRain7,
        ),
    ),
    Rain(
        readableName = UIText.StringResource(R.string.cat_name_rain),
        description = UIText.StringResource(R.string.cat_desc_rain),
        sounds = listOf(
            SoundData.GentleRain,
            SoundData.Rain,
            SoundData.RainOnUmbrella,
            SoundData.RainOnMetalRoof,
            SoundData.Thunder,
        ),
    ),
    Nature(
        readableName = UIText.StringResource(R.string.cat_name_nature),
        description = UIText.StringResource(R.string.cat_desc_nature),
        sounds = listOf(
            SoundData.Brook,
            SoundData.Creek,
            SoundData.River,
            SoundData.River1,
            SoundData.River2,
            SoundData.WindInTrees,
            SoundData.SeaWaves,
            SoundData.Waterfall,
        ),
    ),
    Animals(
        readableName = UIText.StringResource(R.string.cat_name_animals),
        description = UIText.StringResource(R.string.cat_desc_animals),
        sounds = listOf(
            SoundData.Crickets1,
            SoundData.Crickets2,
            SoundData.Crickets3,
            SoundData.Birds1,
            SoundData.Birds2,
            SoundData.Birds3,
            SoundData.Cicadas1,
            SoundData.Cicadas2,
            SoundData.Cicadas3,
            SoundData.Cicadas4,
        ),
    ),
    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(
            SoundData.FirePlace1,
            SoundData.FirePlace2,
            SoundData.AirConditioner,
        ),
    ),
    City(
        readableName = UIText.StringResource(R.string.cat_name_city),
        description = UIText.StringResource(R.string.cat_desc_city),
        sounds = listOf(
            SoundData.Train,
            SoundData.DrivingAtNight,
            SoundData.JetPlane,
        ),
    ),
    Other(
        readableName = UIText.StringResource(R.string.cat_name_other),
        description = UIText.StringResource(R.string.cat_desc_other),
        sounds = listOf(
            SoundData.WindChime1,
            SoundData.WindChime2,
            SoundData.BrownNoise,
            SoundData.Heartbeat,
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
    FirePlace1(
        readableName = UIText.StringResource(R.string.sound_fireplace_1),
        iconResId = R.drawable.ic_sound_fireplace,
        audioResId = R.raw.sound_fireplace_1,
    ),
    FirePlace2(
        readableName = UIText.StringResource(R.string.sound_fireplace_2),
        iconResId = R.drawable.ic_sound_fireplace,
        audioResId = R.raw.sound_fireplace_2,
    ),
    IndoorRain1(
        readableName = UIText.StringResource(R.string.sound_indoor_rain_1),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_indoor_rain_1,
    ),
    IndoorRain2(
        readableName = UIText.StringResource(R.string.sound_indoor_rain_2),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_indoor_rain_2,
    ),
    Cicadas1(
        readableName = UIText.StringResource(R.string.sound_cicadas_1),
        iconResId = R.drawable.ic_sound_cicadas,
        audioResId = R.raw.sound_cicadas_1,
    ),
    Cicadas2(
        readableName = UIText.StringResource(R.string.sound_cicadas_2),
        iconResId = R.drawable.ic_sound_cicadas,
        audioResId = R.raw.sound_cicadas_2,
    ),
    Cicadas3(
        readableName = UIText.StringResource(R.string.sound_cicadas_3),
        iconResId = R.drawable.ic_sound_cicadas,
        audioResId = R.raw.sound_cicadas_3,
    ),
    Cicadas4(
        readableName = UIText.StringResource(R.string.sound_cicadas_4),
        iconResId = R.drawable.ic_sound_cicadas,
        audioResId = R.raw.sound_cicadas_4,
    ),
    Crickets1(
        readableName = UIText.StringResource(R.string.sound_crickets_1),
        iconResId = R.drawable.ic_sound_crickets,
        audioResId = R.raw.sound_crickets_1,
    ),
    Crickets2(
        readableName = UIText.StringResource(R.string.sound_crickets_2),
        iconResId = R.drawable.ic_sound_crickets,
        audioResId = R.raw.sound_crickets_2,
    ),
    Crickets3(
        readableName = UIText.StringResource(R.string.sound_crickets_3),
        iconResId = R.drawable.ic_sound_crickets,
        audioResId = R.raw.sound_crickets_3,
    ),
    AirConditioner(
        readableName = UIText.StringResource(R.string.sound_air_conditioner),
        iconResId = R.drawable.ic_sound_air_conditioner,
        audioResId = R.raw.sound_air_conditioner,
    ),
    River1(
        readableName = UIText.StringResource(R.string.sound_river_1),
        iconResId = R.drawable.ic_sound_river,
        audioResId = R.raw.sound_river_1,
    ),
    River2(
        readableName = UIText.StringResource(R.string.sound_river_2),
        iconResId = R.drawable.ic_sound_river,
        audioResId = R.raw.sound_river_2,
    ),
    ForestRain1(
        readableName = UIText.StringResource(R.string.sound_forest_rain_1),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_forest_rain_1,
    ),
    ForestRain2(
        readableName = UIText.StringResource(R.string.sound_forest_rain_2),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_forest_rain_2,
    ),
    ForestRain3(
        readableName = UIText.StringResource(R.string.sound_forest_rain_3),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_forest_rain_3,
    ),
    ForestRain4(
        readableName = UIText.StringResource(R.string.sound_forest_rain_4),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_forest_rain_4,
    ),
    ForestRain5(
        readableName = UIText.StringResource(R.string.sound_forest_rain_5),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_forest_rain_5,
    ),
    ForestRain6(
        readableName = UIText.StringResource(R.string.sound_forest_rain_6),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_forest_rain_6,
    ),
    ForestRain7(
        readableName = UIText.StringResource(R.string.sound_forest_rain_7),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_forest_rain_7,
    ),
    WindChime1(
        readableName = UIText.StringResource(R.string.sound_wind_chime_1),
        iconResId = R.drawable.ic_sound_wind_chime,
        audioResId = R.raw.sound_wind_chime_1,
    ),
    WindChime2(
        readableName = UIText.StringResource(R.string.sound_wind_chime_2),
        iconResId = R.drawable.ic_sound_wind_chime,
        audioResId = R.raw.sound_wind_chime_2,
    ),
    Rain(
        readableName = UIText.StringResource(R.string.sound_rain),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_rain_soundforyou,
    ),
    GentleRain(
        readableName = UIText.StringResource(R.string.sound_gentle_rain),
        iconResId = R.drawable.ic_sound_gentle_rain,
        audioResId = R.raw.sound_rain_gentle_soundforyou,
    ),
    RainOnUmbrella(
        readableName = UIText.StringResource(R.string.sound_rain_on_umbrella),
        iconResId = R.drawable.ic_sound_rain_on_umbrella,
        audioResId = R.raw.sound_rain_umbrella_soundforyou,
    ),
    Thunder(
        readableName = UIText.StringResource(R.string.sound_thunder),
        iconResId = R.drawable.ic_sound_thunder,
        audioResId = R.raw.sound_thunder_soundforyou,
    ),
    RainOnMetalRoof(
        readableName = UIText.StringResource(R.string.sound_rain_on_metal_roof),
        iconResId = R.drawable.ic_sound_rain_on_metal_roof,
        audioResId = R.raw.sound_rain_metal_on_roof_soundforyou,
    ),
    Creek(
        readableName = UIText.StringResource(R.string.sound_creek),
        iconResId = R.drawable.ic_sound_creek,
        audioResId = R.raw.sound_creek_soundforyou,
    ),
    Brook(
        readableName = UIText.StringResource(R.string.sound_brook),
        iconResId = R.drawable.ic_sound_brook,
        audioResId = R.raw.sound_brook_soundforyou,
    ),
    River(
        readableName = UIText.StringResource(R.string.sound_river),
        iconResId = R.drawable.ic_sound_river,
        audioResId = R.raw.sound_river_soundforyou,
    ),
    Waterfall(
        readableName = UIText.StringResource(R.string.sound_waterfall),
        iconResId = R.drawable.ic_sound_waterfall,
        audioResId = R.raw.sound_waterfall_soundforyou,
    ),
    WindInTrees(
        readableName = UIText.StringResource(R.string.sound_wind_in_trees),
        iconResId = R.drawable.ic_sound_wind_in_trees,
        audioResId = R.raw.sound_wind_in_trees_soundforyou,
    ),
    SeaWaves(
        readableName = UIText.StringResource(R.string.sound_sea_waves),
        iconResId = R.drawable.ic_sound_sea_waves,
        audioResId = R.raw.sound_sea_waves_soundsforyou,
    ),
    Birds1(
        readableName = UIText.StringResource(R.string.sound_birds_1),
        iconResId = R.drawable.ic_sound_birds_1,
        audioResId = R.raw.sound_birds_1_ivolipa,
    ),
    Birds2(
        readableName = UIText.StringResource(R.string.sound_birds_2),
        iconResId = R.drawable.ic_sound_birds_2,
        audioResId = R.raw.sound_birds_2_nektaria909,
    ),
    Birds3(
        readableName = UIText.StringResource(R.string.sound_birds_3),
        iconResId = R.drawable.ic_sound_birds_3,
        audioResId = R.raw.sound_birds_3_swiftoid,
    ),
    Train(
        readableName = UIText.StringResource(R.string.sound_train),
        iconResId = R.drawable.ic_sound_train,
        audioResId = R.raw.sound_train_sspsurvival,
    ),
    JetPlane(
        readableName = UIText.StringResource(R.string.sound_jet_plane),
        iconResId = R.drawable.ic_sound_jet_plane,
        audioResId = R.raw.sound_jet_plane_habbis92,
    ),
    DrivingAtNight(
        readableName = UIText.StringResource(R.string.sound_driving_at_night),
        iconResId = R.drawable.ic_sound_driving_at_night,
        audioResId = R.raw.sound_driving_at_night,
    ),
    BrownNoise(
        readableName = UIText.StringResource(R.string.sound_brown_noise),
        iconResId = R.drawable.ic_sound_brown_noise,
        audioResId = R.raw.sound_brown_noise_digitalspa,
    ),
    Heartbeat(
        readableName = UIText.StringResource(R.string.sound_heartbeat),
        iconResId = R.drawable.ic_sound_hearbeat,
        audioResId = R.raw.sound_heartbeat_placidplace,
    ), ;

    val id: String
        get() = name
}