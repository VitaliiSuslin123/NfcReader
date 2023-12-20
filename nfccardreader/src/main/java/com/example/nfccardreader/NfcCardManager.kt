package com.example.nfccardreader

import android.app.Activity
import android.nfc.NfcAdapter
import android.nfc.Tag
import androidx.core.os.bundleOf
import com.example.nfccardreader.model.EmvCard

class NfcCardManager(private val activity: Activity) {

    private val nfcAdapter: NfcAdapter? = NfcAdapter.getDefaultAdapter(activity)
    private val nfcCardReader: NfcCardReader = NfcCardReader()

    private var listener: NfcCardScannerListener? = null

    fun setScannerListener(listener: NfcCardScannerListener) {
        this.listener = listener
    }

    fun startNfcScanner() {
        if (!isNFCEnable()) {
            listener?.onError(NfcCardError.NFC_NOT_SUPPORTED)
            return
        }

        useNfc {
            enableReaderMode(
                activity,
                { tag -> processNFCScanResult(tag) },
                NfcAdapter.FLAG_READER_NFC_A or
                        NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK or
                        NfcAdapter.FLAG_READER_NFC_B,
                bundleOf(NfcAdapter.EXTRA_READER_PRESENCE_CHECK_DELAY to 250)
            )
        }
    }

    fun stopNfcScanner() {
        useNfc { disableReaderMode(activity) }
    }

    private fun useNfc(action: NfcAdapter.() -> Unit) {
        if (isNFCEnable()) {
            nfcAdapter?.run(action)
        }
    }

    private fun isNFCEnable(): Boolean {
        return nfcAdapter != null && nfcAdapter?.isEnabled == true
    }

    private fun processNFCScanResult(tag: Tag?) {
        tag?.runCatching {
            nfcCardReader.readCard(tag)
        }?.onSuccess {
            when {
                it?.emvCard != null -> {
                    listener?.onScanResult(it.emvCard)
                }

                it?.error != null -> {
                    listener?.onError(it.error)
                }

                else -> {
                    listener?.onError(NfcCardError.UNKNOWN)
                }
            }
        }?.onFailure {
            listener?.onError(NfcCardError.UNKNOWN, it)
        }
    }

    interface NfcCardScannerListener {
        fun onScanResult(emvCard: EmvCard)
        fun onError(error: NfcCardError, throwable: Throwable? = null)
    }
}
