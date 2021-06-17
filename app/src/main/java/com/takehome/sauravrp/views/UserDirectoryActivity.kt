package com.takehome.sauravrp.views

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.takehome.sauravrp.DirectoryComponentProvider
import com.takehome.sauravrp.R
import com.takehome.sauravrp.databinding.UserDirectoryViewBinding
import com.takehome.sauravrp.di.components.DaggerActivityComponent
import com.takehome.sauravrp.viewmodels.User
import com.takehome.sauravrp.viewmodels.UserDirectoryViewModel
import com.takehome.sauravrp.viewmodels.UserDirectoryViewModelFactory
import com.takehome.sauravrp.views.adapter.UserDirectoryAdapter
import timber.log.Timber
import javax.inject.Inject

class UserDirectoryActivity : AppCompatActivity(),
        UserDirectoryAdapter.UserSelectionListener {

    @Inject
    lateinit var employeeDirectoryViewModelFactory: UserDirectoryViewModelFactory

    private lateinit var binding: UserDirectoryViewBinding

    private lateinit var model: UserDirectoryViewModel

    private val listAdapter = UserDirectoryAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDirectoryViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        DaggerActivityComponent
            .factory()
            .create((applicationContext as DirectoryComponentProvider).directoryComponent())
            .inject(this)

        binding.listView.apply {
            adapter = listAdapter
        }

        binding.retryButton.setOnClickListener {
            model.fetchUsers()
        }

        val viewModel: UserDirectoryViewModel by viewModels { employeeDirectoryViewModelFactory }

        viewModel.viewState.observe(this, Observer {
            when (it) {
                is UserDirectoryViewModel.ViewState.Error -> showError(it.error)
                UserDirectoryViewModel.ViewState.Loading -> showLoading()
                is UserDirectoryViewModel.ViewState.Success ->  {
                    if(it.data.isEmpty()) {
                        showEmpty()
                    } else {
                        showUsers(it.data)
                    }
                }
            }
        })

        model = viewModel
    }

    private fun showEmpty() {
        Timber.d("Showing empty state")
        binding.apply {
            listView.isVisible = false
            progress.isVisible = false

            noResultsContainer.isVisible = true
            noResultsMessage.text = getString(R.string.empty_message)
        }
    }

    private fun showError(error: Throwable) {
        Timber.d("Showing error state")
        binding.apply {
            listView.isVisible = false
            progress.isVisible = false

            noResultsContainer.isVisible = true
            noResultsMessage.text = getString(R.string.error_message)

            Timber.e(error)
        }
    }

    private fun showLoading() {
        Timber.d("Showing loading state")
        binding.apply {
            listView.isVisible = false
            progress.isVisible = true
            noResultsContainer.isVisible = false
        }
    }

    private fun showUsers(data: List<User>) {
        Timber.d("Showing users")
        binding.apply {
            listView.isVisible = true
            progress.isVisible = false
            noResultsContainer.isVisible = false
        }
        listAdapter.submitList(data)
    }

    override fun cardItemSelected(user: User) {
        // does nothing
    }
}