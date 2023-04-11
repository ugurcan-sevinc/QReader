package com.ugrcaan.qreader.data

import androidx.lifecycle.LiveData

class LinkRepository(private val linkDao: LinkDao) {

    val readAllLink: LiveData<List<Link>> = linkDao.readAllLink()

    suspend fun addLink(link: Link){
        linkDao.addLink(link)
    }

    suspend fun updateLink(link: Link){
        linkDao.updateLink(link)
    }

    suspend fun deleteLink(link: Link){
        linkDao.deleteLink(link)
    }

    suspend fun deleteAllLinks(){
        linkDao.deleteAllLinks()
    }
}