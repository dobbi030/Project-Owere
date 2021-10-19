package com.blooburn.owere.item

//관심 디자이너 객체
data class FavoriteDesigner(
    val area :String,
    val matchingRate : Int,
    val name : String,
    val profileImagePath : String,
    val rating: Double,
    val reviewCount : Int,
){
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("",0,"","",0.0,0)
}

