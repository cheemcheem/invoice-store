import {gql} from "@apollo/client";

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

export const ARCHIVE_INVOICE = gql`
    mutation($updated: InvoiceUpdate!) {
        updateInvoice(updated: $updated) {
            id
            archived
        }
    }
`;