import { Component, OnInit, ViewChild } from '@angular/core';
import { MatPaginator, PageEvent } from '@angular/material/paginator';
import { MatTableDataSource } from '@angular/material/table';
import { CustomerService } from '../../services/customer.service';
import { CustomerResponseDTO } from './customers.model';

@Component({
  selector: 'app-customers',
  templateUrl: './customers.component.html',
  styleUrls: ['./customers.component.css']
})
export class CustomersComponent implements OnInit {
  customers: MatTableDataSource<CustomerResponseDTO> = new MatTableDataSource<CustomerResponseDTO>();
  totalCustomers = 0;

  @ViewChild(MatPaginator) paginator!: MatPaginator;

  constructor(private customerService: CustomerService) { }

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getCustomers().subscribe(data => {
      this.customers = new MatTableDataSource(data);
      this.customers.paginator = this.paginator;
      this.totalCustomers = data.length; // total customers count
      this.customers.filterPredicate = (data: CustomerResponseDTO, filter: string) => {
        return data.code.toLowerCase().includes(filter) ||
          data.name.toLowerCase().includes(filter) ||
          (data.isActive ? "aktif" : "tidak aktif").toLowerCase().includes(filter);
      };
    });
  }

  deleteCustomer(id: number): void {
    this.customerService.deleteCustomer(id).subscribe(() => {
      this.loadCustomers();
    });
  }

  applyFilter(event: Event): void {
    const filterValue = (event.target as HTMLInputElement).value;
    this.customers.filter = filterValue.trim().toLowerCase();

    if (this.customers.paginator) {
      this.customers.paginator.firstPage();
    }
  }

  pageEvent(event: PageEvent): void {
  }
}
