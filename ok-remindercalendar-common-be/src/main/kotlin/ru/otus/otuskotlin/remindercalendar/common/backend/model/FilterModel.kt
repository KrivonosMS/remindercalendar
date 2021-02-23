package ru.otus.otuskotlin.remindercalendar.common.backend.model

data class FilterModel(
    val from: Int = 0,
    val pageSize: Int = 0,
    val frequencyModel: FrequencyModel = FrequencyModel.NONE,
) {
    companion object {
        val NONE = FilterModel()
    }
}
