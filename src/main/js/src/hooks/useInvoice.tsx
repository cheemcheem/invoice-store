import {Invoice} from "../common/Types";
import {useState} from "react";
import {useEffect} from "react";

export default function useInvoice(invoiceId: string, refresh?: any): Invoice | undefined {
  const [invoice, setInvoice] = useState(undefined as undefined | Invoice);

  useEffect(() => {
    fetch(`/api/invoice/details/${invoiceId}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(invoice => ({...invoice, invoiceDate: new Date(invoice.invoiceDate)}))
    .then(setInvoice)
    .catch();
  }, [setInvoice, invoiceId, refresh]);

  return invoice;
}