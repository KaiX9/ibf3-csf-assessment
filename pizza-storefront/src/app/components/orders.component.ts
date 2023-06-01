import { Component, OnInit, inject } from '@angular/core';
import { PizzaService } from '../pizza.service';
import { ActivatedRoute } from '@angular/router';
import { EmailResponse } from '../models';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {

  pizzaSvc = inject(PizzaService)
  activatedRoute = inject(ActivatedRoute)
  emailInput!: string
  poObs$!: Observable<EmailResponse[]>

  ngOnInit(): void {
    this.emailInput = this.activatedRoute.snapshot.params['email'];
    console.info(">> ", this.emailInput)
    this.poObs$ = this.pizzaSvc.getOrders(this.emailInput);
  }

}
