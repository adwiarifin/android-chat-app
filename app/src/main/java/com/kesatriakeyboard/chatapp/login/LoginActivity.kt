package com.kesatriakeyboard.chatapp.login

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.kesatriakeyboard.chatapp.main.MainActivity
import com.kesatriakeyboard.chatapp.R
import com.kesatriakeyboard.chatapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by lazy { ViewModelProvider(this).get(LoginViewModel::class.java) }
    private val dialogBuilder by lazy { AlertDialog.Builder(this) }
    private lateinit var dialogLoading: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        viewClicked()
        subscribeVM()
        initLoadingDialog()
    }

    override fun onStart() {
        super.onStart()
        checkSessionUser()
    }

    private fun initLoadingDialog() {
        dialogBuilder.setView(R.layout.dialog_loading)
        dialogLoading = dialogBuilder.create()
        dialogLoading.setCancelable(false)
    }

    private fun showLoading() { dialogLoading.show() }
    private fun hideLoading() { dialogLoading.dismiss() }

    private fun viewClicked() {
        binding.btnLogin.setOnClickListener {
            validation()
        }
    }

    private fun validation() {
        if (binding.textEmail.text.toString().isEmpty()) {
            binding.textEmail.error = "Invalid Email"
        } else if (binding.textPassword.text.toString().isEmpty()) {
            binding.textPassword.error = "Invalid Password"
        } else {
            viewModel.doLogin(
                binding.textEmail.text.toString(),
                binding.textPassword.text.toString()
            )
        }
    }

    private fun checkSessionUser() {
        if (viewModel.isLogin()) {
            Intent(this, MainActivity::class.java).also { intent ->
                startActivity(intent)
                finish()
            }
        }
    }

    private fun subscribeVM() {
        viewModel.loginResult.observe(this, Observer { status ->
            Log.e("STATUS", "$status")
            status?.let { s ->
                when (s) {
                    Status.LOADING -> {
                        showLoading()
                    }
                    Status.ERROR -> {
                        hideLoading()
                        Toast.makeText(this, "Wrong password or email", Toast.LENGTH_SHORT).show()
                    }
                    Status.SUCCESS -> {
                        hideLoading()
                        Intent(this, MainActivity::class.java).also { intent ->
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        })
    }
}