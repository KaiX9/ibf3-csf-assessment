export interface Order {
    name: string
    email: string
    size: number
    base: string
    sauce: string
    toppings: string[]
    comments: string
}

export interface OrderResponse {
    orderId: string
    date: string
    name: string
    email: string
    total: number
}

export interface EmailResponse {
    orderId: string
    total: number
    date: string
}