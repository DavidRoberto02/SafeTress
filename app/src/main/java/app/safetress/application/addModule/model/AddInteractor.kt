package app.safetress.application.addModule.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import app.safetress.application.addModule.TreeEntity
import app.safetress.application.utils.Constants
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddInteractor {

    fun getUserData(): LiveData<MutableList<TreeEntity>> {
        val mutableData = MutableLiveData<MutableList<TreeEntity>>()
        
    }

}