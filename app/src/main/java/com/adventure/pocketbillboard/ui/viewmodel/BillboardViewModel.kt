package com.adventure.pocketbillboard.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.adventure.pocketbillboard.data.Billboard
import com.adventure.pocketbillboard.data.BillboardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class BillboardViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = BillboardRepository(application)

    val billboards: StateFlow<List<Billboard>> = repository.billboardsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BillboardRepository.defaultBillboards
    )

    fun saveBillboard(billboard: Billboard) {
        viewModelScope.launch {
            repository.saveBillboard(billboard)
        }
    }

    fun deleteBillboard(id: String) {
        viewModelScope.launch {
            repository.deleteBillboard(id)
        }
    }

    fun toggleFavorite(id: String) {
        viewModelScope.launch {
            repository.toggleFavorite(id)
        }
    }
}
