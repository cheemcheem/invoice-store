export type Invoice = {
  id: string,
  date: Date,
  name: string,
  vatTotal: number,
  total: number,
  invoiceFile?: {
    name: string,
    type: string
  },
  archived: boolean
}
export type BasicInvoice = {
  id: string,
  date: string,
  name: string
  archived: boolean
}
export type User = {
  id: string,
  name: string,
  picture: string
}