package ibf2022.batch3.assessment.csf.orderbackend.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.OrdersRepository;
import ibf2022.batch3.assessment.csf.orderbackend.respositories.PendingOrdersRepository;

@Service
public class OrderingService {

	@Autowired
	private OrdersRepository ordersRepo;

	@Autowired
	private PendingOrdersRepository pendingOrdersRepo;
	
	// TODO: Task 5
	// WARNING: DO NOT CHANGE THE METHOD'S SIGNATURE
	public PizzaOrder placeOrder(PizzaOrder order) throws OrderException {
		MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
		form.add("name", order.getName());
		form.add("email", order.getEmail());
		form.add("sauce", order.getSauce());
		form.add("size", order.getSize().toString());
		form.add("thickCrust", order.getThickCrust().toString());
		for (String topping : order.getTopplings()) {
			form.add("toppings", topping);
		}
		form.add("comments", order.getComments());

		RequestEntity<MultiValueMap<String, String>> req = RequestEntity
			.post("https://pizza-pricing-production.up.railway.app/order")
			.contentType(MediaType.APPLICATION_FORM_URLENCODED)
			.body(form, MultiValueMap.class);
		RestTemplate template = new RestTemplate();
		ResponseEntity<String> resp = template.exchange(req, String.class);
		PizzaOrder po = PizzaOrder.createFromAPI(resp.getBody());
		System.out.println(">> po: " + po);

		return po;
	}

	// For Task 6
	// WARNING: Do not change the method's signature or its implemenation
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		return ordersRepo.getPendingOrdersByEmail(email);
	}

	// For Task 7
	// WARNING: Do not change the method's signature or its implemenation
	public boolean markOrderDelivered(String orderId) {
		return ordersRepo.markOrderDelivered(orderId) && pendingOrdersRepo.delete(orderId);
	}


}
