package ru.android.github.presentation.recycler

import android.support.v7.util.DiffUtil
import ru.android.github.domain.model.Repository

class ReposDiffUtil(
        private val oldRepos: List<Repository>,
        private val newRepos: List<Repository>
) : DiffUtil.Callback() {


    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldRepos[oldItemPosition].name == newRepos[newItemPosition].name
    }

    override fun getOldListSize(): Int = oldRepos.size

    override fun getNewListSize(): Int = newRepos.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

        return oldRepos[oldItemPosition] == newRepos[newItemPosition]
    }
}