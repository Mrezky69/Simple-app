import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { CustomerResponseDTO, CustomerRequestDTO } from '../components/customers/customers.model';

@Injectable({
  providedIn: 'root'
})
export class CustomerService {
  private apiUrl = 'http://localhost:8080/api/customers';

  constructor(private http: HttpClient) { }

  getCustomers(): Observable<CustomerResponseDTO[]> {
    return this.http.get<CustomerResponseDTO[]>(`${this.apiUrl}/list`);
  }

  getCustomerById(id: number): Observable<CustomerResponseDTO> {
    return this.http.get<CustomerResponseDTO>(`${this.apiUrl}/detail/${id}`);
  }

  addCustomer(customer: CustomerRequestDTO): Observable<CustomerResponseDTO> {
    console.log('Customer data to be sent to server for add:', customer);
    return this.http.post<CustomerResponseDTO>(`${this.apiUrl}/tambah`, customer);
  }

  updateCustomer(id: number, customer: CustomerRequestDTO): Observable<CustomerResponseDTO> {
    return this.http.put<CustomerResponseDTO>(`${this.apiUrl}/update/${id}`, customer);
  }

  deleteCustomer(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, { responseType: 'text' });
  }

  generateCustomerCode(): Observable<string> {
    return this.getCustomers().pipe(
      map((customers: CustomerResponseDTO[]) => {
        if (customers.length === 0) {
          return 'CUST-001';
        }
        const lastCustomer = customers.reduce((prev, current) => {
          const prevCode = parseInt(prev.code.split('-')[1], 10);
          const currentCode = parseInt(current.code.split('-')[1], 10);
          return (prevCode > currentCode) ? prev : current;
        });
        const lastCodeNumber = parseInt(lastCustomer.code.split('-')[1], 10);
        const newCodeNumber = lastCodeNumber + 1;
        return `CUST-${newCodeNumber.toString().padStart(3, '0')}`;
      })
    );
  }
}
