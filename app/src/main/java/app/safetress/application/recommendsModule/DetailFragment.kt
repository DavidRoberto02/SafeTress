package app.safetress.application.recommendsModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentResultListener
import com.bumptech.glide.Glide
import com.example.safetress.R
import com.example.safetress.databinding.FragmentDetailBinding
import com.example.safetress.databinding.FragmentRecommendationBinding
import java.lang.Exception

class DetailFragment : Fragment() {

    private lateinit var binding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragment = RecommendationFragment()
        val fragmentTransaction = parentFragmentManager.beginTransaction()

        /*binding.btnBack.setOnClickListener {
            fragmentTransaction.apply {
                replace(R.id.mainContainter, fragment, RecommendationFragment::class.java.simpleName)
                    .addToBackStack(null)
                    .commit()
            }
        }*/

        //fragmentTransaction?.replace(R.id.hostFragment, fragment)?.commit()

        parentFragmentManager.setFragmentResultListener("key", this
        ) { requestKey, result ->
            val name = result.getString("name")
            val description = result.getString("description")
            val photoUrl = result.getString("photoUrl")
            binding.tvName.text = name
            binding.tvDescriptionDetail.text = description
            Glide.with(binding.ivDetail.context)
                .load(photoUrl)
                .into(binding.ivDetail)
        }


    }

}