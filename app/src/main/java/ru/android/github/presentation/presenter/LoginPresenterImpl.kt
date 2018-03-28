package ru.android.github.presentation.presenter

import io.reactivex.disposables.CompositeDisposable
import ru.android.github.data.utils.isOnline
import ru.android.github.domain.abstraction.AuthorizeUserUseCase
import ru.android.github.domain.usecase.AuthorizeUserUseCaseImpl
import ru.android.github.presentation.App
import ru.android.github.presentation.abstraction.LoginPresenter
import ru.android.github.presentation.utils.schedulersIoToMain

class LoginPresenterImpl(
        private val view: LoginPresenter.View
) : LoginPresenter, AuthorizeUserUseCase.Callback {

    private var authorizeUserUseCase: AuthorizeUserUseCase? = null
    private val authorizeCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun start() {
        if (!App.canExecuteSuCommands()) {
            authorizeUserUseCase = AuthorizeUserUseCaseImpl(this)
            authorizeUserUseCase?.checkIfTokenAlreadyExist()
        } else {
            view.showDeviceError()
        }
    }

    override fun authorize(name: String, password: String) {

        view.showLoading()
        view.hideKeyboard()

        if (!isOnline()) {
            view.hideLoading()
            view.showNoConnectionError()
        } else {
            authorizeUser(name, password)
        }
    }

    override fun onUserAuthorized() {
        view.hideLoading()
        view.startNextActivity()
    }

    override fun onAuthorizationFailed(error: String) {
        view.hideLoading()
        view.showError(error)
    }

    override fun onTokenAlreadyExist() {
        onUserAuthorized()
    }

    override fun handleOnDestroy() = authorizeCompositeDisposable.clear()

    private fun authorizeUser(name: String, password: String) {
        authorizeUserUseCase?.let {
            authorizeCompositeDisposable.add(
                    it.authorize(name, password)
                            .schedulersIoToMain()
                            .subscribe(
                                    {
                                        onUserAuthorized()
                                    },
                                    { error ->
                                        view.hideLoading()
                                        view.showError(parseError(error))
                                    }
                            )
            )
        }
    }
}