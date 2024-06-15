package ru.mixail_akulov.a47_network_retrofit_moshi.app.screens.main.tabs.settings

import androidx.recyclerview.widget.DiffUtil
import ru.mixail_akulov.a47_network_retrofit_moshi.app.model.boxes.entities.BoxAndSettings

class BoxSettingsDiffCallback(
    private val oldList: List<BoxAndSettings>,
    private val newList: List<BoxAndSettings>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].box.id == newList[newItemPosition].box.id
    }
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}