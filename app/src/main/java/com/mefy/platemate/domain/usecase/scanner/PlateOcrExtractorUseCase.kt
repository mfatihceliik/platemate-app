package com.mefy.platemate.domain.usecase.scanner

import android.content.Context
import android.net.Uri
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.mefy.platemate.domain.usecase.search.ValidateTurkishPlateUseCase
import kotlinx.coroutines.tasks.await
import java.util.Locale
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class PlateOcrExtractorUseCase @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val validateTurkishPlateUseCase: ValidateTurkishPlateUseCase
) {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    // Türk Plaka Formatı Regex: 01-81 il kodu, 1-3 harf, 2-4 rakam.
    // Örn: 34 ABC 123, 06 K 1234
    // Ekstra boşluklar olabilir.
    private val plateRegex = Regex("""(0[1-9]|[1-7][0-9]|8[0-1])[^a-zA-Z0-9]*[A-Z]{1,3}[^a-zA-Z0-9]*\d{2,4}""")

    suspend fun extractPlateFromUri(uri: Uri): String? {
        return try {
            val image = InputImage.fromFilePath(context, uri)
            val result = recognizer.process(image).await()
            val text = result.text
            
            // Tüm metni büyük harfe çevir
            val upperText = text.uppercase(Locale.getDefault())
            
            val matches = plateRegex.findAll(upperText)
            for (match in matches) {
                val plate = match.value
                if (validateTurkishPlateUseCase(plate)) {
                    return validateTurkishPlateUseCase.normalize(plate)
                }
            }
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    @androidx.annotation.OptIn(ExperimentalGetImage::class)
    suspend fun extractPlateFromImageProxy(imageProxy: ImageProxy): String? {
        return try {
            val mediaImage = imageProxy.image ?: return null
            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
            val result = recognizer.process(image).await()
            val text = result.text.uppercase(java.util.Locale.getDefault())
            
            val matches = plateRegex.findAll(text)
            for (match in matches) {
                val plate = match.value
                if (validateTurkishPlateUseCase(plate)) {
                    return validateTurkishPlateUseCase.normalize(plate)
                }
            }
            return null
        } catch (e: Exception) {
            e.printStackTrace()
            null
        } finally {
            imageProxy.close()
        }
    }

    fun release() {
        recognizer.close()
    }
}
