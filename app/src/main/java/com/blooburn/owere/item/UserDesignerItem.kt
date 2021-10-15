package com.blooburn.owere.item

data class UserDesignerItem(
    var designerId: String = "",
    val name: String = "Name",
    val reviewCount: Int = 0,
    val area: String = "",
    val matchingRate: Int = 0,
    val rating: Float = 0.0f,
    val profileImagePath: String= ""
)
