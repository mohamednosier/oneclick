package com.onclick.task.ui.pager

import com.onclick.task.R
import com.onclick.task.databinding.FragmentMainCategoryBinding
import com.onclick.task.ui.base.BaseFragment

import dagger.hilt.android.AndroidEntryPoint

/**
 * ViewPager with tabs to navigate between [AccountListFragment] and [BudgetsFragment].
 */
@AndroidEntryPoint
class MainCategoriesFragment :
    BaseFragment<PagerViewModel, FragmentMainCategoryBinding>(PagerViewModel::class.java) {

    val categoryNames = arrayOf<String>(
        "Data Recharge",
        "Voice Recharge",
        "Games Cards",
        "(OTT) Cinema",
        "Gift Cards",
        "Favorites"
    )
    val categoryDescription = arrayOf<String>(
        "mobily - zain - Lebara - Virgin - FRIENDI - stc",
        "Mobily - Zain - Lebara - Virgin - FRIENDI - Stc - GO",
        "Nintendo - Xbox - PUBG - FIFA - VIP Jalsat - Mobile Legends...",
        "STARZPLAY - Netflix - Kitab Sawti - Weyyka - Shaid Vip",
        "Skype - Microsoft - Mcafee - Facebook",
        "Long Press On Any Card to Add/Remove Form Your Favorites"
    )
    val categoryRes = arrayOf<Int>(
        R.drawable.ic_data, R.drawable.ic_voice, R.drawable.ic_game_recharge, R.drawable.ic_cinima,
        R.drawable.ic_gift_card, R.drawable.ic_favorite
    )

    override fun getLayoutRes() = R.layout.fragment_main_category

    override fun init() {
        val categoriesListAdaptor = CategoriesListAdaptor(
            this.requireActivity(),
            categoryNames,
            categoryDescription,
            categoryRes
        )
        binding.lvCategories.adapter = categoriesListAdaptor

        binding.lvCategories.setOnItemClickListener { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
        }
    }
}