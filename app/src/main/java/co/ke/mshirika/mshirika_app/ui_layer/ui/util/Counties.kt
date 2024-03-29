@file:Suppress("MemberVisibilityCanBePrivate")

package co.ke.mshirika.mshirika_app.ui_layer.ui.util

object Counties {
    private val COUNTIES = mutableMapOf(
        1 to "Mombasa",
        2 to "Kwale",
        3 to "Kilifi",
        4 to "Tana River",
        5 to "Lamu",
        6 to "Taita Taveta",
        7 to "Garissa",
        8 to "Wajir",
        9 to "Mandera",
        10 to "Marsabit",
        11 to "Isiolo",
        12 to "Meru",
        13 to "Tharaka Nithi",
        14 to "Embu",
        15 to "Kitui",
        16 to "Machakos",
        17 to "Makueni",
        18 to "Nyandarua",
        19 to "Nyeri",
        20 to "Kirinyaga",
        21 to "Murang'a",
        22 to "Kiambu",
        23 to "Turkana",
        24 to "West Pokot",
        25 to "Samburu",
        26 to "Trans Nzoia",
        27 to "Uasin Gishu",
        28 to "Elgeyo Marakwet",
        29 to "Nandi",
        30 to "Baringo",
        31 to "Laikipia",
        32 to "Nakuru",
        33 to "Narok",
        34 to "Kajiado",
        35 to "Kericho",
        36 to "Bomet",
        37 to "Kakamega",
        38 to "Vihiga",
        39 to "Bungoma",
        40 to "Busia",
        41 to "Siaya",
        42 to "Kisumu",
        43 to "Homa Bay",
        44 to "Migori",
        45 to "Kisii",
        46 to "Nyamira",
        47 to "Nairobi"
    )

    val counties get() = COUNTIES.toSortedMap().map { it.value }
}