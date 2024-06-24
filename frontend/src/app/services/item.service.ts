import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { ItemResponseDTO, ItemRequestDTO } from '../components/items/item.model';

@Injectable({
  providedIn: 'root'
})
export class ItemService {
  private apiUrl = 'http://localhost:8080/api/items';

  constructor(private http: HttpClient) { }

  getItems(): Observable<ItemResponseDTO[]> {
    return this.http.get<ItemResponseDTO[]>(`${this.apiUrl}/list`);
  }

  getItemById(id: number): Observable<ItemResponseDTO> {
    return this.http.get<ItemResponseDTO>(`${this.apiUrl}/detail/${id}`);
  }

  addItem(item: ItemRequestDTO): Observable<ItemResponseDTO> {
    return this.http.post<ItemResponseDTO>(`${this.apiUrl}/tambah`, item);
  }

  updateItem(id: number, item: ItemRequestDTO): Observable<ItemResponseDTO> {
    return this.http.put<ItemResponseDTO>(`${this.apiUrl}/update/${id}`, item);
  }

  deleteItem(id: number): Observable<any> {
    return this.http.delete(`${this.apiUrl}/delete/${id}`, { responseType: 'text' });
  }

  generateItemCode(): Observable<string> {
    return this.getItems().pipe(
      map((item: ItemResponseDTO[]) => {
        if (item.length === 0) {
          return 'Item-001';
        }
        const lastItem = item.reduce((prev, current) => {
          const prevCode = parseInt(prev.itemsCode.split('-')[1], 10);
          const currentCode = parseInt(current.itemsCode.split('-')[1], 10);
          return (prevCode > currentCode) ? prev : current;
        });
        const lastCodeNumber = parseInt(lastItem.itemsCode.split('-')[1], 10);
        const newCodeNumber = lastCodeNumber + 1;
        return `Item-${newCodeNumber.toString().padStart(3, '0')}`;
      })
    );
  }
}
