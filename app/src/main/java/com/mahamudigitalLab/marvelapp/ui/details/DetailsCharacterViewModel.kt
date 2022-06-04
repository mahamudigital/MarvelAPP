package com.mahamudigitalLab.marvelapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahamudigitalLab.marvelapp.data.model.character.CharacterModel
import com.mahamudigitalLab.marvelapp.data.model.comic.ComicModelResponse
import com.mahamudigitalLab.marvelapp.repository.MarvelRepository
import com.mahamudigitalLab.marvelapp.ui.state.ResouceState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class DetailsCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
) : ViewModel() {

    private val _details =
        MutableStateFlow<ResouceState<ComicModelResponse>>(ResouceState.Loading())
    val details: StateFlow<ResouceState<ComicModelResponse>> = _details

    fun fetch(characterId: Int) = viewModelScope.launch {
        safeFetch(characterId)
    }

    private suspend fun safeFetch(characterId: Int) {
        _details.value = ResouceState.Loading()
        try {
            val response = repository.getComics(characterId)
            _details.value = handleResponse(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> _details.value = ResouceState
                    .Error("Erro de rede ou conexão internet")
                else -> _details.value = ResouceState.Error("Erro na conversão")
            }
        }
    }

    private fun handleResponse(response: Response<ComicModelResponse>): ResouceState<ComicModelResponse> {
        if (response.isSuccessful) {
            response.body()?.let { values ->
                return ResouceState.Sucess(values)
            }
        }
        return ResouceState.Error(response.message())
    }

    fun insert(characterModel: CharacterModel) = viewModelScope.launch {
        repository.insert(characterModel)
    }
}