package com.blooburn.owere.user.fragment.mainFragment.homeFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.blooburn.owere.R
import com.blooburn.owere.databinding.AllPricesFragmentBinding
import com.blooburn.owere.databinding.ItemPriceMenuBinding
import com.blooburn.owere.databinding.LayoutMenuContainerBinding
import com.blooburn.owere.user.item.MenuItem
import com.blooburn.owere.util.databaseInstance
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class AllPricesFragment(private val isAdditionTreatment: Boolean) :
    Fragment(R.layout.all_prices_fragment), View.OnClickListener {

    private var binding: AllPricesFragmentBinding? = null
    private val menuList = mutableListOf<MenuItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

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
            buttonAllPricesBack.setOnClickListener(this@AllPricesFragment)  // 뒤로가는 버튼
        }
    }

    /**
     * 추가 시술 선택하는 경우
     */
    private fun initForAdditionalTreatment() {
        binding?.apply {
            layoutAllPricesTopBar.visibility = View.GONE    // 일반 가격표 화면 상단바
            buttonAllPricesAdditionalBack.setOnClickListener(this@AllPricesFragment)
        }
    }

    /**
     * 1. 모든 가격표(메뉴)를 불러온다.
     * 2. 컨테이너 뷰(펌, 커트, 염색)를 생성한다.
     * 3. 컨테이너 마다 서브 메뉴를 모두 불러온다.
     * 4. 서브 뷰도 받아온 만큼 동적으로 생성해서 불러온 데이터를 적용한다.
     */
    private fun getAndAddMenuItems() {

        //Todo : 로그인했을 때 아이디 저장해놓고 따러 불러오기
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

                binding?.root?.visibility = View.VISIBLE
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
            ). also{
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

        // 추가 비용 선택하는 화면일 때 체크박스를 보여준다
        if (isAdditionTreatment) {
            subMenuBinding.checkBoxPriceMenu.visibility = View.VISIBLE
            subMenuItem.checkBox = subMenuBinding.checkBoxPriceMenu
        }
    }

    /**
     * 상단의 화면에서 나가는 버튼 리스너
     */
    override fun onClick(view: View?) {
        requireActivity().supportFragmentManager.popBackStack()
    }
}