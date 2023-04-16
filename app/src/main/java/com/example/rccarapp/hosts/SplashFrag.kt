package com.example.rccarapp.hosts

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.rccarapp.R
import com.example.rccarapp.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth


class SplashFrag : Fragment() {

    private lateinit var binding : FragmentSplashBinding
    val user = FirebaseAuth.getInstance().currentUser


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash,container,false)

        startSplash()
        return binding.root
    }

    fun startSplash(){
        val fadeout : Animation = AlphaAnimation(1f,1f)
        fadeout.duration = 2500
        fadeout.setAnimationListener(object : AnimationListener{

            override fun onAnimationStart(p0: Animation?) {
                binding.gif.setBackgroundResource(R.drawable.rc_car)
            }
            override fun onAnimationEnd(p0: Animation?) {
                if(user!=null){
                    findNavController().navigate(R.id.action_splashFrag_to_mainFragment)
                }
                else {
                    findNavController().navigate(R.id.action_splashFrag_to_registerFragment)
                }
            }
            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        binding.gif.startAnimation(fadeout)
    }
}