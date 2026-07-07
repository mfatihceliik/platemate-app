package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.model.admin.AccentColorInput
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class SaveAccentColorUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    /** id == null -> create, otherwise update. */
    suspend operator fun invoke(id: Long?, input: AccentColorInput) =
        if (id == null) repository.addAccentColor(input) else repository.updateAccentColor(id, input)
}
