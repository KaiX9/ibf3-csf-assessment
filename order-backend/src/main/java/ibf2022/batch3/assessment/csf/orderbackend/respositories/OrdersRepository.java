package ibf2022.batch3.assessment.csf.orderbackend.respositories;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import ibf2022.batch3.assessment.csf.orderbackend.models.PizzaOrder;

@Repository
public class OrdersRepository {

	@Autowired
	private MongoTemplate mongoTemplate;

	// TODO: Task 3
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for add()
	public void add(PizzaOrder order) {
		String json = order.toJSONIntoMongo().toString();
		Document doc = Document.parse(json);
		this.mongoTemplate.insert(doc, "orders");
	}
	
	// TODO: Task 6
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for getPendingOrdersByEmail()
	// db.orders.find(
	//	{ "delivered": { "$exists": false }, "email": email}, 
	//  { "_id": 1, "date": 1, "total": 1 }
	//  ).sort({ "date":  -1 })
	public List<PizzaOrder> getPendingOrdersByEmail(String email) {
		Query q = new Query();
		q.addCriteria(Criteria.where("delivered").exists(false)
			.and("email").is(email));
		q.fields().include("_id").include("date").include("total");
		q.with(Sort.by(Sort.Direction.DESC, "date"));
		return this.mongoTemplate.find(q, Document.class, "orders")
			.stream()
			.map(d -> PizzaOrder.createFromDoc(d))
			.toList();
	}

	// TODO: Task 7
	// WARNING: Do not change the method's signature.
	// Write the native MongoDB query in the comment below
	//   Native MongoDB query here for markOrderDelivered()
	public boolean markOrderDelivered(String orderId) {

		return false;
	}


}
