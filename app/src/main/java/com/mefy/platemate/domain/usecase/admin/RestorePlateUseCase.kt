package com.mefy.platemate.domain.usecase.admin

import com.mefy.platemate.domain.repository.AdminRepository
import javax.inject.Inject

class RestorePlateUseCase @Inject constructor(
    private val repository: AdminRepository
) {
    suspend operator fun invoke(plateId: Long) = repository.restorePlate(plateId)
}
