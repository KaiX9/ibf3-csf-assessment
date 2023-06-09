import { HttpClient, HttpHeaders } from "@angular/common/http";
import { Injectable, inject } from "@angular/core";
import { EmailResponse, OrderResponse } from "./models";
import { Observable } from "rxjs";

const URL_ORDER = '/api/order'
const URL_EMAIL = '/api/orders'
const URL_DELIVERED = '/api/order'

@Injectable()
export class PizzaService {

  http = inject(HttpClient)
    // TODO: Task 3
    // You may add any parameters and return any type from placeOrder() method
    // Do not change the method name
    placeOrder(formDataJson: string): Observable<OrderResponse> {

      const headers = new HttpHeaders().set('Content-Type', 'application/json');
      return this.http.post<OrderResponse>(URL_ORDER, formDataJson, { headers });
    }
  
    // TODO: Task 5
    // You may add any parameters and return any type from getOrders() method
    // Do not change the method name
    getOrders(email: string): Observable<EmailResponse[]> {
      const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
      return this.http.get<EmailResponse[]>(`${URL_EMAIL}/${email}`, { headers });
    }
  
    // TODO: Task 7
    // You may add any parameters and return any type from delivered() method
    // Do not change the method name
    delivered(orderId: string): Observable<any> {
      const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
      return this.http.delete<any>(`${URL_DELIVERED}/${orderId}`, { headers });
    }
  
  }
  