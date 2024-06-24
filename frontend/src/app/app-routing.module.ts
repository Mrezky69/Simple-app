import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomersComponent } from './components/customers/customers.component';
import { ItemsComponent } from './components/items/items.component';
import { OrdersComponent } from './components/orders/orders.component';
import { CustomerDetailComponent } from './components/customer-detail/customer-detail.component';
import { ItemDetailComponent } from './components/item-detail/item-detail.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { CustomerDialogComponent } from './components/customer-dialog/customer-dialog.component';
import { ItemDialogComponent } from './components/item-dialog/item-dialog.component';
import { OrderDialogComponent } from './components/order-dialog/order-dialog.component';

const routes: Routes = [
    { path: 'customers', component: CustomersComponent },
    { path: 'items', component: ItemsComponent },
    { path: 'orders', component: OrdersComponent },
    { path: '', redirectTo: '/customers', pathMatch: 'full' },
    { path: 'customers/detail/:id', component: CustomerDetailComponent },
    { path: 'items/detail/:id', component: ItemDetailComponent },
    { path: 'orders/detail/:id', component: OrderDetailComponent },
    { path: 'customer/add', component: CustomerDialogComponent },
    { path: 'customer/edit/:id', component: CustomerDialogComponent },
    { path: 'item/add', component: ItemDialogComponent },
    { path: 'item/edit/:id', component: ItemDialogComponent },
    { path: 'order/add', component: OrderDialogComponent },
    { path: 'order/edit/:id', component: OrderDialogComponent }
];

@NgModule({
    imports: [RouterModule.forRoot(routes)],
    exports: [RouterModule]
})
export class AppRoutingModule { }
