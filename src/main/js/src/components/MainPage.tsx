import React, {useMemo} from "react";
import {BrowserRouter, NavLink, Route, Switch} from "react-router-dom";
import ViewAllInvoices from "./ViewAllInvoices";
import NewInvoicePage from "./NewInvoicePage";
import ViewInvoicePage from "./ViewInvoicePage";

export default function MainPage() {
  const logout = useMemo(() => {
    let port = (window.location.port ? ':' + window.location.port : '');
    // handle localhost dev case
    if (port === ':3000') {
      port = ':8080';
    }
    return window.location.protocol + '//' + window.location.hostname + port + '/logout';
  }, []);

  return <>
    <BrowserRouter forceRefresh>
      <nav>
        <ul className="nav">
          <li><NavLink to="/all">All Invoices</NavLink></li>
          <li><NavLink to="/archived">Archive</NavLink></li>
          <li><NavLink to="/new">New Invoice</NavLink></li>
          <li><a href={logout}>Logout</a></li>
        </ul>
      </nav>

      <Switch>
        <Route path="/all"><ViewAllInvoices/></Route>
        <Route path="/archived"><ViewAllInvoices archived/></Route>
        <Route path="/new"><NewInvoicePage/></Route>
        <Route path="/view/:invoiceId"><ViewInvoicePage/></Route>
        <Route path="/logout"></Route>
      </Switch>
    </BrowserRouter>
  </>
}