package com.blooburn.owere.user.activity.main.userReservation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blooburn.owere.R
import com.blooburn.owere.databinding.ActivityReserveTimeBinding
import com.blooburn.owere.designer.item.DesignerReservation
import com.blooburn.owere.user.adapter.userReservation.ReserveTimeAdapter
import com.blooburn.owere.user.item.ShopListItem
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.DesignerItem
import com.blooburn.owere.util.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalTime
import kotlin.time.hours


//예약하기 액티비티 (날짜, 시간 예약)
class ReserveTimeActivity : AppCompatActivity(), DesignerProfileHandler {

    //프로필에서 전달받을 디자이너 객체
    private lateinit var designerData: DesignerItem

    //전달받을 선택한 메뉴
    private lateinit var menu: StyleMenuItem

    //전달받을 기장 옵션
    private var lengthOption: String = ""

    //전달받을 선택할 미용실
    private lateinit var selectedShop: ShopListItem  //선택할 미용실

    private lateinit var selectedTime: String// 예약 시간
    private lateinit var reservedDate: String //예약날짜

    //날짜 시간 예약 레이아웃 바인딩
    private var binding: ActivityReserveTimeBinding? = null


    private var tempEpochDay = 0L


    private val databaseReference = databaseInstance.reference
    private val storageReference = storageInstance.reference
    private lateinit var designerReference: DatabaseReference

    //시간 선택 버튼 리사이클러뷰
    private val timeTabRecyclerView: RecyclerView by lazy {
        findViewById(R.id.time_button_recyclerview)
    }


    //체크버튼 단일 선택 가능하도록 구현한 어답터
    private val timeTabAdapter = ReserveTimeAdapter(object : SelectTimeInterface {

        //시간 선택 버튼들 배열
        override var checklist: MutableList<CheckBox> = mutableListOf()

        //선택한 시간 버튼
        override var reserveTime: CheckBox? = null

        override fun addTime(timeButton: CheckBox) {

            if (checklist == null) {
                checklist = mutableListOf()
            }

            checklist.add(timeButton)
        }


    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReserveTimeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        initDataAndView()  // 모든 뷰의 데이터 초기화 작업

        showTimeButton()//예약 시간 선택 버튼

        initButton()//시

    }

    private fun initDataAndView() {
        // 수신 인텐트로 전달받을 정보 할당
        getDataFromIntent()
        setDataReferences()// 디자이너 아이디로 DB에서 데이터들 Reference 설정
        setDesignerInformation()

        timeTabRecyclerView.adapter = timeTabAdapter
        timeTabRecyclerView.layoutManager = GridLayoutManager(this, 4)
        timeTabAdapter.clearData()

    }

    /**
     * 디자이너 아이디로 DB Reference 설정
     */
    private fun setDataReferences() {
        // 디자이너의 데이터 참조
        designerReference = databaseReference.child("Designers/${designerData?.designerId}")
        // 가격표 참조
//        priceChartReference =
//            databaseReference.child("designerPriceChart/${designerData?.designerId}")
    }

    /**
     * 디자이너 프로필 set
     */
    private fun setDesignerInformation() {
        if (designerData == null) {
            return
        }


        val reviewStar = convertRatingToStar(designerData!!.rating)
        // 디자이너 프로필 보여주는 view
        findViewById<View>(R.id.reserve_activity_designer_profile).apply {
            bindProfileImage(
                this,
                this.findViewById(R.id.image_user_designer),
                designerData!!.profileImagePath,
                true
            )
            this.findViewById<TextView>(R.id.text_user_designer_name).text =
                designerData!!.name
            this.findViewById<TextView>(R.id.text_user_designer_area).text =
                designerData!!.area
            this.findViewById<TextView>(R.id.text_user_designer_matching).text =
                getString(R.string.matching_rate, designerData?.matchingRate)

            this.findViewById<TextView>(R.id.text_user_designer_star).text = reviewStar

            this.findViewById<TextView>(R.id.text_user_designer_review_count).text =
                designerData!!.reviewCount.toString()

        }


        // 리뷰 별점, 평점, 개수
//        findViewById<TextView>(R.id.text_user_designer_profile_review_star).text = reviewStar
//        findViewById<TextView>(R.id.text_user_designer_profile_review_rating).text =
//            designerData!!.rating.toString()
//        findViewById<TextView>(R.id.text_user_designer_profile_review_count).text =
//            designerData!!.reviewCount.toString()
    }

    private fun initButton() {
        var keepButton = findViewById<TextView>(R.id.reserve_time_keepbutton)
        keepButton.setOnClickListener {

            selectedTime = timeTabAdapter.selectedTime?.text.toString()


            if (selectedTime == null) {
                Toast.makeText(this, "예약시간을 선택해주세요.", Toast.LENGTH_LONG)
            } else {
                val intent = Intent(this, UserWaitingReservation::class.java)
                //디자이너 객체 전송

                intent.putExtra(DESIGNER_DATA_KEY, designerData)
                //선택한 메뉴 객체 전송
                intent.putExtra("SESLECTED_MENU_DATA_KEY", menu)
                //메뉴선택메뉴의 옵션 전송(기장 추가)
                intent.putExtra("lengthOption", lengthOption)
                //선택한 미용실 보내기
                intent.putExtra("selectedShop", selectedShop)
                //선택한 시간
                intent.putExtra("selectedTime", selectedTime)
                //예약 날짜
                intent.putExtra("reservedDate", reservedDate) //1241

                intent.putExtra(
                    "reservationId",
                    "${designerData.designerId}@Time${LocalTime.now()}"
                )
                startActivity(intent)
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

        designerData = extras!!.getParcelable(DESIGNER_DATA_KEY)!!   // DesignerData 객체 읽기
        menu = extras!!.getParcelable("SESLECTED_MENU_DATA_KEY")!! //선택한 메뉴 객체 읽기
        lengthOption = extras.getString("lengthOption").toString()//선택한 옵션
        selectedShop = extras.getParcelable("selectedShop")!!// 선택한 미용실 객체읽기

        if (designerData == null) { //디자이너 객체가 없다면 종료
            finish()
        }
    }

    //디자이너와 미용실의 각각의 예약 내역 DB 조회
    //24시간중 가능한 좌석 예약시간 생성
    // ->현재시간 이전시간 제외, 미용실 영업시간 이외 시간 제외, 미용실 좌석 예약된 시간 제외, 디자이너 예약된 시간 제외

    private fun showTimeButton() {
        // 0시 ~24시를 나타내는 30분 간격으로 자리개수를 나타낸 48개 배열

        // ex) 미용실 예약가능 초기 좌석 3개 -> [00시00분] 3개, [00시30분] 3개, ...
        // { 3,3,3,3,3,3,3,3,3 }
//        var timeArray = arrayOf<Int>(48)

        var timeArray = IntArray(49, { 3 })//자리 3개씩 24시간 30분단위 배정

        //현재시간의 timestamp
        val currentTime = LocalTime.now().toSecondOfDay()
//        현재 시간
        val nowHour = LocalTime.now().hour
        val nowMinute = LocalTime.now().minute

        //미용실 운영시간
        val openTime: Int = (selectedShop.openTime / 3600).toInt()
        val closeTime = (selectedShop.closeTime / 3600).toInt()

//


        /**
        예약객체 DB에서 조회 -> ex) (Long 타입)14:00~14:30, (Long 타입)15:30~16:00, (Long 타입)16:00~17:00
        timeArray[start시간]~ timeArray[end시간] -> timeArray[i]-- 자리 1씩 감소
        디자이너 예약 DB와 미용실 예약 DB둘다 조회
         **/

        //예약된 시간 확인
        val reservationsReferencePath = "designerReservations/${designerData?.designerId}/"

        //캘린더 현재 시간 설정
        binding?.reserveCalendarView!!.currentDate = CalendarDay.today()

        //달력
        binding?.reserveCalendarView!!.setOnDateChangedListener { calendarView, date, isSelected ->

            if (isSelected) {//날짜 선택 하였을 때

                //미용실 좌석 디폴트값 1
                timeArray = IntArray(49, { 1 })
                timeTabAdapter.clearData()

                //선택 날짜 값
                tempEpochDay = date.date.getLong(org.threeten.bp.temporal.ChronoField.EPOCH_DAY)
                reservedDate = tempEpochDay.toString()//넘겨주기 위하여  날짜 할당
                //만약 선택한 날짜와 현재 날짜와 같은 날일 경우
                if (LocalDate.now()
                        .getLong(org.threeten.bp.temporal.ChronoField.EPOCH_DAY) == tempEpochDay
                ) {

                    //오늘인 경우
                    //1. 현재 이전시간의 좌석 예약불가

                    for (i in 0..nowHour * 2) {
                        //현재시간 이전의 이용가능 좌석 개수 0으로 초기화
                        //ex)
                        //00시 ->timeArray[0], 00시 30분 -> timeArray[1]
                        //01시 ->timeArray[2], 01시 30분 -> timeArray[3]
                        //02시 ->timeArray[4], 02시 30분 -> timeArray[5]

                        //ex) 현재 14시 일 때 timeArray[28]까지 0개

                        timeArray[i] = 0
                    }

                    //30분인 경우
                    if (nowMinute != 0) {
                        timeArray[(nowHour * 2) + 1] = 0
                    }
                }
                // 과거인 경우 이용가능 시간대 좌석 0으로 초기화
                if (LocalDate.now()
                        .getLong(org.threeten.bp.temporal.ChronoField.EPOCH_DAY) > tempEpochDay
                ) {
                    timeArray = IntArray(49, { 0 })
                }
                val referencePathOfSelectedDay = reservationsReferencePath + tempEpochDay

                //designerReservations/${designerData?.designerId + tempEpochDay
                databaseInstance.reference.child(referencePathOfSelectedDay)
                    .addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            //들어온 예약 리스트 DB 조회

                            // 반환된 값은 0에서 24 * 60 * 60 – 1 사이 *1000
                            snapshot.children.forEach { reservationSnapshot ->
                                val reservation =
                                    reservationSnapshot.getValue(DesignerReservation::class.java)

                                Log.d(
                                    "시간",
                                    "currentTime: $currentTime, endTime: ${reservation?.endTime!!}"
                                )
                                //reservation.startTime
                                // 이거 시간 분 값 얻은 후 배열로 바꿔줌
                                var startHour = (reservation.startTime / 3600).toInt()
                                Log.d("time start is", " start is$startHour")

                                var startMin = 0
                                if (reservation.startTime % 3600 != 0L) {
                                    startMin = 1
                                }

                                var endHour = (reservation.endTime / 3600).toInt()
                                var endMin = 0

                                if (reservation.endTime % 3600 != 0L) {
                                    endMin = 1
                                }
                                Log.d("time start is", " end is$endHour min $endMin")


                                //예약되어있는 시간의 자리 수 1씩 감소
                                for (i in ((startHour * 2) + startMin)..((endHour * 2) + endMin)) {
                                    if (timeArray[i] != 0) {
                                        timeArray[i] = timeArray[i]-1
                                    }else{
                                        continue
                                    }

                                }


                            }

                            //  {0,0,0,0,0,0,0,0,0,0,0,0,0,,0,0,0,0,0,0,3,3,2,2,23,2,1,1,2,3,3,,0,0,0,0,0,00,1,20,0,0,0,0,0,0,}


                            //오픈시간 이전, 마감시간 이후 자리 0으로 처리
                            for (i in 0..(openTime * 2)-1) {
                                timeArray[i] = 0
                            }
                            for (i in (closeTime * 2)..47) {
                                timeArray[i] = 0
                            }
                            //최종
                            //자리가 0이 아닌 시간대 배열에서 조회
                            //짝수 일 때(정시)


                            for (i in 0 until 48 step 2) {
                                if (timeArray[i] != 0) {

                                    /*
                                        i/2 시 자리 있음 리사이클러뷰 어답터에 넘겨줌 -> i/2 시 예약 버튼 생성
                                        ex) timeArray[6] -> 3시 자리 있음
                                     */

                                    timeTabAdapter.addData(String.format("%02d", i / 2) + ":00")

                                }

                            }
                            //홀수 일 때(00시30분일 때)
                            for (i in 1 until 48 step 2) {
                                if (timeArray[i] != 0) {
                                    /*
                                        i/2 시 30분 자리 있음 -> 리사이클러뷰 어답터에 넘겨줌 -> i/2 시 30분 예약 버튼 생성0.
                                        ex) timeArray[7] -> 3시 30분 자리 있음

                                     */
                                    timeTabAdapter.addData(String.format("%02d", i / 2) + ":30")
                                }


                            }
                            timeTabAdapter.notifyDataSetChanged()




                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })


            }
        }


    }


}

//단일 선택구현을 위한 인터페이스
interface SelectTimeInterface {
    var checklist: MutableList<CheckBox>
    var reserveTime: CheckBox?
    fun addTime(timeButton: CheckBox)

}