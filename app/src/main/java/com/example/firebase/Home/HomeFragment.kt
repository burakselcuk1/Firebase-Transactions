package com.example.firebase.Home

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebase.R
import com.example.firebase.adapter.DusunceAdapter
import com.example.firebase.databinding.FragmentHomeBinding
import com.example.firebase.model.Paylasim
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    val db = Firebase.firestore
    var paylasimListesi = ArrayList<Paylasim>()
    private lateinit var adapter: DusunceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false);
        val view = binding.root;
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        setHasOptionsMenu(true)
        checkEmailVerifiedOrNot()
        getDatas()
        setRecyclerView()
    }

    private fun setRecyclerView() {
        adapter = DusunceAdapter(paylasimListesi)
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

    }

    private fun getDatas() {
        db.collection("Paylasimlar").addSnapshotListener { snapshot, error ->
            if (error != null) {
                Toast.makeText(
                    requireContext(), error.localizedMessage, Toast.LENGTH_SHORT
                ).show()
            } else {
                if (snapshot != null) {
                    if (!snapshot.isEmpty) {
                        val documents = snapshot.documents

                        paylasimListesi.clear()

                        for (document in documents) {
                            val kullaniciAdi = document.get("Kullanıcı Adı") as String
                            val paylasilanYorum = document.get("Paylasilan Yorum") as String

                            val indirilenPaylasim = Paylasim(kullaniciAdi,paylasilanYorum)
                            paylasimListesi.add(indirilenPaylasim)
                        }
                    }
                }
            }
        }
    }

    private fun checkEmailVerifiedOrNot() {
        val verified = auth.currentUser?.isEmailVerified

        if (verified == true) {
            binding.verfiyEmail.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //signOut
        if (item.itemId == R.id.cikis_yap) {
            Toast.makeText(
                requireContext(), "Çıkış Yapıldı", Toast.LENGTH_SHORT
            ).show()
            auth.signOut()
            view?.let {
                Navigation.findNavController(it).navigate(R.id.action_homeFragment_to_loginFragment)
            }
        }
        if (item.itemId == R.id.paylasim) {
            view?.let {
                Navigation.findNavController(it)
                    .navigate(R.id.action_homeFragment_to_paylasimFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}