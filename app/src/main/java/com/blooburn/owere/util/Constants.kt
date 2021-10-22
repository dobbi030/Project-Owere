package com.blooburn.owere.util

import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

var myId = ""
val databaseInstance = FirebaseDatabase.getInstance()
val storageInstance = FirebaseStorage.getInstance()

const val DESIGNER_DATA_KEY = "designerData"