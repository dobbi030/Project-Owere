package com.blooburn.owere.designer.fragment.home


import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup

import androidx.core.content.ContextCompat

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.DesignerConfirmedReservationFragmentBinding
import com.blooburn.owere.designer.adapter.home.DesignerReservationListAdapter
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.user.activity.signUpActivity.SetPositionActivity
import com.blooburn.owere.user.item.ShopListItem
import com.blooburn.owere.util.CustomDividerDecoration
import com.blooburn.owere.util.TypeOfReservation
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import org.threeten.bp.temporal.ChronoField
import java.util.*

class DesignerConfirmedReservationFragment :
    Fragment(R.layout.designer_confirmed_reservation_fragment) ,MapView.CurrentLocationEventListener {


    private val tempDesignerId = "designer0"
    private val reservationsReferencePath = "designerReservations/$tempDesignerId/"
    private var binding: DesignerConfirmedReservationFragmentBinding? = null


    //net.daum.mf.map.api.MapView 객체를 생성
    private var mapView : MapView?  = null
    var latitude: Double = 0.0   //위도
    var longitude: Double = 0.0  //경도


    //디자이너에게 예약된 미용실 이름 리스트(마커찍기에 사용)
    private var salonList = mutableListOf<String>()
    //해당 미용실 이름, DB에서 주소를 가지고있는 미용실 객체들 배열
    private var salonMap = mutableMapOf<String,ShopListItem>()

    //추가되는 마커들을 배열에 저장. 마커 삭제할 때 용이
    private var markerList = mutableListOf<MapPOIItem>()
    private val shopReferencePath = "DesignerShops/$tempDesignerId/" //디자이너가 근무가능한 미용실 목록


    private lateinit var scheduledAdapter: DesignerReservationListAdapter
    private var scheduledList = mutableListOf<DesignerReservation>()
    private var completedList = mutableListOf<DesignerReservation>()
    private lateinit var completedAdapter: DesignerReservationListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding = DesignerConfirmedReservationFragmentBinding.bind(view)

        // 예정된 예약 리사이클러뷰 초기화
        scheduledAdapter = DesignerReservationListAdapter()
        initReservationsRecyclerView(
            binding?.recyclerDesignerConfirmedReservationScheduled!!,
            scheduledAdapter
        )

        // 완료된 예약 리사이클러뷰 초기화
        completedAdapter = DesignerReservationListAdapter()
        initReservationsRecyclerView(
            binding?.recyclerDesignerConfirmedReservationCompleted!!,
            completedAdapter
        )

        // 달력
        binding?.calendarDesignerConfirmedReservation?.currentDate = CalendarDay.today()
        binding?.calendarDesignerConfirmedReservation?.setOnDateChangedListener(dateSelectedListener)



        //맵뷰 사용
        initMapView()
        getMySalonInfo()


    }

    /**
     * 예약 리사이클러뷰 초기화
     */
    private fun initReservationsRecyclerView(
        recyclerView: RecyclerView,
        _adapter: DesignerReservationListAdapter
    ) {
        recyclerView.apply {
            // custom divider 적용
            addItemDecoration(
                CustomDividerDecoration(1f, ContextCompat.getColor(this.context, R.color.gray_939393))
            )

            layoutManager = LinearLayoutManager(context)
            adapter = _adapter
        }
    }

    /**
     * 달력 클릭 리스너
     */

    private val dateSelectedListener =
        OnDateSelectedListener { widget, date, isSelected ->


            if (isSelected) {
                updateTodayText(date.date)  // 오늘 날짜 UI 업데이트

                val selectedDateStamp = date.date.getLong(ChronoField.EPOCH_DAY) // 선택된 날짜 스탬프
                val referencePathOfSelectedDay =
                    reservationsReferencePath + selectedDateStamp   // 해당 날짜의 DB 주소


                //날짜를 바꿀 때 남아있는 미용실 리스트 초기화
                salonList.clear()
                mapView!!.removeAllPOIItems()
                markerList.clear()



                // 선택 날짜의 예약 목록들을 DB에서 불러와서 업데이트한다
                loadAndSetReservationsFromDB(referencePathOfSelectedDay, selectedDateStamp)
                // 예약 개수 나타내는 UI 업데이트
                updateCountOfReservations(scheduledList.size, completedList.size)
            }


        }

    /**
     * 선택 날짜의 예약 목록들을 DB에서 불러온다
     */
    private fun loadAndSetReservationsFromDB(path: String, selectedDayStamp: Long) {
        databaseInstance.reference.child(path)
            .addListenerForSingleValueEvent(object : ValueEventListener {


                override fun onDataChange(snapshot: DataSnapshot) {

                    scheduledList = mutableListOf()
                    completedList = mutableListOf()
                    val currentTime = LocalTime.now().toSecondOfDay()





                    snapshot.children.forEach { reservationSnapshot ->
                        val reservation =
                            reservationSnapshot.getValue(DesignerReservation::class.java)
                        reservation?.userId = reservationSnapshot.key ?: "" // key = userId




                        // 예정된, 정산할, 정산된 예약 분류
                        if (reservation != null) {
                            sortReservation(reservation, selectedDayStamp, currentTime)

                            //지도에 미용실을 띄워주기위해서 예약된 미용실 이름을 배열에 추가
                            salonList.add(reservation.shop)
                        }
                    }



                    scheduledAdapter.setData(selectedDayStamp, scheduledList)
                    completedAdapter.setData(selectedDayStamp, completedList)
                    // 예약 개수 나타내는 UI 업데이트
                    updateCountOfReservations(scheduledList.size, completedList.size)
                }

                override fun onCancelled(error: DatabaseError) {}
            })
    }

    /**
     * 예약 불러올 때, 개수 나타내는 text 업데이트
     */
    private fun updateCountOfReservations(scheduledCount: Int, completedCount: Int) {
        binding?.textDesignerConfirmedReservationScheduledCount?.text = if (scheduledCount > 0)
            getString(R.string.total_reservation_count, scheduledCount) else ""
        binding?.textDesignerConfirmedReservationCompletedCount?.text = if (completedCount > 0)
            getString(R.string.total_reservation_count, completedCount) else ""


        //날짜를 클릭 할 때마다 미용실 리스트 갱신

        addSalonOnMap()
    }

    /**
     * 오늘 월일 텍스트 업데이트
     */
    private fun updateTodayText(date: LocalDate) {
        binding?.textDesignerConfirmedReservationToday?.text =
            getString(R.string.text_today, date.monthValue, date.dayOfMonth)
    }

    private fun sortReservation(
        reservation: DesignerReservation,
        selectedDayStamp: Long,
        currentTime: Int
    ) {
        when(reservation.type){
            TypeOfReservation.SCHEDULED.value -> scheduledList.add(reservation)
            TypeOfReservation.ACCEPTED.value -> return
            else -> completedList.add(reservation)
        }
        /*
        // 선택된 날이 과거일 때
        if (selectedDayStamp < currentDayStamp) {
            reservation.type = TypeOfDesignerReservation.COMPLETED
            completedList.add(reservation)
        }
        // 미래일 때
        else if (currentDayStamp < selectedDayStamp) {
            scheduledList.add(reservation)
        } else {
            // 시술 끝나는 시간이 현재 시간을 지났을 때
            if (currentTime < reservation.endTime) {
                reservation.type = TypeOfDesignerReservation.COMPLETED
                completedList.add(reservation)
            } else {
                scheduledList.add(reservation)
            }
        }

         */



    }

    //맵뷰 설정
    private fun initMapView() {

        //view는 하나의 부모 뷰에만 추가될 수 있음. 이미 존재한다면 부모뷰 제거(스피너로 프래그먼트 변경시 발생하는 에러)
        if (mapView?.parent != null){
            (mapView?.parent as ViewGroup).removeAllViews()
        }
        mapView = MapView(requireContext())


        //Activity 의 content-view 에 삽입하면 지도화면을 손쉽게 구현
        val mapViewContainer = binding?.mapViewDesignerHome
            //findViewById(R.id.map_view_designer_home)

        //맵 사용
//        mapView = MapView(Activity()) //net.daum.mf.map.api.MapView 객체를 생성
        mapView!!.setCurrentLocationEventListener(this)
        mapView!!.setDaumMapApiKey("API_KEY");

        if (mapViewContainer != null) {
            mapViewContainer.addView(mapView)
        }
        //위치값을 가져올 수 있음
        mapView?.currentLocationTrackingMode =
            MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading

        //권한체크는 첫화면에서
//        if (!checkLocationServicesStatus()) {
//            showDialogForLocationServiceSetting()
//
//        } else {
//            checkRunTimePermission()
//        }

        mapView!!.apply {
            setMapCenterPoint(MapPoint.mapPointWithGeoCoord(latitude, longitude), true)
            setZoomLevel(2, true)    //작을수록 가까움
            zoomIn(true)
            zoomOut(true)
        }


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
        mapView?.currentLocationTrackingMode = MapView.CurrentLocationTrackingMode.TrackingModeOff
        mapView?.setShowCurrentLocationMarker(false)
        //MapView.CurrentLocationTrackingMode.TrackingModeOnWithHeading

    }
    /**
     * 예약된 미용실 정보들 조회
     */
    private fun addSalonOnMap(){


        //디자이너에게 예약된 미용실 이름 리스트(마커찍기에 사용)
//        private var salonList = mutableListOf<String>()
//        //해당 미용실 이름을 사용하여 DB에서 주소를 가지고있는 미용실 객체들 배열
//        private var markerList = mutableMapOf<String, ShopListItem>()

        //예약된 미용실 이름 목록중 중복되는 이름들 제거 distinct()
        salonList.distinct()


        //예약된 미용실 이름들을 이용하여 맵 컬렉션에 저장된 미용실들을 조회
        salonList.forEach { salon ->
            var salonItem = salonMap.get(salon)
            //해당 미용실의 좌표로 마커 추가
            if(salonItem != null)
            updateMarker(salonItem!!)
        }



    }

    private fun getMySalonInfo(){
        //디자이너가 속한 미용실 정보들을 DB에서 가져옴
        databaseInstance.reference.child(shopReferencePath).addListenerForSingleValueEvent(object :ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach{snapshotChildren ->
                    var model = snapshotChildren.getValue(ShopListItem::class.java)
                    salonMap.put(model!!.name,model) //미용사가 속한 미용실을 맵컬렉션에 추가(위치정보를 조회하기 위함)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }


    /**
     * 지도에 마커 추가
     */
    //날짜 갱신할 때마다 지도위의 마커 초기화 필요
    private fun updateMarker(shop: ShopListItem) {


        val marker = MapPOIItem()
        markerList.add(marker)


        marker.apply {
            itemName = shop.name   // 마커 이름
            mapPoint = MapPoint.mapPointWithGeoCoord(shop.latitude, shop.longitude)   //  미용실좌표
            tag = 0
            markerType = MapPOIItem.MarkerType.CustomImage          // 마커 모양 (커스텀)
            customImageResourceId = R.drawable.reserved_place_marker            // 커스텀 마커 이미지
            selectedMarkerType = MapPOIItem.MarkerType.CustomImage  // 클릭 시 마커 모양 (커스텀)
            customSelectedImageResourceId = R.drawable.clicked_marker       // 클릭 시 커스텀 마커 이미지

            isCustomImageAutoscale = false     // 커스텀 마커 이미지 크기 자동 조정

            setCustomImageAnchor(0.5f, 1.0f)    // 마커 이미지 기준점

            Log.d("markerOn", "marking")
            mapView?.addPOIItem(marker)
        }
    }


}