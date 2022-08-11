package com.ssafy.heritage.view.feed

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.doOnPreDraw
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ssafy.heritage.ApplicationClass
import com.ssafy.heritage.R
import com.ssafy.heritage.adpter.FeedListAdapter
import com.ssafy.heritage.base.BaseFragment
import com.ssafy.heritage.data.dto.Feed
import com.ssafy.heritage.data.remote.response.FeedListResponse
import com.ssafy.heritage.databinding.FragmentFeedListBinding
import com.ssafy.heritage.listener.FeedListClickListener
import com.ssafy.heritage.view.HomeActivity
import com.ssafy.heritage.viewmodel.FeedViewModel

private const val TAG = "FeedListFragment___"

class FeedListFragment :
    BaseFragment<FragmentFeedListBinding>(R.layout.fragment_feed_list) {

    private val feedAdapter by lazy { FeedListAdapter() }
    private val feedViewModel by activityViewModels<FeedViewModel>()
    val userSeq: Int = ApplicationClass.sharedPreferencesUtil.getUser()
    private var clickMyList: Int = 0
    private var searchedList = listOf<Feed>()

    override fun init() {

        feedViewModel.getFeedListAll()
        initAdapter()
        initObserver()
        initClickListener()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // transition 효과 일단 멈춤
        postponeEnterTransition()

        initObserver()

        _binding = DataBindingUtil.inflate(inflater, layoutResId, container, false)
        return binding.root
    }

    private fun initAdapter() {
        binding.recyclerviewFeedList.adapter = feedAdapter
        binding.recyclerviewFeedList.layoutManager = GridLayoutManager(requireContext(), 3)
        feedAdapter.feedListClickListener = object : FeedListClickListener {
            override fun onClick(position: Int, feed: FeedListResponse, view: View) {
                parentFragmentManager
                    .beginTransaction()
                    .addSharedElement(view, "feed")
                    .addToBackStack(null)
                    .replace(
                        R.id.fragment_container_main,
                        FeedDetailFragment.newInstance(feed)
                    )
                    .commit()
            }
        }
    }

    private fun initObserver() {
        feedViewModel.feedListAll.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver feedListAll: $it")
            feedAdapter.submitList(it)

            // 뷰 다 불러오고나서 transition 효과 시작
            (view?.parent as ViewGroup)?.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    private fun initClickListener() = with(binding) {
        binding.apply {
            fabCreateFeed.setOnClickListener {
                findNavController().navigate(R.id.action_feedListFragment_to_feedCreateFragment)
            }
        }

        tvFeedMy.setOnClickListener {
            clickMyList = 1
            feedViewModel.getMyFeedList()
        }

        tvFeedAll.setOnClickListener {
            feedViewModel.getFeedListAll()
        }
    }
}