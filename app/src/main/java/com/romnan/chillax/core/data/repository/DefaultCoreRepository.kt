package com.romnan.chillax.core.data.repository

import com.romnan.chillax.R
import com.romnan.chillax.core.domain.model.Mood
import com.romnan.chillax.core.domain.model.Sound
import com.romnan.chillax.core.domain.repository.CoreRepository

class DefaultCoreRepository : CoreRepository {
    override fun getSounds(): List<Sound> {
        return soundsList
    }

    override fun getMoods(): List<Mood> {
        return moodsList
    }

    private val soundsList = listOf(
        Sound(
            icon = R.drawable.is_campfire,
            name = R.string.campfire,
            resource = R.raw.sound_campfire
        ),
        Sound(
            icon = R.drawable.is_cicadas,
            name = R.string.cicadas,
            resource = R.raw.sound_cicadas
        ),
        Sound(
            icon = R.drawable.is_rain,
            name = R.string.rain,
            resource = R.raw.sound_rain
        ),
        Sound(
            icon = R.drawable.is_thunder,
            name = R.string.thunder,
            resource = R.raw.sound_thunder
        ),
        Sound(
            icon = R.drawable.is_truck_engine,
            name = R.string.truck_engine,
            resource = R.raw.sound_truck_engine
        ),
        Sound(
            icon = R.drawable.is_underwater,
            name = R.string.underwater,
            resource = R.raw.sound_underwater
        ),
        Sound(
            icon = R.drawable.is_vintage_clock,
            name = R.string.vintage_clock,
            resource = R.raw.sound_vintage_clock
        ),
        Sound(
            icon = R.drawable.is_washing_machine,
            name = R.string.washing_machine,
            resource = R.raw.sound_washing_machine
        ),
        Sound(
            icon = R.drawable.is_water_drops,
            name = R.string.water_drops,
            resource = R.raw.sound_water_drops
        ),
        Sound(
            icon = R.drawable.is_waterfall,
            name = R.string.waterfall,
            resource = R.raw.sound_waterfall
        )
    )

    private val moodsList = listOf(
        Mood(
            image = R.drawable.mood_1,
            name = R.string.sounds_012,
            soundsList = soundsList.subList(0, 3)
        ),
        Mood(
            image = R.drawable.mood_2,
            name = R.string.sound_345,
            soundsList = soundsList.subList(3, 6)
        ),
        Mood(
            image = R.drawable.mood_3,
            name = R.string.sound_678,
            soundsList = soundsList.subList(6, 9)
        ),
        Mood(
            image = R.drawable.mood_4,
            name = R.string.sound_9,
            soundsList = soundsList.subList(9, 9)
        ),
    )
}