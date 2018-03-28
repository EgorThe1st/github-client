package ru.android.github.presentation.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast
import org.jetbrains.anko.yesButton
import ru.android.github.R
import ru.android.github.domain.model.User
import ru.android.github.presentation.abstraction.UserInfoPresenter
import ru.android.github.presentation.presenter.UserInfoPresenterImpl

class UserInfoActivity : AppCompatActivity(), UserInfoPresenter.View {

    private var userInfoPresenter: UserInfoPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        userInfoPresenter = UserInfoPresenterImpl(this)
        userInfoPresenter?.start()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_user_info, menu)
        return true
    }

    override fun onBackPressed() = finish()


    override fun onDestroy() {
        super.onDestroy()
        userInfoPresenter?.handleOnDestroy()
        userInfoPresenter = null
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {
        content_user_info.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
        content_user_info.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progress_bar.visibility = View.GONE
    }


    override fun showError(error: String) {
        Snackbar.make(content_user_info, error, Snackbar.LENGTH_INDEFINITE)
                .setAction(
                        "Load from DB",
                        { userInfoPresenter?.getInfo(true) }
                )
                .show()
    }

    override fun showUserInfo(user: User) {

        with(user) {
            user_name.text = name
            repositories_count.text = reposCount
            starred_repos_count.text = starredReposCount
            gists_count.text = gistsCount
            social_count.text = "$followersCount/$followingCount"
        }

        Picasso.with(this)
                .load(user.avatarUrl)
                .fit()
                .centerCrop()
                .into(user_photo)
    }

    fun onRepositoriesClicked(view: View) {

        startActivity<UserRepositoriesActivity>()
    }

    fun onNotImplClicked(view: View) {
        toast("Not implemented yet").show()
    }

    fun onExitClicked(item: MenuItem) {
        alert(title = "Accept exit?", message = "Click OK to exit") {
            yesButton {
                userInfoPresenter?.deAuthorizeUser()
                startActivity<LoginActivity>()
                finish()
            }
        }.show()
    }

    fun onRefreshClicked(item: MenuItem) {
        userInfoPresenter?.getInfo(false)
    }

    fun onSettingsClicked(item: MenuItem) = startActivity<SettingsActivity>()

}
