package net.tux.mediaplayer.data;

import org.json.JSONArray;
import org.json.JSONObject;

public class JSONPlayList {
	public String JSONCart() {
		// create a JSONObject to hold relevant info for each item in cart and
		// stuff all of these objects in a JSONArray
		JSONArray itemList = new JSONArray();
//		for (CartItem item : this.getItems()) {
//			JSONObject ci = new JSONObject();
//			ci.put("productId", item.getProduct().getId());
//			ci.put("name", item.getProduct().getLabel()); // product name
//			ci.put("price", item.getProduct().getPrice());
//			ci.put("quantity", item.getQuantity());
//			itemList.put(ci);
//		}

		// place the array of item objects as well as other info inside another
		// JSONObject for transport
		JSONObject jsonObj = new JSONObject();
//		jsonObj.put("cartId", this.getId());
//		jsonObj.put("otherStuff", this.getStuff());
//		jsonObj.put("itemList", itemList);

		// return the object as a JSON String
		return jsonObj.toString();
	}
}
