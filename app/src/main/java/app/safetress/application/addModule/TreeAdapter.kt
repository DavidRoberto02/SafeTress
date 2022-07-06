package app.safetress.application.addModule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.safetress.R
import com.example.safetress.databinding.ItemRecommendationBinding
import com.google.firebase.auth.FirebaseAuth


class TreeAdapter(
    private var tree: MutableList<TreeEntity>,
    private var listener: OnClickListener
) : RecyclerView.Adapter<TreeAdapter.ViewHolder>() {

    //contexto
    private lateinit var context: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecommendationBinding.bind(view)

        fun setclicklistener(treeEntity: TreeEntity) {
            with(binding.root) {
                setOnClickListener { listener.onClick(treeEntity) }
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflamos la vista
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_recommendation, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //obtener la posicion
        val positionItem = tree.get(position)
        //campos que se llenaran.
        with(holder){
            setclicklistener(positionItem)
            binding.tvNameRecommendation.text = positionItem.name
            binding.tvDescriptionRecommendation.text = positionItem.description
            binding.cbLike.text = positionItem.like.keys.size.toString()
            //like function
            FirebaseAuth.getInstance().currentUser?.let {
                binding.cbLike.isChecked = positionItem.like
                    .containsKey(it.uid)
            }
            Glide.with(context)
                .load(positionItem.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.ivPhoto)
        }
    }





    override fun getItemCount(): Int = tree.size
}