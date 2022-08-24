package com.ssafy.heritage.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.ssafy.heritage.R
import com.ssafy.heritage.databinding.DialogReconfirmBinding

private const val TAG = "ReconfirmDialog___"
class ReconfirmDialog(context: Context): Dialog(context) {

    private lateinit var binding : DialogReconfirmBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(
            LayoutInflater.from(context),
            R.layout.dialog_reconfirm,
            null,
            false
        )
        setContentView(binding.root)
        Log.d(TAG, "다이얼로그")
        // 배경 투명하게 바꿔줌
        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        initClickListener()
    }
    private fun initClickListener() = with(binding) {
        btnConfirm.setOnClickListener {
            dismiss()
        }

    }

}