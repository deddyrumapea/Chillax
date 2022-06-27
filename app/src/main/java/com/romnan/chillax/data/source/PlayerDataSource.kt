package com.romnan.chillax.data.source

import com.romnan.chillax.R
import com.romnan.chillax.domain.model.Mood
import com.romnan.chillax.domain.model.Sound

object PlayerDataSource {
    val soundsList = listOf(
        Sound(
            id = 1,
            icon = R.drawable.is_campfire,
            name = R.string.campfire,
            resource = R.raw.sound_campfire
        ),
        Sound(
            id = 2,
            icon = R.drawable.is_cicadas,
            name = R.string.cicadas,
            resource = R.raw.sound_cicadas
        ),
        Sound(
            id = 3,
            icon = R.drawable.is_rain,
            name = R.string.rain,
            resource = R.raw.sound_rain
        ),
        Sound(
            id = 4,
            icon = R.drawable.is_thunder,
            name = R.string.thunder,
            resource = R.raw.sound_thunder
        ),
        Sound(
            id = 5,
            icon = R.drawable.is_truck_engine,
            name = R.string.truck_engine,
            resource = R.raw.sound_truck_engine
        ),
        Sound(
            id = 6,
            icon = R.drawable.is_underwater,
            name = R.string.underwater,
            resource = R.raw.sound_underwater
        ),
        Sound(
            id = 7,
            icon = R.drawable.is_vintage_clock,
            name = R.string.vintage_clock,
            resource = R.raw.sound_vintage_clock
        ),
        Sound(
            id = 8,
            icon = R.drawable.is_washing_machine,
            name = R.string.washing_machine,
            resource = R.raw.sound_washing_machine
        ),
        Sound(
            id = 9,
            icon = R.drawable.is_water_drops,
            name = R.string.water_drops,
            resource = R.raw.sound_water_drops
        ),
        Sound(
            id = 10,
            icon = R.drawable.is_waterfall,
            name = R.string.waterfall,
            resource = R.raw.sound_waterfall
        )
    )

    val moodsList = listOf(
        Mood(
            id = 1,
            image = R.drawable.is_waterfall,
            name = R.string.sounds_012,
            soundsList = soundsList.subList(0, 2)
        ),
        Mood(
            id = 2,
            image = R.drawable.is_waterfall,
            name = R.string.sound_345,
            soundsList = soundsList.subList(2, 4)
        ),
        Mood(
            id = 3,
            image = R.drawable.is_waterfall,
            name = R.string.sound_678,
            soundsList = soundsList.subList(4, 6)
        ),
        Mood(
            id = 4,
            image = R.drawable.is_waterfall,
            name = R.string.sound_9,
            soundsList = soundsList.subList(6, 8)
        ),
    )
}