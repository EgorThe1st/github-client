package ru.android.github.presentation.presenter

import io.reactivex.disposables.Disposable
import ru.android.github.data.abstraction.UserInfoRepository
import ru.android.github.data.utils.isOnline
import ru.android.github.domain.model.User
import ru.android.github.presentation.abstraction.SettingsPresenter
import ru.android.github.presentation.utils.schedulersIoToMain

class SettingsPresenterImpl(
        private val repo: UserInfoRepository,
        private val view: SettingsPresenter.View
) : SettingsPresenter {

    private lateinit var disposable: Disposable

    override fun start() {
        disposable = repo.getUser(true)
                .schedulersIoToMain()
                .subscribe(
                        { view.showUserInfo(parseUser(it)) },
                        { view.showError(parseError(it)) }
                )
    }

    override fun handleOnDestroy() {

        if (!disposable.isDisposed) disposable.dispose()
    }

    override fun updateUser(userData: Triple<String, String, String>) {

        view.hideKeyboard()
        if (isOnline()) updateUserFromRepo(userData) else view.showNetError()
    }

    private fun parseUser(user: User): Triple<String, String, String> {

        return Triple(
                user.name,
                user.email,
                user.bio
        )
    }

    private fun updateUserFromRepo(userData: Triple<String, String, String>) {

        view.showLoading()

        repo.updateUser(userData)
                .schedulersIoToMain()
                .subscribe(
                        {
                            view.hideLoading()
                        },
                        {
                            view.hideLoading()
                            view.showError(parseError(it))
                        }
                )
    }

}