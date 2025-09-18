package com.example.gymapprefactor.business.templates.data

import com.example.gymapprefactor.business.templates.domain.TemplatesRepository
import com.example.gymapprefactor.business.interfaces.DataSource
import com.example.gymapprefactor.business.models.AppDataModel
import com.example.gymapprefactor.business.models.exercise.Template

class TemplatesRepositoryImpl(
    override val dataSource: ExerciseSetupsDataSource
) : TemplatesRepository {

    override suspend fun getExerciseSetups(): List<Template> {
        return dataSource.fetchExerciseSetups()
    }

    override suspend fun saveExerciseSetup(template: Template) {
        dataSource.saveExerciseSetup(template)
    }
}

class ExerciseSetupsDataSource : DataSource {
    fun fetchExerciseSetups(): List<Template> {
        return AppDataModel.templateList
    }

    fun saveExerciseSetup(saveTemplate: Template) {
        AppDataModel.templateList.forEachIndexed { index, exerciseSetup ->
            if (exerciseSetup.id == saveTemplate.id) {
                AppDataModel.templateList[index] = saveTemplate
                return
            }
        }
        AppDataModel.templateList.add(saveTemplate)
    }
}
