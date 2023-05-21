package com.example.webx

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.webx.databinding.ItemRecyclerBinding

class GithubAdapter: RecyclerView.Adapter<GithubAdapter.ViewHolder>() {
    var list:UserRepo? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = list?.get(position)
        holder.setRepo(repo)
    }
    override fun getItemCount(): Int {
        return list?.size ?: 0
    }
    class ViewHolder(val binding: ItemRecyclerBinding):RecyclerView.ViewHolder(binding.root) {
        fun setRepo(repo:UserRepoItem?){
            repo?.let{
                binding.textView.text = repo.name
                binding.textView2.text = repo.url
                Glide.with(binding.imageView).load(repo.owner.avatar_url).into(binding.imageView)
            }
        }
    }
}
