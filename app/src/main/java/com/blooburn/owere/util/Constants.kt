package com.blooburn.owere.util

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

var myId = ""
val databaseInstance = FirebaseDatabase.getInstance()
val storageInstance = FirebaseStorage.getInstance()

const val DESIGNER_DATA_KEY = "designerData"

// 년월일 -> LocalDate
// val date2 = LocalDate.of(2021, 11, 2)

// LocalDate -> (Long) epoch day
// date0.getLong(ChronoField.EPOCH_DAY)

// LocalTime -> (Long) milliseconds (epoch time)
// val time0_1 = LocalTime.of(10, 30).toSecondOfDay() * 1000
