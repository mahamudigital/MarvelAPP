package com.mahamudigitalLab.marvelapp.ui.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahamudigitalLab.marvelapp.data.model.character.CharacterModel
import com.mahamudigitalLab.marvelapp.repository.MarvelRepository
import com.mahamudigitalLab.marvelapp.ui.state.ResouceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
): ViewModel() {

    private val _favorites =
        MutableStateFlow<ResouceState<List<CharacterModel>>>(ResouceState.Empty())
    val favorites: StateFlow<ResouceState<List<CharacterModel>>> = _favorites

    init {
        fetch()
    }

    private fun fetch() = viewModelScope.launch{
        repository.getAll().collectLatest { characters ->
            if(characters.isNullOrEmpty()){
                _favorites.value = ResouceState.Empty()
            } else {
                _favorites.value = ResouceState.Sucess(characters)
            }
        }
    }

    fun delete(characterModel: CharacterModel) = viewModelScope.launch {
        repository.delete(characterModel)
    }
}