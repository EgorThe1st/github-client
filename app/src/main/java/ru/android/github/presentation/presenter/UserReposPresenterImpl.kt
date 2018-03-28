package ru.android.github.presentation.presenter

import io.reactivex.disposables.CompositeDisposable
import ru.android.github.data.utils.isOnline
import ru.android.github.domain.model.Repository
import ru.android.github.domain.usecase.GetUserReposUseCaseImpl
import ru.android.github.presentation.abstraction.UserReposPresenter
import ru.android.github.presentation.utils.schedulersIoToMain

class UserReposPresenterImpl(private val view: UserReposPresenter.View) : UserReposPresenter {

    private val getUserReposUseCase = GetUserReposUseCaseImpl()
    private val compositeDisposable = CompositeDisposable()

    override fun start() {
        view.showLoading()
        compositeDisposable.add(
                getUserReposUseCase.getRepos()
                        .schedulersIoToMain()
                        .subscribe(
                                {
                                    onDataReady(it, false)
                                },
                                {
                                    view.hideLoading()
                                    view.showError(parseError(it))
                                }
                        )
        )
    }

    override fun handleOnDestroy() = compositeDisposable.clear()

    override fun refreshReposList() {

        if (isOnline()) {
            getUserReposUseCase.updateReposList()
                    .schedulersIoToMain()
                    .subscribe(
                            { onDataReady(it, false) },
                            { view.showError(parseError(it)) }
                    )

        } else {
            view.hideLoading()
            view.showNetError()
        }


    }

    override fun filter(lang: String) {
        compositeDisposable.add(
                getUserReposUseCase.getOneLanguage(lang)
                        .schedulersIoToMain()
                        .subscribe(
                                { onDataReady(it, true) },
                                { view.showError(parseError(it)) }
                        )
        )
    }

    private fun onDataReady(repos: List<Repository>, oneLanguageRequest: Boolean) {

        view.hideLoading()

        if (repos.isNotEmpty()) {
            if (!oneLanguageRequest) {
                view.showRepos(repos as ArrayList<Repository>, extractLanguages(repos))
            } else {
                view.showFilteredRepos(repos as ArrayList<Repository>)
            }
        } else view.showEmptyList()
    }

    private fun extractLanguages(repos: List<Repository>): List<String> {

        return repos.map { it.language }.distinct()
    }
}