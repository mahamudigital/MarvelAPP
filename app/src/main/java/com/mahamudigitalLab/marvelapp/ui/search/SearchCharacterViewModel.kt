package com.mahamudigitalLab.marvelapp.ui.search

import android.os.Message
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mahamudigitalLab.marvelapp.data.model.character.CharacterModelResponse
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
class SearchCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
): ViewModel() {

    private val _searchCharacter = MutableStateFlow<ResouceState<CharacterModelResponse>>(ResouceState.Empty())
    val searchCharacter: StateFlow<ResouceState<CharacterModelResponse>> = _searchCharacter

    fun fetch(nameStartsWith: String) = viewModelScope.launch{
        safeFetch(nameStartsWith)
}

    private suspend fun safeFetch(nameStartsWith: String) {
        _searchCharacter.value = ResouceState.Loading()
        try {
            val response = repository.list(nameStartsWith)
            _searchCharacter.value = handleResponse(response)
        }catch (t: Throwable){
            when(t){
                is IOException -> _searchCharacter.value = ResouceState.Error("Erro de Conexão com a Internet")
                else -> _searchCharacter.value = ResouceState.Error("Falha na conversão de dados")
            }
        }

    }

    private fun handleResponse(response: Response<CharacterModelResponse>): ResouceState<CharacterModelResponse> {
        if (response.isSuccessful){
            response.body()?. let{ values ->
                return ResouceState.Sucess(values)
            }
        }
        return ResouceState.Error(response.message())
    }
}