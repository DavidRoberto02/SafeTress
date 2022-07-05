package app.safetress.application

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import app.safetress.application.view.MainActivity
import com.example.safetress.R
import com.example.safetress.databinding.FragmentProfileBinding
import com.firebase.ui.auth.AuthUI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    private lateinit var mBinding: FragmentProfileBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentProfileBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.tvName.text = FirebaseAuth.getInstance().currentUser?.displayName
        mBinding.tvEmail.text = FirebaseAuth.getInstance().currentUser?.email


        mBinding.btnLogout.setOnClickListener { signOut() }


    }

    private fun signOut() {
        context?.let {
            AuthUI.getInstance().signOut(it)
                .addOnCompleteListener {
                    Snackbar.make(mBinding.root, R.string.signOut, Snackbar.LENGTH_SHORT).show()
                }
        }

    }


}