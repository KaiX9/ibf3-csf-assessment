import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { AppComponent } from './app.component';
import { MainComponent } from './components/main.component';
import { OrdersComponent } from './components/orders.component';
import { PizzaService } from './pizza.service';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { ReactiveFormsModule } from '@angular/forms';

const routes: Routes = [
  { path: '', component: MainComponent },
  { path: 'orders/:email', component: OrdersComponent },
  { path: '**', redirectTo: '/', pathMatch: 'full' }
]

@NgModule({
  declarations: [
    AppComponent,
    MainComponent,
    OrdersComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    RouterModule.forRoot(routes, { useHash: true })
  ],

  providers: [PizzaService],
  bootstrap: [AppComponent]
})
export class AppModule { }