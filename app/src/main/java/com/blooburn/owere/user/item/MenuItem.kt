package com.blooburn.owere.user.item

import android.widget.CheckBox

open class MenuItem(
    open val menuName: String, //앞머리
    open val menuPrice: String, //5000원
    open val menuTime: String,  //10분
    var checkBox: CheckBox?
) {
    //인자가 없는 경우 추가 필요 (에러)
    constructor() : this("", "", "", null)
}
