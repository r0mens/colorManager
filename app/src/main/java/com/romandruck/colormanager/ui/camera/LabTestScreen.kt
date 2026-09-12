package com.romandruck.colormanager.ui.camera

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.pow
import kotlin.math.sqrt


// =================================================
// RGB COLOR
// =================================================

private data class RgbColor(
    val r: Int,
    val g: Int,
    val b: Int
)


// =================================================
// LAB COLOR
// =================================================

private data class LabColor(
    val l: Double,
    val a: Double,
    val b: Double
)


// =================================================
// LAB TEST SCREEN
// =================================================

@Composable
fun LabTestScreen() {

    // =============================================
    // ТЕКУЩИЙ ЦВЕТ С КАМЕРЫ
    // =============================================

    var currentRgb by remember {

        mutableStateOf(
            RgbColor(
                r = 0,
                g = 0,
                b = 0
            )
        )
    }


    // =============================================
    // ЭТАЛОН
    // =============================================

    var referenceRgb by remember {
        mutableStateOf<RgbColor?>(null)
    }

    var referenceLab by remember {
        mutableStateOf<LabColor?>(null)
    }


    // =============================================
    // ОБРАЗЕЦ
    // =============================================

    var measuredRgb by remember {
        mutableStateOf<RgbColor?>(null)
    }

    var measuredLab by remember {
        mutableStateOf<LabColor?>(null)
    }


    // =============================================
    // DELTA E
    // =============================================

    var deltaE by remember {
        mutableStateOf<Double?>(null)
    }


    // =================================================
    // ОСНОВНОЙ ЭКРАН
    // =================================================

    Box(
        modifier = Modifier.fillMaxSize()
    ) {


        // =================================================
        // КАМЕРА
        // 50% ЭКРАНА
        // =================================================

        CameraPreview(

            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f),

            onColorMeasured = { r, g, b ->

                currentRgb =
                    RgbColor(
                        r = r,
                        g = g,
                        b = b
                    )
            }
        )


        // =================================================
        // ПРИЦЕЛ
        // =================================================

        Box(
            modifier = Modifier
                .padding(
                    top = 170.dp
                )
                .align(
                    Alignment.TopCenter
                )
                .size(150.dp)
                .border(
                    width = 2.dp,
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp)
                )
        )


        // =================================================
        // ВЕРХНЯЯ ПАНЕЛЬ
        // =================================================

        Column(

            modifier = Modifier
                .align(
                    Alignment.TopCenter
                )
                .fillMaxWidth()
                .padding(10.dp)
                .background(
                    Color.Black.copy(
                        alpha = 0.70f
                    ),
                    RoundedCornerShape(12.dp)
                )
                .padding(
                    horizontal = 12.dp,
                    vertical = 10.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {


            Text(

                text = "ИЗМЕРЕНИЕ ЦВЕТА",

                color = Color.White,

                fontSize = 20.sp
            )


            Spacer(
                modifier = Modifier.height(4.dp)
            )


            Text(

                text =
                    "RGB: " +
                        "${currentRgb.r} / " +
                        "${currentRgb.g} / " +
                        "${currentRgb.b}",

                color = Color.White,

                fontSize = 14.sp
            )
        }


        // =================================================
        // НИЖНЯЯ ПАНЕЛЬ
        // =================================================

        Column(

            modifier = Modifier
                .fillMaxHeight(0.50f)
                .align(
                    Alignment.BottomCenter
                )
                .fillMaxWidth()
                .background(
                    Color.Black.copy(
                        alpha = 0.80f
                    )
                )
                .padding(
                    start = 8.dp,
                    end = 8.dp,
                    top = 8.dp,
                    bottom = 8.dp
                ),

            horizontalAlignment =
                Alignment.CenterHorizontally

        ) {


            // =================================================
            // ЭТАЛОН
            // =================================================

            Text(

                text = "ЭТАЛОН",

                color = Color.Yellow,

                fontSize = 16.sp
            )


            referenceRgb?.let { rgb ->

                Text(

                    text =
                        "RGB: " +
                            "${rgb.r} / " +
                            "${rgb.g} / " +
                            "${rgb.b}",

                    color = Color.White,

                    fontSize = 13.sp
                )
            }


            referenceLab?.let { lab ->

                Text(

                    text =
                        "Lab: " +
                            "%.2f / %.2f / %.2f".format(
                                lab.l,
                                lab.a,
                                lab.b
                            ),

                    color = Color.White,

                    fontSize = 13.sp
                )
            }


            // =================================================
            // РАССТОЯНИЕ
            // =================================================

            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // =================================================
            // ОБРАЗЕЦ
            // =================================================

            Text(

                text = "ОБРАЗЕЦ",

                color = Color.Cyan,

                fontSize = 16.sp
            )


            measuredRgb?.let { rgb ->

                Text(

                    text =
                        "RGB: " +
                            "${rgb.r} / " +
                            "${rgb.g} / " +
                            "${rgb.b}",

                    color = Color.White,

                    fontSize = 13.sp
                )
            }


            measuredLab?.let { lab ->

                Text(

                    text =
                        "Lab: " +
                            "%.2f / %.2f / %.2f".format(
                                lab.l,
                                lab.a,
                                lab.b
                            ),

                    color = Color.White,

                    fontSize = 13.sp
                )
            }


            // =================================================
            // ΔE
            // =================================================

            deltaE?.let { value ->

                Spacer(
                    modifier = Modifier.height(8.dp)
                )


                Text(

                    text =
                        "ΔE76 = %.2f".format(
                            value
                        ),

                    color = Color.Yellow,

                    fontSize = 20.sp
                )
            }


            // =================================================
            // СВОБОДНОЕ МЕСТО
            // КНОПКИ УХОДЯТ ВНИЗ
            // =================================================

            Spacer(
                modifier = Modifier.weight(1f)
            )


            // =================================================
            // КНОПКИ
            // =================================================

            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        bottom = 48.dp
                    ),

                horizontalArrangement =
                    Arrangement.spacedBy(
                        12.dp,
                        Alignment.CenterHorizontally
                    )

            ) {


                // =============================================
                // КНОПКА ЭТАЛОН
                // =============================================

                Button(

                    onClick = {

                        val rgb =
                            currentRgb


                        val lab =
                            rgbToLab(
                                rgb.r,
                                rgb.g,
                                rgb.b
                            )


                        referenceRgb =
                            rgb


                        referenceLab =
                            lab


                        // Новый эталон.
                        // Старый результат удаляем.

                        measuredRgb =
                            null

                        measuredLab =
                            null

                        deltaE =
                            null
                    }

                ) {

                    Text(
                        text = "ЭТАЛОН"
                    )
                }


                // =============================================
                // КНОПКА ИЗМЕРИТЬ
                // =============================================

                Button(

                    onClick = {

                        val reference =
                            referenceLab
                                ?: return@Button


                        val rgb =
                            currentRgb


                        val lab =
                            rgbToLab(
                                rgb.r,
                                rgb.g,
                                rgb.b
                            )


                        measuredRgb =
                            rgb


                        measuredLab =
                            lab


                        deltaE =
                            deltaE76(
                                reference,
                                lab
                            )
                    }

                ) {

                    Text(
                        text = "ИЗМЕРИТЬ"
                    )
                }
            }
        }
    }
}


// =================================================
// RGB → LAB
// =================================================

private fun rgbToLab(

    red: Int,
    green: Int,
    blue: Int

): LabColor {


    // =================================================
    // RGB NORMALIZATION
    // =================================================

    fun pivotRgb(
        value: Int
    ): Double {

        val v =
            value / 255.0


        return if (
            v > 0.04045
        ) {

            (
                (v + 0.055) /
                    1.055
                ).pow(2.4)

        } else {

            v / 12.92
        }
    }


    val r =
        pivotRgb(red)


    val g =
        pivotRgb(green)


    val b =
        pivotRgb(blue)


    // =================================================
    // RGB → XYZ
    // =================================================

    val x =
        r * 0.4124564 +
            g * 0.3575761 +
            b * 0.1804375


    val y =
        r * 0.2126729 +
            g * 0.7151522 +
            b * 0.0721750


    val z =
        r * 0.0193339 +
            g * 0.1191920 +
            b * 0.9503041


    // =================================================
    // D65 REFERENCE WHITE
    // =================================================

    val xn =
        0.95047


    val yn =
        1.00000


    val zn =
        1.08883


    // =================================================
    // XYZ PIVOT
    // =================================================

    fun pivotXyz(
        value: Double
    ): Double {

        return if (
            value > 0.008856
        ) {

            value.pow(
                1.0 / 3.0
            )

        } else {

            7.787 * value +
                16.0 / 116.0
        }
    }


    val fx =
        pivotXyz(
            x / xn
        )


    val fy =
        pivotXyz(
            y / yn
        )


    val fz =
        pivotXyz(
            z / zn
        )


    // =================================================
    // XYZ → LAB
    // =================================================

    val l =
        116.0 * fy -
            16.0


    val a =
        500.0 *
            (fx - fy)


    val labB =
        200.0 *
            (fy - fz)


    return LabColor(

        l = l,

        a = a,

        b = labB
    )
}


// =================================================
// ΔE76
// =================================================

private fun deltaE76(

    reference: LabColor,
    measured: LabColor

): Double {


    val dl =
        measured.l -
            reference.l


    val da =
        measured.a -
            reference.a


    val db =
        measured.b -
            reference.b


    return sqrt(

        dl * dl +
            da * da +
            db * db
    )
}
