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
    SpringRain(
        readableName = UIText.StringResource(R.string.mood_name_spring_rain),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Rain,
            SoundDataSource.Thunder,
        ),
    ),
    SummerRain(
        readableName = UIText.StringResource(R.string.mood_name_summer_rain),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Frogs1,
            SoundDataSource.RainOnLeaves,
        ),
    ),
    RainInForest(
        readableName = UIText.StringResource(R.string.mood_name_rain_in_forest),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.RainOnLeaves,
            SoundDataSource.WindOnLeaves,
        ),
    ),
    RainOnWindow(
        readableName = UIText.StringResource(R.string.mood_name_rain_on_window),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.RainOnWindow,
            SoundDataSource.Thunder,
            SoundDataSource.Fireplace,
        ),
    ),
    Forest(
        readableName = UIText.StringResource(R.string.mood_name_forest),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Birds1,
            SoundDataSource.WindOnLeaves,
            SoundDataSource.Creek,
        ),
    ),
    SilentNight(
        readableName = UIText.StringResource(R.string.mood_name_silent_night),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Crickets,
        ),
    ),
    NightCamping(
        readableName = UIText.StringResource(R.string.mood_name_night_camping),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Crickets,
            SoundDataSource.Owls,
            SoundDataSource.Campfire,
        ),
    ),
    Ocean(
        readableName = UIText.StringResource(R.string.mood_name_ocean),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Seagulls,
            SoundDataSource.Waves,
        ),
    ),
    CountrySide(
        readableName = UIText.StringResource(R.string.mood_name_country_side),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Dogs,
            SoundDataSource.Creek,
        ),
    ),
    TrainTravel(
        readableName = UIText.StringResource(R.string.mood_name_train_travel),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.RainOnRoof,
            SoundDataSource.TrainTravel,
        ),
    ),
    Driving(
        readableName = UIText.StringResource(R.string.mood_name_driving),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.RainOnWindow,
            SoundDataSource.DrivingAtNight,
        ),
    ),
    Flight(
        readableName = UIText.StringResource(R.string.mood_name_flight),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Crowd,
            SoundDataSource.JetPlane,
        ),
    )
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
            SoundDataSource.Birds1,
            SoundDataSource.Birds2,
            SoundDataSource.Birds3,
            SoundDataSource.Crickets,
            SoundDataSource.Cicadas,
            SoundDataSource.CatPurring,
            SoundDataSource.Seagulls,
            SoundDataSource.Frogs1,
            SoundDataSource.Frogs2,
        ),
    ),
    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(
            SoundDataSource.WindowAC,
            SoundDataSource.Fireplace,
            SoundDataSource.Keyboard,
            SoundDataSource.DeepFrying,
        ),
    ),
    City(
        readableName = UIText.StringResource(R.string.cat_name_city),
        description = UIText.StringResource(R.string.cat_desc_city),
        sounds = listOf(
            SoundDataSource.Crowd,
            SoundDataSource.KidsPlayground,
            SoundDataSource.TurbopropPlane,
            SoundDataSource.DrivingAtNight,
            SoundDataSource.WindshieldWipers,
            SoundDataSource.JetPlane,
        ),
    ),
    Other(
        readableName = UIText.StringResource(R.string.cat_name_other),
        description = UIText.StringResource(R.string.cat_desc_other),
        sounds = listOf(
            SoundDataSource.StaticNoise,
            SoundDataSource.RadioStatic1,
            SoundDataSource.RadioStatic2,
            SoundDataSource.Electricity,
            SoundDataSource.Heartbeat,
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
    StaticNoise(
        readableName = UIText.StringResource(R.string.sound_static_noise),
        iconResId = R.drawable.ic_sound_static_noise,
        audioResId = R.raw.sound_static_noise_deadrobotmusic,
    ),
    RadioStatic1(
        readableName = UIText.StringResource(R.string.sound_radio_static_1),
        iconResId = R.drawable.ic_sound_radio_static_1,
        audioResId = R.raw.sound_radio_static_1_dylanhsound,
    ),
    RadioStatic2(
        readableName = UIText.StringResource(R.string.sound_radio_static_2),
        iconResId = R.drawable.ic_sound_radio_static_2,
        audioResId = R.raw.sound_radio_static_2_theartguild,
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
    WindOnLeaves(
        readableName = UIText.StringResource(R.string.sound_wind_on_leaves),
        iconResId = R.drawable.ic_sound_wind_on_leaves,
        audioResId = R.raw.sound_blank,
    ),
    HeavyRain(
        readableName = UIText.StringResource(R.string.sound_heavy_rain),
        iconResId = R.drawable.ic_sound_heavy_rain,
        audioResId = R.raw.sound_blank,
    ),
    RainOnLeaves(
        readableName = UIText.StringResource(R.string.sound_rain_on_leaves),
        iconResId = R.drawable.ic_sound_rain_on_leaves,
        audioResId = R.raw.sound_blank,
    ),
    RainOnWindow(
        readableName = UIText.StringResource(R.string.sound_rain_on_window),
        iconResId = R.drawable.ic_sound_rain_on_window,
        audioResId = R.raw.sound_blank,
    ),
    RainOnRoof(
        readableName = UIText.StringResource(R.string.sound_rain_on_roof),
        iconResId = R.drawable.ic_sound_rain_on_roof,
        audioResId = R.raw.sound_blank,
    ),
    RainOnTent(
        readableName = UIText.StringResource(R.string.sound_rain_on_tent),
        iconResId = R.drawable.ic_sound_rain_on_tent,
        audioResId = R.raw.sound_blank,
    ),
    RainOnWindshield(
        readableName = UIText.StringResource(R.string.sound_rain_on_windshield),
        iconResId = R.drawable.ic_sound_rain_on_windshield,
        audioResId = R.raw.sound_blank,
    ),
    DistantThunder(
        readableName = UIText.StringResource(R.string.sound_distant_thunder),
        iconResId = R.drawable.ic_sound_distant_thunder,
        audioResId = R.raw.sound_blank,
    ),
    LightWind(
        readableName = UIText.StringResource(R.string.sound_light_wind),
        iconResId = R.drawable.ic_sound_light_wind,
        audioResId = R.raw.sound_blank,
    ),
    StrongWind(
        readableName = UIText.StringResource(R.string.sound_strong_wind),
        iconResId = R.drawable.ic_sound_strong_wind,
        audioResId = R.raw.sound_blank,
    ),
    HowlingWind(
        readableName = UIText.StringResource(R.string.sound_howling_wind),
        iconResId = R.drawable.ic_sound_howling_wind,
        audioResId = R.raw.sound_blank,
    ),
    PolarWind(
        readableName = UIText.StringResource(R.string.sound_polar_wind),
        iconResId = R.drawable.ic_sound_polar_wind,
        audioResId = R.raw.sound_blank,
    ),
    Storm(
        readableName = UIText.StringResource(R.string.sound_storm),
        iconResId = R.drawable.ic_sound_storm,
        audioResId = R.raw.sound_blank,
    ),
    Campfire(
        readableName = UIText.StringResource(R.string.sound_campfire),
        iconResId = R.drawable.ic_sound_campfire,
        audioResId = R.raw.sound_blank,
    ),
    Lake(
        readableName = UIText.StringResource(R.string.sound_lake),
        iconResId = R.drawable.ic_sound_lake,
        audioResId = R.raw.sound_blank,
    ),
    Waves(
        readableName = UIText.StringResource(R.string.sound_waves),
        iconResId = R.drawable.ic_sound_waves,
        audioResId = R.raw.sound_blank,
    ),
    Underwater(
        readableName = UIText.StringResource(R.string.sound_underwater),
        iconResId = R.drawable.ic_sound_underwater,
        audioResId = R.raw.sound_blank,
    ),
    WaterDrops(
        readableName = UIText.StringResource(R.string.sound_water_drops),
        iconResId = R.drawable.ic_sound_water_drops,
        audioResId = R.raw.sound_blank,
    ),
    SnowWalk(
        readableName = UIText.StringResource(R.string.sound_snow_walk),
        iconResId = R.drawable.ic_sound_snow_walk,
        audioResId = R.raw.sound_blank,
    ),
    Owls(
        readableName = UIText.StringResource(R.string.sound_owls),
        iconResId = R.drawable.ic_sound_owls,
        audioResId = R.raw.sound_blank,
    ),
    Cuckoo(
        readableName = UIText.StringResource(R.string.sound_cuckoo),
        iconResId = R.drawable.ic_sound_cuckoo,
        audioResId = R.raw.sound_blank,
    ),
    Dogs(
        readableName = UIText.StringResource(R.string.sound_dogs),
        iconResId = R.drawable.ic_sound_dogs,
        audioResId = R.raw.sound_blank,
    ),
    ChickenCoop(
        readableName = UIText.StringResource(R.string.sound_chicken_coop),
        iconResId = R.drawable.ic_sound_chicken_coop,
        audioResId = R.raw.sound_blank,
    ),
    Fan(
        readableName = UIText.StringResource(R.string.sound_fan),
        iconResId = R.drawable.ic_sound_fan,
        audioResId = R.raw.sound_blank,
    ),
    AirConditioner(
        readableName = UIText.StringResource(R.string.sound_air_conditioner),
        iconResId = R.drawable.ic_sound_air_conditioner,
        audioResId = R.raw.sound_blank,
    ),
    WashingMachine(
        readableName = UIText.StringResource(R.string.sound_washing_machine),
        iconResId = R.drawable.ic_sound_washing_machine,
        audioResId = R.raw.sound_blank,
    ),
    VintageClock(
        readableName = UIText.StringResource(R.string.sound_vintage_clock),
        iconResId = R.drawable.ic_sound_vintage_clock,
        audioResId = R.raw.sound_blank,
    ),
    FlippingPages(
        readableName = UIText.StringResource(R.string.sound_flipping_pages),
        iconResId = R.drawable.ic_sound_flipping_pages,
        audioResId = R.raw.sound_blank,
    ),
    VacuumCleaner(
        readableName = UIText.StringResource(R.string.sound_vacuum_cleaner),
        iconResId = R.drawable.ic_sound_vacuum_cleaner,
        audioResId = R.raw.sound_blank,
    ),
    BoilingPot(
        readableName = UIText.StringResource(R.string.sound_boiling_pot),
        iconResId = R.drawable.ic_sound_boiling_pot,
        audioResId = R.raw.sound_blank,
    ),
    TapWater(
        readableName = UIText.StringResource(R.string.sound_tap_water),
        iconResId = R.drawable.ic_sound_tap_water,
        audioResId = R.raw.sound_blank,
    ),
    MusicBox(
        readableName = UIText.StringResource(R.string.sound_music_box),
        iconResId = R.drawable.ic_sound_music_box,
        audioResId = R.raw.sound_blank,
    ),
    TrainStation(
        readableName = UIText.StringResource(R.string.sound_train_station),
        iconResId = R.drawable.ic_sound_train_station,
        audioResId = R.raw.sound_blank,
    ),
    ConstructionSite(
        readableName = UIText.StringResource(R.string.sound_construction_site),
        iconResId = R.drawable.ic_sound_construction_site,
        audioResId = R.raw.sound_blank,
    ),
    Traffic(
        readableName = UIText.StringResource(R.string.sound_traffic),
        iconResId = R.drawable.ic_sound_traffic,
        audioResId = R.raw.sound_blank,
    ),
    TrainTravel(
        readableName = UIText.StringResource(R.string.sound_train_travel),
        iconResId = R.drawable.ic_sound_train_travel,
        audioResId = R.raw.sound_blank,
    ),
    TurnSignal(
        readableName = UIText.StringResource(R.string.sound_turn_signal),
        iconResId = R.drawable.ic_sound_turn_signal,
        audioResId = R.raw.sound_blank,
    ),
    TruckEngine(
        readableName = UIText.StringResource(R.string.sound_truck_engine),
        iconResId = R.drawable.ic_sound_truck_engine,
        audioResId = R.raw.sound_blank,
    ),
    ConveyorBelt(
        readableName = UIText.StringResource(R.string.sound_conveyor_belt),
        iconResId = R.drawable.ic_sound_conveyor_belt,
        audioResId = R.raw.sound_blank,
    ),
    ;

    override val volume: Float
        get() = DataConstants.DEFAULT_SOUND_VOLUME
}