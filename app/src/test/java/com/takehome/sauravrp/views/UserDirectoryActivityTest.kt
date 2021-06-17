package com.takehome.sauravrp.views

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.squareup.picasso.Picasso
import com.takehome.sauravrp.R
import com.takehome.sauravrp.TestApplication
import com.takehome.sauravrp.TestDataHelper
import com.takehome.sauravrp.components.DaggerMockDirectoryActivityComponent
import com.takehome.sauravrp.components.modules.MockDirectoryViewModelFactoryModule
import com.takehome.sauravrp.components.modules.MockNetworkModule
import com.takehome.sauravrp.repository.toUser
import com.takehome.sauravrp.viewmodels.UserDirectoryViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric.buildActivity
import org.robolectric.RobolectricTestRunner
import org.robolectric.android.controller.ActivityController
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(
    sdk = [Build.VERSION_CODES.O_MR1],
    application = TestApplication::class
)
class UserDirectoryActivityTest {

    private lateinit var sut: ActivityController<UserDirectoryActivity>

    private var mutableMockedUsersLiveData =
        MutableLiveData<UserDirectoryViewModel.ViewState>()

    @MockK
    lateinit var viewModel: UserDirectoryViewModel

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    companion object {
        @BeforeClass
        fun doOnce() {
            initPicasso()
        }

        private fun initPicasso() {
            val picasso = Picasso.Builder(ApplicationProvider.getApplicationContext()).build()
            Picasso.setSingletonInstance(picasso)
        }
    }

    @Before
    fun setup() {
        MockKAnnotations.init(this)

        every { viewModel.viewState } returns mutableMockedUsersLiveData
        every { viewModel.fetchUsers() } returns Unit

        val component = DaggerMockDirectoryActivityComponent
            .builder()
            .mockDirectoryViewModelFactoryModule(
                MockDirectoryViewModelFactoryModule(viewModel)
            )
            .mockNetworkModule(MockNetworkModule())
            .build()

        (ApplicationProvider.getApplicationContext() as TestApplication).apply {
            mockApplicationComponent = component
            mockDirectoryComponent = component
        }

        sut = buildActivity(UserDirectoryActivity::class.java).create()

    }

    @Test
    fun `when view state sends loading, then show progress bar`() {
        sut.resume()
        sut.visible()

        mutableMockedUsersLiveData.value = UserDirectoryViewModel.ViewState.Loading

        onView(withId(R.id.no_results_container)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.list_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
    }

    @Test
    fun `when view state sends error, then show error view`() {
        sut.resume()
        sut.visible()

        mutableMockedUsersLiveData.value =
            UserDirectoryViewModel.ViewState.Error(Throwable("Some Error"))

        onView(withId(R.id.list_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.no_results_container)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.no_results_message)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.no_results_message)).check(matches(withText(R.string.error_message)))

        onView(withId(R.id.retry_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            .perform(
                ViewActions.click()
            )

        verify { viewModel.fetchUsers() }
    }

    @Test
    fun `when view state sends empty data, then show empty view`() {
        sut.resume()
        sut.visible()

        mutableMockedUsersLiveData.value =
            UserDirectoryViewModel.ViewState.Success(emptyList())

        onView(withId(R.id.list_view)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.no_results_container)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.no_results_message)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.no_results_message)).check(matches(withText(R.string.empty_message)))

        onView(withId(R.id.retry_button)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
            .perform(
                ViewActions.click()
            )

        verify { viewModel.fetchUsers() }
    }

    @Test
    fun `when view state sends non-empty data, then show results view`() {
        // fails without this, Picasso.get() is returning null in adapter
        initPicasso()

        sut.resume()
        sut.visible()

        mutableMockedUsersLiveData.value =
            UserDirectoryViewModel.ViewState.Success(
                listOf(
                    TestDataHelper.userDto().toUser()
                )
            )

        onView(withId(R.id.list_view)).check(matches(withEffectiveVisibility(Visibility.VISIBLE)))
        onView(withId(R.id.progress)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.no_results_container)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        onView(withId(R.id.no_results_message)).check(matches(withEffectiveVisibility(Visibility.GONE)))

        onView(withId(R.id.retry_button)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}

