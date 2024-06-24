import { CustomerResponseDTO } from '../customers/customers.model';
import { ItemResponseDTO } from '../items/item.model';

export interface OrderResponseDTO {
    id: number;
    orderCode: string;
    orderDate: Date;
    totalPrice: number;
    customer: CustomerResponseDTO;
    item: ItemResponseDTO;
    quantity: number;
}

export interface OrderRequestDTO {
    orderCode: string;
    orderDate: Date;
    customerId: number;
    itemId: number;
    itemIdLama: number;
    quantityLama: number;
    quantity: number;
}
