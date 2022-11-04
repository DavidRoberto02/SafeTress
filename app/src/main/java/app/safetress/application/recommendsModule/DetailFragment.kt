package app.safetress.application.recommendsModule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.safetress.databinding.FragmentDetailBinding

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

        parentFragmentManager.setFragmentResultListener("key", this
        ) { _, result ->
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