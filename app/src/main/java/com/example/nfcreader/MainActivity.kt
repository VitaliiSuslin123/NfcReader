package com.example.nfcreader

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.nfcreader.ui.theme.MyTestAppTheme
import com.example.nfccardreader.NfcCardError
import com.example.nfccardreader.NfcCardManager
import com.example.nfccardreader.model.EmvCard

class MainActivity : ComponentActivity() {

    private val manager: NfcCardManager by lazy { NfcCardManager(this) }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupNfcManager()
        setContent {
            MyTestAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    ) {

                        val result = viewModel.message.collectAsState().value
                        Spacer(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(16.dp)
                        )

                        Text(text = "Scan result: $result")
                    }
                }
            }
        }
    }


    private fun setupNfcManager() {
        manager.setScannerListener(object : NfcCardManager.NfcCardScannerListener {
            override fun onScanResult(emvCard: EmvCard) {
                viewModel.processNFCScanResult(emvCard)
            }

            override fun onError(error: NfcCardError, throwable: Throwable?) {
                viewModel.processNfcError(error, throwable)
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
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyTestAppTheme {
        Greeting("Android")
    }
}