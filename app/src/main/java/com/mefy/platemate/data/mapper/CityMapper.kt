package com.mefy.platemate.data.mapper

import com.mefy.platemate.core.mapper.Mapper
import com.mefy.platemate.domain.model.city.City
import javax.inject.Inject
import com.mefy.platemate.data.remote.dto.city.CityDto

class CityMapper @Inject constructor() : Mapper<CityDto, City> {
    override fun map(input: CityDto): City = City(
        id = input.id,
        name = input.name
    )
}
