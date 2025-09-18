package com.example.gymapprefactor.business.models

import com.example.gymapprefactor.business.models.exercise.Template

interface Routine {
    val name: String
    val templates: List<Template>
    val id: String
}

class DefaultRoutine(
    override val id: String,
    override val name: String,
    override val templates: List<Template>,
) : Routine

fun DefaultRoutine.copy(
    name: String = this.name,
    templates: List<Template> = this.templates
): DefaultRoutine {
    return DefaultRoutine(
        id = this.id,
        name = name,
        templates = templates
    )
}

fun DefaultRoutine.addSetupExercise(
    template: Template
): DefaultRoutine {
    return this.copy(
        templates = templates + template
    )
}

fun DefaultRoutine.removeTemplate(
    id: String
): DefaultRoutine {
    return this.copy(
        templates = templates.filter {
            it.id != id
        }
    )
}
