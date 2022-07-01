package app.safetress.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.safetress.R
import com.example.safetress.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // action bar boton regreso
        supportActionBar?.title = getString(R.string.text_title_signin)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}