package com.blooburn.owere.designer.item

import com.blooburn.owere.user.item.MenuItem

data class AdditionalMenuItem(
    override val menuName: String, // 앞머리
    override val menuPrice: String, // 5000원
    override val menuTime: String,  // 10분

): MenuItem(){
    constructor() : this("", "", "")
}
