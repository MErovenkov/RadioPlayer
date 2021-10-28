package com.example.radioplayer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.radioplayer.data.repository.Repository

class ViewModelFactory(private val repository: Repository): ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when(modelClass) {
            RadioViewModel::class.java -> RadioViewModel(repository) as T
            DetailRadioViewModel::class.java -> DetailRadioViewModel(repository) as T
            else -> super.create(modelClass)
        }
    }
}