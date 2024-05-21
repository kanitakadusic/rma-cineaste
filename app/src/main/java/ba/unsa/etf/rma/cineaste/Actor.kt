package ba.unsa.etf.rma.cineaste

import com.google.gson.annotations.SerializedName

data class Actor (
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String
)