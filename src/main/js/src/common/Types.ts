export type Invoice = {
  invoiceDetailsId: string,
  invoiceDate: Date,
  invoiceName: string,
  invoiceTotalVAT: number,
  invoiceTotal: number,
  invoiceFile?: {
    invoiceFileId: string,
    invoiceFileName: string,
    invoiceFileType: string
  },
  invoiceArchived: boolean
}