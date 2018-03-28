package ru.android.github.presentation.recycler

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import kotlinx.android.synthetic.main.repository_list_item.view.*
import ru.android.github.R
import ru.android.github.domain.model.Repository

class ReposAdapterDelegate : AdapterDelegate<List<Repository>>() {

    override fun onCreateViewHolder(parent: ViewGroup?): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.repository_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun isForViewType(items: List<Repository>, position: Int): Boolean {
        return true
    }

    override fun onBindViewHolder(
            items: List<Repository>,
            position: Int,
            holder: RecyclerView.ViewHolder,
            payloads: MutableList<Any>
    ) {

        holder.itemView.repo_name.text = items[position].name
        holder.itemView.repo_lang.text = items[position].language

    }


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}