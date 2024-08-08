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
        R.raw.mood_airplane_cabin_upklyak,
        R.raw.mood_bedroom_vectorpouch,
        R.raw.mood_camping_upklyak,
        R.raw.mood_forest_vectorpouch,
        R.raw.mood_jungle_freepik,
        R.raw.mood_riverside_jcomp,
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
        imageResId = R.raw.mood_forest_vectorpouch,
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
        imageResId = R.raw.mood_bedroom_vectorpouch,
        sounds = listOf(
            SoundData.WindowAC,
            SoundData.Crickets,
        ),
    ),
    AirplaneCabin(
        readableName = UIText.StringResource(R.string.mood_airplane_cabin),
        imageResId = R.raw.mood_airplane_cabin_upklyak,
        sounds = listOf(
            SoundData.WindowAC,
            SoundData.JetPlane,
        ),
    ),
    Camping(
        readableName = UIText.StringResource(R.string.mood_camping),
        imageResId = R.raw.mood_camping_upklyak,
        sounds = listOf(
            SoundData.Cicadas,
            SoundData.Fireplace,
            SoundData.WindInTrees,
        ),
    ),
    Jungle(
        readableName = UIText.StringResource(R.string.mood_jungle),
        imageResId = R.raw.mood_jungle_freepik,
        sounds = listOf(
            SoundData.Cicadas,
            SoundData.Birds1,
            SoundData.Birds2,
            SoundData.Birds3,
            SoundData.Brook,
            SoundData.WindInTrees,
        ),
    ),
    Riverside(
        readableName = UIText.StringResource(R.string.mood_riverside),
        imageResId = R.raw.mood_riverside_jcomp,
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
            SoundData.WindInTrees,
            SoundData.Waterfall,
            SoundData.SeaWaves,
        ),
    ),
    Animals(
        readableName = UIText.StringResource(R.string.cat_name_animals),
        description = UIText.StringResource(R.string.cat_desc_animals),
        sounds = listOf(
            SoundData.Crickets,
            SoundData.Cicadas,
            SoundData.CatPurring,
            SoundData.Seagulls,
            SoundData.Birds1,
            SoundData.Birds2,
            SoundData.Birds3,
            SoundData.Frogs1,
            SoundData.Frogs2,
        ),
    ),
    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(
            SoundData.WindowAC,
            SoundData.Keyboard,
            SoundData.Fireplace,
            SoundData.DeepFrying,
        ),
    ),
    City(
        readableName = UIText.StringResource(R.string.cat_name_city),
        description = UIText.StringResource(R.string.cat_desc_city),
        sounds = listOf(
            SoundData.KidsPlayground,
            SoundData.Crowd,
            SoundData.Train,
            SoundData.JetPlane,
            SoundData.TurbopropPlane,
            SoundData.WindshieldWipers,
            SoundData.DrivingAtNight,
        ),
    ),
    Other(
        readableName = UIText.StringResource(R.string.cat_name_other),
        description = UIText.StringResource(R.string.cat_desc_other),
        sounds = listOf(
            SoundData.BrownNoise,
            SoundData.RadioStatic,
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
    Seagulls(
        readableName = UIText.StringResource(R.string.sound_seagulls),
        iconResId = R.drawable.ic_sound_seagulls,
        audioResId = R.raw.sound_seagulls_olesouwester,
    ),
    Crickets(
        readableName = UIText.StringResource(R.string.sound_crickets),
        iconResId = R.drawable.ic_sound_crickets,
        audioResId = R.raw.sound_crickets_cclaretc,
    ),
    Cicadas(
        readableName = UIText.StringResource(R.string.sound_cicadas),
        iconResId = R.drawable.ic_sound_cicadas,
        audioResId = R.raw.sound_cicadas_sarvegu,
    ),
    Frogs1(
        readableName = UIText.StringResource(R.string.sound_frogs_1),
        iconResId = R.drawable.ic_sound_frogs_1,
        audioResId = R.raw.sound_frogs1_jayalvarez66,
    ),
    Frogs2(
        readableName = UIText.StringResource(R.string.sound_frogs_2),
        iconResId = R.drawable.ic_sound_frogs_2,
        audioResId = R.raw.sound_frogs2_zachrau,
    ),
    CatPurring(
        readableName = UIText.StringResource(R.string.sound_cat_purring),
        iconResId = R.drawable.ic_sound_cat_purring,
        audioResId = R.raw.sound_cat_purring_worldsday,
    ),
    WindowAC(
        readableName = UIText.StringResource(R.string.sound_window_ac),
        iconResId = R.drawable.ic_sound_window_ac,
        audioResId = R.raw.sound_window_ac_benhabrams,
    ),
    Fireplace(
        readableName = UIText.StringResource(R.string.sound_fireplace),
        iconResId = R.drawable.ic_sound_fireplace,
        audioResId = R.raw.sound_fireplace_juliush,
    ),
    DeepFrying(
        readableName = UIText.StringResource(R.string.sound_deep_frying),
        iconResId = R.drawable.ic_sound_deep_frying,
        audioResId = R.raw.sound_deep_frying_juliush,
    ),
    Keyboard(
        readableName = UIText.StringResource(R.string.sound_keyboard),
        iconResId = R.drawable.ic_sound_keyboard,
        audioResId = R.raw.sound_keyboard_kevinchocs,
    ),
    KidsPlayground(
        readableName = UIText.StringResource(R.string.sound_kids_playground),
        iconResId = R.drawable.ic_sound_kids_playground,
        audioResId = R.raw.sound_kids_playground_brunoauzet,
    ),
    Crowd(
        readableName = UIText.StringResource(R.string.sound_crowd),
        iconResId = R.drawable.ic_sound_crowd,
        audioResId = R.raw.sound_crowd_karinalarasart,
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
    TurbopropPlane(
        readableName = UIText.StringResource(R.string.sound_turboprop_plane),
        iconResId = R.drawable.ic_sound_turboprop_plane,
        audioResId = R.raw.sound_turboprop_plane_daveshu88
    ),
    DrivingAtNight(
        readableName = UIText.StringResource(R.string.sound_driving_at_night),
        iconResId = R.drawable.ic_sound_driving_at_night,
        audioResId = R.raw.sound_driving_at_night_augustsandberg,
    ),
    WindshieldWipers(
        readableName = UIText.StringResource(R.string.sound_windshield_wipers),
        iconResId = R.drawable.ic_sound_windshield_wipers,
        audioResId = R.raw.sound_windshield_wipers_beeproductive,
    ),
    BrownNoise(
        readableName = UIText.StringResource(R.string.sound_brown_noise),
        iconResId = R.drawable.ic_sound_brown_noise,
        audioResId = R.raw.sound_brown_noise_digitalspa,
    ),
    RadioStatic(
        readableName = UIText.StringResource(R.string.sound_radio_static),
        iconResId = R.drawable.ic_sound_radio_static,
        audioResId = R.raw.sound_radio_static_theartguild,
    ),
    Heartbeat(
        readableName = UIText.StringResource(R.string.sound_heartbeat),
        iconResId = R.drawable.ic_sound_hearbeat,
        audioResId = R.raw.sound_heartbeat_placidplace,
    ), ;

    val id: String
        get() = name
}