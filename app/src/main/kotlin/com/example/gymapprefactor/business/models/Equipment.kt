package com.example.gymapprefactor.business.models

sealed class Equipment {
    data object Dumbbells : Equipment()
    data object Barbell : Equipment()
    data object BodyWeight : Equipment()
    data object Bands : Equipment()
    data object WeightStack : Equipment()
    data object NegativeWeightStack : Equipment()
}
