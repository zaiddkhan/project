package com.example.rccarapp.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import com.example.rccarapp.R
import com.example.rccarapp.databinding.ErrorToastLayoutBinding

class ErrorDialog(val msg :String) : DialogFragment(){
     var errorText = MutableLiveData("")
     private lateinit var binding : ErrorToastLayoutBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.error_toast_layout,null,false)
        val dialog = Dialog(requireContext())
        errorText.value = msg

        dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

        dialog.window!!.attributes.windowAnimations = R.style.DialogAnimation
        dialog.setContentView(binding.root)



        binding.cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        val wlmp : WindowManager.LayoutParams = dialog.window!!.attributes
        wlmp.x = 5
        wlmp.y = 100
        wlmp.gravity = Gravity.BOTTOM
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        errorText.observe(this,{ msg ->
            dialog.findViewById<TextView>(R.id.errorTextView).text = msg
        })
        return dialog
    }

}