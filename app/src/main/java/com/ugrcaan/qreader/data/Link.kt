package com.ugrcaan.qreader.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "links")
data class Link(
    @PrimaryKey(autoGenerate = true)
    val id: Int ?= null,
    val link: String
)

