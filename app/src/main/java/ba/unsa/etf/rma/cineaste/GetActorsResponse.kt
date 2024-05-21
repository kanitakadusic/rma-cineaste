package ba.unsa.etf.rma.cineaste

import com.google.gson.annotations.SerializedName

data class GetActorsResponse(
    @SerializedName("cast") val actors: List<Actor>
)