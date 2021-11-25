package com.blooburn.owere.designer.activity.myPage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import com.blooburn.owere.R
import com.blooburn.owere.util.databaseInstance
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

/* TODO 1. 처음에 EditText의 backspace 버튼이 눌리지 않는다
*       2. 처음에 EditText의 키보드 인식이 느리다
*/
class EditDesignerProfileActivity : AppCompatActivity() {
    private val tempReferencePath = "Designers/designer0"
    private val profileReference = databaseInstance.reference.child(tempReferencePath)
    private val KEY_NAME = "name"
    private val KEY_AREA = "area"
    private val KEY_INTRODUCTION = "introduction"

    private val nameEditTxtView: EditText by lazy{
        findViewById(R.id.edit_txt_edit_designer_profile_name)
    }
    private val editAreaBtn: TextView by lazy{
        findViewById(R.id.btn_edit_designer_profile_area)
    }
    private val introEditTxtView: EditText by lazy{
        findViewById(R.id.edit_txt_edit_designer_profile_introduction)
    }
    private val addPhotoBtn: ImageButton by lazy{
        findViewById(R.id.btn_designer_profile_add_photo)
    }
    private val cancelBtn: TextView by lazy{
        findViewById(R.id.btn_edit_designer_profile_cancel)
    }
    private val completeBtn: TextView by lazy{
        findViewById(R.id.btn_edit_designer_profile_complete)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_designer_profile)

        getProfileFromDBAndInitViews()
        initActionButtons()
    }

    private fun getProfileFromDBAndInitViews(){
        profileReference.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child(KEY_NAME).getValue(String::class.java).toString()
                val area = snapshot.child(KEY_AREA).getValue(String::class.java).toString()
                val introduction = snapshot.child(KEY_INTRODUCTION).getValue(String::class.java).toString()
                val imagePath = snapshot.child("profileImagePath").getValue(String::class.java).toString()

                initViews(name, area, introduction, imagePath)
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    private fun initViews(name: String, area: String, introduction: String, imagePath: String){
        nameEditTxtView.setText(name)
        editAreaBtn.text = area
        introEditTxtView.setText(introduction)

        Glide.with(this)
            .load(imagePath)
            .circleCrop()
            .into(addPhotoBtn)
    }

    // TODO: setResult 사용해서 이전 액티비티로 되돌아갔을 때 새로고침
    private fun initActionButtons(){
        cancelBtn.setOnClickListener { finish() }
        completeBtn.setOnClickListener {
            updateDB()
            finish()
        }
    }

    // TODO: 위치 변경
    private fun updateDB(){
        profileReference.apply{
            child(KEY_NAME).setValue(nameEditTxtView.text.toString())
            // child(KEY_AREA).setValue()
            child(KEY_INTRODUCTION).setValue(introEditTxtView.text.toString())
        }
    }
}