package app.safetress.application.addModule.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import app.safetress.application.addModule.entities.TreeEntity
import com.example.safetress.R
import com.example.safetress.databinding.FragmentAddBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddFragment : Fragment() {


    private lateinit var mStorageReference: StorageReference
    private lateinit var mDatabaseReference: DatabaseReference
    private val pathTree = "arboles"
    private var mPhotoSelectedUri: Uri? = null
    private lateinit var binding: FragmentAddBinding

    private val galleryResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                mPhotoSelectedUri = it.data?.data
                binding.imgPhoto.setImageURI(mPhotoSelectedUri)
                binding.tilRecommendation.visibility = View.VISIBLE
                binding.tilDescription.visibility = View.VISIBLE
                binding.tvTitleAdd.text = getString(R.string.post_message_valid_title)
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPost.setOnClickListener {
            if (binding.etRecommendation.text!!.isNotEmpty()) {
                postTree()
            } else {
                binding.etRecommendation.requestFocus()
                binding.etRecommendation.error = getString(R.string.required_texts)
            }
        }

        binding.btnSelect.setOnClickListener { openGallery() }

        mStorageReference = FirebaseStorage.getInstance().reference
        mDatabaseReference = FirebaseDatabase.getInstance().reference.child(pathTree)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)
    }

    private fun postTree() {
        binding.progressBar.visibility = View.VISIBLE
        val key = mDatabaseReference.push().key!!

        val storageReference = mStorageReference.child(pathTree)
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child(key)
        if (mPhotoSelectedUri != null) {
            storageReference.putFile(mPhotoSelectedUri!!)
                .addOnProgressListener {
                    val progress = (100 * it.bytesTransferred / it.totalByteCount).toDouble()
                    binding.progressBar.progress = progress.toInt()
                    binding.tvTitleAdd.text = "$progress%"
                }
                .addOnCompleteListener {
                    binding.progressBar.visibility = View.INVISIBLE
                }
                .addOnSuccessListener {
                    Snackbar.make(
                        binding.root, R.string.post_publicado,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                    it.storage.downloadUrl.addOnSuccessListener { result ->
                        saveThree(key, result.toString(), binding.etDescription.text.toString().trim(),
                        binding.etRecommendation.text.toString().trim())
                        binding.tilRecommendation.visibility = View.GONE
                        binding.tilDescription.visibility = View.GONE
                        binding.tvTitleAdd.text = getString(R.string.post_message_title)
                    }
                }
                .addOnFailureListener {
                    Snackbar.make(
                        binding.root, R.string.instantanea_fallida,
                        Snackbar.LENGTH_SHORT
                    )
                        .show()
                }
        }
    }


    private fun saveThree(key: String, url: String, description: String, name: String) {
        val treeEntity = TreeEntity( photoUrl = url, description = description, name = name)
        mDatabaseReference.child(key).setValue(treeEntity)
    }
}