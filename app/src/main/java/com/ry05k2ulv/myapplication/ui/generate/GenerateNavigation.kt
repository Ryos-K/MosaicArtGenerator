package com.ry05k2ulv.myapplication.ui.generate

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.navigation
import com.ry05k2ulv.myapplication.generator.GeneratorConfig
import com.ry05k2ulv.myapplication.ui.generate.output.navigateToOutput
import com.ry05k2ulv.myapplication.ui.generate.output.outputScreen
import com.ry05k2ulv.myapplication.ui.generate.input.INPUT_NAVIGATION_ROUTE
import com.ry05k2ulv.myapplication.ui.generate.input.inputScreen

const val GENERATE_NAVIGATION_ROUTE = "generate"

fun NavGraphBuilder.generateGraph(
    navController: NavController,
    navOptions: NavOptions? = null
) {
    navigation(
        startDestination = INPUT_NAVIGATION_ROUTE,
        route = GENERATE_NAVIGATION_ROUTE
    ) {
        inputScreen(
            onFinish = { targetImageUri: Uri, materialImageUriSet: Set<Uri>, generatorConfig: GeneratorConfig ->
                navController.navigateToOutput(
                    targetImageUri,
                    materialImageUriSet,
                    generatorConfig,
                    navOptions
                )
            }
        )
        outputScreen()
    }
}