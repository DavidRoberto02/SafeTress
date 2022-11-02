package app.safetress.application.recommendsModule.recyclerview.entiti

import androidx.room.PrimaryKey


data class TreeEntityHome(
    @PrimaryKey(autoGenerate = true) var id: Long = 0,
    var description: String = "",
    var name: String = "",
    var photoUrl: String = ""
)
