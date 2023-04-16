package com.example.rccarapp.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.rccarapp.R
import com.example.rccarapp.databinding.FragmentRegisterPhoneBinding

class RegisterPhoneFragment : Fragment() {

    private lateinit var binding : FragmentRegisterPhoneBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register_phone,container,false)
        val phoneViewModel : RegisterPhoneViewModel by activityViewModels()
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            phViewModel = phoneViewModel
        }
        binding.btnContinue.setOnClickListener{
            findNavController().navigate(R.id.action_registerPhoneFragment_to_enterOtpFragment)
        }

        return binding.root
    }


}