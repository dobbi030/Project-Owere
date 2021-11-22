package com.blooburn.owere.user.item

//실시간 DB snapshot에서 좌표를 받아오기 위한 클래스
data class LocationClass(
    val latitude : Double,
    val longitude : Double
){
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this(0.0,0.0)
}
