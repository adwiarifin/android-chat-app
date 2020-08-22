package com.kesatriakeyboard.chatapp.model

data class Chat(
    val sender: String? = null,
    var message: String? = null,
    var firebaseKey: String? = null
) {
    fun toMap(): Map<String, String> {
        return hashMapOf(
            "sender" to sender.orEmpty(),
            "message" to message.orEmpty()
        )
    }
}