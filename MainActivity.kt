package com.example.imageexplorer

import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
//import android.graphics.Camera
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
//import ImageCapture.OnImageSavedCallback

//import android.graphics.Camera?

class MainActivity : AppCompatActivity() {

    var camera: Camera?=null
    var preview: Preview?=null
    var imageCapture: ImageCapture?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PERMISSION_GRANTED){
            startCamera()
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),0)
        }

        captureBtn.setOnClickListener(){
            takePhoto()
        }


    }

    private fun takePhoto() {
    // save picture
        val photofile = File(externalMediaDirs.firstOrNull(), "Image_Explorer_App - ${System.currentTimeMillis()}.jpg")
        //val photofile= File(externalMediaDirs.firstOrNull(), child = "Image_Explorer_App - ${System.currentTimeMillis()}.jpg")
            val output = ImageCapture.OutputFileOptions.Builder(photofile).build()
            imageCapture?.takePicture(output, ContextCompat.getMainExecutor(this),object: ImageCapture.OnImageSavedCallback{
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Toast.makeText(applicationContext, "Image Saved", Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    TODO("Not yet implemented")
                }

            } )
//        imageCapture?.takePicture(output, ContextCompat.getMainExecutor(this), object:ImageCapture.OnImageCapturedCallback{
//            override fun onImageSaved(outputFileResults:ImageCapture.OutputFileResults){
//                Toast.makeText(applicationContext, "Image Saved Successfully",Toast.LENGTH_SHORT).show()
 //           }

       // })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)== PERMISSION_GRANTED){
            startCamera()
        }
        else{
            Toast.makeText(this, "Please accept the permission!", Toast.LENGTH_LONG).show()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun startCamera() {
        //Function to start camera
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener(Runnable {
            val cameraProvider=cameraProviderFuture.get()
            preview=Preview.Builder().build()
            preview?.setSurfaceProvider(cameraView.createSurfaceProvider(camera?.cameraInfo))
            imageCapture=ImageCapture.Builder().build()
           val cameraSelector=CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()
           cameraProvider.unbindAll()
            camera=cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture)
        }, ContextCompat.getMainExecutor(this))
    }
}