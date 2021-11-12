package com.blooburn.owere.user.item

data class UserEntity(
    val myName : String,
    val userId : String,
    val latitude: Double,
    val longitude : Double
){  //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("","",0.0,0.0)
}

