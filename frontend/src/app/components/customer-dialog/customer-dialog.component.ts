import { Component, OnInit } from '@angular/core';
import { CustomerRequestDTO } from '../customers/customers.model';
import { ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { catchError, throwError } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-customer-dialog',
  templateUrl: './customer-dialog.component.html',
  styleUrls: ['./customer-dialog.component.css']
})
export class CustomerDialogComponent implements OnInit {
  customer: CustomerRequestDTO = {
    code: '',
    name: '',
    address: '',
    phone: '',
    isActive: 1,
    pic: ''
  };
  isEditMode = false;
  customerId: number | null = null;
  selectedFile: File | null = null;
  errorMessage: string = '';

  constructor(
    private customerService: CustomerService,
    private route: ActivatedRoute,
    private router: Router
  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.customerId = +id;
      this.customerService.getCustomerById(this.customerId).subscribe(customer => {
        this.customer = customer;
      });
    } else {
      this.customerService.generateCustomerCode().subscribe(newCode => {
        this.customer.code = newCode;
      });
    }
  }

  onFileSelected(event: any): void {
    this.selectedFile = event.target.files[0];
    if (this.selectedFile) {
      const reader = new FileReader();
      reader.onload = () => {
        this.customer.pic = (reader.result as string).split(',')[1];
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit(): void {
    this.errorMessage = '';
    if (this.isEditMode && this.customerId !== null) {
      this.customerService.updateCustomer(this.customerId, this.customer).pipe(
        catchError((error: HttpErrorResponse) => {
          this.errorMessage = this.splitErrorMessage(error.error.message);
          return throwError(error);
        })
      ).subscribe(() => {
        this.router.navigate(['/customers']);
      });
    } else {
      this.customerService.addCustomer(this.customer).pipe(
        catchError((error: HttpErrorResponse) => {
          this.errorMessage = this.splitErrorMessage(error.error.message);
          return throwError(error);
        })
      ).subscribe(() => {
        this.router.navigate(['/customers']);
      });
    }
  }

  onCancel(): void {
    this.router.navigate(['/customers']);
  }

  private splitErrorMessage(errorMessage: string): string {
    const parts = errorMessage.split('"');
    return parts.length > 1 ? parts[1] : errorMessage;
  }
}
