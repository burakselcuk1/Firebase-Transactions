package com.example.firebase.paylasim

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.firebase.R
import com.example.firebase.databinding.FragmentHomeBinding
import com.example.firebase.databinding.FragmentPaylasimBinding
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class PaylasimFragment : Fragment() {

    private var _binding: FragmentPaylasimBinding? = null
    private val binding get() = _binding!!

    val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPaylasimBinding.inflate(inflater, container, false);
        val view = binding.root;
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        with(binding){
            paylasButton.setOnClickListener {
                addToDatabase()
                Navigation.findNavController(it).navigate(R.id.action_paylasimFragment_to_homeFragment)
            }
        }
    }

    private fun addToDatabase() {
        val paylasilanYorum = binding.paylasilanYorum.text.toString()
        val user = auth.currentUser!!.displayName.toString()
        val tarih = Timestamp.now()

        val paylasim = hashMapOf<String, Any>()
        paylasim.put("Paylasilan Yorum", paylasilanYorum)
        paylasim.put("Kullanıcı Adı", user)
        paylasim.put("Tarih", tarih)

        db.collection("Paylasimlar").add(paylasim).addOnCompleteListener {
            Toast.makeText(requireContext()
                , "Database'e eklendi", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(requireContext()
                , "${it.localizedMessage}", Toast.LENGTH_SHORT).show()
        }
    }
}