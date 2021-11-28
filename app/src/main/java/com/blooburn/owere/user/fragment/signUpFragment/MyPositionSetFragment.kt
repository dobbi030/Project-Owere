package com.blooburn.owere.user.fragment.signUpFragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.signUpActivity.SetPositionActivity
import com.blooburn.owere.databinding.MypositionSetFragmentBinding
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
//역지오코딩, 유저 디자이너 여부
class MyPositionSetFragment : Fragment(R.layout.myposition_set_fragment) {


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val database = databaseInstance
    //유저/디자이너 여부 확인
    private var userOrDesigner: String? = null



    private lateinit var bind: MypositionSetFragmentBinding
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val myPostionSetFragmentBinding = MypositionSetFragmentBinding.bind(view)

        bind = myPostionSetFragmentBinding

        getInfoUserOrDesigner()

        initMypositionSetButton()


    }

    override fun onResume() {
        super.onResume()

        val userId = auth.currentUser?.uid.toString()



//        val currentUserDB1 = Firebase.database.reference.child("Users").child(userId).child("Latitude")
//        val currentUserDB2 = Firebase.database.reference.child("Users").child(userId).child("longitude")
//
//

    }
    /**
     * 디자이너or유저 선택 여부
     */
    private fun getInfoUserOrDesigner(){
        //
        setFragmentResultListener("userOrDesigner") { key, bundle ->
            bundle.getString("userOrDesigner")?.let {
                userOrDesigner = it
            }
        }
    }


    private fun initMypositionSetButton() {
//      추가 버튼리스너
        bind.myPositionSetButton1.setOnClickListener {
            //추가 화면으로 인텐트를 통해 이동,
            var intent = Intent(requireContext(), SetPositionActivity::class.java)
//            intent.putExtra("userOrDesigner",userOrDesigner)
            Log.d("userDesigner", "$userOrDesigner")

            var requestcode = 0


            if(userOrDesigner == "User"){
                requestcode = 1
            }else {
                requestcode = 2
            }
            startActivityForResult(intent, requestcode)
        }
        // 프래그먼트(유저or 디자이너 정보) -> 지도액티비티 (좌표,주소) -> 프래그먼트()



        bind.myPositionSetButton2.setOnClickListener {

        }
        bind.mypositionKeepButton.setOnClickListener {

            requireActivity().finish()
        }

    }

    //주소 설정을 하였는지  위치 설정 액티비티로부터 콜백받음
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var userId = auth.currentUser!!.uid

        if ( resultCode == 100) {
            bind.myPositionSetButton1.text = "내 위치 1"
            var latitude = data!!.getDoubleExtra("latitude", 0.0)
            var longitude = data!!.getDoubleExtra("longitude", 0.0)
            var area = data!!.getStringExtra("area")

            if (requestCode == 1) {
                val currentUserDB1 =
                    Firebase.database.reference.child("Users").child(userId).child("위도")
                val currentUserDB2 =
                    Firebase.database.reference.child("Users").child(userId).child("경도")

                currentUserDB1.setValue(latitude)
                currentUserDB2.setValue(longitude)

            } else if (requestCode == 2) {
                val currentUserDB1 =
                    Firebase.database.reference.child("LocationDesigner").child(userId)
//
                Log.d("designer", "${latitude},${longitude},$area")

                var designerUpdate = mutableMapOf<String, Any>()
                designerUpdate["latitude"] = latitude!!
                designerUpdate["longitude"] = longitude!!

                databaseInstance.reference.child("Designers").child(userId).child("area")
                    .setValue(area)
                currentUserDB1.updateChildren(designerUpdate)


//                currentUserDB1.setValue(userLatitude)
//                currentUserDB2.setValue(userLongitue)

//            }

            } else {

            }

        }


    }
}

