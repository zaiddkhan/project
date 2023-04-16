package com.example.rccarapp.register

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.rccarapp.R
import com.example.rccarapp.databinding.FragmentEnterOtpBinding
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class EnterOtpFragment : Fragment() {

    private lateinit var binding : FragmentEnterOtpBinding
    lateinit var auth: FirebaseAuth
    private var verificationId: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_enter_otp,container,false)
        auth = FirebaseAuth.getInstance()
        val phoneViewModel : RegisterPhoneViewModel by activityViewModels()
        val phoneNumber = phoneViewModel.phoneNumber.value
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$phoneNumber")       // Phone number to verify
            .setTimeout(60, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(pac: PhoneAuthCredential) {
                    val code:String = pac.smsCode!!
                    binding.pinView.setText(code)
                }

                override fun onCodeSent(verificationid: String, p1: PhoneAuthProvider.ForceResendingToken) {
                    binding.codeSent.text = "${binding.codeSent.text}\n${phoneNumber}"
                    super.onCodeSent(verificationid, p1)
                    verificationId = verificationid

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(requireContext(), p0.message, Toast.LENGTH_SHORT).show()
                }

            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

        binding.btnLogin.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(verificationId,binding.pinView.text.toString())

            auth.signInWithCredential(credential).addOnSuccessListener {

                findNavController().navigate(R.id.action_enterOtpFragment_to_mainFragment)
            }

        }




        return binding.root
    }

}