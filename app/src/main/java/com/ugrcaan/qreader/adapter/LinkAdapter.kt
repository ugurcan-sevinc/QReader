package com.ugrcaan.qreader.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.ugrcaan.qreader.R
import com.ugrcaan.qreader.data.Link // Replace with your actual Link model class
import com.ugrcaan.qreader.data.LinkViewModel
import com.ugrcaan.qreader.databinding.RowOldscansLayoutBinding

class LinkAdapter: RecyclerView.Adapter<LinkAdapter.LinkViewHolder>() {

    private var links = emptyList<Link>()
    private lateinit var linkVM : LinkViewModel

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val binding = RowOldscansLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {
        val link = links[position]
        holder.bind(link)
    }

    override fun getItemCount(): Int = links.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(linkList: List<Link>){
        this.links = linkList
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun refresh(){
        notifyDataSetChanged()
    }

    inner class LinkViewHolder(private val binding: RowOldscansLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        private val linkTextView: TextView = itemView.findViewById(R.id.linkTV)
        private val copyButton: ImageButton = itemView.findViewById(R.id.copyButton)

        fun bind(link: Link) {
            linkVM = ViewModelProvider(binding.root.context as ViewModelStoreOwner)[LinkViewModel::class.java]
            linkTextView.text = link.link
            copyButton.setOnClickListener {
                // Copy link text to clipboard
                val clipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipData = ClipData.newPlainText("Link", link.link)
                clipboardManager.setPrimaryClip(clipData)
                Toast.makeText(itemView.context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
