package com.example.nfccardreader

import com.example.nfccardreader.model.EmvCard

class NfcCardResponse(val emvCard: EmvCard?, val error: NfcCardError?) {

    companion object {
        fun createResponse(emvCard: EmvCard?): NfcCardResponse {
            return NfcCardResponse(emvCard, null)
        }

        fun createError(error: NfcCardError?): NfcCardResponse {
            return NfcCardResponse(null, error)
        }
    }
}
