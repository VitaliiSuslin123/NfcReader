# NfcReader

Card reader lib in `com.example.nfccardreader`

Thanks for this source https://github.com/vickyramachandra/nfc-card-reader/tree/master

### Using library

Setup and use ```NfcCardManager``` in your Activity.

```

    private val manager: NfcCardManager by lazy { NfcCardManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNfcManager()
    }

    private fun setupNfcManager() {
        manager.setScannerListener(object : NfcCardManager.NfcCardScannerListener {
            override fun onScanResult(emvCard: EmvCard) {
                //TODO your code
            }

            override fun onError(error: NfcCardError, throwable: Throwable?) {
               //TODO your code
            }
        })
    }

    override fun onResume() {
        super.onResume()
        manager.startNfcScanner()
    }

    override fun onPause() {
        super.onPause()
        manager.stopNfcScanner()
    }
    
```
