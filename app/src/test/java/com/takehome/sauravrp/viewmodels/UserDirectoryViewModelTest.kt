package com.takehome.sauravrp.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.common.truth.Truth.assertThat
import com.takehome.sauravrp.repository.DirectoryRepository
import io.mockk.*
import io.mockk.impl.annotations.MockK
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins.reset
import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins.setInitMainThreadSchedulerHandler
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class UserDirectoryViewModelTest {

    private lateinit var viewModel: UserDirectoryViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @MockK
    lateinit var directoryRepository: DirectoryRepository

    @Before
    fun setup() {
        /**
         * https://stackoverflow.com/questions/46549405/testing-asynchronous-rxjava-code-android
         */
        reset()
        RxJavaPlugins.reset()
        RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
        setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        MockKAnnotations.init(this)
        viewModel = UserDirectoryViewModel(directoryRepository)
    }

    @After
    fun tearDown() {
        reset()
        RxJavaPlugins.reset()
    }

    @Test
    fun `when fetch employees called, then loading return true`() {
        val mockedObserver = createUsersObsserver()

        // https://stackoverflow.com/questions/48980897/unit-testing-rxjava-doonsubscribe-and-dofinally
        val data = emptyList<User>()
        val delayer = PublishSubject.create<Boolean>()
        every { (directoryRepository.getUserDirectory()) } returns
                Single.just(data).delaySubscription(delayer)

        // when
        viewModel.viewState.observeForever(mockedObserver)

        // then
        val slot = slot<UserDirectoryViewModel.ViewState>()
        verify { mockedObserver.onChanged(capture(slot)) }

        assertThat(slot.captured).isEqualTo(UserDirectoryViewModel.ViewState.Loading)
    }


    @Test
    fun `when fetch employees called is successful, then view statue return success state`() {
        val mockedObserver = createUsersObsserver()

        val data = emptyList<User>()
        every { (directoryRepository.getUserDirectory()) } returns
                Single.just(data)

        // when
        viewModel.viewState.observeForever(mockedObserver)

        // then
        val slot = slot<UserDirectoryViewModel.ViewState>()
        verify { mockedObserver.onChanged(capture(slot)) }

        assertThat(slot.captured).isInstanceOf(UserDirectoryViewModel.ViewState.Success::class.java)
        assertThat((slot.captured as UserDirectoryViewModel.ViewState.Success).data)
            .isEqualTo(data)
    }

    @Test
    fun `when fetch employees called failed, then view statue return error state`() {
        val mockedObserver = createUsersObsserver()

        val error = Throwable("Some Error")
        every { (directoryRepository.getUserDirectory()) } returns
                Single.error(error)

        // when
        viewModel.viewState.observeForever(mockedObserver)

        // then
        val slot = slot<UserDirectoryViewModel.ViewState>()
        verify { mockedObserver.onChanged(capture(slot)) }

        assertThat(slot.captured).isInstanceOf(UserDirectoryViewModel.ViewState.Error::class.java)
        assertThat((slot.captured as UserDirectoryViewModel.ViewState.Error).error)
            .isEqualTo(error)
    }

    private fun createUsersObsserver(): Observer<UserDirectoryViewModel.ViewState> =
        spyk(Observer { })
}