package ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities

enum class BoxesFilter {
    /**
     * Получить все ящики, как активные, так и неактивные.
     */
    ALL,

    /**
     * Получить только активные ящики
     */
    ONLY_ACTIVE
}