package ibf2022.batch3.assessment.csf.orderbackend.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

// WARNING: DO NOT MODIFY THIS FILE. 2 MARKS WILL BE DEDUCTED FOR EVERY MODIFIED LINE
public class PizzaOrder {

	private String orderId = "not set";
	private Date date;
	private String name;
	private String email;
	private String sauce;
	private Integer size;
	private Boolean thickCrust = false;
	private List<String> toppings = new LinkedList<>();
	private String comments;
	private Float total;

	public void setOrderId(String orderId) { this.orderId = orderId; }
	public String getOrderId() { return this.orderId; }

	public void setDate(Date date) { this.date = date; }
	public Date getDate() { return this.date; }

	public void setName(String name) { this.name = name; }
	public String getName() { return this.name; }
	
	public void setEmail(String email) { this.email = email; }
	public String getEmail() { return this.email; }

	public void setSauce(String sauce) { this.sauce = sauce; }
	public String getSauce() { return this.sauce; }

	public void setSize(Integer size) { this.size = size; }
	public Integer getSize() { return this.size; }

	public void setThickCrust(Boolean thickCrust) { this.thickCrust = thickCrust; }
	public Boolean getThickCrust() { return this.thickCrust; }
	public Boolean isThickCrust() { return this.thickCrust; }

	public void setTopplings(List<String> toppings) { this.toppings = toppings; }
	public List<String> getTopplings() { return this.toppings; }
	public void addTopping(String topping) { this.toppings.add(topping); }

	public void setComments(String comments) { this.comments = comments; }
	public String getComments() { return this.comments; }

	public void setTotal(Float total) { this.total = total; }
	public Float getTotal() { return this.total; }

	@Override
	public String toString() {
		return "PizzaOrder{orderId=%s, date=%s, name=%s, email=%s, sauce=%s, size=%d, thickCurst=%b, toppings=%s, comments=%s, total=%f}"
				.formatted(orderId, date, name, email, sauce, size, thickCrust, toppings, comments, total);
	}

	// WARNING: DO NOT MODIFY THIS FILE. 2 MARKS WILL BE DEDUCTED FOR EVERY MODIFIED LINE

	public JsonObject toJSONIntoMongo() {

		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for (String topping : toppings) {
			arrBuilder.add(topping);
		}

		return Json.createObjectBuilder()
			.add("_id", getOrderId())
			.add("date", getDate().getTime())
			.add("total", getTotal())
			.add("name", getName())
			.add("email", getEmail())
			.add("sauce", getSauce())
			.add("size", getSize())
			.add("crust", getThickCrust())
			.add("comments", getComments())
			.add("toppings", arrBuilder.build())
			.build();
	}

	public JsonObject toJSONIntoRedis() {
		return Json.createObjectBuilder()
			.add("orderId", getOrderId())
			.add("date", getDate().toString())
			.add("total", getTotal())
			.add("name", getName())
			.add("email", getEmail())
			.build();
	}

	public static PizzaOrder createFromAPI(String text) {
		String[] parts = text.split(",");
		PizzaOrder po = new PizzaOrder();
		po.setOrderId(parts[0]);
		long dateLong = Long.parseLong(parts[1]);
		po.setDate(new Date(dateLong));
		Float orderPrice = Float.parseFloat(parts[2]);
		po.setTotal(orderPrice);
		return po;
	}

	public JsonObject toJSONToClient() {
		Date date = getDate();
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
		String formattedDate = formatter.format(date);
		return Json.createObjectBuilder()
			.add("orderId", getOrderId())
			.add("date", formattedDate)
			.add("name", getName())
			.add("email", getEmail())
			.add("total", getTotal())
			.build();
	}

	public static PizzaOrder createFromDoc(Document doc) {
		PizzaOrder po = new PizzaOrder();
		po.setOrderId(doc.getString("_id"));
		long dateLong = doc.getLong("date");
		po.setDate(new Date(dateLong));
		Float orderprice = doc.getDouble("total").floatValue();
		po.setTotal(orderprice);
		return po;
	}

	public JsonObject toJSONForPendingOrders() {
		Date date = getDate();
		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy");
		String formattedDate = formatter.format(date);
		return Json.createObjectBuilder()
			.add("orderId", getOrderId())
			.add("total", getTotal())
			.add("date", formattedDate)
			.build();
	}
}
