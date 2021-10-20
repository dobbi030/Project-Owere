package com.blooburn.owere.user.fragment.mainFragment.homeFragment

import android.os.Bundle
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

class AllPricesFragment(private val designerId: String?) : Fragment(R.layout.all_prices_fragment) {

    private var binding: AllPricesFragmentBinding? = null
    private val priceChartReference =
        databaseInstance.reference.child("designerPriceChart/$designerId")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 디자이너 아이디 전달 못받았으면 종료
        if (designerId == null) {
            onDestroy()
        }

        binding = AllPricesFragmentBinding.bind(view)

        getAndAddMenuItems() // 모든 가격표(메뉴)를 불러오고, view를 동적 생성 해서 보여준다
    }

    /**
     * 1. 모든 가격표(메뉴)를 불러온다.
     * 2. 컨테이너 뷰(펌, 커트, 염색)를 생성한다.
     * 3. 컨테이너 마다 서브 메뉴를 모두 불러온다.
     * 4. 서브 뷰도 받아온 만큼 동적으로 생성해서 불러온 데이터를 적용한다.
     */
    private fun getAndAddMenuItems(){
        priceChartReference.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {

                // 커트, 매직, 펌 등 컨테이너 메뉴
                snapshot.children.forEach { containerSnapshot ->

                    // 컨테이너 view 생성
                    val menuContainerBinding = LayoutMenuContainerBinding.inflate(layoutInflater,
                    binding?.root,
                    true)

                    // 커트, 매직, 펌과 같은 메뉴의 대표 타이틀
                    menuContainerBinding.textMenuContainerTitle.text = containerSnapshot.key

                    // 1. 서브 메뉴(ex: 볼륨 매직, 일반 커트)를 DB에서 불러온다.
                    // 2. 서브 메뉴 view 생성 -> 컨테이너에 추가
                    // 3. view에 서브 메뉴 데이터 세팅
                    getAndInflateSubMenu(containerSnapshot.children, menuContainerBinding.root)

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    /**
     * 1. 서브 메뉴(ex: 볼륨 매직, 일반 커트)를 DB에서 불러온다.
     * 2. 서브 메뉴 view 생성 -> 컨테이너에 추가
     * 3. view에 서브 메뉴 데이터 세팅
     */
    private fun getAndInflateSubMenu(snapshotList: Iterable<DataSnapshot>, containerView: ViewGroup) {

        // 볼륨 매직, 일반 커트와 같은 서브 메뉴
        snapshotList.forEach {
            val subMenuItem = it.getValue(MenuItem::class.java) ?: return

            // view(binding) 동적 생성
            val subMenuBinding = ItemPriceMenuBinding.inflate(
                layoutInflater,
                containerView,
                true
            )

            // 뷰 데이터 설정
            setSubMenuView(subMenuItem, subMenuBinding)
        }
    }

    private fun setSubMenuView(subMenuItem: MenuItem, subMenuBinding: ItemPriceMenuBinding){
        subMenuBinding.textPriceMenuTitle.text = subMenuItem.menuName
        subMenuBinding.textPriceMenuPrice.text = subMenuItem.menuPrice
        subMenuBinding.textPriceMenuTime.text = subMenuItem.menuTime
    }
}