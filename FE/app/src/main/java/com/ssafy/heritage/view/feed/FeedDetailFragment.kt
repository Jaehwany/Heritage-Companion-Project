package com.ssafy.heritage.view.feed

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.transition.Fade
import androidx.transition.TransitionInflater
import com.ssafy.heritage.ApplicationClass
import com.ssafy.heritage.R
import com.ssafy.heritage.base.BaseFragment
import com.ssafy.heritage.data.remote.response.FeedListResponse
import com.ssafy.heritage.databinding.FragmentFeedDetailBinding
import com.ssafy.heritage.view.HomeActivity
import com.ssafy.heritage.viewmodel.FeedViewModel
import com.ssafy.heritage.viewmodel.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "FeedDetailFragment___"
private const val ARG_FEED = "heritage"

class FeedDetailFragment :
    BaseFragment<FragmentFeedDetailBinding>(R.layout.fragment_feed_detail) {

    //    private var feed: Feed? = null
    private var feed: FeedListResponse? = null
    private val feedViewModel by activityViewModels<FeedViewModel>()
    private val userViewModel by activityViewModels<UserViewModel>()
    private val userSeq: Int = ApplicationClass.sharedPreferencesUtil.getUser()

    private lateinit var callback: OnBackPressedCallback

    override fun init() {

        // 전달 받은 피드 데이터 받기
        CoroutineScope(Dispatchers.Main).launch {
            withContext(Dispatchers.Main) {
                arguments?.let {
                    feed = it.getSerializable(ARG_FEED) as FeedListResponse
                    Log.d(TAG, "init onCreate: $feed")
                }
            }
            binding.feed = this@FeedDetailFragment.feed
            var tagResult = ""

            feed?.hashtag?.forEach { tag ->
                tagResult = tagResult.plus("#${tag} ")
            }

            binding.tvFeedHashtag.text = tagResult
            Log.d(TAG, "init init: ${this@FeedDetailFragment.feed}")


//            if (feed!!.feedOpen == 'Y') {
//                binding.switchFeedDetailLock.isChecked = true
//            } else {
//                binding.switchFeedDetailLock.isChecked = false
//            }
//            Log.d(TAG, "init: 공개비공개 ${feed!!.feedOpen}")
        }

//        initObserver()
        initClickListenr()
    }

    private fun initObserver() = with(binding) {
        feedViewModel.feedCount.observe(viewLifecycleOwner) {
            if (feed?.userLike == 'N') {
                binding.imagebtnFeedDetailLike.isSelected = false
                Log.d(TAG, "initClickListenr: 좋아요")
            } else if (feed?.userLike == 'Y') {
                binding.imagebtnFeedDetailLike.isSelected = true
                Log.d(TAG, "initClickListenr: 좋아요 취소")
            }
            feedViewModel.countFeedLike(feed!!.feedSeq)
            binding.tvFeedCount.setText(it)
            Log.d(TAG, "init: 좋아요 수 $it")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        // 뒤로가기 설정 부분임
        (requireActivity() as HomeActivity).backPressedListener.register()

        // fragment에서 back버튼 조작하도록 콜백 등록
        callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                parentFragmentManager.popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun onDetach() {
        super.onDetach()

        // 뒤로가기 설정 부분임
        (requireActivity() as HomeActivity).backPressedListener.unregister()

        // 뒤로가기 콜백 해제
        callback.remove()
    }

    override fun onStart() {
        super.onStart()
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        sharedElementReturnTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
        enterTransition = Fade()
        exitTransition = Fade()
    }

    private fun initClickListenr() = with(binding) {
        // 피드 삭제
        imagebtnFeedDetailDelete.setOnClickListener {

            feedViewModel?.deleteFeed(feed!!.feedSeq)
            // 피드 목록 페이지로 이동하고 싶은데 홈으로 가넹,,
            findNavController().popBackStack()
        }
//        // 피드 공개/비공개
//        switchFeedDetailLock.setOnCheckedChangeListener { _, isChecked ->
//            if (feed?.feedOpen == 'Y' && isChecked) {
//                feedViewModel?.changeFeedOpen(feed!!.feedSeq, 'N')
//                binding.switchFeedDetailLock.isChecked = false
//                Log.d(TAG, "y -> n: ${feed!!.feedOpen}")
//            } else if (feed?.feedOpen == 'N' && !isChecked) {
//                feedViewModel?.changeFeedOpen(feed!!.feedSeq, 'Y')
//                binding.switchFeedDetailLock.isChecked = true
//                Log.d(TAG, "n -> y: ${feed!!.feedOpen}")
//            }
//        }



        // 뒤로가기
        imagebtnFeedBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    companion object {
        fun newInstance(param: FeedListResponse) = FeedDetailFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_FEED, param)
            }
        }
    }

}