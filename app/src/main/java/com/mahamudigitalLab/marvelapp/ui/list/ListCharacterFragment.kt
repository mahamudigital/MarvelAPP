package com.mahamudigitalLab.marvelapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.mahamudigitalLab.marvelapp.R
import com.mahamudigitalLab.marvelapp.databinding.FragmentDetailsCharacterBinding
import com.mahamudigitalLab.marvelapp.databinding.FragmentListCharacterBinding
import com.mahamudigitalLab.marvelapp.ui.adapters.CharacterAdapter
import com.mahamudigitalLab.marvelapp.ui.base.BaseFragment
import com.mahamudigitalLab.marvelapp.ui.state.ResouceState
import com.mahamudigitalLab.marvelapp.util.hide
import com.mahamudigitalLab.marvelapp.util.show
import com.mahamudigitalLab.marvelapp.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ListCharacterFragment: BaseFragment<FragmentListCharacterBinding, ListCharacterViewModel>() {
    override val viewModel: ListCharacterViewModel by viewModels()

    private val characterAdapter by lazy { CharacterAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        clickAdapter()
        collectObserver()
    }

    private fun collectObserver() = lifecycleScope.launch {
        viewModel.list.collect{ resource ->
            when(resource){
                is ResouceState.Sucess -> {
                    resource.data?.let { values->
                        binding.progressCircular.hide()
                        characterAdapter.characters = values.data.results.toList()
                    }
                }
                is ResouceState.Error -> {
                    binding.progressCircular.hide()
                    resource.message?.let { message ->
                        toast(getString(R.string.an_error_occurred))
                        Timber.tag("ListCharacterFragment").e("Error -> $message")
                    }
                }
                is ResouceState.Loading -> {
                    binding.progressCircular.show()
                }
                else ->{}
            }
        }
    }


    private fun clickAdapter() {
        characterAdapter.setOnClickListener { characterModel ->
            val action = ListCharacterFragmentDirections
                .actionListCharacterFragmentToDetailsCharacterFragment(characterModel)
            findNavController().navigate(action)
        }
    }

    private fun setupRecyclerView() = with(binding){
        rvCharacters.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentListCharacterBinding =
        FragmentListCharacterBinding.inflate(inflater,container,false)
}