package com.blooburn.owere.item

data class StyleMenuItem(
    val menuName: String, //앞머리
    val menuPrice: String, //5000원
    val menuTime: String,  //10분
    //길이추가
    val addLength: Int,
    // 커트포함
    val withCut: Int
){
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("", "", "", 0, 0)
}