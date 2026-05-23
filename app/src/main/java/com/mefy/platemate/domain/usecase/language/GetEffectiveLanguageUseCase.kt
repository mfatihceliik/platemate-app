package com.mefy.platemate.domain.usecase.language

import com.mefy.platemate.domain.repository.LanguagePreferenceRepository
import javax.inject.Inject

class GetEffectiveLanguageUseCase @Inject constructor(
    private val repository: LanguagePreferenceRepository
) {
    suspend operator fun invoke() = repository.getLanguageOrNull()
}
