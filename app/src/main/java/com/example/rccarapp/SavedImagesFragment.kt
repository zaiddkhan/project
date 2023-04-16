package com.example.rccarapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridView
import androidx.fragment.app.findFragment
import com.example.rccarapp.adapters.GridViewAdapter
import com.example.rccarapp.models.Image
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.ArrayList


class SavedImagesFragment : Fragment() {

    private lateinit var db : FirebaseFirestore
    private lateinit var arrlist : java.util.ArrayList<Image>
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_saved_images_page,container,false)

        val gv = view.findViewById<GridView>(R.id.gv)
        db = FirebaseFirestore.getInstance()


        arrlist = ArrayList()

        db.collection("Images").whereEqualTo("uid",user!!.uid).get().addOnSuccessListener {
            if(!it.isEmpty){
                val list = it.documents
                for (d in list){
                    val image = d.toObject(Image::class.java) as Image

                    arrlist.add(image)
                }

                val adapter = GridViewAdapter(requireContext(), arrlist)
                gv.adapter = adapter
            }
        }
        return view
    }

}