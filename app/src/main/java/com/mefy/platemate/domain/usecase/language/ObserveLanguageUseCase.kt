package com.mefy.platemate.domain.usecase.language

import com.mefy.platemate.domain.repository.LanguagePreferenceRepository
import javax.inject.Inject

class ObserveLanguageUseCase @Inject constructor(
    private val repository: LanguagePreferenceRepository
) {
    operator fun invoke() = repository.observeLanguage()
}
