import React, {useCallback, useMemo, useState} from "react";
import * as Cookie from "js-cookie";
import NumberFormat from 'react-number-format';
import {makeStyles} from "@material-ui/core/styles";
import {Theme} from "@material-ui/core/styles";
import {createStyles} from "@material-ui/core/styles";
import CloudUploadIcon from '@material-ui/icons/CloudUpload';
import {DropzoneArea} from 'material-ui-dropzone'
import useRedirect from "../hooks/useRedirect";
import {FormControl} from "@material-ui/core";
import {Grid} from "@material-ui/core";
import {Paper} from "@material-ui/core";
import {InputLabel} from "@material-ui/core";
import {Input} from "@material-ui/core";
import {Button} from "@material-ui/core";
import {useSnackbar} from "notistack";

interface NumberFormatCustomProps {
  inputRef: (instance: NumberFormat | null) => void;
  onChange: (event: { target: { name: string; value: string } }) => void;
  name: string;
}

function NumberFormatCustom(props: NumberFormatCustomProps) {
  const {inputRef, onChange, ...other} = props;
  return (
      <NumberFormat
          {...other}
          getInputRef={inputRef}
          onValueChange={(values) => {
            onChange({
              target: {
                name: props.name,
                value: values.value,
              },
            });
          }}
          thousandSeparator
          isNumericString
          prefix="£"
      />
  );
}

const useStyles = makeStyles((theme: Theme) => createStyles({
  grid: {
    height: "80%",
    paddingTop: theme.spacing(4),
    paddingBottom: theme.spacing(4)
  },
  subGrid: {
    height: "100%"
  },
  formItem: {
    width: "100%"
  },
  form: {
    display: "flex",
    flexDirection: "column",
    justifyContent: "space-between",
    alignContent: "center",
    justifyItems: "center",
    alignItems: "center",
    height: "inherit",
    width: "100%",
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


export default function NewInvoicePage() {
  const classes = useStyles();
  const {enqueueSnackbar, closeSnackbar} = useSnackbar();

  const [values, setValues] = useState({
    invoiceDate: '',
    invoiceName: '',
    invoiceTotalVAT: '',
    invoiceTotal: '',
    invoiceFile: '' as any
  });

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    setValues({
      ...values,
      [event.target.name]: event.target.value,
    });
  };


  const {component, triggerRedirect} = useRedirect();


  const submit = useCallback(() => {
    const formData = new FormData();

    Object.keys(values).forEach((name) => formData.append(name, (values as any)[name]))
    const key = enqueueSnackbar("Uploading invoice.", {variant: "info", persist: true});
    fetch(`/api/invoice/new`, {
      method: "POST",
      headers: {"X-XSRF-TOKEN": String(Cookie.get("XSRF-TOKEN"))},
      body: formData
    })
    .then(response => {
      closeSnackbar(key);
      if (response.status === 413) {
        enqueueSnackbar("File size too large!", {variant: "warning"});
      } else if (response.status === 400) {
        enqueueSnackbar("Invalid file.", {variant: "error"});
      } else if (response.status !== 200) {
        throw new Error(`Response status ${response.status}`);
      } else {
        enqueueSnackbar("Created invoice!", {variant: "success"});
        triggerRedirect(new URL(response.url).pathname);
      }
    })
    .catch(() => {
      closeSnackbar(key);
      enqueueSnackbar("Failed to create invoice.", {variant: "error"});
    })
    ;
  }, [values, triggerRedirect, enqueueSnackbar]);

  const dropZoneChange = useMemo(() => (files: File[]) => {
    setValues({...values, invoiceFile: files[0]});
  }, [values, setValues]);

  return <>
    {component}
    <Grid className={classes.grid}
          container
          direction="row"
          justify="center"
          alignContent="center">
      <Grid item xs={10} className={classes.subGrid}>
        <Paper variant="elevation" className={classes.paper}>
          <form className={classes.form}>
            <div className={classes.subForm}
                 onKeyPress={event => event.key === 'Enter' && submit()}>
              <FormControl className={classes.formItem}>
                <InputLabel shrink htmlFor="invoiceDate">Date</InputLabel>
                <Input onChange={handleChange}
                       id="invoiceDate"
                       name="invoiceDate"
                       type="date"
                       required/>
              </FormControl>
              <FormControl className={classes.formItem}>
                <InputLabel shrink htmlFor="invoiceName">Name</InputLabel>
                <Input onChange={handleChange}
                       id="invoiceName"
                       name="invoiceName"
                       type="text"
                       required/>
              </FormControl>
              <FormControl className={classes.formItem}>
                <InputLabel shrink htmlFor="invoiceTotalVAT">VAT Total</InputLabel>
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
                <InputLabel shrink htmlFor="invoiceTotal">Total</InputLabel>
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
            </FormControl>
            <input type="hidden" name="_csrf" value={String(Cookie.get("XSRF-TOKEN"))}/>
            <FormControl className={classes.formItem}>
              <Button id="invoiceSubmit"
                      type="button"
                      variant="outlined"
                      color="primary"
                      onClick={submit}
                      startIcon={<CloudUploadIcon/>}
                      disableElevation>
                Create
              </Button>
            </FormControl>
          </form>
        </Paper>
      </Grid>
    </Grid>
  </>

}