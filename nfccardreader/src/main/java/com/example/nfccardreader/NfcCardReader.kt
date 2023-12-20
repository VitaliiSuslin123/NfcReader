package com.example.nfccardreader

import android.nfc.Tag
import android.nfc.tech.IsoDep
import android.util.Log
import com.example.nfccardreader.parser.EmvParser
import com.example.nfccardreader.utils.Provider
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import java.io.IOException

class NfcCardReader {

    private var isException = false

    fun readCard(tag: Tag?): NfcCardResponse? {
        tag?.let {
            var nfcCardResponse: NfcCardResponse? = null
            if (tag.toString() == NFC_A_TAG || tag.toString() == NFC_B_TAG) {
                try {
                    nfcCardResponse = getCardInfo(tag)
                } catch (e: Exception) {
                    Log.e(NfcCardReader::class.java.name, e.message, e)
                }
                if (!isException) {
                    if (nfcCardResponse?.emvCard != null) {
                        val emvCard = nfcCardResponse.emvCard
                        if (StringUtils.isNotBlank(emvCard?.cardNumber)) {
                            return NfcCardResponse.createResponse(emvCard)
                        } else if (emvCard?.isNfcLocked == true) {
                            return NfcCardResponse.createError(NfcCardError.CARD_LOCKED_WITH_NFC)
                        }
                    } else {
                        return NfcCardResponse.createError(NfcCardError.UNKNOWN_EMV_CARD)
                    }
                } else {
                    return NfcCardResponse.createError(NfcCardError.DONOT_MOVE_CARD_SO_FAST)
                }
            } else {
                return NfcCardResponse.createError(NfcCardError.UNKNOWN_EMV_CARD)
            }
        }
        return null
    }

    private fun getCardInfo(tag: Tag): NfcCardResponse {
        val isoDep = IsoDep.get(tag)
        val provider = Provider()
        if (isoDep == null) {
            return NfcCardResponse.createError(NfcCardError.DONOT_MOVE_CARD_SO_FAST)
        }
        isException = false
        try {
            // Open connection
            isoDep.connect()
            provider.setTagCom(isoDep)
            val parser = EmvParser(provider, true)
            val card = parser.readEmvCard()
            if (card != null) {
                return NfcCardResponse.createResponse(card)
            }
        } catch (e: IOException) {
            isException = true
            Log.e(NfcCardReader::class.java.name, e.message, e)
        } finally {
            IOUtils.closeQuietly(isoDep)
        }
        return NfcCardResponse.createError(NfcCardError.UNKNOWN_EMV_CARD)
    }

    companion object {
        private const val NFC_A_TAG = "TAG: Tech [android.nfc.tech.IsoDep, android.nfc.tech.NfcA]"
        private const val NFC_B_TAG = "TAG: Tech [android.nfc.tech.IsoDep, android.nfc.tech.NfcB]"
    }
}