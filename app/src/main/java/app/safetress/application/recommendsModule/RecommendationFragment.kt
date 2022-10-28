package app.safetress.application.recommendsModule

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import app.safetress.application.addModule.entities.TreeFeedHome
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.safetress.R
import com.example.safetress.databinding.FragmentRecommendationBinding
import com.example.safetress.databinding.ItemRecommendationBinding
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase

class RecommendationFragment : Fragment() {

    private lateinit var binding: FragmentRecommendationBinding

    private lateinit var mFirebaseAdapter: FirebaseRecyclerAdapter<TreeFeedHome, SnapshotHolder>
    private lateinit var mLayoutManager: RecyclerView.LayoutManager


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentRecommendationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = FirebaseDatabase.getInstance().reference.child("arboles")

        val options =
            FirebaseRecyclerOptions.Builder<TreeFeedHome>().setQuery(query) {
                val snapshot = it.getValue(TreeFeedHome::class.java)
                snapshot!!.id = it.key!!
                snapshot
            }.build()

        mFirebaseAdapter = object : FirebaseRecyclerAdapter<TreeFeedHome, SnapshotHolder>(options) {
            private lateinit var mContext: Context

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SnapshotHolder {
                mContext = parent.context

                val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.item_recommendation, parent, false)
                return SnapshotHolder(view)
            }

            override fun onBindViewHolder(
                holder: SnapshotHolder,
                position: Int,
                model: TreeFeedHome
            ) {
                val treeEntity = getItem(position)

                with(holder) {
                    setListener(treeEntity)

                    binding.tvDescriptionRecommendation.text = treeEntity.state
                    binding.tvNameRecommendation.text = treeEntity.name
                    Glide.with(mContext)
                        .load(treeEntity.photoUrl)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .into(binding.ivPhoto)
                    binding.cbLike.text = treeEntity.like.keys.size.toString()
                    FirebaseAuth.getInstance().currentUser?.let {
                        binding.cbLike.isChecked = treeEntity.like
                            .containsKey(it.uid)
                    }
                }
            }

            @SuppressLint("NotifyDataSetChanged") //error interno firebase ui 8.0.0
            override fun onDataChanged() {
                super.onDataChanged()
                binding.progressBar.visibility = View.GONE
                notifyDataSetChanged()
            }

            override fun onError(error: DatabaseError) {
                super.onError(error)
                //Toast.makeText(mContext, error.message, Toast.LENGTH_SHORT).show()
            }
        }


        mLayoutManager = LinearLayoutManager(context)

        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = mLayoutManager
            adapter = mFirebaseAdapter
        }

    }

    override fun onStart() {
        super.onStart()
        mFirebaseAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        mFirebaseAdapter.stopListening()
    }

    private fun deletePost(treeFeed: TreeFeedHome) {
        //alertDialog
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.dialog_delete_snapshot)
            .setPositiveButton(R.string.dialog_delete_confirm) { _, _ ->
                val databaseReference = FirebaseDatabase.getInstance().reference.child("arboles")
                databaseReference.child(treeFeed.id).removeValue()
            }
            .setNegativeButton(R.string.dialog_delete_cancel, null)
            .show()
    }

    private fun setLike(treeFeed: TreeFeedHome, checked: Boolean) {
        val databaseReference = FirebaseDatabase.getInstance().reference.child("arboles")
        if (checked) {
            databaseReference.child(treeFeed.id).child("like")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(checked)
        } else {
            databaseReference.child(treeFeed.id).child("like")
                .child(FirebaseAuth.getInstance().currentUser!!.uid).setValue(null)
        }
    }

    inner class SnapshotHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemRecommendationBinding.bind(view)

        fun setListener(treeFeed: TreeFeedHome) {
            binding.cbDelete.setOnClickListener { deletePost(treeFeed) }
            binding.cbLike.setOnCheckedChangeListener { _, checked ->
                setLike(treeFeed, checked)
            }
        }
    }

}