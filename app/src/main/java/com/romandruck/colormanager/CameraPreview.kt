package com.romandruck.colormanager.ui.camera

import android.graphics.ImageFormat
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.concurrent.Executors
import kotlin.math.max
import kotlin.math.min

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    onColorMeasured: (Int, Int, Int) -> Unit
) {
    AndroidView(
        modifier = modifier,
        factory = { context ->

            val previewView = PreviewView(context)

            val cameraProviderFuture =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({

                val cameraProvider =
                    cameraProviderFuture.get()

                // ============================================
                // PREVIEW
                // ============================================

                val preview =
                    Preview.Builder()
                        .build()

                preview.surfaceProvider =
                    previewView.surfaceProvider


                // ============================================
                // IMAGE ANALYSIS
                // ============================================

                val analysisExecutor =
                    Executors.newSingleThreadExecutor()

                val imageAnalysis =
                    ImageAnalysis.Builder()
                        .setTargetResolution(
                            Size(640, 480)
                        )
                        .setBackpressureStrategy(
                            ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST
                        )
                        .build()

                imageAnalysis.setAnalyzer(
                    analysisExecutor
                ) { imageProxy ->

                    try {

                        val plane =
                            imageProxy.planes[0]

                        val buffer =
                            plane.buffer

                        val pixelStride =
                            plane.pixelStride

                        val rowStride =
                            plane.rowStride

                        val rowPadding =
                            rowStride -
                                pixelStride *
                                imageProxy.width

                        val width =
                            imageProxy.width

                        val height =
                            imageProxy.height


                        // ====================================
                        // ЦЕНТРАЛЬНАЯ ОБЛАСТЬ
                        // ====================================

                        val sampleSize =
                            min(
                                width,
                                height
                            ) / 5

                        val startX =
                            (width - sampleSize) / 2

                        val startY =
                            (height - sampleSize) / 2

                        var totalR = 0L
                        var totalG = 0L
                        var totalB = 0L

                        var count = 0


                        // ====================================
                        // ВАЖНО:
                        // Сейчас анализируем YUV.
                        //
                        // Первый plane = Y
                        // Второй/третий = цветность.
                        //
                        // Для первого теста используем
                        // центральную область.
                        // ====================================

                        val yPlane =
                            imageProxy.planes[0]

                        val uPlane =
                            imageProxy.planes[1]

                        val vPlane =
                            imageProxy.planes[2]

                        val yBuffer =
                            yPlane.buffer

                        val uBuffer =
                            uPlane.buffer

                        val vBuffer =
                            vPlane.buffer

                        val yRowStride =
                            yPlane.rowStride

                        val yPixelStride =
                            yPlane.pixelStride

                        val uRowStride =
                            uPlane.rowStride

                        val uPixelStride =
                            uPlane.pixelStride

                        val vRowStride =
                            vPlane.rowStride

                        val vPixelStride =
                            vPlane.pixelStride


                        for (y in startY until startY + sampleSize step 4) {

                            for (
                                x in startX until startX + sampleSize step 4
                            ) {

                                val yIndex =
                                    y *
                                        yRowStride +
                                        x *
                                        yPixelStride

                                val uvX =
                                    x / 2

                                val uvY =
                                    y / 2

                                val uIndex =
                                    uvY *
                                        uRowStride +
                                        uvX *
                                        uPixelStride

                                val vIndex =
                                    uvY *
                                        vRowStride +
                                        uvX *
                                        vPixelStride

                                if (
                                    yIndex >= yBuffer.limit() ||
                                    uIndex >= uBuffer.limit() ||
                                    vIndex >= vBuffer.limit()
                                ) {
                                    continue
                                }

                                val yValue =
                                    yBuffer.get(
                                        yIndex
                                    ).toInt() and 0xFF

                                val uValue =
                                    uBuffer.get(
                                        uIndex
                                    ).toInt() and 0xFF

                                val vValue =
                                    vBuffer.get(
                                        vIndex
                                    ).toInt() and 0xFF


                                // YUV → RGB

                                val yCorrected =
                                    max(
                                        0,
                                        yValue - 16
                                    )

                                val uCorrected =
                                    uValue - 128

                                val vCorrected =
                                    vValue - 128

                                var r =
                                    (
                                        1.164 *
                                            yCorrected +
                                            1.596 *
                                            vCorrected
                                    ).toInt()

                                var g =
                                    (
                                        1.164 *
                                            yCorrected -
                                            0.392 *
                                            uCorrected -
                                            0.813 *
                                            vCorrected
                                    ).toInt()

                                var b =
                                    (
                                        1.164 *
                                            yCorrected +
                                            2.017 *
                                            uCorrected
                                    ).toInt()


                                r =
                                    r.coerceIn(
                                        0,
                                        255
                                    )

                                g =
                                    g.coerceIn(
                                        0,
                                        255
                                    )

                                b =
                                    b.coerceIn(
                                        0,
                                        255
                                    )

                                totalR += r
                                totalG += g
                                totalB += b

                                count++
                            }
                        }


                        if (count > 0) {

                            val averageR =
                                (totalR / count)
                                    .toInt()

                            val averageG =
                                (totalG / count)
                                    .toInt()

                            val averageB =
                                (totalB / count)
                                    .toInt()

                            onColorMeasured(
                                averageR,
                                averageG,
                                averageB
                            )
                        }

                    } catch (_: Exception) {

                        // Не даём анализатору
                        // остановить камеру.

                    } finally {

                        imageProxy.close()
                    }
                }


                // ============================================
                // CAMERA
                // ============================================

                val cameraSelector =
                    CameraSelector.DEFAULT_BACK_CAMERA

                cameraProvider.unbindAll()

                cameraProvider.bindToLifecycle(
                    context as androidx.lifecycle.LifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalysis
                )

            }, ContextCompat.getMainExecutor(context))

            previewView
        }
    )
}
