import {gql} from "@apollo/client";

export const GET_USER = gql`
    query {
        user {
            id
            name
            picture
        }
    }
`;

export const GET_INVOICE = gql`
    query Invoice($invoiceId: ID!) {
        invoiceById(id: $invoiceId) {
            id
            date
            name
            total
            vatTotal
            archived
            invoiceFile {
                name
                type
            }
        }
    }
`;

export const GET_ALL_INVOICES = gql`
    query {
        user {
            id
            invoices {
                id
                date
                name
                archived
            }
        }
    }
`;

export const ARCHIVE_INVOICE = gql`
    mutation($updated: InvoiceUpdate!) {
        updateInvoice(updated: $updated) {
            id
            archived
        }
    }
`;

export const CREATE_INVOICE = gql`
    mutation($input: NewInvoice!) {
        createInvoice(input: $input) {
            id
            date
            name
            total
            vatTotal
            archived
            invoiceFile {
                name
                type
            }
        }
    }
`;

export const DELETE_INVOICE = gql`
    mutation($id: ID!) {
        deleteInvoice(id: $id)
    }
`;