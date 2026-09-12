package com.romandruck.colormanager.ui.anilox

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions
import com.romandruck.colormanager.data.AniloxFlex


@Composable
fun AniloxFlexScreen(
    viewModel: AniloxFlexViewModel
) {

    val aniloxList by
        viewModel.aniloxList.collectAsState()

    val searchQuery by
        viewModel.searchQuery.collectAsState()

    val isLoading by
        viewModel.isLoading.collectAsState()

    val error by
        viewModel.error.collectAsState()


    var showDialog by
        remember {
            mutableStateOf(false)
        }


    var editingAnilox by
        remember {
            mutableStateOf<AniloxFlex?>(null)
        }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // =====================================================
        // TITLE
        // =====================================================

        Text(
            text = "ANILOX FLEX",

            fontSize = 24.sp,

            fontWeight =
                FontWeight.Bold,

            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
        )


        // =====================================================
        // SEARCH
        // =====================================================

        OutlinedTextField(

            value = searchQuery,

            onValueChange = {
                viewModel.search(it)
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            label = {
                Text("Поиск анилокса")
            },

            singleLine = true
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =====================================================
        // ADD
        // =====================================================

        Button(

            onClick = {

                editingAnilox = null
                showDialog = true
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "+ ДОБАВИТЬ АНИЛОКС",

                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        // =====================================================
        // CONTENT
        // =====================================================

        when {

            isLoading -> {

                Box(
                    modifier = Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }


            error != null -> {

                Box(
                    modifier = Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "Ошибка: $error",

                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }


            aniloxList.isEmpty() -> {

                Box(
                    modifier = Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(
                        text =
                            "Анилокс пока не добавлен"
                    )
                }
            }


            else -> {

                LazyColumn(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentPadding =
                        PaddingValues(
                            top = 8.dp,
                            bottom = 24.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(8.dp)
                ) {

                    items(

                        items = aniloxList,

                        key = {
                            it.name
                        }

                    ) { anilox ->

                        AniloxCard(

                            anilox = anilox,

                            onEdit = {

                                editingAnilox =
                                    anilox

                                showDialog = true
                            },

                            onDelete = {

                                viewModel.deleteAnilox(
                                    anilox
                                )
                            }
                        )
                    }
                }
            }
        }
    }


    // =========================================================
    // ADD / EDIT DIALOG
    // =========================================================

    if (showDialog) {

        AniloxEditDialog(

            anilox = editingAnilox,

            onDismiss = {

                showDialog = false
                editingAnilox = null
            },

            onSave = { name, volume ->

                if (editingAnilox == null) {

                    viewModel.addAnilox(
                        name = name,
                        volume = volume
                    )

                } else {

                    viewModel.updateAnilox(
                        name = name,
                        volume = volume
                    )
                }

                showDialog = false
                editingAnilox = null
            }
        )
    }
}


/* ============================================================
   ANILOX CARD
   ============================================================ */

@Composable
private fun AniloxCard(
    anilox: AniloxFlex,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Column(
            modifier =
                Modifier.weight(1f)
        ) {

            Text(

                text = anilox.name,

                fontSize = 18.sp,

                fontWeight =
                    FontWeight.Bold
            )


            Spacer(
                modifier =
                    Modifier.height(4.dp)
            )


            Text(

                text = anilox.volume?.let {

                    "Объём: ${formatVolume(it)} см³/м²"

                } ?: "Объём не указан",

                fontSize = 14.sp,

                color = Color.Gray
            )
        }


        Column(
            horizontalAlignment =
                Alignment.End
        ) {

            TextButton(
                onClick = onEdit
            ) {

                Text("Изменить")
            }


            TextButton(
                onClick = onDelete
            ) {

                Text(
                    text = "Удалить",

                    color =
                        MaterialTheme
                            .colorScheme
                            .error
                )
            }
        }
    }
}


/* ============================================================
   EDIT DIALOG
   ============================================================ */

@Composable
private fun AniloxEditDialog(

    anilox: AniloxFlex?,

    onDismiss: () -> Unit,

    onSave: (
        String,
        Double?
    ) -> Unit
) {

    var name by remember(anilox) {

        mutableStateOf(
            anilox?.name ?: ""
        )
    }


    var volume by remember(anilox) {

        mutableStateOf(
            anilox?.volume?.toString() ?: ""
        )
    }


    val isEditing =
        anilox != null


    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                if (isEditing)
                    "Изменить анилокс"
                else
                    "Новый анилокс"
            )
        },

        text = {

            Column {

                OutlinedTextField(

                    value = name,

                    onValueChange = {
                        name = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Название")
                    },

                    placeholder = {
                        Text("Например: 360 LPI")
                    },

                    singleLine = true
                )


                Spacer(
                    modifier =
                        Modifier.height(12.dp)
                )


                OutlinedTextField(

                    value = volume,

                    onValueChange = {
                        volume = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Объём")
                    },

                    placeholder = {
                        Text("Например: 4.5")
                    },

                    keyboardOptions =
                        KeyboardOptions(
                            keyboardType =
                                KeyboardType.Decimal
                        ),

                    singleLine = true
                )
            }
        },

        confirmButton = {

            Button(

                onClick = {

                    val cleanName =
                        name.trim()

                    val cleanVolume =
                        volume
                            .replace(",", ".")
                            .trim()
                            .toDoubleOrNull()

                    if (
                        cleanName.isNotEmpty()
                    ) {

                        onSave(
                            cleanName,
                            cleanVolume
                        )
                    }
                },

                enabled =
                    name.trim().isNotEmpty()
            ) {

                Text("Сохранить")
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


/* ============================================================
   FORMAT VOLUME
   ============================================================ */

private fun formatVolume(
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
