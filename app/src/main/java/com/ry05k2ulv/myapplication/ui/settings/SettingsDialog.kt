package com.ry05k2ulv.myapplication.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ry05k2ulv.myapplication.R.string
import com.ry05k2ulv.myapplication.model.DarkThemeConfig
import com.ry05k2ulv.myapplication.ui.theme.supportDynamicColor

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val settingsUiState by viewModel.settingsUiState.collectAsState()
    SettingsDialog(
        onDismiss = onDismiss,
        settingsUiState = settingsUiState,
        onUseDynamicColorChanged = viewModel::updateUseDynamicColor,
        onDarkThemeConfigChanged = viewModel::updateDarkThemeConfig,
    )
}

@Composable
fun SettingsDialog(
    onDismiss: () -> Unit = {},
    settingsUiState: SettingsUiState,
    onUseDynamicColorChanged: (Boolean) -> Unit,
    onDarkThemeConfigChanged: (DarkThemeConfig) -> Unit,
    supportDynamicTheme: Boolean = supportDynamicColor(),
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(string.settings_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Divider()
            Column(
                Modifier.verticalScroll(rememberScrollState())
            ) {
                when (settingsUiState) {
                    SettingsUiState.Loading -> {
                        CircularProgressIndicator(Modifier.padding(20.dp))
                    }

                    is SettingsUiState.Success -> {
                        SettingsPanel(
                            settings = settingsUiState.settings,
                            supportDynamicTheme = supportDynamicTheme,
                            onUseDynamicColorChanged = onUseDynamicColorChanged,
                            onDarkThemeConfigChanged = onDarkThemeConfigChanged,
                        )
                    }
                }
            }
        },
        confirmButton = {
            Text(
                text = stringResource(string.settings_confirm),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onDismiss() }
            )
        }
    )
}

@Composable
private fun ColumnScope.SettingsPanel(
    settings: UserSettings,
    supportDynamicTheme: Boolean,
    onUseDynamicColorChanged: (Boolean) -> Unit,
    onDarkThemeConfigChanged: (DarkThemeConfig) -> Unit,
) {
    if (supportDynamicTheme) {
        SectionTitle(text = stringResource(string.settings_use_dynamic_color_title))
        Column(Modifier.selectableGroup()) {
            ChooserRow(
                text = stringResource(string.settings_use_dynamic_color_yes),
                selected = settings.useDynamicColor,
                onClick = { onUseDynamicColorChanged(true) })
            ChooserRow(
                text = stringResource(string.settings_use_dynamic_color_no),
                selected = !settings.useDynamicColor,
                onClick = { onUseDynamicColorChanged(false) })
        }
    }

    SectionTitle(text = stringResource(string.settings_dark_theme_config_title))
    Column(Modifier.selectableGroup()) {
        ChooserRow(
            text = stringResource(string.settings_dark_theme_config_system),
            selected = settings.darkThemeConfig == DarkThemeConfig.SYSTEM,
            onClick = { onDarkThemeConfigChanged(DarkThemeConfig.SYSTEM) })
        ChooserRow(
            text = stringResource(string.settings_dark_theme_config_light),
            selected = settings.darkThemeConfig == DarkThemeConfig.LIGHT,
            onClick = { onDarkThemeConfigChanged(DarkThemeConfig.LIGHT) })
        ChooserRow(
            text = stringResource(string.settings_dark_theme_config_dark),
            selected = settings.darkThemeConfig == DarkThemeConfig.DARK,
            onClick = { onDarkThemeConfigChanged(DarkThemeConfig.DARK) })
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(vertical = 16.dp)
    )
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
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = selected, onClick = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text)
    }
}
