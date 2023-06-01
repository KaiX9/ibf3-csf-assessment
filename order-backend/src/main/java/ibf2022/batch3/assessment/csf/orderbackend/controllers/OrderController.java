package ibf2022.batch3.assessment.csf.orderbackend.controllers;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.OrdersRepository;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.PendingOrdersRepository;
import ibf2022.batch3.assessment.csf.orderbackend.services.OrderingService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonValue;

@Controller
@CrossOrigin(origins="*")
@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

	@Autowired
	private OrderingService orderingSvc;
	@Autowired
	private OrdersRepository ordersRepo;
	@Autowired
	private PendingOrdersRepository poRepo;

	// TODO: Task 3 - POST /api/order
	@PostMapping(path="/api/order", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> placeOrder(@RequestBody String payload) {

		System.out.println(">> payload: " + payload);
		try {
			JsonReader reader = Json.createReader(new StringReader(payload));
			JsonObject jsonObj = reader.readObject();
			PizzaOrder pOrd = new PizzaOrder();
			pOrd.setName(jsonObj.getString("name"));
			pOrd.setEmail(jsonObj.getString("email"));
			pOrd.setSize(jsonObj.getInt("size"));
			String base = jsonObj.getString("base");
			if (base.contains("thick")) {
				pOrd.setThickCrust(true);
			} else {
				pOrd.setThickCrust(false);
			}
			pOrd.setSauce(jsonObj.getString("sauce"));
			pOrd.setComments(jsonObj.getString("comments"));
			JsonArray toppingsArray = jsonObj.getJsonArray("toppings");
			List<String> toppings = new ArrayList<String>();
			for (JsonValue topping : toppingsArray) {
				toppings.add(topping.toString());
			}
			pOrd.setTopplings(toppings);
			PizzaOrder po = this.orderingSvc.placeOrder(pOrd);
			pOrd.setOrderId(po.getOrderId());
			pOrd.setDate(po.getDate());
			pOrd.setTotal(po.getTotal());
			System.out.println(">>: " + pOrd);
			this.ordersRepo.add(pOrd);
			this.poRepo.add(pOrd);
	
			return ResponseEntity.status(HttpStatus.ACCEPTED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(pOrd.toJSONToClient().toString());
		} catch (Exception ex) {
			Map<String, String> errorBody = new HashMap<>();
			errorBody.put("error", ex.getMessage());
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.contentType(MediaType.APPLICATION_JSON)
				.body(errorBody);
		}
	}

	// TODO: Task 6 - GET /api/orders/<email>
	@GetMapping(path="/api/orders/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<String> getOrdersByEmail(@PathVariable String email) {

		List<PizzaOrder> pendingOrders = this.orderingSvc.getPendingOrdersByEmail(email);
		JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
		for (PizzaOrder p : pendingOrders) {
			arrBuilder.add(p.toJSONForPendingOrders());
		}
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.APPLICATION_JSON)
			.body(arrBuilder.build().toString());
	}

	// TODO: Task 7 - DELETE /api/order/<orderId>

}
