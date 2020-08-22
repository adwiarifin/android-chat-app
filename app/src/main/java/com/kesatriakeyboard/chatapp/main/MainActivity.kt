package com.kesatriakeyboard.chatapp.main

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.kesatriakeyboard.chatapp.R
import com.kesatriakeyboard.chatapp.databinding.ActivityMainBinding
import com.kesatriakeyboard.chatapp.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

    private lateinit var mainAdapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewClicked()
        setupRecyclerView()
        subscribeVM()

        viewModel.readMessageFromFirebase()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_logout -> {
                viewModel.doLogout()
                Intent(this, LoginActivity::class.java).also { intent ->
                    startActivity(intent)
                    finish()
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setupRecyclerView() {
        val layoutManager = LinearLayoutManager(this)
        layoutManager.stackFromEnd = true
        mainAdapter = MainAdapter(viewModel.getUserName(), viewModel)
        binding.rvChat.layoutManager = layoutManager
        binding.rvChat.adapter = mainAdapter
    }

    private fun viewClicked() {
        binding.btnSend.setOnClickListener {
            if (binding.textChat.text.toString().isNotEmpty()) {
                viewModel.postMessage(binding.textChat.text.toString())
                binding.textChat.setText("")
            } else {
                Toast.makeText(this, "Message text can't be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun subscribeVM() {
        viewModel.chatList.observe(this, Observer { chats ->
            chats?.let { mainAdapter.setChatDataList(chats) }
        })

        viewModel.chat.observe(this, Observer { chat ->
            chat?.let {
                val bottomSheet = DialogBottomSheet(it, viewModel)
                bottomSheet.show(supportFragmentManager, "")
            }
        })
    }
}