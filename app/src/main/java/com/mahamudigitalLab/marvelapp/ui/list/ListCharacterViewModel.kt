package com.mahamudigitalLab.marvelapp.ui.list

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
class ListCharacterViewModel @Inject constructor(
    private val repository: MarvelRepository
) : ViewModel(){

    private val _list = MutableStateFlow<ResouceState<CharacterModelResponse>>(ResouceState.Loading())
    val list: StateFlow<ResouceState<CharacterModelResponse>> = _list

    init {
        fetch()
    }
    //isso faz tal coisa
    private fun fetch() = viewModelScope.launch{
        safeFetch()
    }

    private suspend fun safeFetch() {
        try {
            val response = repository.list()
            _list.value = handleResponse(response)
        } catch (t: Throwable){
            when(t){
                is IOException -> _list.value = ResouceState.Error("Erro de Conexão com a Internet")
                else -> _list.value = ResouceState.Error("Falha na conversão de dados")
            }
        }
    }

    private fun handleResponse(response: Response<CharacterModelResponse>): ResouceState<CharacterModelResponse> {
        if (response.isSuccessful){
            response.body()?.let { values ->
                return ResouceState.Sucess(values)
            }
        }
        return ResouceState.Error(response.message())
    }
}