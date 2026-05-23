package com.mefy.platemate.domain.usecase.language

import com.mefy.platemate.domain.model.language.AppLanguage
import com.mefy.platemate.domain.repository.LanguagePreferenceRepository
import javax.inject.Inject

class SetLanguageUseCase @Inject constructor(
    private val repository: LanguagePreferenceRepository
) {
    suspend operator fun invoke(language: AppLanguage) = repository.setLanguage(language)
}

