package com.blooburn.owere.user.item

data class StyleMenuItem(
    //길이추가
    val addLength: Int,
    // 커트포함
    val withCut: Int
): MenuItem() {
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this(0, 0)
}