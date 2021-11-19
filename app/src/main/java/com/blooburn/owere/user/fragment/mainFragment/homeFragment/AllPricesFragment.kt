package com.blooburn.owere.user.fragment.mainFragment.homeFragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.AllPricesFragmentBinding
import com.blooburn.owere.databinding.ItemPriceMenuBinding
import com.blooburn.owere.databinding.LayoutMenuContainerBinding
import com.blooburn.owere.designer.activity.main.MenuChangedListener
import com.blooburn.owere.user.item.MenuItem
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AllPricesFragment(
    private val isAdditionTreatment: Boolean,
    _menuChangedListener: MenuChangedListener? = null
) :
    Fragment(R.layout.all_prices_fragment), View.OnClickListener {

    private var binding: AllPricesFragmentBinding? = null
    private val allMenuItemList = mutableListOf<MenuItem>()
    private val checkedMenuItemList = mutableListOf<MenuItem>()

    // 추가 시술 선택 완료 버튼 리스너
    // DesignerReservationDetailActivity.kt와 통신
    private val menuChangedListener = _menuChangedListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = AllPricesFragmentBinding.bind(view).apply {
            root.visibility = View.INVISIBLE
        }

        // 추가 시술 선택 모드인지 or 일반 가격표만 보는 화면인지에 따라 다르게 화면 적용
        when (isAdditionTreatment) {
            true -> initForAdditionalTreatment()
            else -> initForShowingPrices()
        }

        // 모든 가격표(메뉴)를 불러온 후, view를 동적 생성 해서 보여준다
        getAndAddMenuItems()
    }

    /**
     * 가격표만 보는 경우
     */
    private fun initForShowingPrices() {
        binding?.apply {
            layoutAllPricesAdditionalTopBar.visibility = View.GONE    // 추가 시술 화면 상단바
            buttonAllPricesChoose.visibility = View.GONE    // 선택 완료 버튼
            buttonAllPricesBack.setOnClickListener(this@AllPricesFragment)  // 뒤로가기 버튼
        }
    }

    /**
     * 추가 시술 선택하는 경우
     */
    private fun initForAdditionalTreatment() {
        binding?.apply {
            layoutAllPricesTopBar.visibility = View.GONE    // 일반 가격표 화면 상단바
            buttonAllPricesAdditionalBack.setOnClickListener(this@AllPricesFragment)
            buttonAllPricesChoose.setOnClickListener {
                if (checkedMenuItemList.isEmpty()) {
                    // TODO 임시 메시지, 어떻게 처리?
                    Toast.makeText(requireContext(), "메뉴를 선택해주세요.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                menuChangedListener?.onMenuChanged(checkedMenuItemList)
            }
        }
    }

    /**
     * 1. 모든 가격표(메뉴)를 불러온다.
     * 2. 컨테이너 뷰(펌, 커트, 염색)를 생성한다.
     * 3. 컨테이너 마다 서브 메뉴를 모두 불러온다.
     * 4. 서브 뷰도 받아온 만큼 동적으로 생성해서 불러온 데이터를 적용한다.
     */
    private fun getAndAddMenuItems() {

        //Todo : 로그인했을 때 저장된 아이디로 불러오기
        val tempDesignerId = "designer0"
        val priceChartReference =
            databaseInstance.reference.child("designerPriceChart/$tempDesignerId")

        priceChartReference.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                // 커트, 매직, 펌 등 컨테이너 메뉴
                snapshot.children.forEach { containerSnapshot ->

                    // 컨테이너 view 생성
                    val menuContainerBinding = LayoutMenuContainerBinding.inflate(
                        layoutInflater,
                        binding?.layoutAllPricesContainer,
                        true
                    )

                    // 커트, 매직, 펌과 같은 메뉴의 대표 타이틀
                    menuContainerBinding.textMenuContainerTitle.text = containerSnapshot.key

                    // 1. 서브 메뉴(ex: 볼륨 매직, 일반 커트)를 DB에서 불러온다.
                    // 2. 서브 메뉴 view 생성 -> 컨테이너에 추가
                    // 3. view에 서브 메뉴 데이터 세팅
                    getAndInflateSubMenu(
                        containerSnapshot.children,
                        menuContainerBinding.actualLayoutMenuContainer
                    )
                }

                binding?.root?.visibility = View.VISIBLE    // 데이터 로딩되고 나서 레이아웃 보여준다
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /**
     * 1. 서브 메뉴(ex: 볼륨 매직, 일반 커트)를 DB에서 불러온다.
     * 2. 서브 메뉴 view 생성 -> 컨테이너에 추가
     * 3. view에 서브 메뉴 데이터 세팅
     */
    private fun getAndInflateSubMenu(
        snapshotList: Iterable<DataSnapshot>,
        containerView: ViewGroup
    ) {

        // 볼륨 매직, 일반 커트와 같은 서브 메뉴
        snapshotList.forEach { snapshot ->
            val subMenuItem = snapshot.getValue(MenuItem::class.java) ?: return

            // view(binding) 동적 생성
            val subMenuBinding = ItemPriceMenuBinding.inflate(
                layoutInflater,
                containerView,
                true
            ).also {
                if (isAdditionTreatment) subMenuItem.checkBox = it.checkBoxPriceMenu
            }

            // 뷰 데이터 설정
            setSubMenuView(subMenuItem, subMenuBinding)
        }
    }

    /**
     * 세부 메뉴의 UI에 data 적용
     */
    private fun setSubMenuView(subMenuItem: MenuItem, subMenuBinding: ItemPriceMenuBinding) {
        subMenuBinding.textPriceMenuTitle.text = subMenuItem.menuName
        subMenuBinding.textPriceMenuPrice.text = "${subMenuItem.menuPrice}원"
        subMenuBinding.textPriceMenuTime.text = subMenuItem.menuTime

        // 추가 비용 선택하는 화면 -> 체크박스 관련된 모든 프로세스 추가
        if (isAdditionTreatment) initCheckBox(subMenuItem, subMenuBinding)
    }

    /**
     * 체크박스 관련 프로세스
     */
    private fun initCheckBox(subMenuItem: MenuItem, subMenuBinding: ItemPriceMenuBinding) {
        subMenuBinding.checkBoxPriceMenu.let {
            it.visibility = View.VISIBLE  // 체크박스를 사용자에게 보여준다
            subMenuItem.checkBox = it     // 체크박스를 메뉴 객체가 가리키도록 한다
        }

        // 체크박스와 MenuItem을 어떻게 연동시킬지 고민하다가
        // textSize를 0으로 하고, 체크박스를 리스트에 추가할 때마다 text에 인덱스 값을 넣었습니다.
        subMenuItem.checkBox?.text = allMenuItemList.size.toString()
        allMenuItemList.add(subMenuItem)

        // 체크 O / X -> checkedMenuItemList에 체크박스에 대응되는 menuItem을 추가 / 삭제
        subMenuItem.checkBox?.setOnCheckedChangeListener(onCheckedChanged)

        // 변경 전에 시술 받기로 되어있던 메뉴에 체크, 액티비티 인터페이스에서 전달 받음
        // 메뉴 이름으로 같은 메뉴인지 판단
        if (menuChangedListener?.reservation?.menuList?.contains(subMenuItem.menuName) == true) {
            subMenuItem.checkBox?.isChecked = true
        }
    }

    /**
     * 체크 O / X -> checkedMenuItemList에 체크박스에 대응되는 menuItem을 추가 / 삭제
     */
    private val onCheckedChanged = CompoundButton.OnCheckedChangeListener { checkBox, checked ->
        val idx = checkBox?.text.toString().toInt()

        if (checked) {
            checkedMenuItemList.add(allMenuItemList[idx])
            Log.d("checked", "$checkedMenuItemList")
        } else {
            checkedMenuItemList.remove(allMenuItemList[idx])
        }
    }

    /**
     * 상단의 화면에서 나가는 버튼 리스너
     */
    override fun onClick(view: View?) {
        requireActivity().supportFragmentManager.popBackStack()
    }
}