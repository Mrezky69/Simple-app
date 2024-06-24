import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { OrderService } from '../../services/order.service';
import { CustomerService } from '../../services/customer.service';
import { ItemService } from '../../services/item.service';
import { OrderRequestDTO, OrderResponseDTO } from '../orders/order.model';
import { CustomerResponseDTO } from '../customers/customers.model';
import { ItemResponseDTO } from '../items/item.model';
import { HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-order-dialog',
  templateUrl: './order-dialog.component.html',
  styleUrls: ['./order-dialog.component.css']
})
export class OrderDialogComponent implements OnInit {
  order: OrderRequestDTO = {
    orderCode: '',
    orderDate: new Date(),
    customerId: 0,
    itemId: 0,
    itemIdLama: 0,
    quantityLama: 0,
    quantity: 0
  };
  isEditMode = false;
  orderId: number | null = null;
  activeCustomers: CustomerResponseDTO[] = [];
  availableItems: ItemResponseDTO[] = [];
  errorMessage: string = '';

  constructor(
    private orderService: OrderService,
    private customerService: CustomerService,
    private itemService: ItemService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.orderId = +id;
      this.orderService.getOrderById(this.orderId).subscribe((orderResponse: OrderResponseDTO) => {
        this.order = {
          orderCode: orderResponse.orderCode,
          orderDate: new Date(orderResponse.orderDate),
          customerId: orderResponse.customer.id,
          itemId: orderResponse.item.id,
          itemIdLama: orderResponse.item.id,
          quantityLama: orderResponse.quantity,
          quantity: orderResponse.quantity
        };
      });
    } else {
      this.orderService.generateOrderCode().subscribe(newCode => {
        this.order.orderCode = newCode;
      });
    }

    this.loadActiveCustomers();
    this.loadAvailableItems();
  }

  loadActiveCustomers(): void {
    this.customerService.getCustomers().subscribe(customers => {
      this.activeCustomers = customers.filter(customer => customer.isActive);
    });
  }

  loadAvailableItems(): void {
    this.itemService.getItems().subscribe(items => {
      this.availableItems = items.filter(item => item.isAvailable);
    });
  }

  onSubmit(): void {
    this.errorMessage = '';
    if (this.isEditMode && this.orderId !== null) {
      this.orderService.updateOrder(this.orderId, this.order).pipe(
        catchError((error: HttpErrorResponse) => {
          this.errorMessage = this.splitErrorMessage(error.error.message);
          return throwError(error);
        })
      ).subscribe(() => {
        this.router.navigate(['/orders']);
      });
    } else {
      this.orderService.addOrder(this.order).pipe(
        catchError((error: HttpErrorResponse) => {
          this.errorMessage = this.splitErrorMessage(error.error.message);
          return throwError(error);
        })
      ).subscribe(() => {
        this.router.navigate(['/orders']);
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/orders']);
  }

  private splitErrorMessage(errorMessage: string): string {
    const parts = errorMessage.split('"');
    return parts.length > 1 ? parts[1] : errorMessage;
  }
}
