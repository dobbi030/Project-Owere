package com.blooburn.owere.designer.item

import com.blooburn.owere.util.TypeOfDesignerReservation

// DesignerConfirmedReservationFragment 에서 사용
data class DesignerReservation(
    val userName: String,
    val profileImagePath: String,
    val shop: String,
    val startTime: Long,
    val endTime: Long,
    var type: TypeOfDesignerReservation
) {
    constructor() : this("", "", "", 0, 0, TypeOfDesignerReservation.SCHEDULED)
}
