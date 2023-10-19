package com.ry05k2ulv.myapplication.ui.generate.output

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ry05k2ulv.myapplication.generator.GeneratorConfig
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

internal const val OUTPUT_NAVIGATION_ROUTE = "output"

fun NavController.navigateToOutput(
    targetImageUri: Uri,
    materialImageUriSet: Set<Uri>,
    generatorConfig: GeneratorConfig,
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
    val configJson = GeneratorConfigNavType.jsonAdapter.toJson(generatorConfig)
    navigate(
        "$OUTPUT_NAVIGATION_ROUTE/${encodedTargetImageUri}" +
                "/${encodedMaterialImageUriArray.joinToString(",")}" +
                "/${configJson}",
        navOptions
    )
}

fun NavGraphBuilder.outputScreen() {
    composable(
        route = OUTPUT_NAVIGATION_ROUTE +
                "/{$targetImageUriArg}" +
                "/{$materialImageUrisArg}" +
                "/{$generatorConfigArg}",
        arguments = listOf(
            navArgument(targetImageUriArg) { type = NavType.StringType },
            navArgument(materialImageUrisArg) { type = NavType.StringType },
            navArgument(generatorConfigArg) {type = GeneratorConfigNavType }
        )
    ) {
        OutputScreen()
    }
}

object GeneratorConfigNavType: NavType<GeneratorConfig>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): GeneratorConfig? {
        return bundle.getString(key)?.let { parseValue(it) }
    }

    override fun parseValue(value: String): GeneratorConfig {
        return jsonAdapter.fromJson(value)!!
    }

    override fun put(bundle: Bundle, key: String, value: GeneratorConfig) {
        bundle.putString(key, jsonAdapter.toJson(value))
    }

//     companion object {
        private val moshi: Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<GeneratorConfig> = moshi.adapter(GeneratorConfig::class.java)
//    }
}