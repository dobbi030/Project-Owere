package com.blooburn.owere.designer.item

import android.os.Parcelable
import com.blooburn.owere.util.TypeOfDesignerReservation
import kotlinx.android.parcel.Parcelize

@Parcelize
// DesignerConfirmedReservationFragment 에서 사용
data class DesignerReservation(
    val userName: String,
    val profileImagePath: String,
    val shop: String,
    val startTime: Long,
    val endTime: Long,
    var type: TypeOfDesignerReservation
) : Parcelable {
    constructor() : this("", "", "", 0, 0, TypeOfDesignerReservation.SCHEDULED)
}
