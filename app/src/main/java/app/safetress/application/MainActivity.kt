package app.safetress.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import app.safetress.application.utils.Constants
import com.example.safetress.R
import com.example.safetress.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mFirebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCreateAccount.setOnClickListener { createAccount() }
        binding.tvSignIn.setOnClickListener { signIn() }

        //Configuracion de google SignIn
        val googleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //init firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance()
        initUser()

        binding.btnSigninGoogle.setOnClickListener {
            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, Constants.RC_SIGN_IN)
        }

    }

    private fun initUser() {
        // check if user is logged in or not
        val firebaseUser = mFirebaseAuth.currentUser
        if (firebaseUser != null){
            //start profile activity
            startActivity(Intent(this@MainActivity, ))
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.RC_SIGN_IN) {
            Log.d(Constants.TAG, "onActivityResult: GoogleSignIn intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Google SignIn Success
                val accountAuthenticator = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(accountAuthenticator)

            } catch (e: Exception) {
                Log.d(Constants.TAG, "onActivityResult: ${e.message}")
            }
        }
    }


    private fun signIn() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun createAccount() {
        val intent = Intent(this@MainActivity, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun firebaseAuthWithGoogleAccount(accountAuthenticator: GoogleSignInAccount?) {
        Log.d(
            Constants.TAG,
            "firebaseAuthWithGoogleAccount: begin firebase auth with google account "
        )
        val credential = GoogleAuthProvider.getCredential(accountAuthenticator?.idToken, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->
                Log.d(Constants.TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                //get loggedIn user
                val firebaseUser = mFirebaseAuth.currentUser
                //get user info
                val uid = firebaseUser!!.uid
                val email = firebaseUser!!.email

                Log.d(Constants.TAG, "firebaseAuthWithGoogleAccount: Email: $uid")
                Log.d(Constants.TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                //check if user is new or existing
                if (authResult.additionalUserInfo!!.isNewUser) {
                    Log.d(
                        Constants.TAG,
                        "firebaseAuthWithGoogleAccount: Account created... \n$email"
                    )
                    Snackbar.make(binding.root, "Account created... $email", Snackbar.LENGTH_SHORT)
                        .show()
                } else {
                    //existing user - loggedIN
                    Log.d(
                        Constants.TAG,
                        "firebaseAuthWithGoogleAccount: Existing user... \n$email"
                    )
                    Snackbar.make(binding.root, "LoggedIn... $email", Snackbar.LENGTH_SHORT)
                        .show()
                }

                //start profile activity
                startActivity(Intent(this@MainActivity, ))
                finish()
            }
            .addOnFailureListener { e ->
                Log.d(Constants.TAG, "firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Snackbar.make(binding.root, "Loggin Failed due to ${e.message}", Snackbar.LENGTH_SHORT)
                    .show()
            }
    }


}