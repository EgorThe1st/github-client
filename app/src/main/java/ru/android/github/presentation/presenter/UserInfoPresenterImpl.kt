package ru.android.github.presentation.presenter

import io.reactivex.disposables.CompositeDisposable
import ru.android.github.domain.abstraction.DeAuthorizeUserUseCase
import ru.android.github.domain.abstraction.GetUserInfoUseCase
import ru.android.github.domain.model.User
import ru.android.github.domain.usecase.DeAuthorizeUserUseCaseImpl
import ru.android.github.domain.usecase.GetUserInfoUseCaseImpl
import ru.android.github.presentation.abstraction.UserInfoPresenter
import ru.android.github.presentation.utils.schedulersIoToMain


class UserInfoPresenterImpl(private val view: UserInfoPresenter.View) : UserInfoPresenter {

    private var getUserInfoUseCase: GetUserInfoUseCase? = null
    private var deAuthorizeUserUseCase: DeAuthorizeUserUseCase? = null
    private val getUserCompositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun start() {
        getUserInfoUseCase = GetUserInfoUseCaseImpl()
        deAuthorizeUserUseCase = DeAuthorizeUserUseCaseImpl()
        getUserInfoUseCase?.let { subscribeToUser(it) }
    }

    override fun getInfo(fromDB: Boolean) {
        view.showLoading()
        getUserInfoUseCase?.let { subscribeToUser(it, fromDB) }
    }

    override fun deAuthorizeUser() {
        deAuthorizeUserUseCase?.deAuthorize()
    }

    override fun handleOnDestroy() = getUserCompositeDisposable.clear()


    private fun subscribeToUser(useCase: GetUserInfoUseCase, fromDB: Boolean = false) {

        getUserCompositeDisposable.add(
                useCase.getUserInfo(fromDB)
                        .schedulersIoToMain()
                        .subscribe(
                                { onDataReady(it) },
                                { onLoadFailed(parseError(it)) }
                        )
        )
    }

    private fun onDataReady(user: User) {
        view.hideLoading()
        view.showUserInfo(user)
    }

    private fun onLoadFailed(error: String) {
        view.hideProgressBar()
        view.showError(error)
    }
}