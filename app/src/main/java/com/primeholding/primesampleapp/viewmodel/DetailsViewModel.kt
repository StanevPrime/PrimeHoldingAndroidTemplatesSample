package com.primeholding.primesampleapp.viewmodel


import androidx.lifecycle.ViewModel
import com.primeholding.primesampleapp.misc.extension.whenLoading
import com.primeholding.primesampleapp.misc.extension.whenSuccess
import com.primeholding.primesampleapp.repo.DetailsRepository
import com.primeholding.primesampleapp.viewmodel.loading.LoadingViewModel
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject


//region View Model Interfaces
interface DetailsInput {
    /**
     * Trigger the network request
     */
    fun fetch()
}

interface DetailsOutput {
    /**
     * Observable with details text
     */
    val details: Observable<String>
    /**
     * Observable whether application is loading
     */
    val isLoading: Observable<Boolean>
}

interface DetailsType {
    val input: DetailsInput
    val output: DetailsOutput

}

//endregion
class DetailsViewModel @Inject constructor(
    detailsRepository: DetailsRepository,
    loadingViewModel: LoadingViewModel
) : ViewModel(), DetailsInput, DetailsOutput, DetailsType {

//region Input Output

    override val input: DetailsInput = this
    override val output: DetailsOutput = this

//endregion

    //region Subjects consuming from Input
    private val fetchSubject = PublishSubject.create<Unit>()
//endregion

    //region Output
    override val details: Observable<String>
    override val isLoading: Observable<Boolean> = loadingViewModel.output.isLoading
//endregion

//region Locally used

    private val compositeDisposable = CompositeDisposable()

//endregion

    init {
        val dataRequest = fetchSubject
            .flatMap { detailsRepository.fetchDetails() }
            .share()

        details = dataRequest
            .whenSuccess()
            .startWith(String.empty)

        loadingViewModel.input.addLoadingObservable(dataRequest.whenLoading())
    }


    //region Input methods
    override fun fetch() {
        fetchSubject.onNext(Unit)
    }
// endregion


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}

/**
 * Just Empty String
 */
val String.Companion.empty: String
    get() {
        return ""
    }

//Sample extension returning an Observable
//private fun DetailsType.getName(): Observable<String> {
//    return Observable.just(this.javaClass.simpleName)
//            .map { it -> it.toUpperCase() }
//}
