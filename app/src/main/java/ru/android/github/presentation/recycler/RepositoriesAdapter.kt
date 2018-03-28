package ru.android.github.presentation.recycler

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import ru.android.github.domain.model.Repository

class RepositoriesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfRepos: List<Repository> = arrayListOf()

    private var delegatesManager: AdapterDelegatesManager<List<Repository>> = AdapterDelegatesManager()

    init {
        delegatesManager.addDelegate(ReposAdapterDelegate())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegatesManager.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        return delegatesManager.onBindViewHolder(listOfRepos, position, holder)
    }

    override fun getItemCount() = listOfRepos.size

    fun updateRepos(list: List<Repository>) {

        val diffResult = DiffUtil.calculateDiff(ReposDiffUtil(listOfRepos, list))

        listOfRepos = list

        diffResult.dispatchUpdatesTo(this)
    }
}