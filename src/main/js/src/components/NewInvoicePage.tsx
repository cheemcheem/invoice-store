import React from "react";
import FormInput from "./subcomponents/FormInput";
import "./NewInvoicePage.css";
import * as Cookie from "js-cookie";

export default function NewInvoicePage() {
return <>
  <form method="post" action="/api/invoice/new" encType="multipart/form-data" >
    <FormInput formInputId="invoiceDate" formInputType="date" formInputLabelText="Date"/>
    <FormInput formInputId="invoiceName" formInputType="text" formInputLabelText="Name"/>
    <FormInput formInputId="invoiceTotalVAT" formInputType="number" formInputLabelText="VAT Total"/>
    <FormInput formInputId="invoiceTotal" formInputType="number" formInputLabelText="Total"/>
    <label htmlFor="invoiceFile">Optionally attach file</label>
    <input id="invoiceFile"  name="invoiceFile" type="file" accept="image/*,.pdf"/>
    <input type="hidden" name="_csrf" value={String(Cookie.get("XSRF-TOKEN"))}/>
    <input id="invoiceSubmit" type="submit" value="Create" required/>
  </form>
</>
}