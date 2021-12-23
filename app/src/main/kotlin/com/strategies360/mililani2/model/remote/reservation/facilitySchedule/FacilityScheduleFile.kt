package com.strategies360.mililani2.model.remote.reservation.facilitySchedule

import androidx.recyclerview.widget.DiffUtil
import com.google.gson.annotations.SerializedName

class FacilityScheduleFile {

    var id: String? = null

    @SerializedName("facility_schedule_file")
    var facilityScheduleFile: List<FacilityScheduleList>? = null

    class DiffUtilCallback(
        private val oldList: List<FacilityScheduleFile> = ArrayList(),
        private val newList: List<FacilityScheduleFile> = ArrayList()): DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // TODO: Use your own implementation, this is just a sample
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            // TODO: Use your own implementation, this is just a sample
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]
            return oldItem == newItem
        }
    }

}