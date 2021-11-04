package com.blooburn.owere.util

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

var myId = ""
val databaseInstance = FirebaseDatabase.getInstance()
val storageInstance = FirebaseStorage.getInstance()

const val DESIGNER_DATA_KEY = "designerData"

// DesignerConfirmedReservation.kt - DesignerReservationListAdapter에서 예약 아이템 클릭했을 때 bundle key
const val DESIGNER_RESERVATION_DETAIL_KEY = "designerReservationDetail"

// addToBackStack(ALL_PRICES_FRAGMENT_NAME)
const val ALL_PRICES_FRAGMENT_NAME = "allPrices"

// 년월일 -> LocalDate
// val date2 = LocalDate.of(2021, 11, 2)
// 이런 방식 외에도 LocalDate나 LocalTime으로 변환하는 방법이 더 있는 것 같습니다.

// LocalDate -> (Long) epoch day
// date0.getLong(ChronoField.EPOCH_DAY)

// LocalDate -> time stamp, milliseconds (epoch time)
// LocalDate.atStartOfDay().atZone(ZoneId.of(ZONE_ID)).toEpochSecond()
const val ZONE_ID = "Asia/Seoul"

// LocalTime -> time stamp, milliseconds (epoch time)
// val time0_1 = LocalTime.of(10, 30).toSecondOfDay() * 1000
// -> 10시30분을 밀리세컨드로 변환



