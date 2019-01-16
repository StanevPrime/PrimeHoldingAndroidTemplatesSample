package com.primeholding.primesampleapp.viewmodel


import androidx.lifecycle.ViewModel
import com.primeholding.primesampleapp.misc.extension.whenLoading
import com.primeholding.primesampleapp.misc.extension.whenSuccess
import com.primeholding.primesampleapp.model.ApiResult
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
    private  val loadingViewModel: LoadingViewModel
   //endregion

    init {
        val dataRequest = fetchSubject.mapToDetails(detailsRepository)

        details = dataRequest.whenSuccessWithInitial()

        this.loadingViewModel = loadingViewModel
        loadingViewModel.addLoadingObservable(dataRequest.whenLoading())
    }


    //region Input methods
    override fun fetch() {
        fetchSubject.onNext(Unit)
    }
// endregion


    override fun onCleared() {
        super.onCleared()
        //Clear all view models' bindings
        compositeDisposable.dispose()
        //Clear all LoadingViewModel's bindings
        loadingViewModel.onCleared()
    }
}

/**
 * Just Empty String
 */
val String.Companion.empty: String
    get() {
        return ""
    }

/**
 * Map an unit to a details as getting data from the [repo]
 */
private fun Observable<Unit>.mapToDetails(repo: DetailsRepository): Observable<ApiResult<String>> {
    return flatMap { repo.fetchDetails() }.share()
}

/**
 * Map to success as starting with an empty string
 */
private fun Observable<ApiResult<String>>.whenSuccessWithInitial(): Observable<String> {
    return whenSuccess().startWith(String.empty)
}
