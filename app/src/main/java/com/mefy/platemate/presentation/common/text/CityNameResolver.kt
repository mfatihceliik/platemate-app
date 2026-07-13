package com.mefy.platemate.presentation.common.text

object CityNameResolver {

    fun detectCityFromPlate(input: String): String? {
        if (input.isBlank()) return null

        val digitsMatch = "^\\d{1,2}".toRegex().find(input)
        val digits = digitsMatch?.value ?: return null
        val code = digits.padStart(2, '0')
        return CITY_MAP[code]
    }

    fun resolveCityName(cityName: String?, plateCode: String): String? =
        normalizeCityName(cityName) ?: detectCityFromPlate(plateCode)

    /** Plaka koduna gore sirali 81 il listesi (cityId = plaka kodu). */
    fun allCities(): List<Pair<Int, String>> =
        CITY_MAP.map { (code, name) -> code.toInt() to name }.sortedBy { it.first }

    private fun normalizeCityName(cityName: String?): String? {
        val value = cityName?.trim()?.takeIf { it.isNotBlank() } ?: return null
        if (!value.isLikelyMojibake()) return value

        val repaired = runCatching {
            String(value.toByteArray(Charsets.ISO_8859_1), Charsets.UTF_8)
        }.getOrElse { return value }

        return if (repaired.contains('\uFFFD')) value else repaired
    }

    private fun String.isLikelyMojibake(): Boolean =
        contains('\u00c3') || contains('\u00c4') || contains('\u00c5') || contains('\u00e2')

    private val CITY_MAP = mapOf(
        "01" to "Adana", "02" to "Adıyaman", "03" to "Afyonkarahisar", "04" to "Ağrı", "05" to "Amasya",
        "06" to "Ankara", "07" to "Antalya", "08" to "Artvin", "09" to "Aydın", "10" to "Balıkesir",
        "11" to "Bilecik", "12" to "Bingöl", "13" to "Bitlis", "14" to "Bolu", "15" to "Burdur",
        "16" to "Bursa", "17" to "Çanakkale", "18" to "Çankırı", "19" to "Çorum", "20" to "Denizli",
        "21" to "Diyarbakır", "22" to "Edirne", "23" to "Elazığ", "24" to "Erzincan", "25" to "Erzurum",
        "26" to "Eskişehir", "27" to "Gaziantep", "28" to "Giresun", "29" to "Gümüşhane", "30" to "Hakkari",
        "31" to "Hatay", "32" to "Isparta", "33" to "Mersin", "34" to "İstanbul", "35" to "İzmir",
        "36" to "Kars", "37" to "Kastamonu", "38" to "Kayseri", "39" to "Kırklareli", "40" to "Kırşehir",
        "41" to "Kocaeli", "42" to "Konya", "43" to "Kütahya", "44" to "Malatya", "45" to "Manisa",
        "46" to "Kahramanmaraş", "47" to "Mardin", "48" to "Muğla", "49" to "Muş", "50" to "Nevşehir",
        "51" to "Niğde", "52" to "Ordu", "53" to "Rize", "54" to "Sakarya", "55" to "Samsun",
        "56" to "Siirt", "57" to "Sinop", "58" to "Sivas", "59" to "Tekirdağ", "60" to "Tokat",
        "61" to "Trabzon", "62" to "Tunceli", "63" to "Şanlıurfa", "64" to "Uşak", "65" to "Van",
        "66" to "Yozgat", "67" to "Zonguldak", "68" to "Aksaray", "69" to "Bayburt", "70" to "Karaman",
        "71" to "Kırıkkale", "72" to "Batman", "73" to "Şırnak", "74" to "Bartın", "75" to "Ardahan",
        "76" to "Iğdır", "77" to "Yalova", "78" to "Karabük", "79" to "Kilis", "80" to "Osmaniye",
        "81" to "Düzce"
    )
}
