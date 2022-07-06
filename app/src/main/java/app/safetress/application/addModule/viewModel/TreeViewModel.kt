package app.safetress.application.addModule.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.safetress.application.addModule.TreeEntity
import app.safetress.application.addModule.model.AddInteractor

class TreeViewModel : ViewModel() {

    private val addInteractor = AddInteractor()
    fun fetRecommendsData():LiveData<MutableList<TreeEntity>>{
        val mutableData = MutableLiveData<MutableList<TreeEntity>>()
        addInteractor.getUserData().observeForever { treeList ->
            mutableData.value = treeList
        }
        return mutableData
    }

}