package com.takehome.sauravrp.viewmodels

import android.os.Parcelable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.takehome.sauravrp.repository.DirectoryRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.parcel.Parcelize

class UserDirectoryViewModel(
    private val directoryRepository: DirectoryRepository
) : ViewModel() {

    sealed class ViewState {
        class Error(val error: Throwable) : ViewState()
        object Loading : ViewState()
        class Success(val data: List<User>) : ViewState()
    }

    private val mutableViewState = MutableLiveData<ViewState>()

    val viewState: LiveData<ViewState> by lazy {
        fetchUsers()
        mutableViewState
    }

    private var disposable : Disposable? = null

    fun fetchUsers() {
        disposable?.dispose()
        directoryRepository
            .getUserDirectory()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                mutableViewState.value = ViewState.Loading
            }
            .subscribe({ result ->
                mutableViewState.value = ViewState.Success(data = result)
            }, { error ->
                mutableViewState.value = ViewState.Error(error = error)
            }).also {
                disposable = it
            }
    }

    override fun onCleared() {
        super.onCleared()
        disposable?.dispose()
    }
}

@Parcelize
@Entity
data class User(
    @PrimaryKey val id: String,
    val userName: String,
    val photoUrl: String,
    val type: String
) : Parcelable


