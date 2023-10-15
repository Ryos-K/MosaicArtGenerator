package com.ry05k2ulv.myapplication.ui.generate.result

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal const val RESULT_NAVIGATION_ROUTE = "result"

fun NavController.navigateToResult(
    targetImageUri: Uri,
    materialImageUriSet: Set<Uri>,
    gridSize: Int,
    outputSize: Int,
    navOptions: NavOptions? = null
) {
    val encodedTargetImageUri =
        URLEncoder.encode(targetImageUri.toString(), StandardCharsets.UTF_8.toString())
    val encodedMaterialImageUriArray =
        materialImageUriSet.map {
            URLEncoder.encode(
                it.toString(),
                StandardCharsets.UTF_8.toString()
            )
        }
            .toTypedArray()
    navigate(
        "$RESULT_NAVIGATION_ROUTE/${encodedTargetImageUri}" +
                "/${encodedMaterialImageUriArray.joinToString(",")}" +
                "/${gridSize}" +
                "/${outputSize}",
        navOptions
    )
}

fun NavGraphBuilder.resultScreen() {
    composable(
        route = "$RESULT_NAVIGATION_ROUTE" +
                "/{$targetImageUriArg}" +
                "/{$materialImageUrisArg}" +
                "/{$gridSizeArg}" +
                "/{$outputSizeArg}",
        arguments = listOf(
            navArgument(targetImageUriArg) { type = NavType.StringType },
            navArgument(materialImageUrisArg) { type = NavType.StringType },
            navArgument(gridSizeArg) { type = NavType.IntType },
            navArgument(outputSizeArg) { type = NavType.IntType }
        )
    ) {
        ResultScreen()
    }
}