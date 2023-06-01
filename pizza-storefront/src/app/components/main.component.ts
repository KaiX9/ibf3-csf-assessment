import { Component, OnInit, inject } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { PizzaService } from '../pizza.service';

const SIZES: string[] = [
  "Personal - 6 inches",
  "Regular - 9 inches",
  "Large - 12 inches",
  "Extra Large - 15 inches"
]

const PIZZA_TOPPINGS: string[] = [
    'chicken', 'seafood', 'beef', 'vegetables',
    'cheese', 'arugula', 'pineapple'
]

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  pizzaSize = SIZES[0]
  form!: FormGroup
  fb = inject(FormBuilder)
  router = inject(Router)
  pizzaSvc = inject(PizzaService)

  constructor() {}

  ngOnInit(): void {
    this.form = this.createForm();
    this.setToppings();
  }

  updateSize(size: string) {
    this.pizzaSize = SIZES[parseInt(size)];
  }

  get toppingsArray() {
    return this.form.get('toppings') as FormArray;
  }

  setToppings() {
    const control = PIZZA_TOPPINGS.map(() => this.fb.control(false));
    this.form.setControl('toppings', this.fb.array(control))
  }

  placeOrder() {
    const selectedToppings = this.form.value.toppings
      .map((checked: boolean, i: number) => checked ? PIZZA_TOPPINGS[i] : null)
      .filter((v: string | null) => v !== null);
    const { toppings, ...formData } = this.form.value;
    formData.toppings = selectedToppings;
    const formDataJson = JSON.stringify(formData);
    console.log('>>>: ', formDataJson);
    this.pizzaSvc.placeOrder(formDataJson).subscribe(
      (response) => {
        console.info(JSON.stringify(response));
        this.router.navigate(['/orders', this.form.value.email]);
      },
      (error) => alert(JSON.stringify(error))
    );
  }

  createForm(): FormGroup {
    return this.fb.group({
      name: this.fb.control<string>('', [ Validators.required ]),
      email: this.fb.control<string>('', [ Validators.required ]),
      size: this.fb.control<number>(0, [ Validators.required ]),
      base: this.fb.control<string>('', [ Validators.required ]),
      sauce: this.fb.control<string>('', [ Validators.required ]),
      toppings: this.fb.array([], [ Validators.required ]),
      comments: this.fb.control<string>('')
    })
  }


}
