package com.blooburn.owere.user.item

data class UserReview(
    val userImagePath: String,
    val userName: String,
    val dates: String,
    val description: String,
    val rating: Double
){
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("","", "", "", 0.0)
}
