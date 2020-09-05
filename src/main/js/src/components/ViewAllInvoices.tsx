import React, {useEffect, useState} from "react";

export default function ViewAllInvoices({archived}: {archived: boolean}) {
  const [allInvoices, setAllInvoices] = useState([] as string[]);
  console.log({allInvoices})
  useEffect(() => {
    fetch(`/api/invoice/${archived ? "archived" : "all"}`)
    .then(response => response.text())
    .then(JSON.parse)
    .then(setAllInvoices);
  }, [archived, setAllInvoices]);

  return <ul>
    {allInvoices.map(invoice => <li key={invoice}><a href={`/view/${invoice}`}>Invoice {invoice}</a>
    </li>)}
  </ul>
}