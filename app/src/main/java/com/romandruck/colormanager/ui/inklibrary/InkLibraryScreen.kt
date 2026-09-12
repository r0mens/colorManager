package com.romandruck.colormanager.ui.inklibrary

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.romandruck.colormanager.data.InkLibrary


/* ============================================================
   INK LIBRARY SCREEN
   ============================================================ */

@Composable
fun InkLibraryScreen(
    viewModel: InkLibraryViewModel
) {

    val inks by viewModel.inks.collectAsState()

    val searchQuery by
        viewModel.searchQuery.collectAsState()

    val isLoading by
        viewModel.isLoading.collectAsState()

    val error by
        viewModel.error.collectAsState()


    var editingInk by remember {
        mutableStateOf<InkLibrary?>(null)
    }

    var isEditorOpen by remember {
        mutableStateOf(false)
    }

    var deletingInk by remember {
        mutableStateOf<InkLibrary?>(null)
    }


    /* ========================================================
       EDITOR
       ======================================================== */

    if (isEditorOpen) {

        InkEditorDialog(

            ink = editingInk,

            onDismiss = {

                isEditorOpen = false
                editingInk = null
            },

            onSave = { ink ->

                if (editingInk == null) {

                    viewModel.addInk(
                        ink = ink,
                        onSuccess = {

                            isEditorOpen = false
                            editingInk = null
                        }
                    )

                } else {

                    viewModel.updateInk(
                        ink = ink,
                        onSuccess = {

                            isEditorOpen = false
                            editingInk = null
                        }
                    )
                }
            }
        )
    }


    /* ========================================================
       DELETE CONFIRMATION
       ======================================================== */

    deletingInk?.let { ink ->

        AlertDialog(

            onDismissRequest = {

                deletingInk = null
            },

            title = {
                Text("Удалить краску?")
            },

            text = {

                Text(
                    "Вы действительно хотите удалить " +
                            "\"${ink.manufacturer} ${ink.name}\"?"
                )
            },

            confirmButton = {

                TextButton(

                    onClick = {

                        viewModel.deleteInk(
                            ink = ink,
                            onSuccess = {
                                deletingInk = null
                            }
                        )
                    }
                ) {

                    Text(
                        text = "Удалить",
                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        deletingInk = null
                    }
                ) {

                    Text("Отмена")
                }
            }
        )
    }


    /* ========================================================
       MAIN
       ======================================================== */

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        /* ====================================================
           TITLE
           ==================================================== */

        Text(

            text = "INK LIBRARY",

            fontSize = 24.sp,

            fontWeight = FontWeight.Bold,

            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
        )


        /* ====================================================
           SEARCH
           ==================================================== */

        OutlinedTextField(

            value = searchQuery,

            onValueChange = {
                viewModel.search(it)
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            label = {
                Text("Поиск краски")
            },

            placeholder = {
                Text("Производитель, название или код")
            },

            singleLine = true
        )


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        /* ====================================================
           ADD
           ==================================================== */

        Button(

            onClick = {

                editingInk = null
                isEditorOpen = true
            },

            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = "+ ДОБАВИТЬ КРАСКУ",
                fontWeight = FontWeight.Bold
            )
        }


        Spacer(
            modifier = Modifier.height(12.dp)
        )


        /* ====================================================
           CONTENT
           ==================================================== */

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

                    Column(
                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text = "Ошибка: $error",

                            color =
                                MaterialTheme.colorScheme.error
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        TextButton(
                            onClick = {
                                viewModel.clearError()
                            }
                        ) {

                            Text("Закрыть")
                        }
                    }
                }
            }


            inks.isEmpty() -> {

                Box(

                    modifier = Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Column(

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text = "Библиотека красок пуста",

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )

                        Spacer(
                            modifier =
                                Modifier.height(8.dp)
                        )

                        Text(
                            text =
                                "Нажмите «ДОБАВИТЬ КРАСКУ»"
                        )
                    }
                }
            }


            else -> {

                LazyColumn(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentPadding =
                        PaddingValues(
                            top = 4.dp,
                            bottom = 24.dp
                        ),

                    verticalArrangement =
                        Arrangement.spacedBy(4.dp)
                ) {

                    items(

                        items = inks,

                        key = { ink ->
                            ink.id
                        }

                    ) { ink ->

                        InkLibraryCard(

                            ink = ink,

                            onEdit = {

                                editingInk = ink
                                isEditorOpen = true
                            },

                            onDelete = {

                                deletingInk = ink
                            }
                        )
                    }
                }
            }
        }
    }
}


/* ============================================================
   INK CARD
   ============================================================ */

@Composable
private fun InkLibraryCard(

    ink: InkLibrary,

    onEdit: () -> Unit,

    onDelete: () -> Unit
) {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(8.dp)
            )
            .padding(12.dp)
    ) {

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier =
                    Modifier.weight(1f)
            ) {

                Text(

                    text = ink.manufacturer,

                    fontSize = 15.sp,

                    color = Color.Gray
                )


                Text(

                    text = ink.name,

                    fontSize = 19.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Spacer(
                    modifier =
                        Modifier.height(4.dp)
                )


                Text(

                    text = "Код: ${ink.ink_code}",

                    fontSize = 15.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color = Color.DarkGray
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
                            MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}


/* ============================================================
   ADD / EDIT DIALOG
   ============================================================ */

@Composable
private fun InkEditorDialog(

    ink: InkLibrary?,

    onDismiss: () -> Unit,

    onSave: (InkLibrary) -> Unit
) {

    var manufacturer by remember(ink) {

        mutableStateOf(
            ink?.manufacturer ?: ""
        )
    }


    var name by remember(ink) {

        mutableStateOf(
            ink?.name ?: ""
        )
    }


    var inkCode by remember(ink) {

        mutableStateOf(
            ink?.ink_code ?: ""
        )
    }


    val isEdit =
        ink != null


    val isValid =
        manufacturer.isNotBlank() &&
                name.isNotBlank() &&
                inkCode.isNotBlank()


    AlertDialog(

        onDismissRequest = onDismiss,

        title = {

            Text(
                if (isEdit)
                    "Изменить краску"
                else
                    "Добавить краску"
            )
        },

        text = {

            Column {

                OutlinedTextField(

                    value = manufacturer,

                    onValueChange = {
                        manufacturer = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Производитель")
                    },

                    placeholder = {
                        Text("Например: Siegwerk")
                    },

                    singleLine = true
                )


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


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
                        Text("Например: Black")
                    },

                    singleLine = true
                )


                Spacer(
                    modifier =
                        Modifier.height(10.dp)
                )


                OutlinedTextField(

                    value = inkCode,

                    onValueChange = {
                        inkCode = it
                    },

                    modifier =
                        Modifier.fillMaxWidth(),

                    label = {
                        Text("Код краски")
                    },

                    placeholder = {
                        Text("Например: K-100")
                    },

                    singleLine = true
                )
            }
        },

        confirmButton = {

            TextButton(

                enabled = isValid,

                onClick = {

                    onSave(

                        InkLibrary(

                            id = ink?.id ?: 0,

                            manufacturer =
                                manufacturer.trim(),

                            name =
                                name.trim(),

                            ink_code =
                                inkCode.trim()
                        )
                    )
                }
            ) {

                Text("СОХРАНИТЬ")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {

                Text("ОТМЕНА")
            }
        }
    )
}
