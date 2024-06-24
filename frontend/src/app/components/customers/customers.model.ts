export interface CustomerResponseDTO {
    id: number;
    name: string;
    address: string;
    code: string;
    phone: string;
    isActive: number;
    lastOrder: Date;
    pic: string;
}

export interface CustomerRequestDTO {
    name: string;
    address: string;
    code: string;
    phone: string;
    isActive: number;
    pic: string;
}
