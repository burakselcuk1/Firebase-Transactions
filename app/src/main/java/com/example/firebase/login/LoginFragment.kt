package com.example.firebase.login

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.firebase.R
import com.example.firebase.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase


class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var eMail: String
    private lateinit var password: String
    private lateinit var userName: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false);
        val view = binding.root;
        return view;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        val currentUser = auth.currentUser
        if (currentUser != null) {
            view.let { it1 ->
                Navigation.findNavController(it1)
                    .navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }

        //Clicks
        with(binding) {
            button2.setOnClickListener {
                kayitOl()
            }
            button.setOnClickListener {
                girisYap()
            }
        }
    }

    private fun girisYap() {
        eMail = binding.editTextTextEmailAddress.text.toString()
        password = binding.editTextTextPassword.text.toString()
        auth.signInWithEmailAndPassword(eMail, password).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(
                    requireContext(), "Hoşgendin ${auth.currentUser?.email.toString()}",
                    Toast.LENGTH_SHORT
                ).show()
                view?.let { it1 ->
                    Navigation.findNavController(it1)
                        .navigate(R.id.action_loginFragment_to_homeFragment)
                }

            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun kayitOl() {
        eMail = binding.editTextTextEmailAddress.text.toString()
        password = binding.editTextTextPassword.text.toString()
        userName = binding.userName.text.toString()
        auth.createUserWithEmailAndPassword(eMail, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    //Send E-mail verification
                    auth.currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener {
                            Toast.makeText(
                                requireContext(), "Please verfiy your email adress",
                                Toast.LENGTH_SHORT
                            ).show()
                            view?.let { it1 ->
                                Navigation.findNavController(it1)
                                    .navigate(R.id.action_loginFragment_to_homeFragment)
                            }
                        }?.addOnFailureListener {
                            Toast.makeText(
                                requireContext(), it.localizedMessage, Toast.LENGTH_SHORT
                            ).show()
                        }

                    //Add User Name
                    val profileUpdates = userProfileChangeRequest {
                        displayName = userName
                    }
                    auth.currentUser?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d("user", "User profile updated.")
                            }
                        }

                }
            }.addOnFailureListener {
                Toast.makeText(
                    requireContext(), it.localizedMessage, Toast.LENGTH_SHORT
                ).show()

            }
    }

}