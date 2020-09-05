import React, {useCallback} from "react";
import {BrowserRouter, NavLink, Route, Switch} from "react-router-dom";
import ViewAllInvoices from "./ViewAllInvoices";
import NewInvoicePage from "./NewInvoicePage";
import ViewInvoicePage from "./ViewInvoicePage";
import * as Cookie from "js-cookie";

export default function MainPage() {

  const logout = useCallback(
      () => fetch(`/logout`, {
        method: "POST",
        headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))}
      })
      .then(() => window.location.pathname = "/")
      , [])
  return <>
    <BrowserRouter forceRefresh>
      <nav>
        <ul className="nav">
          <li><NavLink to="/all">All Invoices</NavLink></li>
          <li><NavLink to="/archived">Archive</NavLink></li>
          <li><NavLink to="/new">New Invoice</NavLink></li>
          <li>
            <button onClick={logout}>Logout</button>
          </li>
        </ul>
      </nav>

      <Switch>
        <Route path="/all"><ViewAllInvoices/></Route>
        <Route path="/archived"><ViewAllInvoices archived/></Route>
        <Route path="/new"><NewInvoicePage/></Route>
        <Route path="/view/:invoiceId"><ViewInvoicePage/></Route>
      </Switch>
    </BrowserRouter>
  </>
}