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
            SoundDataSource.Frogs,
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
            SoundDataSource.Driving,
        ),
    ),
    Flight(
        readableName = UIText.StringResource(R.string.mood_name_flight),
        imageResId = R.raw.img_forest,
        sounds = listOf(
            SoundDataSource.Crowd,
            SoundDataSource.Flight,
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
            SoundDataSource.Rain,
            SoundDataSource.GentleRain,
            SoundDataSource.HeavyRain,
            SoundDataSource.RainOnLeaves,
            SoundDataSource.RainOnWindow,
            SoundDataSource.RainOnRoof,
            SoundDataSource.RainUnderUmbrella,
            SoundDataSource.RainOnTent,
            SoundDataSource.RainOnWindshield,
            SoundDataSource.Thunder,
            SoundDataSource.DistantThunder,
        ),
    ),
    Nature(
        readableName = UIText.StringResource(R.string.cat_name_nature),
        description = UIText.StringResource(R.string.cat_desc_nature),
        sounds = listOf(
            SoundDataSource.LightWind,
            SoundDataSource.StrongWind,
            SoundDataSource.WindOnLeaves,
            SoundDataSource.HowlingWind,
            SoundDataSource.PolarWind,
            SoundDataSource.Storm,
            SoundDataSource.Campfire,
            SoundDataSource.Lake,
            SoundDataSource.Waves,
            SoundDataSource.Creek,
            SoundDataSource.Waterfall,
            SoundDataSource.Underwater,
            SoundDataSource.WaterDrops,
            SoundDataSource.SnowWalk,
        ),
    ),
    Animals(
        readableName = UIText.StringResource(R.string.cat_name_animals),
        description = UIText.StringResource(R.string.cat_desc_animals),
        sounds = listOf(
            SoundDataSource.Birds1,
            SoundDataSource.Birds2,
            SoundDataSource.Birds3,
            SoundDataSource.Owls,
            SoundDataSource.Cuckoo,
            SoundDataSource.Seagulls,
            SoundDataSource.Crickets,
            SoundDataSource.Cicadas,
            SoundDataSource.Frogs,
            SoundDataSource.CatPurring,
            SoundDataSource.Dogs,
            SoundDataSource.ChickenCoop,
        ),
    ),
    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(
            SoundDataSource.Fireplace,
            SoundDataSource.Fan,
            SoundDataSource.AirConditioner,
            SoundDataSource.WashingMachine,
            SoundDataSource.VintageClock,
            SoundDataSource.FlippingPages,
            SoundDataSource.Keyboard,
            SoundDataSource.VacuumCleaner,
            SoundDataSource.BoilingPot,
            SoundDataSource.FryingPan,
            SoundDataSource.TapWater,
            SoundDataSource.MusicBox,
        ),
    ),
    City(
        readableName = UIText.StringResource(R.string.cat_name_city),
        description = UIText.StringResource(R.string.cat_desc_city),
        sounds = listOf(
            SoundDataSource.TrainStation,
            SoundDataSource.KidsPlayground,
            SoundDataSource.Crowd,
            SoundDataSource.ConstructionSite,
            SoundDataSource.Traffic,
            SoundDataSource.TrainTravel,
            SoundDataSource.Flight,
            SoundDataSource.Driving,
            SoundDataSource.WindshieldWipers,
            SoundDataSource.TurnSignal,
            SoundDataSource.TruckEngine,
            SoundDataSource.ConveyorBelt,
        ),
    ),
    Other(
        readableName = UIText.StringResource(R.string.cat_name_other),
        description = UIText.StringResource(R.string.cat_desc_other),
        sounds = listOf(
            SoundDataSource.Heartbeat,
            SoundDataSource.WhiteNoise,
            SoundDataSource.BrownNoise,
            SoundDataSource.PinkNoise,
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
        audioResId = R.raw.sound_gentle_rain,
    ),
    GentleRain(
        readableName = UIText.StringResource(R.string.sound_gentle_rain),
        iconResId = R.drawable.ic_sound_gentle_rain,
        audioResId = R.raw.sound_gentle_rain,
    ),
    HeavyRain(
        readableName = UIText.StringResource(R.string.sound_heavy_rain),
        iconResId = R.drawable.ic_sound_heavy_rain,
        audioResId = R.raw.sound_gentle_rain,
    ),
    RainOnLeaves(
        readableName = UIText.StringResource(R.string.sound_rain_on_leaves),
        iconResId = R.drawable.ic_sound_rain_on_leaves,
        audioResId = R.raw.sound_gentle_rain,
    ),
    RainOnWindow(
        readableName = UIText.StringResource(R.string.sound_rain_on_window),
        iconResId = R.drawable.ic_sound_rain_on_window,
        audioResId = R.raw.sound_gentle_rain,
    ),
    RainOnRoof(
        readableName = UIText.StringResource(R.string.sound_rain_on_roof),
        iconResId = R.drawable.ic_sound_rain_on_roof,
        audioResId = R.raw.sound_gentle_rain,
    ),
    RainUnderUmbrella(
        readableName = UIText.StringResource(R.string.sound_rain_under_umbrella),
        iconResId = R.drawable.ic_sound_rain_under_umbrella,
        audioResId = R.raw.sound_gentle_rain
    ),
    RainOnTent(
        readableName = UIText.StringResource(R.string.sound_rain_on_tent),
        iconResId = R.drawable.ic_sound_rain_on_tent,
        audioResId = R.raw.sound_gentle_rain,
    ),
    RainOnWindshield(
        readableName = UIText.StringResource(R.string.sound_rain_on_windshield),
        iconResId = R.drawable.ic_sound_rain_on_windshield,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Thunder(
        readableName = UIText.StringResource(R.string.sound_thunder),
        iconResId = R.drawable.ic_sound_thunder,
        audioResId = R.raw.sound_gentle_rain,
    ),
    DistantThunder(
        readableName = UIText.StringResource(R.string.sound_distant_thunder),
        iconResId = R.drawable.ic_sound_distant_thunder,
        audioResId = R.raw.sound_gentle_rain,
    ),
    LightWind(
        readableName = UIText.StringResource(R.string.sound_light_wind),
        iconResId = R.drawable.ic_sound_light_wind,
        audioResId = R.raw.sound_gentle_rain,
    ),
    StrongWind(
        readableName = UIText.StringResource(R.string.sound_strong_wind),
        iconResId = R.drawable.ic_sound_strong_wind,
        audioResId = R.raw.sound_gentle_rain,
    ),
    WindOnLeaves(
        readableName = UIText.StringResource(R.string.sound_wind_on_leaves),
        iconResId = R.drawable.ic_sound_wind_on_leaves,
        audioResId = R.raw.sound_gentle_rain,
    ),
    HowlingWind(
        readableName = UIText.StringResource(R.string.sound_howling_wind),
        iconResId = R.drawable.ic_sound_howling_wind,
        audioResId = R.raw.sound_gentle_rain,
    ),
    PolarWind(
        readableName = UIText.StringResource(R.string.sound_polar_wind),
        iconResId = R.drawable.ic_sound_polar_wind,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Storm(
        readableName = UIText.StringResource(R.string.sound_storm),
        iconResId = R.drawable.ic_sound_storm,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Campfire(
        readableName = UIText.StringResource(R.string.sound_campfire),
        iconResId = R.drawable.ic_sound_campfire,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Lake(
        readableName = UIText.StringResource(R.string.sound_lake),
        iconResId = R.drawable.ic_sound_lake,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Waves(
        readableName = UIText.StringResource(R.string.sound_waves),
        iconResId = R.drawable.ic_sound_waves,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Creek(
        readableName = UIText.StringResource(R.string.sound_creek),
        iconResId = R.drawable.ic_sound_creek,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Waterfall(
        readableName = UIText.StringResource(R.string.sound_waterfall),
        iconResId = R.drawable.ic_sound_waterfall,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Underwater(
        readableName = UIText.StringResource(R.string.sound_underwater),
        iconResId = R.drawable.ic_sound_underwater,
        audioResId = R.raw.sound_gentle_rain,
    ),
    WaterDrops(
        readableName = UIText.StringResource(R.string.sound_water_drops),
        iconResId = R.drawable.ic_sound_water_drops,
        audioResId = R.raw.sound_gentle_rain,
    ),
    SnowWalk(
        readableName = UIText.StringResource(R.string.sound_snow_walk),
        iconResId = R.drawable.ic_sound_snow_walk,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Birds1(
        readableName = UIText.StringResource(R.string.sound_birds_1),
        iconResId = R.drawable.ic_sound_birds_1,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Birds2(
        readableName = UIText.StringResource(R.string.sound_birds_2),
        iconResId = R.drawable.ic_sound_birds_2,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Birds3(
        readableName = UIText.StringResource(R.string.sound_birds_3),
        iconResId = R.drawable.ic_sound_birds_3,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Owls(
        readableName = UIText.StringResource(R.string.sound_owls),
        iconResId = R.drawable.ic_sound_owls,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Cuckoo(
        readableName = UIText.StringResource(R.string.sound_cuckoo),
        iconResId = R.drawable.ic_sound_cuckoo,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Seagulls(
        readableName = UIText.StringResource(R.string.sound_seagulls),
        iconResId = R.drawable.ic_sound_seagulls,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Crickets(
        readableName = UIText.StringResource(R.string.sound_crickets),
        iconResId = R.drawable.ic_sound_crickets,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Cicadas(
        readableName = UIText.StringResource(R.string.sound_cicadas),
        iconResId = R.drawable.ic_sound_cicadas,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Frogs(
        readableName = UIText.StringResource(R.string.sound_frogs),
        iconResId = R.drawable.ic_sound_frogs,
        audioResId = R.raw.sound_gentle_rain,
    ),
    CatPurring(
        readableName = UIText.StringResource(R.string.sound_cat_purring),
        iconResId = R.drawable.ic_sound_cat_purring,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Dogs(
        readableName = UIText.StringResource(R.string.sound_dogs),
        iconResId = R.drawable.ic_sound_dogs,
        audioResId = R.raw.sound_gentle_rain,
    ),
    ChickenCoop(
        readableName = UIText.StringResource(R.string.sound_chicken_coop),
        iconResId = R.drawable.ic_sound_chicken_coop,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Fireplace(
        readableName = UIText.StringResource(R.string.sound_fireplace),
        iconResId = R.drawable.ic_sound_fireplace,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Fan(
        readableName = UIText.StringResource(R.string.sound_fan),
        iconResId = R.drawable.ic_sound_fan,
        audioResId = R.raw.sound_gentle_rain,
    ),
    AirConditioner(
        readableName = UIText.StringResource(R.string.sound_air_conditioner),
        iconResId = R.drawable.ic_sound_air_conditioner,
        audioResId = R.raw.sound_gentle_rain,
    ),
    WashingMachine(
        readableName = UIText.StringResource(R.string.sound_washing_machine),
        iconResId = R.drawable.ic_sound_washing_machine,
        audioResId = R.raw.sound_gentle_rain,
    ),
    VintageClock(
        readableName = UIText.StringResource(R.string.sound_vintage_clock),
        iconResId = R.drawable.ic_sound_vintage_clock,
        audioResId = R.raw.sound_gentle_rain,
    ),
    FlippingPages(
        readableName = UIText.StringResource(R.string.sound_flipping_pages),
        iconResId = R.drawable.ic_sound_flipping_pages,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Keyboard(
        readableName = UIText.StringResource(R.string.sound_keyboard),
        iconResId = R.drawable.ic_sound_keyboard,
        audioResId = R.raw.sound_gentle_rain,
    ),
    VacuumCleaner(
        readableName = UIText.StringResource(R.string.sound_vacuum_cleaner),
        iconResId = R.drawable.ic_sound_vacuum_cleaner,
        audioResId = R.raw.sound_gentle_rain,
    ),
    BoilingPot(
        readableName = UIText.StringResource(R.string.sound_boiling_pot),
        iconResId = R.drawable.ic_sound_boiling_pot,
        audioResId = R.raw.sound_gentle_rain,
    ),
    FryingPan(
        readableName = UIText.StringResource(R.string.sound_frying_pan),
        iconResId = R.drawable.ic_sound_frying_pan,
        audioResId = R.raw.sound_gentle_rain,
    ),
    TapWater(
        readableName = UIText.StringResource(R.string.sound_tap_water),
        iconResId = R.drawable.ic_sound_tap_water,
        audioResId = R.raw.sound_gentle_rain,
    ),
    MusicBox(
        readableName = UIText.StringResource(R.string.sound_music_box),
        iconResId = R.drawable.ic_sound_music_box,
        audioResId = R.raw.sound_gentle_rain,
    ),
    TrainStation(
        readableName = UIText.StringResource(R.string.sound_train_station),
        iconResId = R.drawable.ic_sound_train_station,
        audioResId = R.raw.sound_gentle_rain,
    ),
    KidsPlayground(
        readableName = UIText.StringResource(R.string.sound_kids_playground),
        iconResId = R.drawable.ic_sound_kids_playground,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Crowd(
        readableName = UIText.StringResource(R.string.sound_crowd),
        iconResId = R.drawable.ic_sound_crowd,
        audioResId = R.raw.sound_gentle_rain,
    ),
    ConstructionSite(
        readableName = UIText.StringResource(R.string.sound_construction_site),
        iconResId = R.drawable.ic_sound_construction_site,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Traffic(
        readableName = UIText.StringResource(R.string.sound_traffic),
        iconResId = R.drawable.ic_sound_traffic,
        audioResId = R.raw.sound_gentle_rain,
    ),
    TrainTravel(
        readableName = UIText.StringResource(R.string.sound_train_travel),
        iconResId = R.drawable.ic_sound_train_travel,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Flight(
        readableName = UIText.StringResource(R.string.sound_flight),
        iconResId = R.drawable.ic_sound_flight,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Driving(
        readableName = UIText.StringResource(R.string.sound_driving),
        iconResId = R.drawable.ic_sound_driving,
        audioResId = R.raw.sound_gentle_rain,
    ),
    WindshieldWipers(
        readableName = UIText.StringResource(R.string.sound_windshield_wipers),
        iconResId = R.drawable.ic_sound_windshield_wiper,
        audioResId = R.raw.sound_gentle_rain,
    ),
    TurnSignal(
        readableName = UIText.StringResource(R.string.sound_turn_signal),
        iconResId = R.drawable.ic_sound_turn_signal,
        audioResId = R.raw.sound_gentle_rain,
    ),
    TruckEngine(
        readableName = UIText.StringResource(R.string.sound_truck_engine),
        iconResId = R.drawable.ic_sound_truck_engine,
        audioResId = R.raw.sound_gentle_rain,
    ),
    ConveyorBelt(
        readableName = UIText.StringResource(R.string.sound_conveyor_belt),
        iconResId = R.drawable.ic_sound_conveyor_belt,
        audioResId = R.raw.sound_gentle_rain,
    ),
    Heartbeat(
        readableName = UIText.StringResource(R.string.sound_heartbeat),
        iconResId = R.drawable.ic_sound_hearbeat,
        audioResId = R.raw.sound_gentle_rain,
    ),
    WhiteNoise(
        readableName = UIText.StringResource(R.string.sound_white_noise),
        iconResId = R.drawable.ic_sound_noise,
        audioResId = R.raw.sound_gentle_rain,
    ),
    BrownNoise(
        readableName = UIText.StringResource(R.string.sound_brown_noise),
        iconResId = R.drawable.ic_sound_noise,
        audioResId = R.raw.sound_gentle_rain,
    ),
    PinkNoise(
        readableName = UIText.StringResource(R.string.sound_pink_noise),
        iconResId = R.drawable.ic_sound_noise,
        audioResId = R.raw.sound_gentle_rain,
    ),
    ;

    override val volume: Float
        get() = DataConstants.DEFAULT_SOUND_VOLUME
}