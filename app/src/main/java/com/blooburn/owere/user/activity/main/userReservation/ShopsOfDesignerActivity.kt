package com.blooburn.owere.user.activity.main.userReservation

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R


import com.blooburn.owere.user.activity.signUpActivity.SetPositionActivity
import com.blooburn.owere.user.adapter.userReservation.ShopListOfDesignerAdapter
import com.blooburn.owere.user.item.ShopListItem
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserDesignerItem
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView


class ShopsOfDesignerActivity : AppCompatActivity(), MapView.CurrentLocationEventListener,
    MapView.POIItemEventListener {

    private var designerData: UserDesignerItem? = null//프로필에서 전달받을 디자이너 객체
    private var menu: StyleMenuItem? = null//선택한 메뉴

    //기장 옵션
    private var lengthOption: String = ""
    private var selectedShop: ShopListItem? = null //선택할 미용실

    private val databaseReference = databaseInstance.reference

    private lateinit var mapView: MapView

    /**
     *  바텀 시트뷰에 추가해줄 리사이클러뷰(미용실 목록)
     */

    private val shopRecyclerView: RecyclerView by lazy {
        findViewById(R.id.shopof_designer_recyclerView)
    }

    /** 바텀 시트뷰 미용실 개수 표시 타이틀
     *
     */
    private val bottomsheetTitleTextView: TextView by lazy {
        findViewById(R.id.bottomSheetTitleTextView)
    }

    /**
     * 미용실 리스트
     */
    private var salonList = mutableListOf<ShopListItem>()
    private var markerList = mutableMapOf<String, ShopListItem>()

    //어답터
    private val shopListOfDesignerAdapter = ShopListOfDesignerAdapter { shopListItem ->
        this.let {
            val intent = Intent(this, ReserveSalonActivity::class.java)
            //디자이너 객체 전송
            intent.putExtra(DESIGNER_DATA_KEY, designerData)
            //선택한 메뉴 객체 전송
            intent.putExtra("SESLECTED_MENU_DATA_KEY", menu)
            //메뉴선택메뉴의 옵션 전송(기장 추가)
            intent.putExtra("lengthOption", lengthOption)
            startActivity(intent)
        }
    }

    //디자이너 아이디
    var designerId: String? = null


//     DB 레퍼런스

    var shopsDB: DatabaseReference? = null

    var REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION)

    //    mapPointGeo?.latitude, mapPointGeo?.longitude
    var latitude: Double = 0.0   //위도
    var longitude: Double = 0.0  //경도


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shops_of_designer)



        getDataFromIntent()
        setDataReferences()

        /**
         * 리사이클러뷰 어답터 설정
         */
        initAdapter()

        //데이터를 어답터에 업데이트
        getAndSetShop(shopsDB, shopListOfDesignerAdapter)

        /**
         * 맵뷰 추가
         */

        initMapView()


    }

    override fun onCurrentLocationUpdate(p0: MapView?, p1: MapPoint?, p2: Float) {
        var mapPointGeo: MapPoint.GeoCoordinate? = p1?.mapPointGeoCoord
        Log.d(
            SetPositionActivity.LOG_TAG, String.format(
                "MapView onCurrentLocationUpdate (%f,%f) accuracy (%f)",
                mapPointGeo?.latitude,
                mapPointGeo?.longitude,
                p2
            )
        )



        latitude = mapPointGeo!!.latitude //위도 값 업데이트
        longitude = mapPointGeo!!.longitude  //경도 값 업데이트
    }

    override fun onCurrentLocationDeviceHeadingUpdate(p0: MapView?, p1: Float) {

    }

    override fun onCurrentLocationUpdateFailed(p0: MapView?) {

    }

    override fun onCurrentLocationUpdateCancelled(p0: MapView?) {

    }


    override fun onDestroy() {
        super.onDestroy()
        //추적 모드 중지
        mapView.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView.setShowCurrentLocationMarker(false)
        //MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading

    }

    /**
     * 마커 찍기
     */

    private fun updateMarker(shop: ShopListItem) {


        val marker = MapPOIItem()
        markerList.put(shop.name, shop)
        marker.apply {

            itemName = shop.name   // 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(shop.latitude, shop.longitude)   //  미용실좌표
            tag = 0
            markerType = MapPOIItem.MarkerType.CustomImage          // 마커 모양 (커스텀)
            customImageResourceId = R.drawable.place_marker            // 커스텀 마커 이미지
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage  // 클릭 시 마커 모양 (커스텀)
            customSelectedImageResourceId = R.drawable.clicked_marker       // 클릭 시 커스텀 마커 이미지
            isCustomImageAutoscale = false     // 커스텀 마커 이미지 크기 자동 조정
            setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점

            Log.d("markerOn", "marking")
            mapView.addPOIItem(marker)
        }
    }


    //맵뷰 설정
    fun initMapView() {
        //Activity 의 content-view 에 삽입하면 지도화면을 손쉽게 구현
        val mapViewContainer: ViewGroup = findViewById(R.id.shopof_designer_mapview)

        //맵 사용
        mapView = MapView(this) //net.daum.mf.map.api.MapView 객체를 생성
        mapView.setCurrentLocationEventListener(this)
        mapView.setDaumMapApiKey("API_KEY");

        mapViewContainer.addView(mapView)

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting()

        } else {
            checkRunTimePermission()
        }

        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true)
        mapView.setZoomLevel(2, true)    //작을수록 가까움
        mapView.zoomIn(true)
        mapView.zoomOut(true)
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

            //위치값을 가져올 수 있음
            mapView.currentLocationTrackingMode =
                MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading


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
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.size == REQUIRED_PERMISSIONS.size) {
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
                mapView.currentLocationTrackingMode
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

    /**
     * 수신 인텐트로 전달받은 디자이너 정보 저장
     */
    private fun getDataFromIntent() {
        val extras = intent.extras  // 송신 액티비티가 보낸 데이터 참조
        if (extras == null) {
            finish()
        }

        designerData = extras!!.getParcelable(DESIGNER_DATA_KEY)   // DesignerData 객체 읽기
        menu = extras!!.getParcelable("SESLECTED_MENU_DATA_KEY") //선택한 메뉴 객체 읽기
        lengthOption = extras.getString("lengthOption").toString()//선택한 옵션

        if (designerData == null) { //디자이너 객체가 없다면 종료
            finish()
        }
    }

    /**
     * 디자이너 아이디로 DB Reference 설정
     */
    private fun setDataReferences() {
        // 디자이너의 데이터 참조
        //DB 사용할 레퍼런스 초기화 (designerPriceChart -> 디자이너 Id -> 메뉴들)
        designerId = designerData?.designerId.toString();

        //디자이너 아이디
        shopsDB = databaseReference.child("DesignerShops").child(designerId!!)
    }

    /**
     * 리사이클러뷰 어답터 설정
     */
    private fun initAdapter() {
        //미용실 목록 리사이클러뷰 어답터 설정
        shopRecyclerView.adapter = shopListOfDesignerAdapter
        shopRecyclerView.layoutManager = LinearLayoutManager(this)
        shopListOfDesignerAdapter.clearList()
        salonList.clear()
    }

    /**
     *  데이터를 어답터에 업데이트
     */
    private fun getAndSetShop(
        menuReference: DatabaseReference?,
        Adapter: ShopListOfDesignerAdapter
    ) {
        menuReference!!.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach {
                    val model = it.getValue(ShopListItem::class.java)
                    model ?: return

                    //DB에 변화가 생긴다면

                    Adapter.addData(model)//어답터에 모델 추가
                    salonList.add(model)    //미용실 리스트에 모델추가 (마커찍기 메소드에 넣어줄 리스트)
                    Log.d("markerOn", "item is ${salonList.get(0).name}")

                    /**
                     * 지도에 마커 찍기
                     */
                    updateMarker(model) //마커 추가해주기
                }
                shopListOfDesignerAdapter.notifyDataSetChanged()// 뷰목록 업데이트
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }

    companion object {
        val PERMISSION_REQUEST_CODE = 100
        val GPS_ENABLE_REQUEST_CODE = 2001

        val LOG_TAG = "MainActivity"
    }

    //마커 클릭 관련인터페이스 implement
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {

    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {

    }

    //마커 풍선 클릭 시 다음 액티비티로 이동
    override fun onCalloutBalloonOfPOIItemTouched(
        p0: MapView?,
        p1: MapPOIItem?,
        p2: MapPOIItem.CalloutBalloonButtonType?
    ) {
        val intent = Intent(this, ReserveSalonActivity::class.java)
        //전달받은 MapPOIItem의 이름으로 가진 미용실객체를 맵에서 꺼내옴
        selectedShop = markerList.get(p1!!.itemName)

        intent.putExtra("selectedShop", selectedShop)

        //디자이너 객체 전송
        intent.putExtra(DESIGNER_DATA_KEY, designerData)
        //선택한 메뉴 객체 전송
        intent.putExtra("SESLECTED_MENU_DATA_KEY", menu)
        //메뉴선택메뉴의 옵션 전송(기장 추가)
        intent.putExtra("lengthOption", lengthOption)
        startActivity(intent)

    }

    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {

    }



}