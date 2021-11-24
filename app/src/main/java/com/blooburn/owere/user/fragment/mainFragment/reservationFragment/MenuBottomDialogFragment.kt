package com.blooburn.owere.user.fragment.mainFragment.reservationFragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.main.userReservation.ShopsOfDesignerActivity
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.user.item.DesignerItem
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//메뉴 선택 후 길이추가 커스텀 다이얼로그
class MenuBottomDialogFragment : BottomSheetDialogFragment() {
    private var designerData: DesignerItem? = null//프로필에서 전달받을 디자이너 객체
    private var menu :StyleMenuItem? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.menu_bottom_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //디자이너 객체 받기
        designerData = arguments?.getParcelable<Parcelable>(DESIGNER_DATA_KEY) as DesignerItem?
        //번들로 하는 거 아님 실수 주의
        //designerData = bundle.getParcelable<Parcelable>(DESIGNER_DATA_KEY) as DesignerItem?
        //menu = bundle.getParcelable<Parcelable>("SESLECTED_MENU_DATA_KEY") as StyleMenuItem?
        menu = arguments?.getParcelable<Parcelable>("SESLECTED_MENU_DATA_KEY") as StyleMenuItem?
        Log.d(DESIGNER_DATA_KEY,designerData!!.name)
        Log.d("SESLECTED_MENU_DATA_KEY",menu!!.menuName)

        //전달해줄 기장 옵션
        var lengthOption :String = "기본+0"








        // 선택완료 버튼
        val choiceButton = view.findViewById<TextView>(R.id.menu_bottom_complete_button)
        choiceButton.visibility = GONE

        val radiogroup = view.findViewById<RadioGroup>(R.id.menu_bottom_radiogroup)
        radiogroup.setOnCheckedChangeListener {group, checkedId ->
            when(checkedId){
                R.id.menuBottomRadio_basic -> lengthOption ="기본+0"
                R.id.menuBottomRadio_chin -> lengthOption ="턱선 아래+10,000"
                R.id.menuBottomRadio_shoulder -> lengthOption ="어깨선 아래+20,000"
                R.id.menuBottomRadio_chest -> lengthOption ="가슴선 아래+30,000"
            }
            choiceButton.visibility = VISIBLE

        }

        // 선택완료 버튼 리스너
        choiceButton.setOnClickListener {
            // 디자이너 설정 미용실 액티비티로 이동
            val intent = Intent(requireContext(), ShopsOfDesignerActivity::class.java)
            //디자이너 객체 전송
            intent.putExtra(DESIGNER_DATA_KEY,designerData)
            //선택한 메뉴 객체 전송
            intent.putExtra("SESLECTED_MENU_DATA_KEY",menu)
            //메뉴선택메뉴의 옵션 전송(기장 추가)
            intent.putExtra("lengthOption",lengthOption)
            startActivity(intent)
        }
    }
}