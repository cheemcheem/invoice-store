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
  invoiceId: string,
  invoiceDate: string,
  invoiceName: string
}