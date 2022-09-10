package com.romnan.chillax.data.source

import com.romnan.chillax.R
import com.romnan.chillax.data.model.CategoryData
import com.romnan.chillax.data.model.MoodData
import com.romnan.chillax.data.model.SoundData
import com.romnan.chillax.data.util.DataConstants
import com.romnan.chillax.domain.model.UIText

object AppDataSource {
    val moods: List<MoodData> = MoodDataSource.values().toList()
    val categories: List<CategoryData> = CategoryDataSource.values().toList()

    fun getSoundFromName(soundName: String): SoundData? {
        return try {
            SoundDataSource.valueOf(soundName)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun getMoodFromName(moodName: String): MoodData? {
        return try {
            MoodDataSource.valueOf(moodName)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}

private enum class MoodDataSource(
    override val readableName: UIText,
    override val imageResId: Int,
    override val sounds: List<SoundData>
) : MoodData {

}

private enum class CategoryDataSource(
    override val readableName: UIText,
    override val description: UIText,
    override val sounds: List<SoundData>
) : CategoryData {
    Rain(
        readableName = UIText.StringResource(R.string.cat_name_rain),
        description = UIText.StringResource(R.string.cat_desc_rain),
        sounds = listOf(
            SoundDataSource.GentleRain,
            SoundDataSource.Rain,
            SoundDataSource.RainOnUmbrella,
            SoundDataSource.RainOnMetalRoof,
            SoundDataSource.Thunder,
        ),
    ),
    Nature(
        readableName = UIText.StringResource(R.string.cat_name_nature),
        description = UIText.StringResource(R.string.cat_desc_nature),
        sounds = listOf(
            SoundDataSource.Brook,
            SoundDataSource.Creek,
            SoundDataSource.River,
            SoundDataSource.WindInTrees,
            SoundDataSource.Waterfall,
        ),
    ),
    Animals(
        readableName = UIText.StringResource(R.string.cat_name_animals),
        description = UIText.StringResource(R.string.cat_desc_animals),
        sounds = listOf(
            SoundDataSource.Crickets,
            SoundDataSource.Cicadas,
            SoundDataSource.CatPurring,
            SoundDataSource.Seagulls,
            SoundDataSource.Birds1,
            SoundDataSource.Birds2,
            SoundDataSource.Birds3,
            SoundDataSource.Frogs1,
            SoundDataSource.Frogs2,
        ),
    ),
    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(
            SoundDataSource.WindowAC,
            SoundDataSource.Keyboard,
            SoundDataSource.Fireplace,
            SoundDataSource.DeepFrying,
        ),
    ),
    City(
        readableName = UIText.StringResource(R.string.cat_name_city),
        description = UIText.StringResource(R.string.cat_desc_city),
        sounds = listOf(
            SoundDataSource.KidsPlayground,
            SoundDataSource.Crowd,
            SoundDataSource.JetPlane,
            SoundDataSource.TurbopropPlane,
            SoundDataSource.WindshieldWipers,
            SoundDataSource.DrivingAtNight,
        ),
    ),
    Other(
        readableName = UIText.StringResource(R.string.cat_name_other),
        description = UIText.StringResource(R.string.cat_desc_other),
        sounds = listOf(
            SoundDataSource.RadioStatic,
            SoundDataSource.Heartbeat,
            SoundDataSource.Electricity,
        ),
    ),
}

private enum class SoundDataSource(
    override val readableName: UIText,
    override val iconResId: Int,
    override val audioResId: Int,
) : SoundData {
    Rain(
        readableName = UIText.StringResource(R.string.sound_rain),
        iconResId = R.drawable.ic_sound_rain,
        audioResId = R.raw.sound_rain_sfy,
    ),
    GentleRain(
        readableName = UIText.StringResource(R.string.sound_gentle_rain),
        iconResId = R.drawable.ic_sound_gentle_rain,
        audioResId = R.raw.sound_rain_gentle_sfy,
    ),
    RainOnUmbrella(
        readableName = UIText.StringResource(R.string.sound_rain_on_umbrella),
        iconResId = R.drawable.ic_sound_rain_on_umbrella,
        audioResId = R.raw.sound_rain_umbrella_sfy,
    ),
    Thunder(
        readableName = UIText.StringResource(R.string.sound_thunder),
        iconResId = R.drawable.ic_sound_thunder,
        audioResId = R.raw.sound_thunder_sfy,
    ),
    RainOnMetalRoof(
        readableName = UIText.StringResource(R.string.sound_rain_on_metal_roof),
        iconResId = R.drawable.ic_sound_rain_on_metal_roof,
        audioResId = R.raw.sound_rain_metal_roof_sfy,
    ),
    Creek(
        readableName = UIText.StringResource(R.string.sound_creek),
        iconResId = R.drawable.ic_sound_creek,
        audioResId = R.raw.sound_creek_sfy,
    ),
    Brook(
        readableName = UIText.StringResource(R.string.sound_brook),
        iconResId = R.drawable.ic_sound_brook,
        audioResId = R.raw.sound_brook_sfy,
    ),
    River(
        readableName = UIText.StringResource(R.string.sound_river),
        iconResId = R.drawable.ic_sound_river,
        audioResId = R.raw.sound_river_sfy,
    ),
    Waterfall(
        readableName = UIText.StringResource(R.string.sound_waterfall),
        iconResId = R.drawable.ic_sound_waterfall,
        audioResId = R.raw.sound_waterfall_sfy,
    ),
    WindInTrees(
        readableName = UIText.StringResource(R.string.sound_wind_in_trees),
        iconResId = R.drawable.ic_sound_wind_in_trees,
        audioResId = R.raw.sound_wind_in_trees_sfy,
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
        audioResId = R.raw.sound_deep_frying_julish,
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
    RadioStatic(
        readableName = UIText.StringResource(R.string.sound_radio_static),
        iconResId = R.drawable.ic_sound_radio_static,
        audioResId = R.raw.sound_radio_static_theartguild,
    ),
    Electricity(
        readableName = UIText.StringResource(R.string.sound_electricity),
        iconResId = R.drawable.ic_sound_electricity,
        audioResId = R.raw.sound_electricity_flashtrauma,
    ),
    Heartbeat(
        readableName = UIText.StringResource(R.string.sound_heartbeat),
        iconResId = R.drawable.ic_sound_hearbeat,
        audioResId = R.raw.sound_heartbeat_placidplace,
    ),
    ;

    override val volume: Float
        get() = DataConstants.DEFAULT_SOUND_VOLUME
}