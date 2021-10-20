package com.blooburn.owere.user.item

open class MenuItem(
    val menuName: String, //앞머리
    val menuPrice: String, //5000원
    val menuTime: String,  //10분
){
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("", "", "")
}
