import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { OrderResponseDTO, OrderRequestDTO } from '../components/orders/order.model';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private apiUrl = 'http://localhost:8080/api/orders';

  constructor(private http: HttpClient) { }

  getOrders(): Observable<OrderResponseDTO[]> {
    return this.http.get<OrderResponseDTO[]>(`${this.apiUrl}/list`);
  }

  getOrderById(id: number): Observable<OrderResponseDTO> {
    return this.http.get<OrderResponseDTO>(`${this.apiUrl}/detail/${id}`);
  }

  addOrder(order: OrderRequestDTO): Observable<OrderResponseDTO> {
    return this.http.post<OrderResponseDTO>(`${this.apiUrl}/tambah`, order);
  }

  updateOrder(id: number, order: OrderRequestDTO): Observable<OrderResponseDTO> {
    return this.http.put<OrderResponseDTO>(`${this.apiUrl}/update/${id}`, order);
  }

  deleteOrder(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, { responseType: 'text' });
  }

  generateOrderReport(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/report`, { responseType: 'blob' });
  }

  generateOrderCode(): Observable<string> {
    return this.getOrders().pipe(
      map((item: OrderResponseDTO[]) => {
        if (item.length === 0) {
          return 'Ord-001';
        }
        const lastItem = item.reduce((prev, current) => {
          const prevCode = parseInt(prev.orderCode.split('-')[1], 10);
          const currentCode = parseInt(current.orderCode.split('-')[1], 10);
          return (prevCode > currentCode) ? prev : current;
        });
        const lastCodeNumber = parseInt(lastItem.orderCode.split('-')[1], 10);
        const newCodeNumber = lastCodeNumber + 1;
        return `Ord-${newCodeNumber.toString().padStart(3, '0')}`;
      })
    );
  }
}
