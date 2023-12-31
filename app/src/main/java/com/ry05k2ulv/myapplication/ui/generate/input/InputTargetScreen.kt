package com.ry05k2ulv.myapplication.ui.generate.input

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.ry05k2ulv.myapplication.generator.GeneratorConfig
import com.ry05k2ulv.myapplication.generator.GeneratorPriority
import kotlin.math.roundToInt

@Composable
internal fun InputTargetScreen(
    modifier: Modifier,
    uiState: TargetUiState,
    onGridSizeChange: (Int) -> Unit,
    onOutputSizeChange: (Int) -> Unit,
    onPriorityChange: (GeneratorPriority) -> Unit,
    updateTargetImageUri: (Uri?) -> Unit
) {
    val targetImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        updateTargetImageUri(uri)
    }

    val uri = uiState.imageUri
    val generatorConfig = uiState.generatorConfig

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            OperationBar(
                modifier = Modifier.align(Alignment.TopCenter),
                onPictureClick = { targetImageLauncher.launch("image/*") }
            )
            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = "Image",
                modifier = Modifier.fillMaxHeight(0.7f),
                contentScale = ContentScale.Fit
            )

            AdvancedConfigurationCard(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(8.dp, 16.dp),
                generatorConfig = generatorConfig,
                onGridSizeChange = onGridSizeChange,
                onOutputSizeChange = onOutputSizeChange,
                onPriorityChange = onPriorityChange
            )
        }
    }
}


@Composable
private fun OperationBar(
    modifier: Modifier = Modifier,
    onPictureClick: () -> Unit,
) {
    val itemModifier = Modifier
        .padding(8.dp)
    Row(
        modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.75f))
            .height(48.dp)
    ) {
        PictureButton(onClick = onPictureClick, itemModifier)
    }
}

@Composable
private fun PictureButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier) {
        Icon(
            imageVector = Icons.Default.AddPhotoAlternate,
            contentDescription = "Add photo"
        )
    }
}

@Composable
private fun AdvancedConfigurationCard(
    modifier: Modifier,
    generatorConfig: GeneratorConfig,
    onGridSizeChange: (Int) -> Unit,
    onOutputSizeChange: (Int) -> Unit,
    onPriorityChange: (GeneratorPriority) -> Unit
) {
    val gridSize = generatorConfig.gridSize
    val outputSize = generatorConfig.outputSize
    val priority = generatorConfig.priority

    var expanded by remember { mutableStateOf(false) }

    Card(modifier.animateContentSize()) {
        Row(
            modifier = Modifier
                .clickable { expanded = !expanded }
                .padding(16.dp, 4.dp)
                .fillMaxWidth()
        ) {
            if (expanded) Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
            else Icon(imageVector = Icons.Default.ArrowRight, contentDescription = null)
            Text(
                text = "Advanced Configuration",
                modifier = Modifier.padding(4.dp, 0.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }
        if (expanded) {
            Divider(
                Modifier.padding(16.dp, 2.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            SliderSection(
                modifier = Modifier,
                title = "Grid Size",
                value = gridSize,
                onValueChange = onGridSizeChange,
                valueRange = with(GeneratorConfig) { MIN_GRID_SIZE.toFloat()..MAX_GRID_SIZE.toFloat() },
                steps = with(GeneratorConfig) { MAX_UNIT_PER_GRID - MIN_UNIT_PER_GRID - 1 }
            )
            SliderSection(
                modifier = Modifier,
                title = "Output Size",
                value = outputSize,
                onValueChange = onOutputSizeChange,
                valueRange = with(GeneratorConfig) { MIN_OUTPUT_SIZE.toFloat()..MAX_OUTPUT_SIZE.toFloat() },
                steps = with(GeneratorConfig) { (MAX_OUTPUT_SIZE - MIN_OUTPUT_SIZE) / MIN_OUTPUT_SIZE - 1 }
            )
            ChooserSection(
                modifier = Modifier,
                title = "Priority",
                textList = listOf("Quality", "Medium", "Speed"),
                selectedIndex = priority.ordinal,
                onChange = { index -> onPriorityChange(GeneratorPriority.values()[index]) }
            )
        }
    }
}

@Composable
private fun SliderSection(
    modifier: Modifier,
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: ClosedFloatingPointRange<Float>,
    steps: Int
) {
    Column(modifier) {
        Text(
            text = "$title : $value",
            modifier = Modifier.padding(24.dp, 4.dp),
            style = MaterialTheme.typography.titleSmall
        )
        Slider(
            value = value.toFloat(),
            onValueChange = { onValueChange(it.roundToInt()) },
            modifier = Modifier.padding(16.dp, 4.dp),
            valueRange = valueRange,
            steps = steps
        )
    }
}

@Composable
private fun ChooserSection(
    modifier: Modifier,
    title: String,
    textList: List<String>,
    selectedIndex: Int,
    onChange: (index: Int) -> Unit
) {
    Column(modifier) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.padding(24.dp, 4.dp)
        )
        Row(
            Modifier
                .selectableGroup()
                .padding(bottom = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            textList.forEachIndexed { index, text ->
                ChooserRow(
                    text = text,
                    selected = index == selectedIndex,
                    onClick = { onChange(index) })
            }
        }
    }
}

@Composable
private fun ChooserRow(
    text: String,
    selected: Boolean,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .selectable(
                selected = selected,
                role = Role.RadioButton,
                onClick = onClick,
            )
//            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodyMedium)
    }
}