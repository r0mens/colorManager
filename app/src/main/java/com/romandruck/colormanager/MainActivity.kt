
package com.romandruck.colormanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import androidx.lifecycle.viewmodel.compose.viewModel

import com.romandruck.colormanager.data.CustomRecipe
import com.romandruck.colormanager.data.PantoneDatabase
import com.romandruck.colormanager.data.PantoneItem
import com.romandruck.colormanager.data.PantoneRepository
import com.romandruck.colormanager.data.RecipeRepository
import com.romandruck.colormanager.data.RecipeType

import com.romandruck.colormanager.ui.anilox.AniloxFlexScreen
import com.romandruck.colormanager.ui.anilox.AniloxFlexViewModel
import com.romandruck.colormanager.ui.anilox.AniloxFlexViewModelFactory

import com.romandruck.colormanager.ui.inklibrary.InkLibraryScreen
import com.romandruck.colormanager.ui.inklibrary.InkLibraryViewModel
import com.romandruck.colormanager.ui.inklibrary.InkLibraryViewModelFactory

import com.romandruck.colormanager.ui.pantone.PantoneViewModel
import com.romandruck.colormanager.ui.pantone.PantoneViewModelFactory

import com.romandruck.colormanager.ui.recipe.RecipeEditorScreen
import com.romandruck.colormanager.ui.recipe.RecipeViewModel
import com.romandruck.colormanager.ui.recipe.RecipeViewModelFactory

import com.romandruck.colormanager.ui.theme.ColorManagerTheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            ColorManagerTheme {

                val database = remember {

                    PantoneDatabase.getInstance(
                        applicationContext
                    )
                }

                val pantoneRepository = remember {

                    PantoneRepository(database)
                }

                val pantoneFactory = remember {

                    PantoneViewModelFactory(
                        pantoneRepository
                    )
                }

                val pantoneViewModel: PantoneViewModel =
                    viewModel(
                        factory = pantoneFactory
                    )

                val recipeRepository = remember {

                    RecipeRepository(database)
                }

                val recipeFactory = remember {

                    RecipeViewModelFactory(
                        recipeRepository
                    )
                }

                val recipeViewModel: RecipeViewModel =
                    viewModel(
                        factory = recipeFactory
                    )

                val inkLibraryFactory = remember {

                    InkLibraryViewModelFactory(
                        recipeRepository
                    )
                }

                val inkLibraryViewModel: InkLibraryViewModel =
                    viewModel(
                        factory = inkLibraryFactory
                    )

                val aniloxFlexFactory = remember {

                    AniloxFlexViewModelFactory(
                        recipeRepository
                    )
                }

                val aniloxFlexViewModel: AniloxFlexViewModel =
                    viewModel(
                        factory = aniloxFlexFactory
                    )

                PantoneApp(

                    viewModel = pantoneViewModel,

                    recipeViewModel = recipeViewModel,

                    inkLibraryViewModel =
                        inkLibraryViewModel,

                    aniloxFlexViewModel =
                        aniloxFlexViewModel
                )
            }
        }
    }
}


/* ============================================================
   MAIN APP
   ============================================================ */

@Composable
fun PantoneApp(

    viewModel: PantoneViewModel,

    recipeViewModel: RecipeViewModel,

    inkLibraryViewModel: InkLibraryViewModel,

    aniloxFlexViewModel: AniloxFlexViewModel
) {

    var selectedColor by remember {

        mutableStateOf<PantoneItem?>(null)
    }

    var selectedRecipe by remember {

        mutableStateOf<CustomRecipe?>(null)
    }

    var selectedRecipeType by remember {

        mutableStateOf<RecipeType?>(null)
    }

    var isRecipeEditorOpen by remember {

        mutableStateOf(false)
    }

    var selectedTab by remember {

        mutableStateOf(0)
    }


    /* ========================================================
       PANTONE CALCULATOR
       ======================================================== */

    if (selectedColor != null) {

        PantoneCalculatorScreen(

            color = selectedColor!!,

            onBack = {

                selectedColor = null
            }
        )

        return
    }


    /* ========================================================
       RECIPE EDITOR
       ======================================================== */

    if (
        isRecipeEditorOpen &&
        selectedRecipeType != null
    ) {

        RecipeEditorScreen(

            recipe = selectedRecipe,

            recipeType = selectedRecipeType!!,

            viewModel = recipeViewModel,

            inkLibraryViewModel =
                inkLibraryViewModel,

            onBack = {

                isRecipeEditorOpen = false
                selectedRecipe = null
                selectedRecipeType = null
            },

            onSaved = {

                isRecipeEditorOpen = false
                selectedRecipe = null
                selectedRecipeType = null

                recipeViewModel.loadAllRecipes()
            }
        )

        return
    }


    /* ========================================================
       FLEXO / OFFSET CALCULATOR
       ======================================================== */

    if (selectedRecipe != null) {

        RecipeCalculatorScreen(

            recipe = selectedRecipe!!,

            onBack = {

                selectedRecipe = null
            }
        )

        return
    }


    /* ========================================================
       MAIN
       ======================================================== */

    Column(

        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        ScrollableTabRow(

            selectedTabIndex = selectedTab
        ) {

            Tab(

                selected = selectedTab == 0,

                onClick = {

                    selectedTab = 0
                    recipeViewModel.clearSearch()
                },

                text = {

                    Text("PANTONE")
                }
            )


            Tab(

                selected = selectedTab == 1,

                onClick = {

                    selectedTab = 1
                    recipeViewModel.clearSearch()
                },

                text = {

                    Text("FLEXO")
                }
            )


            Tab(

                selected = selectedTab == 2,

                onClick = {

                    selectedTab = 2
                    recipeViewModel.clearSearch()
                },

                text = {

                    Text("ОФСЕТ")
                }
            )


            Tab(

                selected = selectedTab == 3,

                onClick = {

                    selectedTab = 3
                    recipeViewModel.clearSearch()
                },

                text = {

                    Text("INKS")
                }
            )


            Tab(

                selected = selectedTab == 4,

                onClick = {

                    selectedTab = 4
                    recipeViewModel.clearSearch()
                },

                text = {

                    Text("ANILOX")
                }
            )
        }


        when (selectedTab) {

            /* =================================================
               PANTONE
               ================================================= */

            0 -> {

                PantoneScreen(

                    viewModel = viewModel,

                    onColorClick = {

                        selectedColor = it
                    }
                )
            }


            /* =================================================
               FLEXO
               ================================================= */

            1 -> {

                RecipeListScreen(

                    title = "FLEXO",

                    recipes =
                        recipeViewModel.flexoRecipes,

                    searchQuery =
                        recipeViewModel.searchQuery,

                    isLoading =
                        recipeViewModel.isLoading,

                    error =
                        recipeViewModel.error,

                    onSearch = {

                        recipeViewModel.searchFlexo(it)
                    },

                    onRecipeClick = {

                        selectedRecipe = it
                    },

                    onEdit = {

                        selectedRecipe = it

                        selectedRecipeType =
                            RecipeType.FLEXO

                        isRecipeEditorOpen = true
                    },

                    onAdd = {

                        selectedRecipe = null

                        selectedRecipeType =
                            RecipeType.FLEXO

                        isRecipeEditorOpen = true
                    },

                    onDelete = {

                        recipeViewModel.deleteRecipe(it)
                    }
                )
            }


            /* =================================================
               OFFSET
               ================================================= */

            2 -> {

                RecipeListScreen(

                    title = "ОФСЕТ",

                    recipes =
                        recipeViewModel.offsetRecipes,

                    searchQuery =
                        recipeViewModel.searchQuery,

                    isLoading =
                        recipeViewModel.isLoading,

                    error =
                        recipeViewModel.error,

                    onSearch = {

                        recipeViewModel.searchOffset(it)
                    },

                    onRecipeClick = {

                        selectedRecipe = it
                    },

                    onEdit = {

                        selectedRecipe = it

                        selectedRecipeType =
                            RecipeType.OFFSET

                        isRecipeEditorOpen = true
                    },

                    onAdd = {

                        selectedRecipe = null

                        selectedRecipeType =
                            RecipeType.OFFSET

                        isRecipeEditorOpen = true
                    },

                    onDelete = {

                        recipeViewModel.deleteRecipe(it)
                    }
                )
            }


            /* =================================================
               INKS
               ================================================= */

            3 -> {

                InkLibraryScreen(

                    viewModel =
                        inkLibraryViewModel
                )
            }


            /* =================================================
               ANILOX
               ================================================= */

            4 -> {

                AniloxFlexScreen(

                    viewModel =
                        aniloxFlexViewModel
                )
            }
        }
    }
}


/* ============================================================
   PANTONE LIST
   ============================================================ */

@Composable
fun PantoneScreen(

    viewModel: PantoneViewModel,

    onColorClick: (PantoneItem) -> Unit
) {

    val colors by
        viewModel.colors.collectAsState()

    val searchQuery by
        viewModel.searchQuery.collectAsState()

    val isLoading by
        viewModel.isLoading.collectAsState()

    val error by
        viewModel.error.collectAsState()


    Column(

        modifier =
            Modifier.fillMaxSize()
    ) {

        Text(

            text = "ColorManager",

            style =
                MaterialTheme.typography.headlineMedium,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
        )


        OutlinedTextField(

            value = searchQuery,

            onValueChange = {

                viewModel.search(it)
            },

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

            label = {

                Text("Поиск цвета")
            },

            singleLine = true
        )


        when {

            isLoading -> {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }


            error != null -> {

                Text(

                    text =
                        "Ошибка: $error",

                    color =
                        MaterialTheme.colorScheme.error,

                    modifier =
                        Modifier.padding(16.dp)
                )
            }


            colors.isEmpty() -> {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text("Цвета не найдены")
                }
            }


            else -> {

                LazyColumn(

                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp),

                    contentPadding =
                        PaddingValues(
                            top = 0.dp,
                            bottom = 16.dp
                        )
                ) {

                    items(

                        items = colors,

                        key = {
                            color -> color.name
                        }

                    ) { color ->

                        PantoneColorCard(

                            color = color,

                            onClick = {

                                onColorClick(color)
                            }
                        )
                    }
                }
            }
        }
    }
}


/* ============================================================
   RECIPE LIST
   ============================================================ */

@Composable
fun RecipeListScreen(

    title: String,

    recipes:
        kotlinx.coroutines.flow.StateFlow<
            List<CustomRecipe>
        >,

    searchQuery:
        kotlinx.coroutines.flow.StateFlow<String>,

    isLoading:
        kotlinx.coroutines.flow.StateFlow<Boolean>,

    error:
        kotlinx.coroutines.flow.StateFlow<String?>,

    onSearch: (String) -> Unit,

    onRecipeClick: (CustomRecipe) -> Unit,

    onEdit: (CustomRecipe) -> Unit,

    onAdd: () -> Unit,

    onDelete: (CustomRecipe) -> Unit
) {

    val recipeList by
        recipes.collectAsState()

    val query by
        searchQuery.collectAsState()

    val loading by
        isLoading.collectAsState()

    val errorMessage by
        error.collectAsState()


    Column(

        modifier =
            Modifier.fillMaxSize()
    ) {

        Text(

            text = title,

            fontSize = 24.sp,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier.padding(
                    start = 16.dp,
                    top = 16.dp,
                    end = 16.dp,
                    bottom = 8.dp
                )
        )


        OutlinedTextField(

            value = query,

            onValueChange = onSearch,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),

            label = {

                Text("Поиск рецепта")
            },

            singleLine = true
        )


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        Button(

            onClick = onAdd,

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
        ) {

            Text(

                text =
                    "+ ДОБАВИТЬ РЕЦЕПТ",

                fontWeight =
                    FontWeight.Bold
            )
        }


        Spacer(
            modifier =
                Modifier.height(12.dp)
        )


        when {

            loading -> {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    CircularProgressIndicator()
                }
            }


            errorMessage != null -> {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Text(

                        text =
                            "Ошибка: $errorMessage",

                        color =
                            MaterialTheme.colorScheme.error
                    )
                }
            }


            recipeList.isEmpty() -> {

                Box(

                    modifier =
                        Modifier.fillMaxSize(),

                    contentAlignment =
                        Alignment.Center
                ) {

                    Column(

                        horizontalAlignment =
                            Alignment.CenterHorizontally
                    ) {

                        Text(

                            text =
                                "Рецептов пока нет",

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
                                "Нажмите «ДОБАВИТЬ РЕЦЕПТ»"
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
                            top = 8.dp,
                            bottom = 24.dp
                        )
                ) {

                    items(

                        items = recipeList,

                        key = { recipe ->

                            "${recipe.type}_${recipe.name}"
                        }

                    ) { recipe ->

                        RecipeCard(

                            recipe = recipe,

                            onClick = {

                                onRecipeClick(recipe)
                            },

                            onEdit = {

                                onEdit(recipe)
                            },

                            onDelete = {

                                onDelete(recipe)
                            }
                        )
                    }
                }
            }
        }
    }
}


/* ============================================================
   RECIPE CARD
   ============================================================ */

@Composable
fun RecipeCard(

    recipe: CustomRecipe,

    onClick: () -> Unit,

    onEdit: () -> Unit,

    onDelete: () -> Unit
) {

    Column(

        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
                .padding(
                    horizontal = 16.dp,
                    vertical = 8.dp
                )
    ) {

        Row(

            modifier =
                Modifier.fillMaxWidth(),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            val color =
                remember(recipe.hexCode) {

                    try {

                        Color(
                            android.graphics.Color.parseColor(
                                recipe.hexCode
                            )
                        )

                    } catch (
                        e: Exception
                    ) {

                        Color.Gray
                    }
                }


            Box(

                modifier =
                    Modifier
                        .width(55.dp)
                        .height(55.dp)
                        .background(color)
                        .border(
                            1.dp,
                            Color.LightGray
                        )
            )


            Spacer(
                modifier =
                    Modifier.width(12.dp)
            )


            Column(

                modifier =
                    Modifier.weight(1f)
            ) {

                Text(

                    text =
                        recipe.name,

                    fontSize = 18.sp,

                    fontWeight =
                        FontWeight.Bold
                )


                Text(

                    text =
                        recipe.type.name,

                    fontSize = 13.sp,

                    color =
                        Color.Gray
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


        Column(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 67.dp,
                        top = 4.dp,
                        bottom = 8.dp
                    )
        ) {

            RecipeRow(

                colorName =
                    recipe.baseColor1,

                percent =
                    recipe.percentColor1
            )


            RecipeRow(

                colorName =
                    recipe.baseColor2,

                percent =
                    recipe.percentColor2
            )


            if (
                !recipe.baseColor3
                    .isNullOrBlank()
            ) {

                RecipeRow(

                    colorName =
                        recipe.baseColor3,

                    percent =
                        recipe.percentColor3 ?: 0.0
                )
            }


            if (
                !recipe.baseColor4
                    .isNullOrBlank()
            ) {

                RecipeRow(

                    colorName =
                        recipe.baseColor4,

                    percent =
                        recipe.percentColor4 ?: 0.0
                )
            }
        }
    }
}


/* ============================================================
   CALCULATOR DATA
   ============================================================ */

private data class CalculatorComponent(

    val name: String,

    val percent: Double
)


private enum class CalculatorMode {

    TOTAL_WEIGHT,

    NEW_RECIPE
}


/* ============================================================
   FLEXO / OFFSET CALCULATOR
   ============================================================ */


@Composable
fun RecipeCalculatorScreen(
    recipe: CustomRecipe,
    onBack: () -> Unit
) {

    var mode by remember {
        mutableStateOf(
            CalculatorMode.TOTAL_WEIGHT
        )
    }

    var totalWeight by remember {
        mutableStateOf("")
    }

    // ============================================================
    // БАЗОВЫЕ ЦВЕТА И ПРОЦЕНТЫ ИЗ СОХРАНЁННОГО РЕЦЕПТА
    // ============================================================

    val components = remember(recipe) {

        buildList {

            if (recipe.baseColor1.isNotBlank()) {

                add(
                    CalculatorComponent(
                        name = recipe.baseColor1,
                        percent = recipe.percentColor1
                    )
                )
            }

            if (recipe.baseColor2.isNotBlank()) {

                add(
                    CalculatorComponent(
                        name = recipe.baseColor2,
                        percent = recipe.percentColor2
                    )
                )
            }

            if (!recipe.baseColor3.isNullOrBlank()) {

                add(
                    CalculatorComponent(
                        name = recipe.baseColor3!!,
                        percent = recipe.percentColor3 ?: 0.0
                    )
                )
            }

            if (!recipe.baseColor4.isNullOrBlank()) {

                add(
                    CalculatorComponent(
                        name = recipe.baseColor4!!,
                        percent = recipe.percentColor4 ?: 0.0
                    )
                )
            }
        }
    }

    // ============================================================
    // ВЕС, КОТОРЫЙ ПОЛЬЗОВАТЕЛЬ ВВОДИТ ДЛЯ ОДНОГО ЦВЕТА
    // ============================================================

    var selectedComponentIndex by remember {
        mutableStateOf<Int?>(null)
    }

    var selectedComponentWeight by remember {
        mutableStateOf("")
    }

    // ============================================================
    // СУММА ПРОЦЕНТОВ СОХРАНЁННОГО РЕЦЕПТА
    // ============================================================

    val recipeTotalPercent = remember(components) {

        components.sumOf {
            it.percent
        }
    }

    // ============================================================
    // РАСЧЁТ ПО ОДНОМУ БАЗОВОМУ ЦВЕТУ
    // ============================================================

    val enteredWeight =
        selectedComponentWeight
            .replace(",", ".")
            .toDoubleOrNull()

    val calculatedTotalWeight =
        if (
            enteredWeight != null &&
            enteredWeight > 0.0 &&
            selectedComponentIndex != null &&
            recipeTotalPercent > 0.0
        ) {

            val selectedPercent =
                components[
                    selectedComponentIndex!!
                ].percent

            if (selectedPercent > 0.0) {

                enteredWeight *
                        recipeTotalPercent /
                        selectedPercent

            } else {

                0.0
            }

        } else {

            0.0
        }

    // ============================================================
    // РАССЧИТАННЫЕ ВЕСА ВСЕХ БАЗОВЫХ ЦВЕТОВ
    // ============================================================

    val calculatedWeights =
        remember(
            components,
            selectedComponentIndex,
            enteredWeight,
            calculatedTotalWeight
        ) {

            if (
                selectedComponentIndex == null ||
                enteredWeight == null ||
                enteredWeight <= 0.0 ||
                calculatedTotalWeight <= 0.0
            ) {

                List(components.size) {
                    null
                }

            } else {

                components.mapIndexed { index, component ->

                    if (
                        index ==
                        selectedComponentIndex
                    ) {

                        enteredWeight

                    } else {

                        calculatedTotalWeight *
                                component.percent /
                                recipeTotalPercent
                    }
                }
            }
        }

    // ============================================================
    // ЭКРАН
    // ============================================================

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // ========================================================
        // HEADER
        // ========================================================

        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 8.dp,
                        top = 12.dp,
                        end = 16.dp,
                        bottom = 12.dp
                    ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            TextButton(
                onClick = onBack
            ) {

                Text("← Назад")
            }

            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )

            Text(
                text = "Калькулятор",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // ========================================================
        // CONTENT
        // ========================================================

        Column(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .verticalScroll(
                        rememberScrollState()
                    )
                    .padding(
                        horizontal = 16.dp
                    )
        ) {

            // ====================================================
            // НАЗВАНИЕ РЕЦЕПТА
            // ====================================================

            Text(
                text = recipe.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )

            Text(
                text = "Базовые цвета рецепта",
                fontSize = 16.sp,
                color = Color.Gray
            )

            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )

            // ====================================================
            // РЕЖИМЫ
            // ====================================================

            Row(
                modifier =
                    Modifier.fillMaxWidth()
            ) {

                FilterChip(
                    selected =
                        mode ==
                                CalculatorMode.TOTAL_WEIGHT,

                    onClick = {

                        mode =
                            CalculatorMode.TOTAL_WEIGHT
                    },

                    label = {

                        Text("Общий вес")
                    }
                )

                Spacer(
                    modifier =
                        Modifier.width(8.dp)
                )

                FilterChip(
                    selected =
                        mode ==
                                CalculatorMode.NEW_RECIPE,

                    onClick = {

                        mode =
                            CalculatorMode.NEW_RECIPE
                    },

                    label = {

                        Text(
                            "Расчёт по базовым цветам"
                        )
                    }
                )
            }

            Spacer(
                modifier =
                    Modifier.height(20.dp)
            )

            // ====================================================
            // РЕЖИМЫ КАЛЬКУЛЯТОРА
            // ====================================================

            when (mode) {

                // =================================================
                // ОБЩИЙ ВЕС
                // =================================================

                CalculatorMode.TOTAL_WEIGHT -> {

                    OutlinedTextField(

                        value =
                            totalWeight,

                        onValueChange = {

                            totalWeight = it
                        },

                        modifier =
                            Modifier.fillMaxWidth(),

                        label = {

                            Text("Общий вес")
                        },

                        placeholder = {

                            Text("Например: 1000")
                        },

                        suffix = {

                            Text("г")
                        },

                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType =
                                    KeyboardType.Decimal
                            ),

                        singleLine = true
                    )

                    Spacer(
                        modifier =
                            Modifier.height(24.dp)
                    )

                    Text(
                        text = "Рецепт",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    val weight =
                        totalWeight
                            .replace(",", ".")
                            .toDoubleOrNull()

                    components.forEach { component ->

                        RecipeCalculatorRow(

                            colorName =
                                component.name,

                            percent =
                                component.percent,

                            totalWeight =
                                weight
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )
                    }

                    if (weight != null) {

                        Spacer(
                            modifier =
                                Modifier.height(12.dp)
                        )

                        Text(
                            text =
                                "Итого: ${
                                    formatWeight(weight)
                                } г",

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }

                // =================================================
                // РАСЧЁТ ПО БАЗОВЫМ ЦВЕТАМ
                // =================================================

                CalculatorMode.NEW_RECIPE -> {

                    Text(
                        text =
                            "Расчёт по базовым цветам",

                        fontSize = 22.sp,

                        fontWeight =
                            FontWeight.Bold
                    )

                    Spacer(
                        modifier =
                            Modifier.height(8.dp)
                    )

                    Text(
                        text =
                            "Выберите базовый цвет и введите его вес. " +
                            "Остальные цвета и общий вес будут рассчитаны " +
                            "по процентам сохранённого рецепта.",

                        fontSize = 14.sp,

                        color = Color.Gray
                    )

                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )

                    // =================================================
                    // ЦВЕТА
                    // =================================================

                    components.forEachIndexed {
                            index,
                            component ->

                        val isSelected =
                            selectedComponentIndex == index

                        Column(
                            modifier =
                                Modifier.fillMaxWidth()
                        ) {

                            Row(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .clickable {

                                            selectedComponentIndex =
                                                index

                                            selectedComponentWeight =
                                                calculatedWeights[
                                                    index
                                                ]
                                                    ?.let {
                                                        formatWeight(
                                                            it
                                                        )
                                                    }
                                                    ?: ""
                                        }
                                        .background(
                                            color =
                                                if (isSelected) {

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
                                        .padding(
                                            12.dp
                                        ),

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Column(
                                    modifier =
                                        Modifier.weight(1f)
                                ) {

                                    Text(
                                        text =
                                            component.name,

                                        fontSize = 16.sp,

                                        fontWeight =
                                            FontWeight.Bold
                                    )

                                    Spacer(
                                        modifier =
                                            Modifier.height(2.dp)
                                    )

                                    Text(
                                        text =
                                            "Рецепт: ${
                                                formatPercent(
                                                    component.percent
                                                )
                                            }%",

                                        fontSize = 14.sp,

                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .primary
                                    )
                                }

                                if (isSelected) {

                                    Text(
                                        text = "✓",

                                        color =
                                            MaterialTheme
                                                .colorScheme
                                                .primary,

                                        fontSize = 22.sp,

                                        fontWeight =
                                            FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(6.dp)
                            )
                        }
                    }

                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )

                    // =================================================
                    // ВВОД ВЕСА ВЫБРАННОГО ЦВЕТА
                    // =================================================

                    if (selectedComponentIndex != null) {

                        val selectedComponent =
                            components[
                                selectedComponentIndex!!
                            ]

                        OutlinedTextField(

                            value =
                                selectedComponentWeight,

                            onValueChange = {

                                selectedComponentWeight =
                                    it
                            },

                            modifier =
                                Modifier.fillMaxWidth(),

                            label = {

                                Text(
                                    "Вес: ${selectedComponent.name}"
                                )
                            },

                            placeholder = {

                                Text("Например: 100")
                            },

                            suffix = {

                                Text("г")
                            },

                            keyboardOptions =
                                KeyboardOptions(
                                    keyboardType =
                                        KeyboardType.Decimal
                                ),

                            singleLine = true
                        )

                        Spacer(
                            modifier =
                                Modifier.height(20.dp)
                        )

                        // =================================================
                        // ОБЩИЙ ВЕС
                        // =================================================

                        if (
                            calculatedTotalWeight > 0.0
                        ) {

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween,

                                verticalAlignment =
                                    Alignment.CenterVertically
                            ) {

                                Text(
                                    text =
                                        "Общий вес",

                                    fontSize = 20.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )

                                Text(
                                    text =
                                        "${
                                            formatWeight(
                                                calculatedTotalWeight
                                            )
                                        } г",

                                    fontSize = 20.sp,

                                    fontWeight =
                                        FontWeight.Bold,

                                    color =
                                        MaterialTheme
                                            .colorScheme
                                            .primary
                                )
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(20.dp)
                            )

                            // =================================================
                            // РЕЗУЛЬТАТ
                            // =================================================

                            Text(
                                text = "Расчёт",

                                fontSize = 20.sp,

                                fontWeight =
                                    FontWeight.Bold
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(10.dp)
                            )

                            components.forEachIndexed {
                                    index,
                                    component ->

                                val calculatedWeight =
                                    calculatedWeights[
                                        index
                                    ]

                                Row(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(
                                                vertical = 6.dp
                                            ),

                                    verticalAlignment =
                                        Alignment.CenterVertically
                                ) {

                                    Column(
                                        modifier =
                                            Modifier.weight(1f)
                                    ) {

                                        Text(
                                            text =
                                                component.name,

                                            fontSize = 16.sp,

                                            fontWeight =
                                                FontWeight.Bold
                                        )

                                        Text(
                                            text =
                                                "${
                                                    formatPercent(
                                                        component.percent
                                                    )
                                                }%",

                                            fontSize = 14.sp,

                                            color =
                                                Color.Gray
                                        )
                                    }

                                    Text(
                                        text =
                                            if (
                                                calculatedWeight != null
                                            ) {

                                                "${
                                                    formatWeight(
                                                        calculatedWeight
                                                    )
                                                } г"

                                            } else {

                                                "—"
                                            },

                                        fontSize = 17.sp,

                                        fontWeight =
                                            FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(
                                modifier =
                                    Modifier.height(12.dp)
                            )

                            // =================================================
                            // ПРОВЕРКА
                            // =================================================

                            Row(
                                modifier =
                                    Modifier.fillMaxWidth(),

                                horizontalArrangement =
                                    Arrangement.SpaceBetween
                            ) {

                                Text(
                                    text =
                                        "Сумма процентов",

                                    fontSize = 16.sp
                                )

                                Text(
                                    text =
                                        "${
                                            formatPercent(
                                                recipeTotalPercent
                                            )
                                        }%",

                                    fontSize = 16.sp,

                                    fontWeight =
                                        FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )
        }
    }
}




/* ============================================================
   RECIPE CALCULATOR ROW
   ============================================================ */

@Composable
private fun RecipeCalculatorRow(

    colorName: String,

    percent: Double,

    totalWeight: Double?
) {

    val grams =
        totalWeight?.let {

            it *
                percent /
                100.0
        }


    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Color.LightGray
                )
                .padding(
                    vertical = 10.dp,
                    horizontal = 8.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            text =
                colorName,

            modifier =
                Modifier.weight(1f),

            fontSize = 17.sp,

            fontWeight =
                FontWeight.Bold
        )


        Text(

            text =
                "${formatPercent(percent)}%",

            modifier =
                Modifier.width(80.dp),

            textAlign =
                TextAlign.End,

            fontWeight =
                FontWeight.Bold
        )


        Text(

            text =
                grams?.let {
                    formatWeight(it)
                } ?: "—",

            modifier =
                Modifier.width(100.dp),

            textAlign =
                TextAlign.End,

            fontWeight =
                FontWeight.Bold
        )
    }
}


/* ============================================================
   NEW RECIPE ROW
   ============================================================ */

@Composable
private fun NewRecipeCalculatorRow(

    colorName: String,

    weight: String,

    percent: Double,

    onWeightChange: (String) -> Unit
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Color.LightGray
                )
                .padding(
                    vertical = 8.dp,
                    horizontal = 8.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            text =
                colorName,

            modifier =
                Modifier.weight(1f),

            fontSize = 17.sp,

            fontWeight =
                FontWeight.Bold
        )


        OutlinedTextField(

            value =
                weight,

            onValueChange =
                onWeightChange,

            modifier =
                Modifier.width(120.dp),

            label = {

                Text("Кол-во")
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Decimal
                ),

            singleLine = true
        )


        Spacer(
            modifier =
                Modifier.width(8.dp)
        )


        Text(

            text =
                if (
                    weight.isNotBlank()
                ) {

                    "${formatPercent(percent)}%"

                } else {

                    "—"
                },

            modifier =
                Modifier.width(65.dp),

            textAlign =
                TextAlign.End,

            fontWeight =
                FontWeight.Bold
        )
    }
}


/* ============================================================
   PANTONE COLOR CARD
   ============================================================ */

@Composable
fun PantoneColorCard(

    color: PantoneItem,

    onClick: () -> Unit
) {

    val backgroundColor =
        remember(color.hexCode) {

            try {

                Color(

                    android.graphics.Color.parseColor(
                        color.hexCode
                    )
                )

            } catch (
                e: Exception
            ) {

                Color.Gray
            }
        }


    Column(

        modifier =
            Modifier
                .fillMaxWidth()
                .clickable {
                    onClick()
                }
    ) {

        Box(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(
                        backgroundColor
                    )
        )


        Row(

            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 12.dp,
                        end = 12.dp,
                        top = 10.dp,
                        bottom = 36.dp
                    ),

            verticalAlignment =
                Alignment.Top
        ) {

            Column(

                modifier =
                    Modifier
                        .weight(1f)
                        .padding(end = 16.dp),

                horizontalAlignment =
                    Alignment.Start
            ) {

                Text(

                    text = "PANTONE",

                    fontSize = 20.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color.Black
                )


                Text(

                    text =
                        "${color.name} C",

                    fontSize = 20.sp,

                    fontWeight =
                        FontWeight.Bold,

                    color =
                        Color.Black
                )
            }


            Column(

                modifier =
                    Modifier.width(200.dp),

                horizontalAlignment =
                    Alignment.Start
            ) {

                if (
                    color.baseColor1
                        .isNotBlank()
                ) {

                    RecipeRow(

                        colorName =
                            color.baseColor1,

                        percent =
                            color.percentColor1
                    )
                }


                if (
                    color.baseColor2
                        .isNotBlank()
                ) {

                    RecipeRow(

                        colorName =
                            color.baseColor2,

                        percent =
                            color.percentColor2
                    )
                }


                if (
                    !color.baseColor3
                        .isNullOrBlank()
                ) {

                    RecipeRow(

                        colorName =
                            color.baseColor3,

                        percent =
                            color.percentColor3
                                ?: 0.0
                    )
                }


                if (
                    !color.baseColor4
                        .isNullOrBlank()
                ) {

                    RecipeRow(

                        colorName =
                            color.baseColor4,

                        percent =
                            color.percentColor4
                                ?: 0.0
                    )
                }
            }
        }
    }
}


/* ============================================================
   RECIPE ROW
   ============================================================ */

@Composable
private fun RecipeRow(

    colorName: String,

    percent: Double
) {

    Row(

        modifier =
            Modifier.fillMaxWidth(),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            text =
                colorName,

            fontSize = 14.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color.Black,

            modifier =
                Modifier.weight(1f)
        )


        Text(

            text =
                formatPercent(percent),

            fontSize = 14.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color.Black,

            textAlign =
                TextAlign.End,

            modifier =
                Modifier
                    .width(55.dp)
                    .padding(start = 8.dp)
        )
    }
}



/* ============================================================
   PANTONE CALCULATOR
   ============================================================ */

@Composable
fun PantoneCalculatorScreen(

    color: PantoneItem,

    onBack: () -> Unit
) {

    /*===================
      Общая масса краски
    ======================

      Это основное поле.

      Если пользователь меняет общую массу,
      массы всех базовых цветов пересчитываются
      по сохранённым процентам Pantone.
     */

    var totalWeightText by remember {

        mutableStateOf("")
    }


    /*
     * --------------------------------------------------------
     * Массы базовых цветов
     * --------------------------------------------------------
     *
     * Здесь хранятся именно значения в граммах,
     * которые отображаются в редактируемых полях.
     */

    var componentWeights by remember(color) {

        mutableStateOf(
            listOf("", "", "", "")
        )
    }


    /*
     * --------------------------------------------------------
     * Проценты Pantone
     * --------------------------------------------------------
     *
     * Проценты НЕ изменяются пользователем.
     */

    val percentages = remember(color) {

        listOf(
            color.percentColor1,
            color.percentColor2,
            color.percentColor3 ?: 0.0,
            color.percentColor4 ?: 0.0
        )
    }


    /*
     * --------------------------------------------------------
     * Названия базовых цветов
     * --------------------------------------------------------
     */

    val colorNames = remember(color) {

        listOf(
            color.baseColor1,
            color.baseColor2,
            color.baseColor3 ?: "",
            color.baseColor4 ?: ""
        )
    }


    /*
     * --------------------------------------------------------
     * Сколько компонентов реально используется
     * --------------------------------------------------------
     */

    val componentCount = remember(color) {

        colorNames.count {
            it.isNotBlank()
        }
    }


    /*
     * --------------------------------------------------------
     * Синхронизация полей при смене Pantone
     * --------------------------------------------------------
     */

    androidx.compose.runtime.LaunchedEffect(color) {

        totalWeightText = ""

        componentWeights =
            listOf("", "", "", "")
    }


    /*
     * --------------------------------------------------------
     * Изменение ОБЩЕЙ МАССЫ
     * --------------------------------------------------------
     *
     * Например:
     *
     * Pantone:
     *
     * 50%
     * 30%
     * 20%
     *
     * Общая масса = 1000
     *
     * Получаем:
     *
     * 500 г
     * 300 г
     * 200 г
     */

    fun updateFromTotalWeight(
        value: String
    ) {

        totalWeightText = value

        val totalWeight =
            value
                .replace(",", ".")
                .toDoubleOrNull()

        if (
            totalWeight == null ||
            totalWeight < 0.0
        ) {

            componentWeights =
                listOf("", "", "", "")

            return
        }


        componentWeights =
            percentages.mapIndexed { index, percent ->

                if (
                    index < componentCount &&
                    colorNames[index].isNotBlank()
                ) {

                    formatWeight(
                        totalWeight *
                                percent /
                                100.0
                    )

                } else {

                    ""
                }
            }
    }


    /*
     * --------------------------------------------------------
     * Изменение МАССЫ ОДНОГО БАЗОВОГО ЦВЕТА
     * --------------------------------------------------------
     *
     * Например:
     *
     * 50% = 500 г
     * 30% = 300 г
     * 20% = 200 г
     *
     * Пользователь меняет первый цвет:
     *
     * 500 -> 600 г
     *
     * Тогда:
     *
     * Общая масса:
     *
     * 600 / 50 * 100 = 1200 г
     *
     * Остальные:
     *
     * 30% = 360 г
     * 20% = 240 г
     */

    fun updateFromComponentWeight(
        index: Int,
        value: String
    ) {

        val newWeights =
            componentWeights
                .toMutableList()


        newWeights[index] =
            value


        val changedWeight =
            value
                .replace(",", ".")
                .toDoubleOrNull()


        val percent =
            percentages[index]


        /*
         * Если значение пустое —
         * просто очищаем поле.
         */

        if (
            changedWeight == null ||
            changedWeight < 0.0
        ) {

            componentWeights =
                newWeights

            return
        }


        /*
         * Защита от деления на ноль.
         */

        if (percent <= 0.0) {

            componentWeights =
                newWeights

            return
        }


        /*
         * Рассчитываем новую общую массу.
         */

        val newTotalWeight =
            changedWeight *
                    100.0 /
                    percent


        /*
         * Записываем новую общую массу.
         */

        totalWeightText =
            formatWeight(
                newTotalWeight
            )


        /*
         * Теперь пересчитываем ВСЕ компоненты
         * относительно новой общей массы.
         */

        componentWeights =
            percentages.mapIndexed {
                    componentIndex,
                    componentPercent ->

                if (
                    componentIndex < componentCount &&
                    colorNames[componentIndex]
                        .isNotBlank()
                ) {

                    /*
                     * Для изменяемого компонента
                     * оставляем введённое пользователем
                     * значение.
                     *
                     * Это позволяет сохранить ровно
                     * введённую массу.
                     */

                    if (
                        componentIndex == index
                    ) {

                        value

                    } else {

                        formatWeight(
                            newTotalWeight *
                                    componentPercent /
                                    100.0
                        )
                    }

                } else {

                    ""
                }
            }
    }


    /*
     * --------------------------------------------------------
     * UI
     * --------------------------------------------------------
     */

    Column(

        modifier =
            Modifier.fillMaxSize()
    ) {

        /*
         * ----------------------------------------------------
         * HEADER
         * ----------------------------------------------------
         */

        Row(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 8.dp,
                    top = 12.dp,
                    end = 16.dp,
                    bottom = 12.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            TextButton(

                onClick = onBack

            ) {

                Text(
                    text = "← Назад",
                    fontSize = 16.sp
                )
            }


            Spacer(
                modifier =
                    Modifier.width(8.dp)
            )


            Text(

                text =
                    "Калькулятор",

                fontSize = 22.sp,

                fontWeight =
                    FontWeight.Bold
            )
        }


        Column(

            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = 16.dp
                )
        ) {

            /*
             * ------------------------------------------------
             * НАЗВАНИЕ PANTONE
             * ------------------------------------------------
             */

            Text(

                text =
                    "Pantone ${color.name} C",

                fontSize = 28.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color.Black
            )


            Spacer(
                modifier =
                    Modifier.height(16.dp)
            )


            /*
             * ------------------------------------------------
             * ОБЩАЯ МАССА
             * ------------------------------------------------
             */

            OutlinedTextField(

                value =
                    totalWeightText,

                onValueChange = {

                    updateFromTotalWeight(it)
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text(
                        "Общий вес краски"
                    )
                },

                placeholder = {

                    Text(
                        "Например: 1000"
                    )
                },

                singleLine = true,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    )
            )


            Spacer(
                modifier =
                    Modifier.height(24.dp)
            )


            /*
             * ------------------------------------------------
             * ЗАГОЛОВОК
             * ------------------------------------------------
             */

            Text(

                text =
                    "Рецепт",

                fontSize = 22.sp,

                fontWeight =
                    FontWeight.Bold,

                color =
                    Color.Black
            )


            Spacer(
                modifier =
                    Modifier.height(12.dp)
            )


            /*
             * ------------------------------------------------
             * ТАБЛИЦА
             * ------------------------------------------------
             */

            PantoneCalculatorTableHeader()


            /*
             * ------------------------------------------------
             * БАЗОВЫЕ ЦВЕТА
             * ------------------------------------------------
             */

            colorNames.forEachIndexed {

                index,
                colorName ->

                if (
                    colorName.isNotBlank()
                ) {

                    PantoneEditableRecipeRow(

                        colorName =
                            colorName,

                        percent =
                            percentages[index],

                        weight =
                            componentWeights[index],

                        onWeightChange = {

                            value ->

                            updateFromComponentWeight(
                                index = index,
                                value = value
                            )
                        }
                    )
                }
            }


            /*
             * ------------------------------------------------
             * ИТОГО
             * ------------------------------------------------
             */

            val calculatedTotal =
                componentWeights.sumOf {

                    it
                        .replace(",", ".")
                        .toDoubleOrNull()
                        ?: 0.0
                }


            Spacer(
                modifier =
                    Modifier.height(8.dp)
            )


            Row(

                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFFF0F0F0)
                    )
                    .border(
                        width = 1.dp,
                        color = Color.LightGray
                    )
                    .padding(
                        vertical = 12.dp
                    ),

                verticalAlignment =
                    Alignment.CenterVertically
            ) {

                Text(

                    text =
                        "Итого",

                    fontSize = 18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(
                                start = 8.dp
                            )
                )


                Text(

                    text =
                        formatWeight(
                            calculatedTotal
                        ) + " г",

                    fontSize = 18.sp,

                    fontWeight =
                        FontWeight.Bold,

                    textAlign =
                        TextAlign.End,

                    modifier =
                        Modifier
                            .width(120.dp)
                            .padding(
                                end = 8.dp
                            )
                )
            }
        }
    }
}


/* ============================================================
   PANTONE CALCULATOR TABLE HEADER
   ============================================================ */

@Composable
private fun PantoneCalculatorTableHeader() {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFE8E8E8),
                RoundedCornerShape(
                    topStart = 6.dp,
                    topEnd = 6.dp
                )
            )
            .border(
                width = 1.dp,
                color = Color.LightGray
            )
            .padding(
                vertical = 10.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            text =
                "Базовый цвет",

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier
                    .weight(1f)
                    .padding(
                        start = 8.dp
                    )
        )


        Text(

            text =
                "%",

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier =
                Modifier
                    .width(70.dp)
                    .padding(
                        end = 8.dp
                    )
        )


        Text(

            text =
                "Вес, г",

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier =
                Modifier
                    .width(120.dp)
                    .padding(
                        end = 8.dp
                    )
        )
    }
}


/* ============================================================
   PANTONE EDITABLE RECIPE ROW
   ============================================================ */

@Composable
private fun PantoneEditableRecipeRow(

    colorName: String,

    percent: Double,

    weight: String,

    onWeightChange: (String) -> Unit
) {

    Row(

        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray
            )
            .padding(
                vertical = 6.dp,
                horizontal = 8.dp
            ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        /*
         * Название цвета
         */

        Text(

            text =
                colorName,

            fontSize = 17.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color.Black,

            modifier =
                Modifier.weight(1f)
        )


        /*
         * Процент.
         *
         * Он только отображается.
         * Пользователь его не меняет.
         */

        Text(

            text =
                "${formatPercent(percent)}%",

            fontSize = 17.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color.DarkGray,

            textAlign =
                TextAlign.End,

            modifier =
                Modifier
                    .width(70.dp)
                    .padding(
                        end = 8.dp
                    )
        )


        /*
         * Масса компонента.
         *
         * ЭТО ПОЛЕ РЕДАКТИРУЕМОЕ.
         */

        OutlinedTextField(

            value =
                weight,

            onValueChange =
                onWeightChange,

            modifier =
                Modifier.width(120.dp),

            label = {

                Text("г")
            },

            keyboardOptions =
                KeyboardOptions(
                    keyboardType =
                        KeyboardType.Decimal
                ),

            singleLine = true
        )
    }
}



/* ============================================================
   PANTONE MASS TABLE HEADER
   ============================================================ */

@Composable
private fun PantoneMassTableHeader() {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    Color(0xFFE8E8E8),

                    RoundedCornerShape(
                        topStart = 6.dp,
                        topEnd = 6.dp
                    )
                )
                .border(
                    1.dp,
                    Color.LightGray
                )
                .padding(
                    vertical = 10.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            text =
                "Базовый цвет",

            fontSize = 15.sp,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
        )


        Text(

            text =
                "%",

            fontSize = 15.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier =
                Modifier
                    .width(60.dp)
                    .padding(end = 8.dp)
        )


        Text(

            text =
                "Вес, г",

            fontSize = 15.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier =
                Modifier
                    .width(130.dp)
                    .padding(end = 8.dp)
        )
    }
}


/* ============================================================
   PANTONE MASS ROW
   ============================================================ */

@Composable
private fun PantoneMassRow(

    colorName: String,

    percent: Double,

    calculatedWeight: Double?,

    isSelected: Boolean,

    enteredWeight: String,

    onWeightChange: (String) -> Unit
) {

    Row(

        modifier =
            Modifier
                .fillMaxWidth()
                .border(
                    1.dp,
                    Color.LightGray
                )
                .padding(
                    vertical = 6.dp,
                    horizontal = 8.dp
                ),

        verticalAlignment =
            Alignment.CenterVertically
    ) {

        Text(

            text =
                colorName,

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold,

            modifier =
                Modifier.weight(1f)
        )


        Text(

            text =
                "${formatPercent(percent)}%",

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier =
                Modifier.width(60.dp)
        )


        /*
         * Здесь пользователь может ввести массу
         * любого выбранного компонента.
         *
         * Для остальных компонентов показывается
         * автоматически рассчитанная масса.
         */

        if (isSelected) {

            OutlinedTextField(

                value =
                    enteredWeight,

                onValueChange =
                    onWeightChange,

                modifier =
                    Modifier.width(130.dp),

                label = {

                    Text("Вес")
                },

                singleLine = true,

                keyboardOptions =
                    KeyboardOptions(
                        keyboardType =
                            KeyboardType.Decimal
                    )
            )

        } else {

            Text(

                text =
                    calculatedWeight?.let {

                        formatWeight(it)

                    } ?: "—",

                fontSize = 16.sp,

                fontWeight =
                    FontWeight.Bold,

                textAlign =
                    TextAlign.End,

                modifier =
                    Modifier.width(130.dp)
            )
        }
    }
}


/* ============================================================
   FORMAT PERCENT
   ============================================================ */

private fun formatPercent(

    value: Double

): String {

    return if (
        value % 1.0 == 0.0
    ) {

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


/* ============================================================
   FORMAT WEIGHT
   ============================================================ */

private fun formatWeight(

    value: Double

): String {

    return if (
        value % 1.0 == 0.0
    ) {

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
