package app.safetress.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.safetress.R
import com.example.safetress.databinding.ActivityMainFrBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivityFr : AppCompatActivity() {

    private lateinit var binding: ActivityMainFrBinding
    private lateinit var  mActiveFragment: Fragment
    private lateinit var mFragmentManager: FragmentManager

    private lateinit var mAuthListener: FirebaseAuth.AuthStateListener
    private var mFirebaseAuth: FirebaseAuth? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainFrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupBottomNav()

    }


    private fun setupBottomNav() {
        mFragmentManager = supportFragmentManager

        val homeFragment = HomeFragment()
        val recommendationFragment = RecommendationFragment()
        val addFragment = AddFragment()
        val profileFragment = ProfileFragment()


        mActiveFragment = homeFragment
        mFragmentManager.beginTransaction().add(R.id.hostFragment,
        profileFragment, ProfileFragment::class.java.name)
            .hide(profileFragment)
            .commit()

        mFragmentManager.beginTransaction().add(R.id.hostFragment,
            recommendationFragment, RecommendationFragment::class.java.name)
            .hide(recommendationFragment)
            .commit()

        mFragmentManager.beginTransaction().add(R.id.hostFragment,
            addFragment, AddFragment::class.java.name)
            .hide(addFragment)
            .commit()

        mFragmentManager.beginTransaction().add(R.id.hostFragment,
            homeFragment, HomeFragment::class.java.name)
            .commit()

        binding.bottomNav.setOnNavigationItemReselectedListener {
            when(it.itemId){
                R.id.action_home -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(homeFragment).commit()
                    mActiveFragment = homeFragment
                }
                R.id.action_recommends -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(recommendationFragment).commit()
                    mActiveFragment = recommendationFragment
                }
                R.id.action_add -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(addFragment).commit()
                    mActiveFragment = addFragment
                }
                R.id.action_profile -> {
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(profileFragment).commit()
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
                    mFragmentManager.beginTransaction().hide(mActiveFragment).show(recommendationFragment)
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

    override fun onResume() {
        super.onResume()
        mFirebaseAuth?.addAuthStateListener { mAuthListener }
    }

    override fun onPause() {
        super.onPause()
        mFirebaseAuth?.removeAuthStateListener { mAuthListener }
    }
}