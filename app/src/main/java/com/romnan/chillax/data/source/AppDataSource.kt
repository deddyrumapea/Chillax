package com.romnan.chillax.data.source

import com.romnan.chillax.R
import com.romnan.chillax.data.util.DataConstants
import com.romnan.chillax.data.model.CategoryData
import com.romnan.chillax.data.model.MoodData
import com.romnan.chillax.data.model.SoundData
import com.romnan.chillax.domain.model.UIText

object AppDataSource {
    val moods: List<MoodData> = MoodDataSource.values().toList()
    val categories: List<CategoryData> = CategoryDataDataSource.values().toList()

    fun getSoundFromName(soundName: String): SoundData? {
        return try {
            SoundDataSource.valueOf(soundName)
        } catch (e: Exception) {
            null
        }
    }
}


enum class SoundDataSource(
    override val readableName: UIText,
    override val iconResId: Int,
    override val audioResId: Int,
) : SoundData {
    Campfire(
        readableName = UIText.StringResource(R.string.campfire),
        iconResId = R.drawable.is_campfire,
        audioResId = R.raw.sound_campfire,
    ),
    Cicadas(
        readableName = UIText.StringResource(R.string.cicadas),
        iconResId = R.drawable.is_cicadas,
        audioResId = R.raw.sound_cicadas,
    ),
    Rain(
        readableName = UIText.StringResource(R.string.rain),
        iconResId = R.drawable.is_rain,
        audioResId = R.raw.sound_rain,
    ),
    Thunder(
        readableName = UIText.StringResource(R.string.thunder),
        iconResId = R.drawable.is_thunder,
        audioResId = R.raw.sound_thunder,
    ),
    TruckEngine(
        readableName = UIText.StringResource(R.string.truck_engine),
        iconResId = R.drawable.is_truck_engine,
        audioResId = R.raw.sound_truck_engine,
    ),
    Underwater(
        readableName = UIText.StringResource(R.string.underwater),
        iconResId = R.drawable.is_underwater,
        audioResId = R.raw.sound_underwater,
    ),
    VintageClock(
        readableName = UIText.StringResource(R.string.vintage_clock),
        iconResId = R.drawable.is_vintage_clock,
        audioResId = R.raw.sound_vintage_clock,
    ),
    WashingMachine(
        readableName = UIText.StringResource(R.string.washing_machine),
        iconResId = R.drawable.is_washing_machine,
        audioResId = R.raw.sound_washing_machine,
    ),
    WaterDrops(
        readableName = UIText.StringResource(R.string.water_drops),
        iconResId = R.drawable.is_water_drops,
        audioResId = R.raw.sound_water_drops,
    ),
    Waterfall(
        readableName = UIText.StringResource(R.string.waterfall),
        iconResId = R.drawable.is_waterfall,
        audioResId = R.raw.sound_waterfall,
    ), ;

    override val volume: Float
        get() = DataConstants.DEFAULT_SOUND_VOLUME
}

private enum class MoodDataSource(
    override val readableName: UIText,
    override val imageResId: Int,
    override val sounds: List<SoundData>
) : MoodData {
    RainInForest(
        readableName = UIText.StringResource(R.string.mood_name_rain_forest),
        imageResId = R.raw.img_forest,
        sounds = listOf(SoundDataSource.Rain, SoundDataSource.Thunder, SoundDataSource.Cicadas),
    ),
    LivingRoom(
        readableName = UIText.StringResource(R.string.mood_name_living_room),
        imageResId = R.raw.img_living_room,
        sounds = listOf(SoundDataSource.VintageClock, SoundDataSource.WashingMachine),
    ),
    Forest(
        readableName = UIText.StringResource(R.string.mood_name_forest),
        imageResId = R.raw.img_jungle,
        sounds = listOf(SoundDataSource.Cicadas, SoundDataSource.Campfire),
    ),
    Mountains(
        readableName = UIText.StringResource(R.string.mood_name_mountains),
        imageResId = R.raw.img_mountains,
        sounds = listOf(SoundDataSource.Underwater, SoundDataSource.Waterfall),
    ),
    RainInBackyard(
        readableName = UIText.StringResource(R.string.mood_name_rain_in_backyard),
        imageResId = R.raw.img_rain_umberella,
        sounds = listOf(SoundDataSource.Rain, SoundDataSource.Campfire),
    ),
}

private enum class CategoryDataDataSource(
    override val readableName: UIText,
    override val description: UIText,
    override val sounds: List<SoundData>
) : CategoryData {
    Rain(
        readableName = UIText.StringResource(R.string.cat_name_rain),
        description = UIText.StringResource(R.string.cat_desc_rain),
        sounds = listOf(
            SoundDataSource.Rain,
            SoundDataSource.Thunder,
            SoundDataSource.Campfire,
            SoundDataSource.Waterfall,
            SoundDataSource.WaterDrops,
        )
    ),

    Nature(
        readableName = UIText.StringResource(R.string.cat_name_nature),
        description = UIText.StringResource(R.string.cat_desc_nature),
        sounds = listOf(SoundDataSource.Cicadas, SoundDataSource.Campfire)
    ),

    Animals(
        readableName = UIText.StringResource(R.string.cat_name_animals),
        description = UIText.StringResource(R.string.cat_desc_animals),
        sounds = listOf(SoundDataSource.VintageClock, SoundDataSource.Cicadas)
    ),

    Room(
        readableName = UIText.StringResource(R.string.cat_name_room),
        description = UIText.StringResource(R.string.cat_desc_room),
        sounds = listOf(SoundDataSource.VintageClock, SoundDataSource.WashingMachine)
    ),

    City(
        readableName = UIText.StringResource(R.string.cat_name_city),
        description = UIText.StringResource(R.string.cat_desc_city),
        sounds = listOf(SoundDataSource.TruckEngine, SoundDataSource.Underwater)
    ),

    Other(
        readableName = UIText.StringResource(R.string.cat_name_other),
        description = UIText.StringResource(R.string.cat_desc_other),
        sounds = listOf(SoundDataSource.WaterDrops, SoundDataSource.Waterfall)
    ),
}