package app.safetress.application.view


import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.safetress.application.*
import app.safetress.application.addModule.AddFragment
import com.example.safetress.R
import com.example.safetress.databinding.ActivityMainBinding
import com.facebook.FacebookSdk
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var mActiveFragment: Fragment
    private lateinit var mFragmentManager: FragmentManager

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mFirebaseAuth: FirebaseAuth? = null

    private val authResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Snackbar.make(binding.root, R.string.welcome_login_success, Snackbar.LENGTH_SHORT)
                    .show()
            } else {
                if (IdpResponse.fromResultIntent(result.data) == null) {
                    finish()
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAuth()
        setupBottomNav()
        generateFBKey()

        FacebookSdk.sdkInitialize(applicationContext)

    }

    private fun setupBottomNav() {
        mFragmentManager = supportFragmentManager

        val homeFragment = HomeFragment()
        val recommendationFragment = RecommendationFragment()
        val addFragment = AddFragment()
        val profileFragment = ProfileFragment()


        mActiveFragment = homeFragment
        mFragmentManager.beginTransaction().add(
            R.id.hostFragment,
            profileFragment, ProfileFragment::class.java.name
        )
            .hide(profileFragment)
            .commit()

        mFragmentManager.beginTransaction().add(
            R.id.hostFragment,
            recommendationFragment, RecommendationFragment::class.java.name
        )
            .hide(recommendationFragment)
            .commit()

        mFragmentManager.beginTransaction().add(
            R.id.hostFragment,
            addFragment, AddFragment::class.java.name
        )
            .hide(addFragment)
            .commit()

        mFragmentManager.beginTransaction().add(
            R.id.hostFragment,
            homeFragment, HomeFragment::class.java.name
        )
            .commit()

        binding.bottomNav.setOnNavigationItemReselectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment)
                        .commit()
                    mActiveFragment = homeFragment
                }
                R.id.action_recommends -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(recommendationFragment).commit()
                    mActiveFragment = recommendationFragment
                }
                R.id.action_add -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(addFragment)
                        .commit()
                    mActiveFragment = addFragment
                }
                R.id.action_profile -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment)
                        .commit()
                    mActiveFragment = profileFragment
                }
                else -> false
            }
        }

        binding.bottomNav.setOnItemReselectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment)
                        .commit()
                    mActiveFragment = homeFragment
                    (homeFragment as HomeAux).goToTop()
                    true
                }
                R.id.action_recommends -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment)
                        .show(recommendationFragment)
                        .commit()
                    mActiveFragment = recommendationFragment
                    (homeFragment as HomeAux).goToTop()
                    true
                }
                R.id.action_add -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(addFragment)
                        .commit()
                    mActiveFragment = addFragment
                    (homeFragment as HomeAux).goToTop()
                    true
                }
                R.id.action_profile -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment)
                        .commit()
                    mActiveFragment = profileFragment
                    (homeFragment as HomeAux).goToTop()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupAuth() {
        mFirebaseAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            val user = it.currentUser
            if (user == null) {
                authResult.launch(
                    AuthUI.getInstance().createSignInIntentBuilder()
                        .setIsSmartLockEnabled(false)
                        .setAvailableProviders(
                            Arrays.asList(
                                AuthUI.IdpConfig.FacebookBuilder().build(),
                                AuthUI.IdpConfig.EmailBuilder().build(),
                                AuthUI.IdpConfig.GoogleBuilder().build()
                            )
                        )
                        .setTheme(R.style.LoginTheme)
                        .setLogo(R.drawable.safe_tress_img)
                        .build()
                )
            }
        }
    }

    fun generateFBKey(){
        try {
            val info = packageManager.getPackageInfo(
                "app.safetress.application",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }
    }

    override fun onResume() {
        super.onResume()
        mFirebaseAuth?.addAuthStateListener(mAuthListener)
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth?.removeAuthStateListener(mAuthListener)
    }


}