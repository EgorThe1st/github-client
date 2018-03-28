package ru.android.github.presentation.abstraction

import ru.android.github.domain.model.Repository

interface UserReposPresenter : BasePresenter {

    fun filter(lang: String)

    fun refreshReposList()

    interface View : BaseView {

        fun showRepos(repos: ArrayList<Repository>, langs: List<String>)

        fun showFilteredRepos(repos: ArrayList<Repository>)

        fun showNetError()

        fun showEmptyList()
    }
}