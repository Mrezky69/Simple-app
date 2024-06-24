import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatTableDataSource, MatTableModule } from '@angular/material/table';
import { MatDialogModule } from '@angular/material/dialog';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { CustomersComponent } from './components/customers/customers.component';
import { ItemsComponent } from './components/items/items.component';
import { OrdersComponent } from './components/orders/orders.component';
import { CustomerDialogComponent } from './components/customer-dialog/customer-dialog.component';
import { ItemDialogComponent } from './components/item-dialog/item-dialog.component';
import { OrderDialogComponent } from './components/order-dialog/order-dialog.component';
import { NavbarComponent } from './components/navbar/navbar.component';
import { MatGridListModule } from '@angular/material/grid-list';
import { CustomerDetailComponent } from './components/customer-detail/customer-detail.component';
import { ItemDetailComponent } from './components/item-detail/item-detail.component';
import { OrderDetailComponent } from './components/order-detail/order-detail.component';
import { MatPaginatorModule } from '@angular/material/paginator';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    CustomersComponent,
    ItemsComponent,
    OrdersComponent,
    CustomerDialogComponent,
    ItemDialogComponent,
    OrderDialogComponent,
    CustomerDetailComponent,
    ItemDetailComponent,
    OrderDetailComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    FormsModule,
    MatButtonModule,
    MatInputModule,
    MatTableModule,
    MatGridListModule,
    MatDialogModule,
    MatToolbarModule,
    MatIconModule,
    MatCardModule,
    MatPaginatorModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
