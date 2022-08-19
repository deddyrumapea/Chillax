package com.romnan.chillax.domain.model

interface Sound {
    val name: String
    val readableName: UIText
    val iconResId: Int
    val audioResId: Int
}