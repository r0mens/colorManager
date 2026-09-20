package com.romandruck.colormanager

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.FilterChip

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

import com.romandruck.colormanager.ui.inklibrary.InkLibraryScreen
import com.romandruck.colormanager.ui.inklibrary.InkLibraryViewModel
import com.romandruck.colormanager.ui.inklibrary.InkLibraryViewModelFactory

import com.romandruck.colormanager.ui.pantone.PantoneViewModel
import com.romandruck.colormanager.ui.pantone.PantoneViewModelFactory

import com.romandruck.colormanager.ui.recipe.RecipeEditorScreen
import com.romandruck.colormanager.ui.recipe.RecipeViewModel
import com.romandruck.colormanager.ui.recipe.RecipeViewModelFactory

import com.romandruck.colormanager.ui.theme.ColorManagerTheme
import com.romandruck.colormanager.ui.anilox.AniloxFlexScreen
import com.romandruck.colormanager.ui.anilox.AniloxFlexViewModel
import com.romandruck.colormanager.ui.anilox.AniloxFlexViewModelFactory
import com.romandruck.colormanager.ui.camera.LabTestScreen



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {

            ColorManagerTheme {

                // =================================================
                // DATABASE
                // =================================================

                val database = remember {

                    PantoneDatabase.getInstance(
                        applicationContext
                    )
                }


                // =================================================
                // PANTONE REPOSITORY
                // =================================================

                val pantoneRepository = remember {

                    PantoneRepository(
                        database
                    )
                }


                // =================================================
                // PANTONE FACTORY
                // =================================================

                val pantoneFactory = remember {

                    PantoneViewModelFactory(
                        pantoneRepository
                    )
                }


                // =================================================
                // PANTONE VIEWMODEL
                // =================================================

                val pantoneViewModel: PantoneViewModel =
                    viewModel(
                        factory = pantoneFactory
                    )


                // =================================================
                // RECIPE REPOSITORY
                // =================================================

                val recipeRepository = remember {

                    RecipeRepository(
                        database
                    )
                }


                // =================================================
                // RECIPE FACTORY
                // =================================================

                val recipeFactory = remember {

                    RecipeViewModelFactory(
                        recipeRepository
                    )
                }


                // =================================================
                // RECIPE VIEWMODEL
                // =================================================

                val recipeViewModel: RecipeViewModel =
                    viewModel(
                        factory = recipeFactory
                    )


                // =================================================
                // INK LIBRARY FACTORY
                // =================================================

                val inkLibraryFactory = remember {

                    InkLibraryViewModelFactory(
                        recipeRepository
                    )
                }


                // =================================================
                // INK LIBRARY VIEWMODEL
                // =================================================

                val inkLibraryViewModel: InkLibraryViewModel =
                    viewModel(
                        factory = inkLibraryFactory
                    )
                //==================================================
                //ANILOX VIEWMODEL
                //==================================================
                val aniloxFlexFactory = remember {
                    AniloxFlexViewModelFactory(recipeRepository)
                }
                val aniloxFlexViewModel: AniloxFlexViewModel = viewModel(factory = aniloxFlexFactory)


                // =================================================
                // APP
                // =================================================

                PantoneApp(

                    viewModel = pantoneViewModel,

                    recipeViewModel = recipeViewModel,

                    inkLibraryViewModel = inkLibraryViewModel,

                    aniloxFlexViewModel = aniloxFlexViewModel
                )

                 // =================================================
                // LAB TEST
                // =================================================

                //LabTestScreen()
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

    // =========================================================
    // SELECTED PANTONE
    // =========================================================

    var selectedColor by remember {

        mutableStateOf<PantoneItem?>(null)
    }


    // =========================================================
    // SELECTED RECIPE
    // =========================================================

    var selectedRecipe by remember {

        mutableStateOf<CustomRecipe?>(null)
    }


    // =========================================================
    // RECIPE TYPE
    // =========================================================

    var selectedRecipeType by remember {

        mutableStateOf<RecipeType?>(null)
    }


    // =========================================================
    // RECIPE EDITOR
    // =========================================================

    var isRecipeEditorOpen by remember {

        mutableStateOf(false)
    }


    // =========================================================
    // SELECTED TAB
    // =========================================================

    var selectedTab by remember {

        mutableStateOf(0)
    }


    // =========================================================
    // PANTONE CALCULATOR
    // =========================================================

    if (selectedColor != null) {

        PantoneCalculatorScreen(

            color = selectedColor!!,

            onBack = {

                selectedColor = null
            }
        )

        return
    }


    // =========================================================
    // RECIPE EDITOR
    // =========================================================

    if (
        isRecipeEditorOpen &&
        selectedRecipeType != null
    ) {

        RecipeEditorScreen(

            recipe = selectedRecipe,
            recipeType = selectedRecipeType!!,
            viewModel = recipeViewModel,
            inkLibraryViewModel = inkLibraryViewModel,
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


    // =========================================================
    // RECIPE CALCULATOR
    // =========================================================

    if (selectedRecipe != null) {

        RecipeCalculatorScreen(

            recipe = selectedRecipe!!,

            onBack = {

                selectedRecipe = null
            }
        )

        return
    }


    // =========================================================
    // MAIN
    // =========================================================

    Column(

        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
    ) {

        // =====================================================
        // TABS
        // =====================================================

        ScrollableTabRow(

            selectedTabIndex = selectedTab
        ) {

            // =================================================
            // TAB 1 — PANTONE
            // =================================================

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


            // =================================================
            // TAB 2 — FLEXO
            // =================================================

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


            // =================================================
            // TAB 3 — OFFSET
            // =================================================

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


            // =================================================
            // TAB 4 — INK LIBRARY
            // =================================================

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
            // =================================================
            // TAB 5 — ANILOX
            // =================================================

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


        // =====================================================
        // CONTENT
        // =====================================================

        when (selectedTab) {

            // =================================================
            // PANTONE
            // =================================================

            0 -> {

                PantoneScreen(

                    viewModel = viewModel,

                    onColorClick = { color ->

                        selectedColor = color
                    }
                )
            }


            // =================================================
            // FLEXO
            // =================================================

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

                    onRecipeClick = { recipe ->

                        selectedRecipe = recipe
                    },

                    onEdit = { recipe ->

                        selectedRecipe = recipe

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

                    onDelete = { recipe ->

                        recipeViewModel.deleteRecipe(
                            recipe
                        )
                    }
                )
            }


            // =================================================
            // OFFSET
            // =================================================

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

                    onRecipeClick = { recipe ->

                        selectedRecipe = recipe
                    },

                    onEdit = { recipe ->

                        selectedRecipe = recipe

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

                    onDelete = { recipe ->

                        recipeViewModel.deleteRecipe(
                            recipe
                        )
                    }
                )
            }


            // =================================================
            // INK LIBRARY
            // =================================================

            3 -> {

                InkLibraryScreen(

                    viewModel = inkLibraryViewModel
                )
            }
            //==================================================
            // ANILOX
            //==================================================

            4 -> {
                AniloxFlexScreen(
                    viewModel = aniloxFlexViewModel
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

            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 16.dp
            )
        )


        OutlinedTextField(

            value = searchQuery,

            onValueChange = { query ->

                viewModel.search(query)
            },

            modifier = Modifier
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

                        key = { color ->

                            color.name
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

            modifier = Modifier.padding(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 8.dp
            )
        )


        OutlinedTextField(

            value = query,

            onValueChange = onSearch,

            modifier = Modifier
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

            modifier = Modifier
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

        modifier = Modifier
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

                modifier = Modifier
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

                        text =
                            "Удалить",

                        color =
                            MaterialTheme
                                .colorScheme
                                .error
                    )
                }
            }
        }


        Column(

            modifier = Modifier
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
                        recipe.percentColor3
                            ?: 0.0
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
                        recipe.percentColor4
                            ?: 0.0
                )
            }
        }
    }
}
/*===============================================
DATA class для рецептов
============================================= */
private data class CalculatorComponent(
    val name: String,
    val percent: Double,
    val weight: String = ""
)
/*=================================
CALCULATOR MODE
 ================================*/
private enum class CalculatorMode {
    TOTAL_WEIGHT,
    NEW_RECIPE
}

/* ============================================================
   RECIPE CALCULATOR
   ============================================================ */


@Composable
fun RecipeCalculatorScreen(
    recipe: CustomRecipe,
    onBack: () -> Unit
) {

    /*
     * Два режима:
     *
     * TOTAL_WEIGHT
     *     Пользователь задаёт общую массу.
     *     Проценты берутся из сохранённого рецепта.
     *
     * NEW_RECIPE
     *     Пользователь вводит фактическое количество
     *     каждого базового цвета.
     *     Проценты рассчитываются автоматически.
     */


    var mode by remember {
        mutableStateOf(CalculatorMode.TOTAL_WEIGHT)
    }

    var totalWeight by remember {
        mutableStateOf("")
    }

    /*
     * Формируем список базовых цветов
     * из существующего CustomRecipe.
     */
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
                        name = recipe.baseColor3,
                        percent = recipe.percentColor3 ?: 0.0
                    )
                )
            }

            if (!recipe.baseColor4.isNullOrBlank()) {

                add(
                    CalculatorComponent(
                        name = recipe.baseColor4,
                        percent = recipe.percentColor4 ?: 0.0
                    )
                )
            }
        }
    }

    /*
     * Количества компонентов в режиме
     * создания нового рецепта.
     */
    var componentWeights by remember(recipe) {

        mutableStateOf(
            components.map {
                ""
            }
        )
    }

    /*
     * Общий вес, рассчитанный из введённых
     * пользователем компонентов.
     */
    val calculatedTotalWeight = remember(
        componentWeights
    ) {

        componentWeights.sumOf {

            it
                .replace(",", ".")
                .toDoubleOrNull()
                ?: 0.0
        }
    }

    /*
     * Проценты для режима NEW_RECIPE.
     */
    val calculatedPercentages = remember(
        componentWeights,
        calculatedTotalWeight
    ) {

        if (calculatedTotalWeight <= 0.0) {

            List(componentWeights.size) {
                0.0
            }

        } else {

            componentWeights.map {

                val weight =
                    it
                        .replace(",", ".")
                        .toDoubleOrNull()
                        ?: 0.0

                weight /
                        calculatedTotalWeight *
                        100.0
            }
        }
    }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        /*
         * HEADER
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

                Text("← Назад")
            }

            Spacer(
                modifier = Modifier.width(8.dp)
            )

            Text(
                text = "Калькулятор",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {

            Text(
                text = recipe.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )


            /*
             * ПЕРЕКЛЮЧАТЕЛЬ РЕЖИМА
             */
            Row(
                modifier = Modifier.fillMaxWidth()
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
                    modifier = Modifier.width(8.dp)
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
                        Text("Новый рецепт")
                    }
                )
            }


            Spacer(
                modifier = Modifier.height(20.dp)
            )


            when (mode) {

                /*
                 * ========================================
                 * РЕЖИМ ОБЩЕГО ВЕСА
                 * ========================================
                 */
                CalculatorMode.TOTAL_WEIGHT -> {

                    OutlinedTextField(

                        value = totalWeight,

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


                    components.forEach {

                        RecipeCalculatorRow(
                            colorName = it.name,
                            percent = it.percent,
                            totalWeight = weight
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
                                }",

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }
                }


                /*
                 * ========================================
                 * НОВЫЙ РЕЦЕПТ
                 * ========================================
                 */
                CalculatorMode.NEW_RECIPE -> {

                    Text(
                        text =
                            "Введите фактическое количество " +
                                    "каждого базового цвета",

                        fontSize = 16.sp
                    )


                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )


                    components.forEachIndexed {
                            index,
                            component ->

                        NewRecipeCalculatorRow(

                            colorName =
                                component.name,

                            weight =
                                componentWeights[index],

                            percent =
                                calculatedPercentages[index],

                            onWeightChange = { value ->

                                componentWeights =
                                    componentWeights
                                        .toMutableList()
                                        .also {

                                            it[index] =
                                                value
                                        }
                            }
                        )

                        Spacer(
                            modifier =
                                Modifier.height(6.dp)
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.height(12.dp)
                    )


                    /*
                     * ИТОГОВЫЙ ВЕС
                     */
                    Row(
                        modifier =
                            Modifier.fillMaxWidth(),
                        horizontalArrangement =
                            Arrangement.SpaceBetween
                    ) {

                        Text(
                            text = "Общий вес",
                            fontSize = 18.sp,
                            fontWeight =
                                FontWeight.Bold
                        )

                        Text(
                            text =
                                if (
                                    calculatedTotalWeight > 0
                                ) {

                                    formatWeight(
                                        calculatedTotalWeight
                                    )

                                } else {

                                    "—"
                                },

                            fontSize = 18.sp,

                            fontWeight =
                                FontWeight.Bold
                        )
                    }


                    Spacer(
                        modifier =
                            Modifier.height(16.dp)
                    )


                    /*
                     * ПРОВЕРКА ПРОЦЕНТОВ
                     */
                    if (
                        calculatedTotalWeight > 0
                    ) {

                        val totalPercent =
                            calculatedPercentages.sum()


                        Text(
                            text =
                                "Сумма процентов: ${
                                    formatPercent(
                                        totalPercent
                                    )
                                }%",

                            fontSize = 16.sp,

                            color =
                                if (
                                    kotlin.math.abs(
                                        totalPercent - 100.0
                                    ) < 0.01
                                ) {

                                    Color(0xFF2E7D32)

                                } else {

                                    Color(0xFFC62828)
                                }
                        )
                    }
                }
            }
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
            it * percent / 100.0
        }

    Row(
        modifier = Modifier
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
            text = colorName,

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
/*===========================================


=========================================== */
@Composable
private fun NewRecipeCalculatorRow(

    colorName: String,

    weight: String,

    percent: Double,

    onWeightChange: (String) -> Unit

) {

    Row(

        modifier = Modifier
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

            text = colorName,

            modifier =
                Modifier.weight(1f),

            fontSize = 17.sp,

            fontWeight =
                FontWeight.Bold
        )


        OutlinedTextField(

            value = weight,

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
                if (weight.isNotBlank()) {

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

        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
    ) {

        Box(

            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    color =
                        backgroundColor
                )
        )


        Row(

            modifier = Modifier
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

                modifier = Modifier
                    .weight(1f)
                    .padding(end = 16.dp),

                horizontalAlignment =
                    Alignment.Start
            ) {

                Text(

                    text =
                        "PANTONE",

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

        modifier = Modifier
            .fillMaxWidth(),

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

            modifier = Modifier
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

    var totalWeight by remember {

        mutableStateOf("")
    }


    val weight =
        totalWeight
            .replace(",", ".")
            .toDoubleOrNull()


    val totalPercent =

        color.percentColor1 +

        color.percentColor2 +

        (color.percentColor3 ?: 0.0) +

        (color.percentColor4 ?: 0.0)


    Column(
        modifier =
            Modifier.fillMaxSize()
    ) {

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
                    text =
                        "← Назад",

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
                .padding(horizontal = 16.dp)
        ) {

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


            OutlinedTextField(

                value =
                    totalWeight,

                onValueChange = {

                    totalWeight = it
                },

                modifier =
                    Modifier.fillMaxWidth(),

                label = {

                    Text("Общий вес краски")
                },

                placeholder = {

                    Text("Например: 1000")
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


            CalculatorTableHeader()


            CalculatorRecipeRow(

                colorName =
                    color.baseColor1,

                percent =
                    color.percentColor1,

                totalWeight =
                    weight
            )


            CalculatorRecipeRow(

                colorName =
                    color.baseColor2,

                percent =
                    color.percentColor2,

                totalWeight =
                    weight
            )


            if (
                !color.baseColor3
                    .isNullOrBlank()
            ) {

                CalculatorRecipeRow(

                    colorName =
                        color.baseColor3,

                    percent =
                        color.percentColor3
                            ?: 0.0,

                    totalWeight =
                        weight
                )
            }


            if (
                !color.baseColor4
                    .isNullOrBlank()
            ) {

                CalculatorRecipeRow(

                    colorName =
                        color.baseColor4,

                    percent =
                        color.percentColor4
                            ?: 0.0,

                    totalWeight =
                        weight
                )
            }


            CalculatorTotalRow(

                totalPercent =
                    totalPercent,

                totalWeight =
                    weight
            )
        }
    }
}


/* ============================================================
   CALCULATOR TABLE HEADER
   ============================================================ */

@Composable
private fun CalculatorTableHeader() {

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

            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )


        Text(

            text =
                "%",

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier = Modifier
                .width(90.dp)
                .padding(end = 8.dp)
        )


        Text(

            text =
                "Вес, г",

            fontSize = 16.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier = Modifier
                .width(100.dp)
                .padding(end = 8.dp)
        )
    }
}


/* ============================================================
   CALCULATOR RECIPE ROW
   ============================================================ */

@Composable
private fun CalculatorRecipeRow(

    colorName: String,

    percent: Double,

    totalWeight: Double?
) {

    if (colorName.isBlank()) {

        return
    }


    val grams =

        if (totalWeight != null) {

            totalWeight *
                percent /
                100.0

        } else {

            null
        }


    Row(

        modifier = Modifier
            .fillMaxWidth()
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
                colorName,

            fontSize = 18.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color.Black,

            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )


        Text(

            text =
                "${formatPercent(percent)}%",

            fontSize = 18.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color.DarkGray,

            textAlign =
                TextAlign.End,

            modifier = Modifier
                .width(90.dp)
                .padding(end = 8.dp)
        )


        Text(

            text = if (grams != null) {

                formatWeight(grams)

            } else {

                "—"
            },

            fontSize = 18.sp,

            fontWeight =
                FontWeight.Bold,

            color =
                Color.Black,

            textAlign =
                TextAlign.End,

            modifier = Modifier
                .width(100.dp)
                .padding(end = 8.dp)
        )
    }
}


/* ============================================================
   TOTAL ROW
   ============================================================ */

@Composable
private fun CalculatorTotalRow(

    totalPercent: Double,

    totalWeight: Double?
) {

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

            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        )


        Text(

            text =
                "${formatPercent(totalPercent)}%",

            fontSize = 18.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier = Modifier
                .width(90.dp)
                .padding(end = 8.dp)
        )


        Text(

            text = if (totalWeight != null) {

                formatWeight(
                    totalWeight *
                        totalPercent /
                        100.0
                )

            } else {

                "—"
            },

            fontSize = 18.sp,

            fontWeight =
                FontWeight.Bold,

            textAlign =
                TextAlign.End,

            modifier = Modifier
                .width(100.dp)
                .padding(end = 8.dp)
        )
    }
}


/* ============================================================
   FORMAT PERCENT
   ============================================================ */

private fun formatPercent(
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


/* ============================================================
   FORMAT WEIGHT
   ============================================================ */

private fun formatWeight(
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
