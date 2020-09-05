import React from 'react';
import './App.css';
import {BrowserRouter as Router, NavLink, Route, Switch} from "react-router-dom";
import LoginPage from "./components/LoginPage";
import NewInvoicePage from "./components/NewInvoicePage";
import ViewInvoicePage from "./components/ViewInvoicePage";
import ViewAllInvoices from "./components/ViewAllInvoices";

export default function App() {

  return <Router>
    <nav>
      <ul className="nav">
        <li><NavLink to="/login">Login</NavLink></li>
        <li><NavLink to="/all">All Invoices</NavLink></li>
        <li><NavLink to="/archived">Archive</NavLink></li>
        <li><NavLink to="/new">New Invoice</NavLink></li>
      </ul>
    </nav>

    <Switch>
      <Route path="/login"><LoginPage/></Route>
      <Route path="/all"><ViewAllInvoices archived={false}/></Route>
      <Route path="/archived"><ViewAllInvoices archived={true}/></Route>
      <Route path="/new"><NewInvoicePage/></Route>
      <Route path="/view/:invoiceId"><ViewInvoicePage/></Route>
    </Switch>
  </Router>
}