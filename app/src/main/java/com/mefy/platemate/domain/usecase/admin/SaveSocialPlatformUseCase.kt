package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.model.admin.SocialPlatformInput
import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class SaveSocialPlatformUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    /** id == null -> create, otherwise update. */
    suspend operator fun invoke(id: Long?, input: SocialPlatformInput) =
        if (id == null) repository.addSocialPlatform(input) else repository.updateSocialPlatform(id, input)
}
