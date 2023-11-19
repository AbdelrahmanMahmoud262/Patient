package com.androdevelopment.patient.data.models

import java.util.UUID

data class FilterModel(val id:UUID, val title:String, var isChecked:Boolean)
