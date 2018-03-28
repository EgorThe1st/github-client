package ru.android.github.presentation.activity

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateInterpolator
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activivty_login_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.noButton
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.yesButton
import ru.android.github.R
import ru.android.github.presentation.abstraction.LoginPresenter
import ru.android.github.presentation.presenter.LoginPresenterImpl
import ru.android.github.presentation.utils.MyTextWatcher
import ru.android.github.presentation.utils.removeKeyboardFromScreen


class LoginActivity : AppCompatActivity(), LoginPresenter.View {

    private var loginPresenter: LoginPresenter? = null
    private var canStartAnimation: Boolean = true
    private lateinit var disposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activivty_login_main)
        supportActionBar?.title = getString(R.string.activity_authorization)

        loginPresenter = LoginPresenterImpl(this)
        loginPresenter?.start()

        prepareUI()
    }

    override fun onDestroy() {
        super.onDestroy()

        loginPresenter?.handleOnDestroy()
        loginPresenter = null

        if (!disposable.isDisposed) disposable.dispose()

    }

    override fun showLoading() {
        progress_bar.visibility = View.VISIBLE
        next_button.isClickable = false
    }

    override fun hideLoading() {
        progress_bar.visibility = View.GONE
        next_button.isClickable = true
    }

    override fun showError(error: String) {
        Snackbar.make(parent_coord, error, Snackbar.LENGTH_LONG).show()
    }

    override fun showNoConnectionError() {
        showError(getString(R.string.no_connection))
    }

    override fun startNextActivity() {

        startActivity<UserInfoActivity>()
        finish()
    }

    override fun showDeviceError() {

        alert(getString(R.string.error_root), "Error!") {
            yesButton {}
            noButton { finish() }
            isCancelable = false
        }.show()
    }

    override fun hideKeyboard() = this.removeKeyboardFromScreen()

    private fun prepareUI() {

        hideNextButton()

        animation_view.useHardwareAcceleration()
        animation_view.enableMergePathsForKitKatAndAbove(true)

        val observableNameEditText = createObservableNameEditText()
        val observablePasswordEditText = createObservablePasswordEditText()

        disposable = Observable.combineLatest(
                observableNameEditText,
                observablePasswordEditText,
                BiFunction { nameIsCorrect: Boolean, passwordIsCorrect: Boolean ->
                    nameIsCorrect && passwordIsCorrect
                }
        ).subscribe { handleNextButtonVisibility(it) }


        next_button.setOnClickListener {
            onNextButtonClicked()
        }
    }

    private fun handleNextButtonVisibility(userDataIsCorrect: Boolean) {

        if (userDataIsCorrect) {
            if (canStartAnimation) showNextButtonWithAnimation()
        } else {
            hideNextButton()
        }
    }

    private fun showNextButtonWithAnimation() {

        next_button.animate()
                .scaleX(1f)
                .scaleY(1f)
                .setInterpolator(AccelerateInterpolator())
                .setDuration(100)
                .withStartAction {
                    canStartAnimation = false
                    next_button.visibility = View.VISIBLE
                }
                .withEndAction { canStartAnimation = true }
                .start()
    }

    private fun hideNextButton() {

        next_button.visibility = View.GONE
        next_button.scaleX = 0f
        next_button.scaleY = 0f
    }

    private fun onNextButtonClicked() {

        loginPresenter?.authorize(
                user_name.text.toString(),
                user_password.text.toString()
        )
    }

    private fun createObservableNameEditText(): Observable<Boolean> {

        return Observable.create<Boolean> { emitter ->

            val textWatcher = object : TextWatcher by MyTextWatcher {

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    emitter.onNext(!isNameValid(p0))
                }
            }

            user_name.addTextChangedListener(textWatcher)

            emitter.setCancellable {
                user_name.removeTextChangedListener(textWatcher)
            }
        }
    }

    private fun createObservablePasswordEditText(): Observable<Boolean> {

        return Observable.create { emitter ->

            val textWatcher = object : TextWatcher by MyTextWatcher {

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                    emitter.onNext(isPasswordValid(p0))
                }
            }

            user_password.addTextChangedListener(textWatcher)

            emitter.setCancellable {
                user_password.removeTextChangedListener(textWatcher)
            }
        }
    }

    private fun isNameValid(name: CharSequence?) = name.isNullOrEmpty()

    private fun isPasswordValid(password: CharSequence?): Boolean {

        return password != null &&
                password.length > 6 &&
                password.any(Char::isDigit) &&
                password.any(Char::isLowerCase)
    }
}
