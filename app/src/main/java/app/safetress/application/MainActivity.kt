package app.safetress.application

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import app.safetress.application.utils.Constants
import com.example.safetress.R
import com.example.safetress.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

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
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.server_client_id))
                .requestEmail()
                .build()
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        //init firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance()

        binding.btnSigninGoogle.setOnClickListener {
            val intent = mGoogleSignInClient.signInIntent
            startActivityForResult(intent, Constants.RC_SIGN_IN)
        }

    }

    // onActivityResult() function : this is where
    // we provide the task and data for the Google Account
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.RC_SIGN_IN) {
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleResult(task)
        }
    }

    private fun handleResult(task: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            if (account != null) {
                UpdateUI(account)
            }
        } catch (e: ApiException) {
            Snackbar.make(binding.root, e.toString(), Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun UpdateUI(account: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        mFirebaseAuth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                // if is successful navigate activity to home fragment
                /*val fragment = HomeFragment()
                val fragmentManager = supportFragmentManager
                val fragmentTransaction = fragmentManager.beginTransaction()

                fragmentTransaction.add(R.id.mainContainter, fragment)
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()*/
                val i = Intent(this@MainActivity, MainActivityFr::class.java)
                startActivity(i)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()
    }


    private fun signIn() {
        val intent = Intent(this@MainActivity, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun createAccount() {
        val intent = Intent(this@MainActivity, SignUpActivity::class.java)
        startActivity(intent)
    }


}