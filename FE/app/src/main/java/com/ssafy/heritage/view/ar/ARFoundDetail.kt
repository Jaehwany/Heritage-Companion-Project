package com.ssafy.heritage.view.ar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.andrefrsousa.superbottomsheet.SuperBottomSheetFragment
import com.ssafy.heritage.R
import com.ssafy.heritage.adpter.StampDetailListAdapter
import com.ssafy.heritage.data.dto.Stamp
import com.ssafy.heritage.databinding.FragmentARFoundDetailBinding
import com.ssafy.heritage.util.CategoryConverter

class ARFoundDetail(val stampList: List<Stamp>, val cnt: String, val name: String) :
    SuperBottomSheetFragment() {

    lateinit var binding: FragmentARFoundDetailBinding
    val stampDetailListAdapter by lazy { StampDetailListAdapter() }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_a_r_found_detail, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            name = this@ARFoundDetail.name
            cnt = this@ARFoundDetail.cnt
            src = CategoryConverter.imageMap[this@ARFoundDetail.name]

            recyclerView.apply {
                adapter = stampDetailListAdapter
                layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

                stampDetailListAdapter.submitList(this@ARFoundDetail.stampList)
            }
        }
    }
}