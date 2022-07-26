package app.safetress.application.addModule.entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TreeEntity(
    @get:Exclude var id: String = "",
    var name: String = "",
    var description: String = "",
    var photoUrl: String = "",
    var like: Map<String, Boolean> = mutableMapOf()
)
