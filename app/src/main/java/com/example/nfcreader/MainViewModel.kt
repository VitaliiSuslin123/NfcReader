package com.example.nfcreader

import androidx.lifecycle.ViewModel
import com.example.nfccardreader.NfcCardError
import com.example.nfccardreader.model.EmvCard
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel : ViewModel() {

    private val _message = MutableStateFlow(String())
    val message: StateFlow<String> = _message

    fun processNFCScanResult(emvCard: EmvCard) {
        _message.value =
            "\nCard number: ${emvCard.cardNumber}\nExpired date: ${emvCard.expireDate}"
    }

    fun processNfcError(error: NfcCardError, throwable: Throwable?) {
        _message.value = "Error: ${error.name}\n throwable: $throwable"
    }

}