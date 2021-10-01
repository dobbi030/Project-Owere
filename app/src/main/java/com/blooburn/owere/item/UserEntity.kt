package com.blooburn.owere.item

data class UserEntity(
    val myName : String,
    val userId : String,
    val latitude: Float,
    val longitude : Float
){  //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("","",0.0f,0.0f)
}

