package com.blooburn.owere.user.item

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDesignerItem(var designerId: String = "") : FavoriteDesigner(), Parcelable
