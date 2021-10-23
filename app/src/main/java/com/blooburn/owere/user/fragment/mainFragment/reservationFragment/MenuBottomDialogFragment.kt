package com.blooburn.owere.user.fragment.mainFragment.reservationFragment

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import com.blooburn.owere.R
import com.blooburn.owere.user.activity.main.userReservation.ShopsOfDesignerActivity
import com.blooburn.owere.user.item.StyleMenuItem
import com.blooburn.owere.util.DESIGNER_DATA_KEY
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

//메뉴 선택 후 길이추가 커스텀 다이얼로그
class MenuBottomDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.menu_bottom_dialog, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var bundle = bundleOf()
        bundle.getParcelable<Parcelable>(DESIGNER_DATA_KEY)

        // 선택완료 버튼
        var choiceButton = view.findViewById<TextView>(R.id.menu_bottom_complete_button)


        // 선택완료 버튼 리스너
        choiceButton.setOnClickListener {
            // 디자이너 설정 미용실 액티비티로 이동
            val intent = Intent(requireContext(), ShopsOfDesignerActivity::class.java)
            startActivity(intent)
        }

    }
}