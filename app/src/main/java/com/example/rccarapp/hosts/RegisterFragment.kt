package com.example.rccarapp.hosts

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.rccarapp.R
import com.example.rccarapp.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {



        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_register,container,false)
        setTransition()
        binding.cardEmail.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_registerEmail)
        }

        binding.cardPhoneNumber.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_registerPhoneFragment)
        }
        return binding.root
    }

    private fun setTransition(){

        exitTransition = TransitionInflater.from(activity).inflateTransition(android.R.transition.move)
        enterTransition = TransitionInflater.from(activity).inflateTransition(android.R.transition.slide_bottom)
        reenterTransition = TransitionInflater.from(activity).inflateTransition(android.R.transition.fade)

    }


}