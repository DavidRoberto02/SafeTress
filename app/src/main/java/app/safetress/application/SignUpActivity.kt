package app.safetress.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safetress.R
import com.example.safetress.databinding.ActivitySignUpBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

enum class ProviderType {
    BASIC
}

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    //private var mActivity: MainActivity? = null

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mFirebaseAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize Firebase Auth
        mFirebaseAuth = Firebase.auth

        // action bar boton regreso
        supportActionBar?.title = getString(R.string.text_title_signup)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnCreateAccount.setOnClickListener { initSession() }

    }

    private fun initSession() {
        if (binding.etEmail.text!!.isNotEmpty() && binding.etName.text!!.isNotEmpty()
            && binding.etPassword.text!!.isNotEmpty()){

            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(binding.etEmail.text.toString().trim(),
                binding.etPassword.text.toString().trim()).addOnCompleteListener { auth ->

                    if (auth.isSuccessful){

                    }else{
                        snackbarAlert()
                    }
                }

        }else {

        }

    }

    private fun snackbarAlert() {
        MaterialAlertDialogBuilder(this)
            .setTitle(getString(R.string.main_error))
            .setMessage(getString(R.string.main_message_error))
            .setPositiveButton(getString(R.string.main_btn_positive_sckbr), null)
            .show()
    }

    private fun showHome(email: String, provider: ProviderType) {

    }

}