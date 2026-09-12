package com.romandruck.colormanager.ui.recipe

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romandruck.colormanager.data.AniloxFlex
import com.romandruck.colormanager.data.CustomRecipe
import com.romandruck.colormanager.data.RecipeType
import com.romandruck.colormanager.ui.inklibrary.InkLibraryViewModel



@Composable
fun RecipeEditorScreen(
    recipe: CustomRecipe?,
    recipeType: RecipeType,
    viewModel: RecipeViewModel,
    inkLibraryViewModel: InkLibraryViewModel,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {

    // ============================================================
    // MODE
    // ============================================================

    val isEditMode = recipe != null


    // ============================================================
    // NAME
    // ============================================================

    var name by remember(recipe) {
        mutableStateOf(
            recipe?.name ?: ""
        )
    }


    // ============================================================
    // HEX
    // ============================================================

    var hexCode by remember(recipe) {
        mutableStateOf(
            recipe?.hexCode ?: "#FFFFFF"
        )
    }


    // ============================================================
    // BASE COLOR 1
    // ============================================================

    var baseColor1 by remember(recipe) {
        mutableStateOf(
            recipe?.baseColor1 ?: ""
        )
    }

    var percentColor1 by remember(recipe) {
        mutableStateOf(
            recipe?.percentColor1?.let {
                formatEditorNumber(it)
            } ?: ""
        )
    }


    // ============================================================
    // BASE COLOR 2
    // ============================================================

    var baseColor2 by remember(recipe) {
        mutableStateOf(
            recipe?.baseColor2 ?: ""
        )
    }

    var percentColor2 by remember(recipe) {
        mutableStateOf(
            recipe?.percentColor2?.let {
                formatEditorNumber(it)
            } ?: ""
        )
    }


    // ============================================================
    // BASE COLOR 3
    // ============================================================

    var baseColor3 by remember(recipe) {
        mutableStateOf(
            recipe?.baseColor3 ?: ""
        )
    }

    var percentColor3 by remember(recipe) {
        mutableStateOf(
            recipe?.percentColor3?.let {
                formatEditorNumber(it)
            } ?: ""
        )
    }


    // ============================================================
    // BASE COLOR 4
    // ============================================================

    var baseColor4 by remember(recipe) {
        mutableStateOf(
            recipe?.baseColor4 ?: ""
        )
    }

    var percentColor4 by remember(recipe) {
        mutableStateOf(
            recipe?.percentColor4?.let {
                formatEditorNumber(it)
            } ?: ""
        )
    }


    // ============================================================
    // ANILOX
    // ============================================================

    var anilox by remember(recipe) {
        mutableStateOf(
            recipe?.anilox ?: ""
        )
    }


    // ============================================================
    // DESCRIPTION
    // ============================================================

    var description by remember(recipe) {
        mutableStateOf(
            recipe?.description ?: ""
        )
    }


    // ============================================================
    // ERROR
    // ============================================================

    var localError by remember {
        mutableStateOf<String?>(null)
    }

    val error by viewModel.error.collectAsState()


    // ============================================================
    // COLOR PICKER
    // ============================================================

    var showColorPicker by remember {
        mutableStateOf(false)
    }


    // ============================================================
    // ANILOX DIALOG
    // ============================================================

    var showAniloxDialog by remember {
        mutableStateOf(false)
    }


    // ============================================================
    // TYPE
    // ============================================================

    val typeName = when (recipeType) {

        RecipeType.FLEXO -> "FLEXO"

        RecipeType.OFFSET -> "ОФСЕТ"
    }


    // ============================================================
    // PREVIEW COLOR
    // ============================================================

    val previewColor = remember(hexCode) {

        try {

            Color(
                android.graphics.Color.parseColor(
                    hexCode
                )
            )

        } catch (e: Exception) {

            Color.LightGray
        }
    }


    // ============================================================
    // PERCENTAGES
    // ============================================================

    val p1 =
        percentColor1
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0

    val p2 =
        percentColor2
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0

    val p3 =
        percentColor3
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0

    val p4 =
        percentColor4
            .replace(",", ".")
            .toDoubleOrNull()
            ?: 0.0


    // ============================================================
    // TOTAL
    // ============================================================

    val totalPercent =
        p1 + p2 + p3 + p4


    // ============================================================
    // ERROR FROM VIEWMODEL
    // ============================================================

    LaunchedEffect(error) {

        if (error != null) {

            localError = error
        }
    }


    // ============================================================
    // SCREEN
    // ============================================================

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(16.dp)
    ) {

        // ========================================================
        // HEADER
        // ========================================================

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            OutlinedButton(
                onClick = onBack
            ) {

                Text("← Назад")
            }

            Spacer(
                modifier = Modifier.width(12.dp)
            )

            Text(
                text =
                    if (isEditMode) {
                        "Изменить рецепт"
                    } else {
                        "Новый рецепт"
                    },

                fontSize = 24.sp,

                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        // ========================================================
        // TYPE
        // ========================================================

        Text(
            text = typeName,

            fontSize = 16.sp,

            fontWeight = FontWeight.Bold,

            color = MaterialTheme.colorScheme.primary
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ========================================================
        // NAME
        // ========================================================

        OutlinedTextField(

            value = name,

            onValueChange = {

                name = it
                localError = null
            },

            modifier = Modifier.fillMaxWidth(),

            label = {
                Text("Название рецепта")
            },

            placeholder = {
                Text("Например: Красный 032")
            },

            singleLine = true
        )


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ========================================================
        // COLOR
        // ========================================================

        Text(
            text = "Цвет",

            fontSize = 18.sp,

            fontWeight = FontWeight.Bold
        )


        Spacer(
            modifier = Modifier.height(8.dp)
        )


        Row(
            modifier = Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Box(

                modifier = Modifier
                    .width(70.dp)
                    .height(55.dp)

                    .background(
                        color = previewColor,

                        shape =
                            RoundedCornerShape(8.dp)
                    )

                    .border(
                        width = 1.dp,

                        color = Color.LightGray,

                        shape =
                            RoundedCornerShape(8.dp)
                    )

                    .clickable {

                        showColorPicker = true
                    }
            )


            Spacer(
                modifier = Modifier.width(12.dp)
            )


            OutlinedButton(

                onClick = {

                    showColorPicker = true
                }
            ) {

                Text("Выбрать цвет")
            }
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )


        // ========================================================
        // RECIPE
        // ========================================================

        Text(
            text = "Состав рецепта",

            fontSize = 20.sp,

            fontWeight = FontWeight.Bold
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // ========================================================
        // COLOR 1
        // ========================================================

        RecipeEditorColorRow(

            number = 1,

            colorName = baseColor1,

            percent = percentColor1,

            inkLibraryViewModel = inkLibraryViewModel,

            onColorChange = {

                baseColor1 = it
                localError = null
            },

            onPercentChange = {

                percentColor1 = it
                localError = null
            }
        )


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        // ========================================================
        // COLOR 2
        // ========================================================

        RecipeEditorColorRow(

            number = 2,

            colorName = baseColor2,

            percent = percentColor2,

            inkLibraryViewModel = inkLibraryViewModel,

            onColorChange = {

                baseColor2 = it
                localError = null
            },

            onPercentChange = {

                percentColor2 = it
                localError = null
            }
        )


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        // ========================================================
        // COLOR 3
        // ========================================================

        RecipeEditorColorRow(

            number = 3,

            colorName = baseColor3,

            percent = percentColor3,

            inkLibraryViewModel = inkLibraryViewModel,

            onColorChange = {

                baseColor3 = it
                localError = null
            },

            onPercentChange = {

                percentColor3 = it
                localError = null
            }
        )


        Spacer(
            modifier = Modifier.height(10.dp)
        )


        // ========================================================
        // COLOR 4
        // ========================================================

        RecipeEditorColorRow(

            number = 4,

            colorName = baseColor4,

            percent = percentColor4,

            inkLibraryViewModel = inkLibraryViewModel,

            onColorChange = {

                baseColor4 = it
                localError = null
            },

            onPercentChange = {

                percentColor4 = it
                localError = null
            }
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // ========================================================
        // TOTAL
        // ========================================================

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement =
                Arrangement.End
        ) {

            Text(

                text =
                    "Итого: " +
                            "${formatEditorNumber(totalPercent)}%",

                fontSize = 18.sp,

                fontWeight = FontWeight.Bold,

                color =
                    if (totalPercent == 100.0) {

                        Color(0xFF188038)

                    } else {

                        Color.DarkGray
                    }
            )
        }


        // ========================================================
        // ANILOX FLEXO
        // ========================================================

        if (recipeType == RecipeType.FLEXO) {

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text = "Анилокс",

                fontSize = 18.sp,

                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                OutlinedTextField(

                    value = anilox,

                    onValueChange = {

                        anilox = it
                        localError = null
                    },

                    modifier =
                        Modifier.weight(1f),

                    label = {
                        Text("Анилокс")
                    },

                    placeholder = {
                        Text("Например: 120 l/cm")
                    },

                    singleLine = true
                )


                Spacer(
                    modifier = Modifier.width(8.dp)
                )


                OutlinedButton(

                    onClick = {

                        showAniloxDialog = true
                    }
                ) {

                    Text("Выбрать")
                }
            }
        }


        Spacer(
            modifier = Modifier.height(20.dp)
        )


        // ========================================================
        // DESCRIPTION
        // ========================================================

        OutlinedTextField(

            value = description,

            onValueChange = {

                description = it
                localError = null
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp),

            label = {
                Text("Описание")
            },

            placeholder = {
                Text(
                    "Дополнительная информация"
                )
            }
        )


        Spacer(
            modifier = Modifier.height(16.dp)
        )


        // ========================================================
        // ERROR
        // ========================================================

        if (localError != null) {

            Text(

                text = localError!!,

                color =
                    MaterialTheme.colorScheme.error,

                fontSize = 14.sp,

                modifier =
                    Modifier.padding(
                        bottom = 12.dp
                    )
            )
        }


        // ========================================================
        // SAVE
        // ========================================================

        Button(

            onClick = {

                // ==================================================
                // VALIDATION
                // ==================================================

                if (name.isBlank()) {

                    localError =
                        "Введите название рецепта"

                    return@Button
                }


                if (hexCode.isBlank()) {

                    localError =
                        "Выберите цвет"

                    return@Button
                }


                if (
                    baseColor1.isBlank() &&
                    baseColor2.isBlank() &&
                    baseColor3.isBlank() &&
                    baseColor4.isBlank()
                ) {

                    localError =
                        "Добавьте хотя бы один базовый цвет"

                    return@Button
                }


                if (totalPercent <= 0.0) {

                    localError =
                        "Сумма процентов должна быть больше 0"

                    return@Button
                }


                if (totalPercent > 100.0) {

                    localError =
                        "Сумма процентов не может быть больше 100%"

                    return@Button
                }


                // ==================================================
                // CREATE RECIPE
                // ==================================================

                val newRecipe = CustomRecipe(

                    name = name.trim(),

                    hexCode = hexCode.trim(),

                    type = recipeType,

                    baseColor1 =
                        baseColor1.trim(),

                    percentColor1 = p1,

                    baseColor2 =
                        baseColor2.trim(),

                    percentColor2 = p2,

                    baseColor3 =
                        baseColor3
                            .trim()
                            .ifBlank {
                                null
                            },

                    percentColor3 =
                        if (
                            baseColor3.isBlank()
                        ) {

                            null

                        } else {

                            p3
                        },

                    baseColor4 =
                        baseColor4
                            .trim()
                            .ifBlank {
                                null
                            },

                    percentColor4 =
                        if (
                            baseColor4.isBlank()
                        ) {

                            null

                        } else {

                            p4
                        },

                    // ==================================================
                    // ANILOX
                    // Только FLEXO
                    // ==================================================

                    anilox =
                        if (
                            recipeType == RecipeType.FLEXO
                        ) {

                            anilox
                                .trim()
                                .ifBlank {
                                    null
                                }

                        } else {

                            null
                        },

                    description =
                        description
                            .trim()
                            .ifBlank {
                                null
                            }
                )


                // ==================================================
                // ADD / UPDATE
                // ==================================================

                if (isEditMode) {

                    viewModel.updateRecipe(

                        recipe = newRecipe,

                        onSuccess = onSaved
                    )

                } else {

                    viewModel.addRecipe(

                        recipe = newRecipe,

                        onSuccess = onSaved
                    )
                }
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
        ) {

            Text(

                text =
                    if (isEditMode) {

                        "СОХРАНИТЬ ИЗМЕНЕНИЯ"

                    } else {

                        "СОХРАНИТЬ РЕЦЕПТ"
                    },

                fontSize = 16.sp,

                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(24.dp)
        )
    }


    // ============================================================
    // COLOR PICKER
    // ============================================================

    if (showColorPicker) {

        ColorPickerDialog(

            initialColor = previewColor,

            onColorSelected = { color ->

                val red =
                    (color.red * 255)
                        .toInt()
                        .coerceIn(0, 255)

                val green =
                    (color.green * 255)
                        .toInt()
                        .coerceIn(0, 255)

                val blue =
                    (color.blue * 255)
                        .toInt()
                        .coerceIn(0, 255)

                hexCode = String.format(
                    "#%02X%02X%02X",
                    red,
                    green,
                    blue
                )

                localError = null

                showColorPicker = false
            },

            onDismiss = {

                showColorPicker = false
            }
        )
    }


    // ============================================================
    // ANILOX PICKER
    // ============================================================

    if (
        showAniloxDialog &&
        recipeType == RecipeType.FLEXO
    ) {

        AniloxPickerDialog(

            viewModel = viewModel,

            currentAnilox = anilox,

            onAniloxSelected = {

                anilox = it

                localError = null

                showAniloxDialog = false
            },

            onDismiss = {

                showAniloxDialog = false
            }
        )
    }
}


// =================================================================
// COLOR ROW
// =================================================================

@Composable
private fun RecipeEditorColorRow(
    number: Int,
    colorName: String,
    percent: String,

    inkLibraryViewModel: InkLibraryViewModel,

    onColorChange: (String) -> Unit,
    onPercentChange: (String) -> Unit
) {

    val inks by
        inkLibraryViewModel.inks.collectAsState()

    val filteredInks = remember(
        inks,
        colorName
    ) {

        val query = colorName
            .trim()
            .lowercase()

        if (query.isBlank()) {
            emptyList()
        } else {
            inks
                .filter { ink ->

                    ink.manufacturer
                        .lowercase()
                        .contains(query) ||

                    ink.name
                        .lowercase()
                        .contains(query) ||

                    ink.ink_code
                        .lowercase()
                        .contains(query)
                }
                .take(5)
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            OutlinedTextField(

                value = colorName,

                onValueChange = onColorChange,

                modifier = Modifier.weight(1f),

                label = {
                    Text("Базовый цвет $number")
                },

                placeholder = {
                    Text("Например: White")
                },

                singleLine = true
            )


            Spacer(
                modifier = Modifier.width(8.dp)
            )


            OutlinedTextField(

                value = percent,

                onValueChange = onPercentChange,

                modifier = Modifier.width(95.dp),

                label = {
                    Text("%")
                },

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    ),

                singleLine = true
            )
        }


        // =========================================================
        // INK LIBRARY SUGGESTIONS
        // =========================================================

        if (filteredInks.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(4.dp)
            ) {

                Text(
                    text = "INK LIBRARY",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme
                        .colorScheme
                        .primary,
                    modifier = Modifier.padding(
                        horizontal = 8.dp,
                        vertical = 6.dp
                    )
                )

                filteredInks.forEach { ink ->

                    Row(

                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {

                                onColorChange(
                                    ink.name
                                )
                            }
                            .padding(
                                horizontal = 8.dp,
                                vertical = 8.dp
                            ),

                        verticalAlignment =
                            Alignment.CenterVertically
                    ) {

                        Column(
                            modifier =
                                Modifier.weight(1f)
                        ) {

                            Text(
                                text = ink.name,
                                fontSize = 15.sp,
                                fontWeight =
                                    FontWeight.Bold
                            )

                            Text(
                                text =
                                    "${ink.manufacturer} • ${ink.ink_code}",

                                fontSize = 12.sp,

                                color =
                                    Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }
}



// =================================================================
// ANILOX PICKER DIALOG
// =================================================================

@Composable
private fun AniloxPickerDialog(

    viewModel: RecipeViewModel,

    currentAnilox: String,

    onAniloxSelected: (String) -> Unit,

    onDismiss: () -> Unit
) {

    var searchText by remember {
        mutableStateOf("")
    }


    val aniloxRecipes by
        viewModel.aniloxRecipes.collectAsState()


    LaunchedEffect(searchText) {

        viewModel.searchAnilox(
            searchText
        )
    }


    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                text = "Выбор анилокс",
                fontWeight = FontWeight.Bold
            )
        },

        text = {

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {

                // ==================================================
                // SEARCH
                // ==================================================

                OutlinedTextField(

                    value = searchText,

                    onValueChange = {

                        searchText = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Поиск")
                    },

                    placeholder = {
                        Text("Например: 120")
                    },

                    singleLine = true
                )


                Spacer(
                    modifier = Modifier.height(12.dp)
                )


                // ==================================================
                // CLEAR
                // ==================================================

                if (currentAnilox.isNotBlank()) {

                    OutlinedButton(

                        onClick = {

                            onAniloxSelected("")
                        },

                        modifier =
                            Modifier.fillMaxWidth()
                    ) {

                        Text("Без анилокс")
                    }


                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )
                }


                // ==================================================
                // LIST
                // ==================================================

                Column(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .verticalScroll(
                            rememberScrollState()
                        )
                ) {

                    if (
                        aniloxRecipes.isEmpty()
                    ) {

                        Text(

                            text =
                                "Анилокс не найден",

                            color =
                                Color.Gray,

                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        16.dp
                                    ),

                            textAlign =
                                TextAlign.Center
                        )

                    } else {

                        aniloxRecipes.forEach { item ->

                            AniloxItemRow(

                                item = item,

                                selected =
                                    item.name ==
                                            currentAnilox,

                                onClick = {

                                    onAniloxSelected(
                                        item.name
                                    )
                                }
                            )
                        }
                    }
                }
            }
        },

        confirmButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Закрыть")
            }
        }
    )
}


// =================================================================
// ANILOX ITEM
// =================================================================

@Composable
private fun AniloxItemRow(

    item: AniloxFlex,

    selected: Boolean,

    onClick: () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick
            )
            .background(

                color =
                    if (selected) {

                        MaterialTheme
                            .colorScheme
                            .primaryContainer

                    } else {

                        Color.Transparent
                    },

                shape =
                    RoundedCornerShape(
                        8.dp
                    )
            )
            .padding(12.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Column(
            modifier = Modifier.weight(1f)
        ) {

            Text(

                text = item.name,

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.Bold
            )


            if (item.volume != null) {

                Text(

                    text =
                        "${formatEditorNumber(item.volume)}",

                    color = Color.Gray,

                    fontSize = 14.sp
                )
            }
        }


        if (selected) {

            Text(

                text = "✓",

                color =
                    MaterialTheme
                        .colorScheme
                        .primary,

                fontSize = 20.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }
    }
}


// =================================================================
// COLOR PICKER DIALOG
// =================================================================

@Composable
private fun ColorPickerDialog(

    initialColor: Color,

    onColorSelected: (Color) -> Unit,

    onDismiss: () -> Unit
) {

    var red by remember(initialColor) {

        mutableStateOf(
            (initialColor.red * 255)
                .toInt()
        )
    }

    var green by remember(initialColor) {

        mutableStateOf(
            (initialColor.green * 255)
                .toInt()
        )
    }

    var blue by remember(initialColor) {

        mutableStateOf(
            (initialColor.blue * 255)
                .toInt()
        )
    }


    val selectedColor = Color(

        red = red / 255f,

        green = green / 255f,

        blue = blue / 255f
    )


    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(

                text = "Выбор цвета",

                fontWeight =
                    FontWeight.Bold
            )
        },

        text = {

            Column(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                // =================================================
                // PREVIEW
                // =================================================

                Box(

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)

                        .background(

                            color =
                                selectedColor,

                            shape =
                                RoundedCornerShape(
                                    10.dp
                                )
                        )

                        .border(

                            width = 1.dp,

                            color =
                                Color.LightGray,

                            shape =
                                RoundedCornerShape(
                                    10.dp
                                )
                        )
                )


                Spacer(
                    modifier =
                        Modifier.height(20.dp)
                )


                // =================================================
                // RED
                // =================================================

                ColorSliderRow(

                    title = "Красный",

                    value = red,

                    color = Color.Red,

                    onValueChange = {

                        red = it
                    }
                )


                // =================================================
                // GREEN
                // =================================================

                ColorSliderRow(

                    title = "Зелёный",

                    value = green,

                    color = Color.Green,

                    onValueChange = {

                        green = it
                    }
                )


                // =================================================
                // BLUE
                // =================================================

                ColorSliderRow(

                    title = "Синий",

                    value = blue,

                    color = Color.Blue,

                    onValueChange = {

                        blue = it
                    }
                )


                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )


                // =================================================
                // HEX
                // =================================================

                Text(

                    text = String.format(
                        "#%02X%02X%02X",
                        red,
                        green,
                        blue
                    ),

                    fontSize = 18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    modifier =
                        Modifier.fillMaxWidth(),

                    textAlign =
                        TextAlign.Center
                )
            }
        },

        confirmButton = {

            Button(

                onClick = {

                    onColorSelected(
                        selectedColor
                    )
                }
            ) {

                Text("Выбрать")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("Отмена")
            }
        }
    )
}


// =================================================================
// COLOR SLIDER
// =================================================================

@Composable
private fun ColorSliderRow(

    title: String,

    value: Int,

    color: Color,

    onValueChange: (Int) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Row(

            modifier = Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Text(

                text = title,

                modifier =
                    Modifier.weight(1f),

                fontWeight =
                    FontWeight.Bold
            )


            Text(

                text = value.toString(),

                fontWeight =
                    FontWeight.Bold
            )
        }


        Slider(

            value = value.toFloat(),

            onValueChange = {

                onValueChange(
                    it.toInt()
                )
            },

            valueRange = 0f..255f,

            colors =
                SliderDefaults.colors(

                    thumbColor = color,

                    activeTrackColor = color
                )
        )
    }
}


// =================================================================
// FORMAT NUMBER
// =================================================================

private fun formatEditorNumber(
    value: Double
): String {

    return if (value % 1.0 == 0.0) {

        value.toInt().toString()

    } else {

        String.format(
            java.util.Locale.US,
            "%.3f",
            value
        )
            .trimEnd('0')
            .trimEnd('.')
    }
}
