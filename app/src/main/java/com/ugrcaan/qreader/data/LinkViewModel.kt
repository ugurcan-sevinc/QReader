package com.ugrcaan.qreader.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LinkViewModel(application: Application): AndroidViewModel(application) {

    private val readAllTasks: LiveData<List<Link>>
    private val repository: LinkRepository

    init {
        val linkDao = LinkDatabase.getDatabase(application).linkDao()
        repository = LinkRepository(linkDao)
        readAllTasks = repository.readAllLink
    }

    fun addLink(link: Link){
        viewModelScope.launch(Dispatchers.IO){
            repository.addLink(link)
        }
    }

    fun updateLink(link: Link){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateLink(link)
        }
    }

    fun deleteLink(link: Link){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteLink(link)
        }
    }

    fun deleteAllLinks(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteAllLinks()
        }
    }

    fun getAllLinks(): LiveData<List<Link>> {
        return readAllTasks
    }
}
