package ru.android.github.presentation.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.activity_user_repositories.*
import org.jetbrains.anko.toast
import ru.android.github.R
import ru.android.github.domain.model.Repository
import ru.android.github.presentation.abstraction.UserReposPresenter
import ru.android.github.presentation.presenter.UserReposPresenterImpl
import ru.android.github.presentation.recycler.RepositoriesAdapter


class UserRepositoriesActivity : AppCompatActivity(),
        UserReposPresenter.View,
        PopupMenu.OnMenuItemClickListener {

    private lateinit var adapter: RepositoriesAdapter

    private var userReposPresenter: UserReposPresenter? = null
    private var langsArray: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_repositories)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        userReposPresenter = UserReposPresenterImpl(this)
        userReposPresenter?.start()

        prepareUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        userReposPresenter
        userReposPresenter = null
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
        if (layout_refresh.isRefreshing) layout_refresh.isRefreshing = false
    }

    override fun showError(error: String) {
        toast(error)
    }

    override fun showNetError() {
        Snackbar.make(
                layout_parent,
                getString(R.string.no_connection),
                Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showRepos(repos: ArrayList<Repository>, langs: List<String>) {

        langsArray = langs
        adapter.updateRepos(repos)
    }

    override fun showFilteredRepos(repos: ArrayList<Repository>) {

        adapter.updateRepos(repos)
    }

    override fun showEmptyList() {
        toast(getString(R.string.toast_no_repos))
    }

    override fun onMenuItemClick(menuItem: MenuItem): Boolean {
        userReposPresenter?.filter(menuItem.title.toString())
        return true
    }

    private fun prepareUI() {

        adapter = RepositoriesAdapter()
        recycler_repositories.layoutManager = LinearLayoutManager(this)
        recycler_repositories.adapter = adapter

        layout_refresh.setColorSchemeColors(getColor(R.color.colorAccent))
        layout_refresh.setOnRefreshListener { userReposPresenter?.refreshReposList() }
    }

    fun showReposMenu(view: View) {

        val popup = PopupMenu(this, view)
        popup.setOnMenuItemClickListener(this)

        if (langsArray.size > 1) {

            langsArray.mapIndexed { index, _ -> popup.menu.add(langsArray[index]) }
            popup.show()
        }
    }
}
