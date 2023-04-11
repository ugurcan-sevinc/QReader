package com.ugrcaan.qreader

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.ugrcaan.qreader.adapter.LinkAdapter
import com.ugrcaan.qreader.data.Link
import com.ugrcaan.qreader.data.LinkViewModel
import com.ugrcaan.qreader.databinding.ActivityMainBinding
import java.io.ByteArrayOutputStream
import com.ugrcaan.qreader.ScanUtils


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var linkVM: LinkViewModel
    private lateinit var adapter: LinkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = LinkAdapter()
        binding.savedLinksList.adapter = adapter
        binding.savedLinksList.layoutManager = LinearLayoutManager(this)

        linkVM = ViewModelProvider(this)[LinkViewModel::class.java]

        linkVM.getAllLinks().observe(this) { tasks ->
            adapter.setData(tasks)
        }

        binding.clearAllLinks.setOnClickListener {


            val alertDialog = AlertDialog.Builder(this)
                .setTitle("Are You Sure ?")
                .setNegativeButton("NO") { dialog, _ ->
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setPositiveButton("YES") { dialog, _ ->
                    linkVM.deleteAllLinks()
                    adapter.refresh()
                    Toast.makeText(this, "Link History Cleared!", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                .setCancelable(true)
                .create()
            alertDialog.show()
        }

        binding.btnCamera.setOnClickListener {
            onCameraButtonClick()
        }

        binding.btnGallery.setOnClickListener{
            onGalleryButtonClick()
        }

        //TODO 10.04.2023, 17:28 ADD UPLOAD FROM GALLERY SUPPORT
    }

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else {
            val link = Link(link = result.contents)
            linkVM.addLink(link = link)
            showResultDialog(this, result.contents)
        }
    }

    private fun onCameraButtonClick() {
        val options = ScanOptions()
        options.setPrompt("Scan a barcode")
        options.setCameraId(0)
        options.captureActivity = CaptureActivityPortrait::class.java
        options.setBeepEnabled(false)
        options.setBarcodeImageEnabled(true)
        options.setOrientationLocked(true)
        barcodeLauncher.launch(options)
    }

    private fun onGalleryButtonClick() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1001)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            // Gallery image selected
            data?.data?.let { uri ->
                // Convert gallery image URI to bitmap
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                // Process the bitmap for QR code scanning
                processImageForQrCode(bitmap)
            }
        }
    }

    private fun processImageForQrCode(bitmap: Bitmap) {
        // Convert bitmap to byte array
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        // Decode byte array to bitmap with RGB_565 format for QR code scanning
        val options = BitmapFactory.Options()
        options.inPreferredConfig = Bitmap.Config.RGB_565
        val decodedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size, options)
        // Start QR code scanning with the decoded bitmap
        val result = ScanUtils.decodeQrCode(decodedBitmap)
        if (result == null) {
            Toast.makeText(this, "No QR code found in the image", Toast.LENGTH_LONG).show()
        } else {
            // Handle the QR code result
            val link = Link(link = result)
            linkVM.addLink(link = link)
            showResultDialog(this, result)
        }
    }



    private fun showResultDialog(context: Context, link: String) {
        val domainExtensions = listOf(".com", ".net", ".org", ".edu", ".gov", ".co.uk", ".ca", ".au", ".us", ".info", ".biz", ".io", ".org.uk", ".online", ".xyz")
        var formattedLink = link

        if (domainExtensions.any { formattedLink.contains(it) }) {
            if (!formattedLink.startsWith("http://") && !formattedLink.startsWith("https://")) {
                formattedLink = "http://$formattedLink"
            }
        }


        val alertDialog = AlertDialog.Builder(context)
            .setTitle("QR Code Result")
            .setMessage(formattedLink)
            .setNegativeButton("Copy Link") { dialog, _ ->
                val clipboard =
                    context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Link", formattedLink)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Link copied to clipboard", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setPositiveButton("Open in Browser") { dialog, _ ->
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(formattedLink)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(Intent.createChooser(intent, "Open link with..."))
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
        alertDialog.show()
    }

    /*
    private fun onButtonClick() {
        val options = arrayOf("Camera", "Gallery") // Add gallery option
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Choose Image Source")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Camera option
                        val scanOptions = ScanOptions()
                        scanOptions.setPrompt("Scan a barcode")
                        scanOptions.setCameraId(0)
                        scanOptions.captureActivity = CaptureActivityPortrait::class.java
                        scanOptions.setBeepEnabled(false)
                        scanOptions.setBarcodeImageEnabled(true)
                        scanOptions.setOrientationLocked(true)
                        barcodeLauncher.launch(scanOptions)
                    }
                    1 -> {
                        // Gallery option
                        onGalleryButtonClick()
                    }
                }
                dialog.dismiss()
            }
            .setCancelable(true)
            .create()
        alertDialog.show()
    }*/

}
