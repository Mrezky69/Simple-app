export interface ItemResponseDTO {
    id: number;
    itemsName: string;
    itemsCode: string;
    stock: number;
    price: number;
    isAvailable: number;
    lastReStock: Date;
}

export interface ItemRequestDTO {
    itemsName: string;
    itemsCode: string;
    stock: number;
    price: number;
    isAvailable: number;
}
