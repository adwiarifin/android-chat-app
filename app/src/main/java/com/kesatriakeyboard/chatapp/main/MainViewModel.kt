package com.kesatriakeyboard.chatapp.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.kesatriakeyboard.chatapp.model.Chat

class MainViewModel: ViewModel() {

    private val firebaseAuth by lazy { FirebaseAuth.getInstance() }
    private val databaseReference by lazy { FirebaseDatabase.getInstance().reference }
    private lateinit var valueEventListener: ValueEventListener

    private val _chatList by lazy { MutableLiveData<List<Chat>>() }
    val chatList: LiveData<List<Chat>> get() = _chatList

    private val _chat by lazy { MutableLiveData<Chat>() }
    val chat: LiveData<Chat> get() = _chat

    private val TAG = "MainViewModel"
    private val MESSAGE = "message"

    fun doLogout() {
        firebaseAuth.signOut()
    }

    fun getUserName(): String {
        val email = firebaseAuth.currentUser?.email.orEmpty()
        return email.split('@').first()
    }

    fun onChatLongPress(chat: Chat) {
        _chat.value = chat
    }

    fun postMessage(message: String) {
        val chat = Chat(getUserName(), message)
        databaseReference.child(MESSAGE).push().setValue(chat)
    }

    fun readMessageFromFirebase() {
        valueEventListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dataChat = snapshot.children.toMutableList().map { child ->
                    val chat = child.getValue(Chat::class.java)
                    chat?.firebaseKey = child.key
                    chat ?: Chat()
                }

                _chatList.value = dataChat
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message)
            }
        }

        databaseReference.child(MESSAGE).addValueEventListener(valueEventListener)
    }

    fun updateChat(chat: Chat) {
        val childUpdater = hashMapOf<String, Any>(
            "$MESSAGE/${chat.firebaseKey}" to chat.toMap()
        )
        databaseReference.updateChildren(childUpdater)
    }

    fun deleteChat(chat: Chat) {
        databaseReference.child(MESSAGE).child(chat.firebaseKey.orEmpty()).removeValue()
    }
}