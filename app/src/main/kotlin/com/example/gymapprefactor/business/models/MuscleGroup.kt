package com.example.gymapprefactor.business.models

sealed class MuscleGroup {

    abstract val sections: List<MuscleSection>
    interface MuscleSection

    //******PUSH******//
    data class Chest(
        override val sections: List<ChestSection>
    ) : MuscleGroup()

    sealed class ChestSection : MuscleSection {
        data object Upper : ChestSection()
        data object Middle : ChestSection()
        data object Lower : ChestSection()
    }

    data class Shoulders(
        override val sections: List<ShoulderSection>
    ) : MuscleGroup()

    sealed class ShoulderSection : MuscleSection{
        data object Front : ShoulderSection()
        data object Side : ShoulderSection()
        data object Rear : ShoulderSection()
    }

    data class Triceps(
        override val sections: List<TricepsSection>
    ) : MuscleGroup()

    sealed class TricepsSection : MuscleSection{
        data object LongHead : TricepsSection()
        data object LateralHead : TricepsSection()
        data object MedialHead : TricepsSection()
    }

    //******PULL******//

    data class Biceps(
        override val sections: List<BicepsSection>
    ) : MuscleGroup()

    sealed class BicepsSection : MuscleSection{
        data object ShortHead : BicepsSection()
        data object LongHead : BicepsSection()
        data object Brachialis : BicepsSection()
    }

    data class Traps(
        override val sections: List<TrapsSection>
    ) : MuscleGroup()

    sealed class TrapsSection : MuscleSection{
        data object Upper : TrapsSection()
        data object Middle : TrapsSection()
        data object Lower : TrapsSection()
    }

    data class Lats(
        override val sections: List<LatsSection>
    ) : MuscleGroup()

    sealed class LatsSection : MuscleSection{
        data object Upper : LatsSection()
        data object Middle : LatsSection()
        data object Lower : LatsSection()
    }

    data class Forearms (
        override val sections: List<ForearmsSection>
    ) : MuscleGroup()

    sealed class ForearmsSection : MuscleSection{
        data object Anterior : ForearmsSection()
        data object Posterior : ForearmsSection()
    }

    //******LOWER******// eh later

}
