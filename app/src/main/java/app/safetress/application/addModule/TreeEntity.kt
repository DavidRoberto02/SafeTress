package app.safetress.application.addModule

data class TreeEntity(
    var name: String,
    var description: String = "",
    var photoUrl: String,
    var like: Map<String, Boolean> = mutableMapOf()
)
