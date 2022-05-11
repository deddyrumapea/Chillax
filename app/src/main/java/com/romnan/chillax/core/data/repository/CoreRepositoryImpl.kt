package com.romnan.chillax.core.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import com.romnan.chillax.R
import com.romnan.chillax.core.domain.model.Sound
import com.romnan.chillax.core.domain.model.UIText
import com.romnan.chillax.core.domain.repository.CoreRepository

class CoreRepositoryImpl : CoreRepository {
    override fun getSounds(): List<Sound> {
        return listOf(
            Sound(
                icon = Icons.Default.LocalFireDepartment,
                name = UIText.StringResource(R.string.campfire),
                resource = R.raw.sound_campfire
            ),
            Sound(
                icon = Icons.Default.PestControl,
                name = UIText.StringResource(R.string.cicadas),
                resource = R.raw.sound_cicadas
            ),
            Sound(
                icon = Icons.Default.WaterDrop,
                name = UIText.StringResource(R.string.rain),
                resource = R.raw.sound_rain
            ),
            Sound(
                icon = Icons.Default.Storm,
                name = UIText.StringResource(R.string.thunder),
                resource = R.raw.sound_thunder
            ),
            Sound(
                icon = Icons.Default.Engineering,
                name = UIText.StringResource(R.string.truck_engine),
                resource = R.raw.sound_truck_engine
            ),
            Sound(
                icon = Icons.Default.Water,
                name = UIText.StringResource(R.string.underwater),
                resource = R.raw.sound_underwater
            ),
            Sound(
                icon = Icons.Default.LockClock,
                name = UIText.StringResource(R.string.vintage_clock),
                resource = R.raw.sound_vintage_clock
            ),
            Sound(
                icon = Icons.Default.Wash,
                name = UIText.StringResource(R.string.washing_machine),
                resource = R.raw.sound_washing_machine
            ),
            Sound(
                icon = Icons.Default.WaterDamage,
                name = UIText.StringResource(R.string.water_drops),
                resource = R.raw.sound_water_drops
            ),
            Sound(
                icon = Icons.Default.WaterfallChart,
                name = UIText.StringResource(R.string.waterfall),
                resource = R.raw.sound_waterfall
            )
        )
    }
}