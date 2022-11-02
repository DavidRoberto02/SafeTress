package app.safetress.application.recommendsModule.recyclerview.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import app.safetress.application.recommendsModule.recyclerview.entiti.TreeEntityHome
import app.safetress.application.recommendsModule.recyclerview.utils.OnClickListener
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.safetress.R
import com.example.safetress.databinding.ItemTreeBinding

class RecommendsAdapter(
    private val clicklistener: OnClickListener,
    private var recommends : MutableList<TreeEntityHome>

) : RecyclerView.Adapter<RecommendsAdapter.ViewHolder>() {

    private lateinit var mContext: Context

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemTreeBinding.bind(view)

        fun setClickListener(treeEntityHome: TreeEntityHome){
            with(binding.root){
                setOnClickListener { clicklistener.onClick(treeEntityHome) }
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        mContext = parent.context
        val view =  LayoutInflater.from(mContext).inflate(R.layout.item_tree, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rec = recommends[position]

        with(holder) {
            setClickListener(rec)
            binding.tvName.text = rec.name
            Glide.with(mContext)
                .load(rec.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPhoto)
        }
    }

    override fun getItemCount(): Int = recommends.size

}