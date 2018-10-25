package com.makecity.client.data.address


import com.makecity.core.data.entity.Location
import com.squareup.moshi.FromJson
import org.json.JSONObject


class AddressAdapter {

	@FromJson
	fun fromJson(resultJson: String): AddressRemote {
		var houseNumber = ""
		var street = ""

		// Common
		val result = JSONObject(resultJson).getJSONArray("results")
		val component = result.getJSONObject(0)

		// Address
		val addressComponents = component.getJSONArray("address_components")
		for (addressIndex in 0 until addressComponents.length()) {
			val addressComponent = addressComponents.getJSONObject(addressIndex)
			val types = addressComponent.getJSONArray("types")
			for (typeIndex in 0 until types.length()) {
				val type = addressComponents.getString(typeIndex)

				if (type == "street_number") {
					houseNumber = addressComponent.getString("long_name")
					continue
				}

				if (type == "route") {
					street = addressComponent.getString("long_name")
					continue
				}
			}
		}

		// Coordinates
		val geometry = component.getJSONObject("geometry")
		val location = geometry.getJSONObject("location")
		val lat = location.getDouble("lat")
		val lon = location.getDouble("lng")

		// Result
		val addressLocation = Location(lat, lon)
		return AddressRemote(houseNumber, street, addressLocation)
	}
}

