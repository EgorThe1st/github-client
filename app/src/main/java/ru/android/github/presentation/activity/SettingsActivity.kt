package ru.android.github.presentation.activity

import android.app.Service
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.redmadrobot.inputmask.MaskedTextChangedListener
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.activity_settings_main.*
import ru.android.github.R
import ru.android.github.data.repository.UserInfoRepositoryImpl
import ru.android.github.presentation.abstraction.SettingsPresenter
import ru.android.github.presentation.presenter.SettingsPresenterImpl
import ru.android.github.presentation.utils.removeKeyboardFromScreen

class SettingsActivity : AppCompatActivity(), SettingsPresenter.View {

    private var settingsPresenter: SettingsPresenter? = null
    private lateinit var listener: MaskedTextChangedListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_main)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        prepareUI()

        settingsPresenter = SettingsPresenterImpl(UserInfoRepositoryImpl(), this)
        settingsPresenter?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        settingsPresenter?.handleOnDestroy()
        settingsPresenter = null
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            super.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLoading() {

        content_settings.visibility = View.GONE
        next_button.visibility = View.GONE
        progress_bar.visibility = View.VISIBLE
    }

    override fun hideLoading() {

        progress_bar.visibility = View.GONE
        next_button.visibility = View.VISIBLE
        content_settings.visibility = View.VISIBLE
    }

    override fun showError(error: String) {

        Snackbar.make(
                parent_coord,
                error,
                Snackbar.LENGTH_LONG
        ).show()
    }

    override fun hideKeyboard() = this.removeKeyboardFromScreen()

    override fun showNetError() {

        Snackbar.make(
                parent_coord,
                getString(R.string.no_connection),
                Snackbar.LENGTH_LONG
        ).show()
    }

    override fun showUserInfo(info: Triple<String, String, String>) {

        user_name.setText(info.first)
        user_email.setText(info.second)
        listener.setText(info.third)
    }

    fun onDoneButtonClicked(view: View) {

        settingsPresenter?.updateUser(userNewData())
    }

    fun onEditClicked(item: MenuItem) {

        showKeyboard(user_name)
        user_name.requestFocus()
        user_name.setSelection(user_name.text.length)

    }

    private fun prepareUI() {

        listener = MaskedTextChangedListener(
                "+7 ([000]) [000]-[00]-[00]",
                false,
                user_bio,
                null,
                null
        )

        user_bio.addTextChangedListener(listener)
        user_bio.onFocusChangeListener = listener
        user_bio.hint = listener.placeholder()

    }

    private fun userNewData(): Triple<String, String, String> {

        return Triple(
                user_name.text.toString(),
                user_email.text.toString(),
                user_bio.text.toString()
        )
    }

    private fun showKeyboard(view: View) {
        val imm = getSystemService(Service.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(
                view.applicationWindowToken,
                InputMethodManager.SHOW_IMPLICIT,
                0
        )
    }
}
