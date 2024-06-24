import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { OrderService } from '../../services/order.service';
import { OrderResponseDTO } from './order.model';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent implements OnInit {
  orders: MatTableDataSource<OrderResponseDTO> = new MatTableDataSource<OrderResponseDTO>();
  totalOrders = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.loadOrders();
  }

  loadOrders(): void {
    this.orderService.getOrders().subscribe(data => {
      this.orders = new MatTableDataSource(data);
      this.orders.paginator = this.paginator;
      this.totalOrders = data.length; // total orders count
      this.orders.filterPredicate = (data: OrderResponseDTO, filter: string) => {
        return data.orderCode.toLowerCase().includes(filter) ||
          data.orderDate.toString().toLowerCase().includes(filter) ||
          data.customer.name.toLowerCase().includes(filter);
      };
    });
  }

  deleteOrder(id: number): void {
    this.orderService.deleteOrder(id).subscribe(() => {
      this.loadOrders();
    });
  }

  downloadReport(): void {
    this.orderService.generateOrderReport().subscribe(blob => {
      const link = document.createElement('a');
      link.href = window.URL.createObjectURL(blob);
      link.download = 'order_report.pdf';
      link.click();
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.orders.filter = filterValue.trim().toLowerCase();

    if (this.orders.paginator) {
      this.orders.paginator.firstPage();
    }
  }

  pageEvent(event: PageEvent): void {
  }
}
