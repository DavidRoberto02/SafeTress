package app.safetress.application.addModule.entities

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class TreeFeedHome(
    @get:Exclude var id: String = "",
    var name: String = "",
    var state: String = "",
    var photoUrl: String = "",
    var like: Map<String, Boolean> = mutableMapOf()
)
