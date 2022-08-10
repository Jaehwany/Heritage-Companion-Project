package com.ssafy.heritage.view.feed

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn.hasPermissions
import com.google.android.material.textfield.TextInputLayout
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.ssafy.heritage.ApplicationClass
import com.ssafy.heritage.R
import com.ssafy.heritage.base.BaseFragment
import com.ssafy.heritage.data.remote.request.FeedAddRequest
import com.ssafy.heritage.databinding.FragmentFeedCreateBinding
import com.ssafy.heritage.util.FileUtil
import com.ssafy.heritage.util.FormDataUtil
import com.ssafy.heritage.viewmodel.FeedViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

private const val TAG = "FeedCreateFragment___"
private val PERMISSIONS_REQUIRED = arrayOf(
    Manifest.permission.READ_EXTERNAL_STORAGE
)

class FeedCreateFragment : BaseFragment<FragmentFeedCreateBinding>(R.layout.fragment_feed_create) {

    val userSeq: Int = ApplicationClass.sharedPreferencesUtil.getUser()
    private val feedViewModel by activityViewModels<FeedViewModel>()
    private lateinit var feedInfo: FeedAddRequest

    var img_multipart: MultipartBody.Part? = null
    private var title: String = ""
    private var hashTags: String = ""
    private var content: String = ""
    private var feedOpen: Char = 'Y'

    override fun init() {
        initObserver()
        initClickListener()
    }

    private fun initObserver() = with(binding) {
        feedViewModel.insertFeedInfo.observe(viewLifecycleOwner) {
            Log.d(TAG, "initObserver: $it")
            if (it!! != "0") {
                Glide.with(ivFeedImage.context)
                    .load(it)
                    .into(ivFeedImage)
            }

//            if (it != null) {
//                makeToast("피드가 등록되었습니다.")
////                val action = FeedCreateFragmentDirections.actionFeedCreateFragmentToFeedDetailFragment()
////                findNavController().navigate(action)
//            } else {
//                makeToast("피드 등록에 실패했습니다. 다시 확인해주세요.")
//            }
        }
    }

    private fun initClickListener() = with(binding) {
        // 사진 추가
        ivFeedImage.setOnClickListener {
            // 권한 체크
            if (!hasPermissions()) {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            } else {
                pick()
            }
        }

        // 공개/비공개 여부
        switchFeedCreateLock.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // 기본값이 공개
                makeToast("공개")
            } else {
                // 비공개
                feedOpen = 'N'
                makeToast("비공개")
            }
        }

        // 해시태그랑 같이 post할 수 있게 바뀌어야 함
        // 피드 등록하기 클릭 시
        tvGroupInsert.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                title = etFeedCreateTitle.editText?.text.toString()
                // 해시태그는 리스트로 받게 수정해야함
                var tags = getHashTags(binding.etFeedCreateTag.editText?.text.toString())
                var tagResult = listOf<String>()

                tags.forEach { tag ->
                    tagResult = tagResult.plus("${tag.value}")
                }
                content = etFeedCreateContent.editText?.text.toString()

                when {
                    title == "" -> {
                        makeToast("제목을 입력해주세요")
                    }
    //                hashTag == "" -> {
    //                    makeToast("해시태그를 입력해주세요")
    //                }
                }

                // imageUrl을 필수값으로 하자..사진 피드니까!!
                if (img_multipart == null || img_multipart?.let { feedViewModel.sendImage(it) } == true) {
                    feedInfo = FeedAddRequest( userSeq, feedViewModel.insertFeedInfo.value!!, title, content, feedOpen, tagResult )
                    feedViewModel.insertFeed(feedInfo)
                    val action = FeedCreateFragmentDirections.actionFeedCreateFragmentToFeedDetailFragment()
                    findNavController().navigate(action)
                    Log.d(TAG, "initClickListener: ${feedInfo}")
                    Log.d(TAG, "initClickListener: 클릭했니?")

                } else {
                    makeToast("제목을 입력해주세요.")
                    return@launch
                }
            }
        }
        imagebtnFeedBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // 사진 선택
    private fun pick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        filterActivityLauncher.launch(intent)
    }

    // 사진 골라서 가져온 결과
    private val filterActivityLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK && it.data != null) {

                val imagePath = it.data!!.data
                binding.ivFeedImage.setImageURI(imagePath)
                val file = FileUtil.toFile(requireContext(), imagePath!!)
                img_multipart = FormDataUtil.getImageBody("file", file)

            } else if (it.resultCode == RESULT_CANCELED) {
                makeToast("사진 선택 취소")
            } else {
                Log.d("ActivityResult", "something wrong")
            }
        }

    val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // PERMISSION GRANTED
            pick()
        } else {
            // PERMISSION NOT GRANTED
            makeToast("권한이 거부됨")
        }
    }

    private fun getHashTags(text: String): Sequence<MatchResult> {
        val pattern = """#([^#\s]+)""" // 태그 추출 정규식
        val regex = pattern.toRegex()
        val matches = regex.findAll(text)

        return matches
    }

    fun hasPermissions() = PERMISSIONS_REQUIRED.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }

    private fun makeToast(msg: String) {
        Toast.makeText(requireActivity(), msg, Toast.LENGTH_SHORT).show()
    }

}