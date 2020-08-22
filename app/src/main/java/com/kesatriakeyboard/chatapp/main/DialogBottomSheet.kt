package com.kesatriakeyboard.chatapp.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kesatriakeyboard.chatapp.databinding.BottomSheetDialogBinding
import com.kesatriakeyboard.chatapp.model.Chat

class DialogBottomSheet(
    private val chat: Chat,
    private val viewModel: MainViewModel
): BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        binding = BottomSheetDialogBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.etEdit.setText(chat.message.orEmpty())
        viewClicked()
    }

    private fun viewClicked(){
        binding.tvUpdate.setOnClickListener {
            chat.message = binding.etEdit.text.toString()
            viewModel.updateChat(chat)
            dismiss()
        }

        binding.tvDelete.setOnClickListener {
            viewModel.deleteChat(chat)
            dismiss()
        }

        binding.tvCancel.setOnClickListener {
            dismiss()
        }
    }
}