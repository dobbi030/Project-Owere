package com.blooburn.owere.user.activity.main.userReservation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.blooburn.owere.databinding.ActivityUserWaitingReservationBinding
import com.blooburn.owere.user.activity.main.UserMainActivity
import com.blooburn.owere.user.item.ShopListItem
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.UserDesignerItem
import com.blooburn.owere.user.item.UserEntity
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.blooburn.owere.util.TypeOfReservation
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

//예약 신청 완료
//예약 정보 보여주기 액티비티(예약대기중) -> 홈으로 이동
class UserWaitingReservation : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth    //파이어베이스 인증 사용

    //프로필에서 전달받을 디자이너 객체
    private lateinit var designerData: UserDesignerItem

    //전달받을 선택한 메뉴
    private lateinit var menu: StyleMenuItem

    //전달받을 기장 옵션
    private var lengthOption: String = ""

    //전달받을 선택할 미용실
    private lateinit var selectedShop: ShopListItem  //선택할 미용실

    private lateinit var selectedTime: String //예약한 시간
    private lateinit var reservedDate: String //예약한 날짜

    // 예약대기 레이아웃 바인딩
    private lateinit var binding: ActivityUserWaitingReservationBinding

    private lateinit var reservationId : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //유저 정보를 접근하기위한 인증 인스턴스
        auth = Firebase.auth



        binding = ActivityUserWaitingReservationBinding.inflate(layoutInflater)
        setContentView(binding.root)


        getDataFromIntent() //수신 인텐트로 전달받은 디자이너 정보 저장



        updateDataReservation()//수신 인텐트로 전달받은 디자이너 정보 저장
        setReservationData() //전달받은 예약정보를 뷰에 적용

        initButton()


    }

    private fun initButton() {
        binding.waitingReservationGohomeButton.setOnClickListener {

            val intent = Intent(this,UserMainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            //FLAG_ACTIVITY_CLEAR_TOP 사용하여 홈 액티비티가 호출 될 시 그 위에 있던 액티비티 모두 삭제
            startActivity(intent)
        }
    }

    /**
     * 전달받은 예약정보를 뷰에 적용
     */
    private fun setReservationData() {
        binding.apply {
            reservationTimeText.text = selectedTime//00:00
            reservationDateText.text = getDateTime(reservedDate) //18948
            reservationDesignerText.text = designerData.name //김철수
            reservationShopText.text = selectedShop.name    //00미용실
            reservationMenuText.text = menu.menuName    //메뉴이름
            reservationPriceText.text = menu.menuPrice  //000원

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
        selectedTime = extras.getString("selectedTime").toString()//예약 시간 받기

        reservedDate = extras.getString("reservedDate").toString()//예약 날짜 받기
        reservationId = extras.getString("reservationId").toString()//유저 예약내역 PrimaryKey
        //날짜 필요
        if (designerData == null) { //디자이너 객체가 없다면 종료
            finish()
        }
    }

    /**
     * 수신 인텐트로 전달받은 디자이너 정보 저장
     */
    private fun updateDataReservation() {


        //유저 정보를 조회하기 위한 레퍼런스
        val currentUserDB = databaseInstance.reference.child("Users")

        //해당 날짜 유저 예약내역 레퍼런스
        val ReservationUserDB = databaseInstance.reference.child("UserReservation")
            .child(auth.currentUser!!.uid).child(firebaseStringPathInput())

        //해당 날짜 디자이너 예약내역 레퍼런스
        val ReservationDesignerDB = databaseInstance.reference.child("designerReservations")
            .child(designerData.designerId).child(reservedDate)



        var designerUpdate = mutableMapOf<String, Any>()
        var userUpdate = mutableMapOf<String, Any>()


        currentUserDB
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    //userEntity = snapshot.getValue(UserEntity::class.java)
                    snapshot.children.forEach {
                        val model = it.getValue(UserEntity::class.java)
                        model ?: return
                        //DB에 변화가 생긴다면


                        if(auth!!.currentUser!!.uid == model.userId){
                            designerUpdate["userName"] = model!!.myName


                            designerUpdate["profileImagePath"] = ""

                            designerUpdate["shop"] = selectedShop.name

                            // hour to secondofDay + minute to secondofDay
                            //00:00
                            designerUpdate["startTime"] = hourMinuteToSecondOfDay(selectedTime)

                            //임시로 시작 시간 + 20분으로 해둠
                            designerUpdate["endTime"] = hourMinuteToSecondOfDay(selectedTime) + 60 * 60 * 1000 / 3

                            designerUpdate["accepted"] = 0

                            designerUpdate["type"] = TypeOfReservation.SCHEDULED


                            //유저 Reservation
                            //designerName: "디자이너1"
                            //endTime: 39600000
                            //profileImagePath: "profileImages/users/37mCYRBcd2QRRg6f9LjI3SqIclY..."
                            //shop: "오월 미용실"
                            //startTime: 37800000
                            //userName: "박성준"
                            userUpdate["userName"] = model!!.myName
                            userUpdate["accepted"] = 0 //임시 원래는 0
                            userUpdate["designerName"] = designerData.name

                            userUpdate["endTime"] =
                                reservedDate.toLong()*86400000+ hourMinuteToSecondOfDay(selectedTime) + 60 * 60 / 3 //임시로 시작 시간 + 20분으로 해둠

                            userUpdate["profileImagePath"] = designerData.profileImagePath

                            userUpdate["shop"] = selectedShop.name
                            userUpdate["designerId"] = designerData.designerId

                            userUpdate["startTime"] = reservedDate.toLong()* 86400000+hourMinuteToSecondOfDay(selectedTime)


                            //디자이너 예약내역 DB업데이트
                            ReservationDesignerDB.child(auth.currentUser!!.uid)
                                .updateChildren(designerUpdate)

                            //유저 예약내역 DB업데이트


                            ReservationUserDB
                                .updateChildren(userUpdate)
                        }





                    }



                }

                override fun onCancelled(error: DatabaseError) {

                }
            })





    }

    /**
     * 매개변수로 받은 00:00형식의 시간을 secondOfDay로 변환
     */
    private fun hourMinuteToSecondOfDay(arg: String): Long {
        var hourMinute = arg.split(":") // hour:minute ->  [hour, minute]

        return (hourMinute[0].toLong() * 60 * 60  + hourMinute[1].toLong() * 60)

    }
    /**
     * 매개변수로 받은 00000형식의 날짜를 yyyy년 MM월 dd일로 변환
     */
    private fun getDateTime(s: String): String? {
        try {
            val sdf = SimpleDateFormat("yyyy년 MM월 dd일")
            val netDate = Date(s.toLong()* 86400000L)//날짜 * 하루 밀리세컨드
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }
    /**
     * 파이어베이스 child에 들어갈 경로 (예약 primarykey를 예약 시간 타임스탬프로 설정)
     */
    private fun firebaseStringPathInput() : String{
        return (reservedDate.toLong()* 86400000+hourMinuteToSecondOfDay(selectedTime)).toString()
    }


}


// DesignerConfirmedReservationFragment 에서 사용
//data class DesignerReservation(
//    val userName: String,
//    val profileImagePath: String,
//    val shop: String,
//    val startTime: Long,
//    val endTime: Long,
//    var type: TypeOfDesignerReservation,
//    var accepted : Int // 디자이너 수락여부
//)


