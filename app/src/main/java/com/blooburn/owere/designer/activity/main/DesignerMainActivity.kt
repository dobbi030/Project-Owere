package com.blooburn.owere.designer.activity.main

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.designer.fragment.chatting.ChattingForDesignerFragment
import com.blooburn.owere.designer.fragment.home.DesignerHomeFragment
import com.blooburn.owere.designer.fragment.myPage.MypageDesignerFragment
import com.blooburn.owere.user.activity.main.userReservation.ShopsOfDesignerActivity
import com.blooburn.owere.user.activity.signUpActivity.SetPositionActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

class DesignerMainActivity : AppCompatActivity() {

    companion object {
        val PERMISSION_REQUEST_CODE = 100
        val GPS_ENABLE_REQUEST_CODE = 2001

        val LOG_TAG = "MainActivity"
    }
    var REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

    private val homeFragment: DesignerHomeFragment by lazy{
        DesignerHomeFragment()
    }
    //채팅 프래그먼트
    private val chattingFragment: ChattingForDesignerFragment by lazy{
        ChattingForDesignerFragment()
    }
    private val mypageFragment : MypageDesignerFragment by lazy{
        MypageDesignerFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_designer_main)

        val bottomNavigationView =
            findViewById<BottomNavigationView>(R.id.bottom_navigation_view_designer_main)
        replaceFragment(homeFragment)

        bottomNavigationView.setOnItemSelectedListener(bottomNavigationViewItemClickListener)

        //권한 체크
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()

        } else {
            checkRunTimePermission()
        }



    }

    /**
     * 프래그먼트 교체
     */
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .apply {
                replace(R.id.fragment_container_view_designer_main, fragment)
                commit()
            }
    }

    private val bottomNavigationViewItemClickListener =
        NavigationBarView.OnItemSelectedListener { item ->
            when(item.itemId){
                R.id.designer_home -> replaceFragment(homeFragment)
                R.id.designer_browse -> {}
                R.id.designer_chatting -> replaceFragment(chattingFragment)
                R.id.designer_recruit -> {}
                else -> replaceFragment(mypageFragment)
            }

            true
        }

    private fun checkRunTimePermission() {
        //런타입 퍼미션 처리
        //1. 위치 퍼미션을 가지고 있는지 체크
        var hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED) {
            //이미 퍼미션을 가지고 있다면
            // 안드로이드 6.0이하 버전은 퍼미션 체크 안 해도 통과




        } else {  //퍼미션 요청 허용한 적 없는 경우 -> 요청 필요

            //사용자가 퍼미션 거부를 한 적이 있는 경우
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    REQUIRED_PERMISSIONS[0]
                )
            ) {

                //요청을 다시하기 전에 퍼미션이 필요한 이유를 설명
                Toast.makeText(this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show()
                //사용자에게 퍼미션 요청-> 요청 결과는 onRequestPermissionResult에서 수신
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    SetPositionActivity.PERMISSION_REQUEST_CODE
                )

            } else {
                //사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                //사용자에게 퍼미션 요청-> 요청 결과는 onRequestPermissionResult에서 수신
                ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS,
                    SetPositionActivity.PERMISSION_REQUEST_CODE
                )
            }

        }
    }

    //GPS 활성화를 위한 메소드
    private fun showDialogForLocationServiceSetting() {

        var intent = Intent(this, Settings.ACTION_LOCATION_SOURCE_SETTINGS::class.java)

        var builder = AlertDialog.Builder(this)
        builder.setTitle("위치서비스 비활성화")
            .setMessage(
                "앱을 사용하기 위해서는 위치 서비스가 필요합니다. \n" +
                        "위치 설정을 수정하실래요?"
            )
            .setCancelable(true)
            .setPositiveButton("설정", DialogInterface.OnClickListener { dialog, which ->
                startActivityForResult(intent, SetPositionActivity.GPS_ENABLE_REQUEST_CODE)

            })
            .setNegativeButton("취소", DialogInterface.OnClickListener { dialog, which ->
                dialog.cancel()

            })
            .create()
            .show()


    }

    //인텐트 반환
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            SetPositionActivity.GPS_ENABLE_REQUEST_CODE ->
                //사용자가 GPS활성화 했는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {
                        Log.d("@@@", "onActivityResult : GPS활성화 되있음")
                        checkRunTimePermission()
                        return
                    }
                }

        }
    }


    private fun checkLocationServicesStatus(): Boolean {
        var locationManager: LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled((LocationManager.NETWORK_PROVIDER))
    }

    //    사용자에게 퍼미션 요청-> 요청 결과
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //요청코드가 PERMISSION_REQUEST_CODE이고 요청한 퍼미션 개수 만큼 수신되었다면
        if (requestCode == ShopsOfDesignerActivity.PERMISSION_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
            var check_result = true

            //모든 퍼미션 허용 체크
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false
                    break
                }
            }

            if (check_result) {
                Log.d("@@@", "start")

                //위치값을 가져올수 있음
//                mapView.currentLocationTrackingMode//
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        this,
                        REQUIRED_PERMISSIONS[0]
                    )
                ) {
                    Toast.makeText(
                        this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요",
                        Toast.LENGTH_LONG
                    ).show()
                    finish()
                } else {
                    Toast.makeText(
                        this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


    }




}