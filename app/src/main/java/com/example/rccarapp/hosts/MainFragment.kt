package com.example.rccarapp.hosts

import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.fragment.app.Fragment
import android.os.Bundle
import android.util.Log
import android.view.*
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.rccarapp.R
import com.example.rccarapp.databinding.FragmentMainBinding
import com.example.rccarapp.models.Image
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import io.github.controlwear.virtual.joystick.android.JoystickView
import okhttp3.*
import java.io.ByteArrayOutputStream
import java.io.IOException

class MainFragment : Fragment() {



    private lateinit var binding: FragmentMainBinding
    private lateinit var db : FirebaseFirestore
    var storage = FirebaseStorage.getInstance()
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_main,container,false)

        binding.webView.webViewClient = WebViewClient()

        binding.webView.setInitialScale(100)
        // this will load the url of the website
        binding.webView.loadUrl("http://192.168.103.42:8081")

        // this will enable the javascript settings, it can also allow xss vulnerabilities
        binding.webView.settings.javaScriptEnabled = true

        // if you want to enable zoom feature
        binding.webView.settings.setSupportZoom(true)

        db = FirebaseFirestore.getInstance()
        binding.toolbar.inflateMenu(R.menu.menu)

        binding.toolbar.setOnMenuItemClickListener{
            when(it.itemId){
                R.id.log_out -> {
                    FirebaseAuth.getInstance().signOut()
                    findNavController().navigate(R.id.action_mainFragment_to_registerFragment)
                 true
                }
                R.id.saved_images -> {

                    findNavController().navigate(R.id.action_mainFragment_to_savedImagesFragment)
                    true
                 }
                 else -> false
            }
        }
        val http = OkHttpClient()

        //initializing all the request objects to different routes
        val leftRequest = Request.Builder().url("http://192.168.103.42:5000/left").build()
        val rightRequest = Request.Builder().url("http://192.168.103.42:5000/right").build()
        val forwardRequest = Request.Builder().url("http://192.168.103.42:5000/forward").build()
        val backwardRequest = Request.Builder().url("http://192.168.103.42:5000/backward").build()

        val stop = Request.Builder().url("http://192.168.103.42:5000/stop").build()

        binding.joystick.setOnMoveListener(object : JoystickView.OnMoveListener{
            override fun onMove(angle: Int, strength: Int) {
                //botto,
                if(angle==0){
                    http.newCall(stop).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {
                        }

                        override fun onResponse(call: Call, response: Response) {
                        }

                    })
                }
                if(angle >= 220 && angle < 320){
                    Log.d("TAG",angle.toString())
                    http.newCall(backwardRequest).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {}
                        override fun onResponse(call: Call, response: Response) {}
                    })
                }
                //top
                else if(angle >= 50 && angle <= 130){
                    http.newCall(forwardRequest).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {}
                        override fun onResponse(call: Call, response: Response) {}
                    })
                }
                //left
                else if(angle >= 130 && angle <= 220){
                    http.newCall(leftRequest).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {}
                        override fun onResponse(call: Call, response: Response) {}
                    })
                }
                //right
                else{
                    http.newCall(rightRequest).enqueue(object : Callback{
                        override fun onFailure(call: Call, e: IOException) {}
                        override fun onResponse(call: Call, response: Response) {}
                    })
                }
            }

        },1000)
        val view : View = binding.webView
        storage = FirebaseStorage.getInstance()

        val reference= storage.getReferenceFromUrl("gs://rccarapp-db69c.appspot.com")

        binding.cameraSs.setOnClickListener {
                val bitmap = getBitmap(view)
                uploadToFirebase(reference,bitmap)
        }

        return binding.root

    }


    fun getBitmap(view:View): Bitmap {
        val totalHeight = view.height
        val totalWidth = view.width
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        view.layout(0, 0, totalWidth, totalHeight)


        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun uploadToFirebase(reference: StorageReference, bitmap: Bitmap){
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val data: ByteArray = stream.toByteArray()
        val imageReference = reference.child(System.currentTimeMillis().toString()+"/img1")
        val uploadTask = imageReference.putBytes(data).addOnSuccessListener {
            imageReference.downloadUrl.addOnSuccessListener {

                val images = db.collection("Images")

                val downloadUrl = it!!.toString()
                val image = Image(System.currentTimeMillis().toString(),downloadUrl,user!!.uid)

                images.add(image).addOnCompleteListener {
                    if(it.isSuccessful){
                        Toast.makeText(requireContext(), "data uploaded", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}