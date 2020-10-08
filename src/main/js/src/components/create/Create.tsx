import React, {useMemo, useState} from "react";
import * as Cookie from "js-cookie";
import Grid from "@material-ui/core/Grid";
import Card from "@material-ui/core/Card";
import FormControl from "@material-ui/core/FormControl";
import ErrorBoundary from "../common/ErrorBoundary";
import Typography from "@material-ui/core/Typography";
import {KeyboardDatePicker, MuiPickersUtilsProvider} from "@material-ui/pickers";
import DateFnsUtils from "@date-io/date-fns";
import InputLabel from "@material-ui/core/InputLabel";
import Input from "@material-ui/core/Input";
import NumberFormatCustom from "./NumberFormat";
import {DropzoneArea} from "material-ui-dropzone";
import Button from "@material-ui/core/Button";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import {createStyles, makeStyles, Theme} from "@material-ui/core/styles";

const useStyles = makeStyles((theme: Theme) => createStyles({
  grid: {
    height: "100%",
    padding: theme.spacing(2),
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4)
  },
  cardContent: {
    width: "100%",
    height: window.innerHeight - theme.spacing(16),
    overflowY: "scroll",
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
  },
  subGrid: {
    height: "100%"
  },
  formItem: {
    width: "100%"
  },
  form: {
    display: "flex",
    padding: theme.spacing(2),
    flexDirection: "column",
    justifyContent: "space-between",
    alignContent: "center",
    justifyItems: "center",
    alignItems: "center",
    height: "inherit",
    width: window.innerWidth - theme.spacing(8),
  },
  subForm: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-around",
    alignContent: "center",
    justifyItems: "center",
    alignItems: "center",
    width: "100%",
  },
  input: {
    display: 'none',
  },
  paper: {
    padding: theme.spacing(2),
    boxSizing: "border-box",
    height: "inherit"
  },
  imageImage: {
    width: "100%"
  },
  imageItem: {
    width: "100%",
    minWidth: "100%"
  },
}));

export type CreateState = {
  invoiceDate: Date,
  invoiceName: string,
  invoiceTotalVAT: string,
  invoiceTotal: string,
  invoiceFile: any
};
export type CreateProps = {
  submit: (values: CreateState) => void
}
export default function Create({submit}: CreateProps) {
  const classes = useStyles();
  const [values, setValues] = useState({
    invoiceDate: new Date(),
    invoiceName: '',
    invoiceTotalVAT: '',
    invoiceTotal: '',
    invoiceFile: ''
  } as CreateState);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setValues({
      ...values,
      [event.target.name]: event.target.value,
    });
  };

  const dropZoneChange = useMemo(() => (files: File[]) => {
    setValues({...values, invoiceFile: files[0]});
  }, [values, setValues]);

  return <>
    <Grid className={classes.grid}
          container
          direction="row"
          justify="center"
          alignContent="flex-start">
      <Card className={classes.cardContent}>
        <form className={classes.form}>
          <div className={classes.subForm}
               onKeyPress={event => event.key === 'Enter' && submit(values)}>
            <FormControl className={classes.formItem}>
              <ErrorBoundary renderError={<Typography variant="h6">error</Typography>}>
                <MuiPickersUtilsProvider utils={DateFnsUtils}>
                  <KeyboardDatePicker
                      fullWidth
                      margin="normal"
                      id="invoiceDate"
                      label="INVOICE DATE"
                      format="dd/MM/yyyy"
                      value={values.invoiceDate}
                      onChange={(date) => setValues({
                        ...values,
                        invoiceDate: date || values.invoiceDate
                      })}
                      KeyboardButtonProps={{
                        'aria-label': 'change date',
                      }}
                  />
                </MuiPickersUtilsProvider>
              </ErrorBoundary>
            </FormControl>
            <FormControl className={classes.formItem}>
              <InputLabel shrink htmlFor="invoiceName">INVOICE NAME</InputLabel>
              <Input onChange={handleChange}
                     id="invoiceName"
                     name="invoiceName"
                     type="text"
                     required/>
            </FormControl>
            <FormControl className={classes.formItem}>
              <InputLabel shrink htmlFor="invoiceTotalVAT">VAT TOTAL</InputLabel>
              <Input onChange={handleChange}
                     inputComponent={NumberFormatCustom as any}
                     placeholder={"£0.00"}
                     value={values.invoiceTotalVAT}
                     id="invoiceTotalVAT"
                     name="invoiceTotalVAT"
                     required
              />
            </FormControl>
            <FormControl className={classes.formItem}>
              <InputLabel shrink htmlFor="invoiceTotal">TOTAL</InputLabel>
              <Input onChange={handleChange}
                     inputComponent={NumberFormatCustom as any}
                     placeholder={"£0.00"}
                     value={values.invoiceTotal}
                     id="invoiceTotal"
                     name="invoiceTotal"
                     required
              />
            </FormControl>
          </div>
          <FormControl variant={"filled"}>
            <ErrorBoundary renderError={<>
              <Typography variant="body1" color="error">
                Cannot attach a file at this time.
              </Typography>
            </>}>
              <DropzoneArea
                  onChange={dropZoneChange}
                  acceptedFiles={["image/*", ".pdf"]}
                  filesLimit={1}
                  maxFileSize={5000000}
                  showFileNames={true}
                  classes={{
                    icon: "test-image"
                  }}
                  previewGridClasses={{
                    image: classes.imageImage,
                    item: classes.imageItem,
                  }}
              />
            </ErrorBoundary>
          </FormControl>
          <input type="hidden" name="_csrf" value={String(Cookie.get("XSRF-TOKEN"))}/>
          <FormControl className={classes.formItem}>
            <Button id="invoiceSubmit"
                    type="button"
                    variant="outlined"
                    color="primary"
                    onClick={() => submit(values)}
                    startIcon={<CloudUploadIcon/>}
                    disableElevation>
              Create
            </Button>
          </FormControl>
        </form>
      </Card>
    </Grid>
  </>
}