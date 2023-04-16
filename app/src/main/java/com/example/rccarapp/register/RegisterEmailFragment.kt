package com.example.rccarapp.register

import android.os.Bundle
import android.os.CountDownTimer
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelLazy
import androidx.navigation.fragment.findNavController
import com.example.rccarapp.R
import com.example.rccarapp.databinding.FragmentRegisterEmailBinding
import com.example.rccarapp.dialogs.ErrorDialog
import com.example.rccarapp.extras.Constants.MESSAGE
import com.google.firebase.auth.FirebaseAuth


class RegisterEmailFragment : Fragment() {

   private lateinit var binding: FragmentRegisterEmailBinding
   private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register_email,container,false)

        //default view model factory as no view model factory required.
        val emailViewModel : RegisterEmailViewModel by ViewModelLazy(RegisterEmailViewModel::class,{viewModelStore},{defaultViewModelProviderFactory})

        auth = FirebaseAuth.getInstance()

        setTransitions()

        //setting the viewModel data to the data binding's data object
        binding.apply {
            lifecycleOwner = this@RegisterEmailFragment
            emViewModel = emailViewModel
        }

        binding.btnSignIN.setOnClickListener{
        //observing the changes in the live data using the utility observe once method

            if(!emailViewModel.validateEmailAddress(emailViewModel.userEmailId.value!!)){
                val ed = ErrorDialog("Please enter a valid email address")
                ed.show(childFragmentManager,"")
                val cdt =object: CountDownTimer(2000,1000){
                    override fun onTick(p0: Long) {
                       // Toast.makeText(requireContext(), ed.errorText.value, Toast.LENGTH_SHORT).show()

                    }

                    override fun onFinish() {
                        ed.dismiss()
                    }

                }
                cdt.start()
            }

            auth.createUserWithEmailAndPassword(emailViewModel.userEmailId.value!!,emailViewModel.userPassword.value!!)
                .addOnCompleteListener{ task ->
                    if(!task.isSuccessful){
                        Toast.makeText(requireContext(), task.exception?.message!!, Toast.LENGTH_SHORT).show()
                        auth.signInWithEmailAndPassword(emailViewModel.userEmailId.value!!,emailViewModel.userPassword.value!!)
                            .addOnCompleteListener{ task ->
                                if(task.isSuccessful){
                                    findNavController().navigate(R.id.action_registerEmail_to_mainFragment)
                                }
                            }
                    }else{
                        if(task.exception?.message!!.equals(MESSAGE)) {
                            Toast.makeText(requireContext(), emailViewModel.userEmailId.value, Toast.LENGTH_SHORT).show()


                        }
                    }
                }

        }
        return binding.root
    }

    private fun setTransitions(){
        enterTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_bottom)
        exitTransition = TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.slide_top)
    }
}