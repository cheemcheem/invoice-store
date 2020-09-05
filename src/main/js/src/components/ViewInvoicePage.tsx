import React, {useCallback, useEffect, useState} from "react";
import {useParams} from "react-router-dom";
import {Invoice} from "../common/Types";
import download from "downloadjs";

export default function ViewInvoicePage() {
  const {invoiceId} = useParams();
  const [invoice, setInvoice] = useState({} as Invoice);

  useEffect(() => {
    fetch(`/api/invoice/details/${invoiceId}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(setInvoice);
  }, [setInvoice, invoiceId]);

  const archiveButton = useCallback(
      () => fetch(`/api/invoice/${invoice.invoiceArchived ? "restore" : "archive"}/${invoice.invoiceDetailsId}`, {method: "PUT"})
      .then(() => window.location.reload()),
      [invoice.invoiceArchived, invoice.invoiceDetailsId])

  const deleteButton = useCallback(
      () => fetch(`/api/invoice/delete/${invoice.invoiceDetailsId}`, {method: "DELETE"})
      .then(() => window.location.pathname = "/all")
      , [invoice.invoiceDetailsId])

  const downloadFile = useCallback(() => {
    if (!invoice.invoiceFile) {
      return;
    }

    fetch(`/api/invoice/file/${invoice.invoiceFile.invoiceFileId}`)
    .then(response => response.blob())
    .then(blob => {
      download(blob,invoice.invoiceFile?.invoiceFileName, invoice.invoiceFile?.invoiceFileType)
    });

  }, [invoice.invoiceFile])

  return <>
    <h1>
      View invoice {invoiceId}
    </h1>
    <p>{invoice.invoiceDate}</p>
    <p>{invoice.invoiceName}</p>
    <p>{invoice.invoiceTotalVAT}</p>
    <p>{invoice.invoiceTotal}</p>
    <p>{String(invoice.invoiceArchived)}</p>
    {invoice.invoiceFile && <>
      <p>{invoice.invoiceFile.invoiceFileId}</p>
      <p>{invoice.invoiceFile.invoiceFileName}</p>
      <p>{invoice.invoiceFile.invoiceFileType}</p>
      <button onClick={downloadFile}>Download</button>
    </>}
    <button onClick={archiveButton}>{invoice.invoiceArchived ? "Restore" : "Archive"}</button>
    {invoice.invoiceArchived && <button onClick={deleteButton}>Delete</button>}
  </>
}