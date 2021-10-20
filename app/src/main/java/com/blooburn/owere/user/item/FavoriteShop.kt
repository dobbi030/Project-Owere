package com.blooburn.owere.user.item

data class FavoriteShop(
    val area: String,
    val name: String,
    val profileImagePath: String,
    val rating: Double,
    val reviewCount: Int
) {
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("", "", "", 0.0, 0)
}

